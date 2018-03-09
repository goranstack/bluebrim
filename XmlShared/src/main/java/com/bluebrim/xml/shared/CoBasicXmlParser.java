package com.bluebrim.xml.shared;


import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.apache.crimson.tree.*;
import org.w3c.dom.*;
import org.w3c.dom.CharacterData;
import org.xml.sax.*;

import com.bluebrim.extensibility.shared.*;


/**
 * The <code>CoBasicXmlParser</code> is the "parse them all" parser which is used
 * when the <code>CoXmlConsumer</code> does not know what kind of XML will be parsed.
 * 
 * It is also the base implementation class for most concrete parsers since
 * most parsers are depth-first-preorder and should be able to spawn subparsers.
 * 
 * To be able to spawn it holds a static <code>HashMap</code> mapping XML elementnames
 * to different parser classes so that it can spawn "child parse jobs"
 * for specific XML elements.
 * 
 * Each parser should be registered with <code>CoBasicXmlParser</code> for this purpose
 * using <code>registerParser(String, Class)</code>.
 * 
 * NOTE: This is now done in the constructor of
 *       <code>CoBasicXmlParser</code> but should actually be done in a static block.
 * 
 * The <code>CoBasicXmlParser</code> instantiates the corresponding "build them all"
 * <code>CoModelBuilder</code> called <code>CoBasicModelBuilder</code>.
 */
 
public class CoBasicXmlParser extends CoDepthFirstXmlParser {

	static private HashMap m_builderClasses = new HashMap();
	/**
	 *	Keeping track of already created models
	 */
	private HashMap m_createdModels = new HashMap();

	final static private int CURRENT_NODE = 0;
	final static private int NAMED_OBJECT = 1;

public CoBasicXmlParser(CoXmlContext context) {
	super();
	context.initializeXmlParser(this);
}
/**
 * Instantiate the correct builder. We have a corresponding basic builder.
 * Also register all the parser classes that we want to be able to detect
 * and spawn. And instantiate a new empty cache for parsers.
 */
public CoBasicXmlParser(XmlDocument doc, CoXmlContext context)
{
	super(doc);
	context.initializeXmlParser(this);
	builder = new CoBasicModelBuilder(this);

	// Here specialized parsers can be registered keyed on element name. Example:
	// registerParser("articlearea", CoArticleLayoutXmlParser.class);
	// Currently no specialized parsers are used. Instead instances of CoBasicXmlParser is used

	// Here the builders to be used are keyed on element name. These builders will be instantiated
	// when the parser encounters the different element names in the XML. A wrapping default parser
	// (of class CoBasicXmlParser) will be instantiated and initialized with the correct builder

	// Builders for Java classes
	registerBuilder( CoJavaCollectionModelBuilder.XML_TAG, CoJavaCollectionModelBuilder.class );
	registerBuilder( CoJavaNumberModelBuilder.XML_TAG, CoJavaNumberModelBuilder.class );
	registerBuilder( CoJavaMapEntry.XML_TAG, CoJavaMapEntry.class );
	registerBuilder( CoJavaMapModelBuilder.XML_TAG, CoJavaMapModelBuilder.class );


	registerBuilder("str", 						CoStringWrapperModelBuilder.class);
	registerBuilder("list", 					CoCollectionModelBuilder.class);

	initDistributedMappings();
}
	/**
	Build a parser that is able to convert an XML tag into a model.

	@param elementName the XML tag that needs a parser
	*/
	private CoXmlParserIF buildDefaultParser(String elementName, CoXmlContext context) throws CoXmlReadException {
		CoXmlParserIF parser = null;

		Class builderClass =  (Class) m_builderClasses.get(elementName);
		try {
			if(builderClass != null) {
				if (CoModelBuilderIF.class.isAssignableFrom(builderClass)) {
					CoModelBuilderIF builder;
					parser = new CoBasicXmlParser(context);
			
					// Instantiate a new model builder that will build an Object from this tag
					Class[] pc = new Class[1];
					pc[0] = CoXmlParserIF.class;
					Constructor constr = builderClass.getConstructor(pc);
				
					Object[] params = new Object[1];
					params[0] = parser;
				
					builder = (CoModelBuilderIF)constr.newInstance(params);
				
					parser.setBuilder(builder);
				} else if (CoXmlImportEnabledIF.class.isAssignableFrom(builderClass)) {
					CoModelBuilderIF builder;
					parser = new CoBasicXmlParser(context);
					
					// Instantiate a new model builder that will build an Object from this tag
					builder = new CoXmlImportEnabledModelBuilder(parser);
					((CoXmlImportEnabledModelBuilder)builder).setXmlImportEnabled(builderClass);
				
					parser.setBuilder(builder);
				} else {
					System.out.println(this.getClass() + ".buildDefaultParser(String): Class " + builderClass + " must implement either CoModelBuilderIF or CoXmlImportEnabledIF");
				}
			}
		} catch(Exception e) {
			e.printStackTrace(System.out);
			throw new CoXmlReadException("Could not create a builder for element named: " + elementName);
		}

		return parser;
	}
/**
 * Traverse the XML document node using the DOM, preorder depth first.
 * This implementation has the ability to spawn other parsers.
 *
 * @return the <code>Object</code> represented by the XML.
 */
// FIXME: This method needs a better name, but what?
private Object classicTraverse(Object parentModel, Node startNode, CoXmlContext context) 
	throws
		SAXParseException,
		CoXmlReadException
{
	CoXmlParserIF newParser;

	// Uses a TreeWalker to traverse preorder depth first.
	walker = new TreeWalker(currentNode);
	// Tell the builder to create an "empty" model.
	builder.createModel(parentModel, currentNode, context);

	// Register the model in our model cache
	registerModel(currentNode, builder.getModel(currentNode, context));
	Node modelNode = currentNode;

	Object currentModel = builder.getModel( currentNode, context );

	currentNode = skipTextNodes(walker);
	while (currentNode != null) {
		if (currentNode.getNodeName() == "wrapper")
		{
			Object[] parsedWrapper = parseWrapper(currentModel, currentNode, context);

			currentNode = (Node)(parsedWrapper[CURRENT_NODE]);
			CoNamedObject namedObject = (CoNamedObject)(parsedWrapper[NAMED_OBJECT]);
		
			builder.addSubModel(
				namedObject.getName(), 
				namedObject.getObject(), 
				context);

			// Set the currentNode to point to the node after the ending </wrapper>
			currentNode = walker.removeCurrent();
		}
		else
		{
			// Check if the element should be parsed with another parser.
			newParser = matchParser(currentNode.getNodeName(), context);
			if (newParser != null && newParser != this)
			{
				Object subModel = newParser.traverse(currentModel, currentNode, context);

				if (subModel != null) {
					String subModelName = null;

					// Mold parsed native java types into the standard shape
					if (subModel instanceof CoNamedObject)
					{
						subModelName = ((CoNamedObject)subModel).getName();
						subModel = ((CoNamedObject)subModel).getObject();
					}
			
					// Add the submodel to the "large" model.
					builder.addSubModel(
						subModelName,
						subModel,
						context);
				}
				currentNode = walker.removeCurrent();
			} else {
				// Call the builder to build some more on the model...
				pullWalker = null;
				parseNode(currentNode, context);
				currentNode = skipTextNodes(walker);
			}
		}
	}

	// Notify the model builder about the fact that the XML import is finished
	builder.xmlImportFinished(modelNode, context );

	// Let the builder finalize the model and return it.
	return builder.getModel(startNode, context);
}
	public HashMap getCreatedModels() {
		return m_createdModels;
	}
	private Integer getObjectId(Node node) {
		if(node == null) {
			return null;
		}
		if (node.getAttributes() == null) {
			return null;
		}
		Attr attr = (Attr)node.getAttributes().getNamedItem("id");
		if(attr != null) {
			return new Integer(attr.getValue());
		} else {
			return null;
		}
	}
	/**
	This method starts the XML parsing.

	@param node The XML node where to begin.
	*/

	public Object go(Object parentModel, Node node, CoXmlContext context) 
		throws SAXParseException, CoXmlReadException
	{
		CoXmlParserIF parser = matchParser(node.getNodeName(), context);
		
		if (parser != null) {
			// Let the new parser traverse the tree.
			Object obj =  parser.traverse(parentModel, node, context);
			// Let the context do post load setup
			((CoXmlContext)context).postLoadSetup();
			
			return obj;
		} else {
			System.out.println("Could not create a parser for node " + node.getNodeName());
			return null;
		}
	}
	public void hardRegisterModel(Node node, Object model) {
		Integer objId = getObjectId(node);
		if (objId != null) {
			m_createdModels.put(objId, model);
		}
	}
	public void initializeFrom(CoXmlParserIF parser) {
		m_createdModels = parser.getCreatedModels();
	}
/**
 * Traverse the XML document node using the DOM, preorder depth first.
 * This implementation has the ability to spawn other parsers.
 *
 * @return the <code>Object</code> represented by the XML.
 */
private Object lazyTraverse(Object parentModel, Node startNode, CoXmlContext context) 
	throws
		SAXParseException,
		CoXmlReadException
{
	CoXmlParserIF newParser;
	ArrayList childModels = new ArrayList();
	
	// Use a TreeWalker to traverse depth first
	walker = new TreeWalker(currentNode);
	
	// Go through all child nodes and store them in the ArrayList
	currentNode = skipTextNodes(walker);
	while (currentNode != null) {
		if (currentNode.getNodeName() == "wrapper")
		{
			Object[] parsedWrapper = parseWrapper(parentModel, currentNode, context);

			currentNode = (Node)(parsedWrapper[CURRENT_NODE]);
			CoNamedObject namedObject = (CoNamedObject)(parsedWrapper[NAMED_OBJECT]);

			childModels.add(namedObject);

			// Set the currentNode to point to the node after the ending </wrapper>
			currentNode = walker.removeCurrent();
		}
		else
		{
			// Check if the element should be parsed with another parser.
			newParser = matchParser(currentNode.getNodeName(), context);
			if (newParser != null && newParser != this)
			{
				Object subModel = newParser.traverse(null, currentNode, context);
				if (subModel != null) {
					String subModelName = null;

					// Mold parsed native java types into the standard shape
					if (subModel instanceof CoNamedObject) {
						childModels.add(subModel);
					} else {
						childModels.add(new CoNamedObject(parentModel, subModelName, subModel));
					}
				}
				
				currentNode = walker.removeCurrent();
			} else {
				// Call the builder to build some more on the model...
				pullWalker = null;
				parseNode(currentNode, context);
				currentNode = skipTextNodes(walker);
			}
		}
	}

	// Create a new object, passing it the list of child nodes
	builder.createModel(parentModel, childModels, startNode, context);
	Object currentModel = builder.getModel(startNode, context);

	if (currentModel != null) {
		// Register the model in our model cache
		registerModel(startNode, currentModel);
	
		// Go through all the child nodes and add them to the model using addSubModel
		Iterator iter = childModels.iterator();
		while (iter.hasNext()) {
			CoNamedObject subModel = (CoNamedObject)(iter.next());
			builder.addSubModel(subModel.getName(), subModel.getObject(), context);
		}
	
		// Notify the model builder about the fact that the XML import is finished
		builder.xmlImportFinished(startNode, context);
	}
	
	// Let the builder finalize the model and return it.
	return currentModel;
}
	public Object lookupModel(Node node) {
		Integer objId = getObjectId(node);
		Object model = m_createdModels.get(objId);
		return model;
	}
/**
 * Match the correct Parser for the element.
 * Also checks if we already have a cached parser we can reuse.
 *
 * @param elementName The name of the tag that needs a parser.
 *
 * @return an initialized parser for the given <code>elementName</code>
 */
protected CoXmlParserIF matchParser(String elementName, CoXmlContext context) throws CoXmlReadException {
	CoXmlParserIF match = buildDefaultParser(elementName, context);
	
	// Let the parser initialize from the last builder
	if(match != null) {
		match.initializeFrom(this);
		// The builder gets a change to reset any state it might have
		match.getBuilder().resetState();
		if(!match.equals(this)) {
			match.getBuilder().initializeFrom(getBuilder());
		}
	}
	return match;
}
/** Parse an escaped character as generated by CoXmlBuilder.exportChar.
 *
 * @param possibleEscapedCharacter may contain a character encoded as 0x<i>NNNN</i>.
 *
 * @return a String containing the decoded character if
 *        <code>possibleEscapedCharacter</code> contained exactly one encoded
 *        character.  Otherwise return <code>possibleEscapedCharacter</code>.
 */
private String parseEscapedCharacter(String possibleEscapedCharacter) {
	if (possibleEscapedCharacter == null) {
		return possibleEscapedCharacter;
	}
	
	if (possibleEscapedCharacter.length() != 6 /* 6 = length of "0xNNNN" */) {
		return possibleEscapedCharacter;
	}

	/* Verify that the syntax follows "0xNNNN" where N is [0-9a-fA-F] */
	if (! possibleEscapedCharacter.substring(0, 2).equals("0x")) {
		return possibleEscapedCharacter;
	}
	for (int i = 2; i < possibleEscapedCharacter.length(); i++) {
		char c = possibleEscapedCharacter.charAt(i);

		if (((c < '0') && (c > '9')) &&
			((c < 'a') && (c > 'f')) &&
			((c < 'A') && (c > 'F'))) {
			return possibleEscapedCharacter;
		}
	}

	int charValue = Integer.parseInt(possibleEscapedCharacter.substring(2, 6), 16);

	return new Character((char)charValue).toString();
}
/**
 * Parse an XML entry inside &lt;wrapper&gt; tags.
 *
 * @return An array with two elements.  One is a <code>CoNamedObject</code>
 *        containing an instance of the (optionally named) parsed object.
 *        The other is the value of the first unparsed <code>Node</code>
 *        (i.e. where the caller of this method should continue parsing).
 */
private Object[] parseWrapper(Object parentModel, Node currentNode, CoXmlContext context)
	throws SAXParseException, CoXmlReadException
{
	CoNamedObject namedObject;

	// FIXME: Verify that we actually got ourselves a genuine wrapper

	NamedNodeMap attributes = currentNode.getAttributes();

	String flavor = null;
	String parameter = null;

	try
	{
		flavor = attributes.getNamedItem("flavor").getNodeValue();
		parameter = attributes.getNamedItem("parameter").getNodeValue();
	}
	catch (NullPointerException ignored)
	{
		throw new CoXmlReadException("Wrapper attribute \"flavor\" or \"parameter\" is missing!");
	}

	if (flavor.equals(CoXmlWrapperFlavors.NAMED.getXmlName()))
	{
		// Traverse the child of the current node
		pullWalker = null;
		currentNode = skipTextNodes(walker);

		namedObject = new CoNamedObject(parentModel, parameter);
		namedObject.setObject(go(namedObject, currentNode, context));
	}
	else if (flavor.equals(CoXmlWrapperFlavors.BINARY.getXmlName()))
	{
		// FIXME: The location should be an URL
		try
		{
			File location = context.resolveFileName(attributes.getNamedItem("location").getNodeValue());

			namedObject = new CoNamedObject(parentModel, parameter);
			namedObject.setObject(location);
		}
		catch (FileNotFoundException ignored)
		{
			throw new CoXmlReadException("Error reading binary attachment \"" + attributes.getNamedItem("location").getNodeValue() + "\" (file not found).");
		}
	}
	else if (flavor.equals(CoXmlWrapperFlavors.STRING.getXmlName()))
	{
		CharacterData textNode = null;
		for (Node childNode = currentNode.getFirstChild(); childNode != null; childNode = childNode.getNextSibling())
		{
			if (childNode.getNodeType() != Node.TEXT_NODE)
			{
				throw new CoXmlReadException("A string wrapper must not contain anything but a TEXT section");
			}
			textNode = (CharacterData) childNode;
		}

		namedObject = new CoNamedObject(parentModel, parameter);
		namedObject.setObject(textNode == null ? null : textNode.getData());
	}
	else if (flavor.equals(CoXmlWrapperFlavors.CHARACTER.getXmlName()))
	{
		CharacterData characterNode = null;

		for (Node childNode = currentNode.getFirstChild(); childNode != null; childNode = childNode.getNextSibling())
		{
			if (childNode.getNodeType() == Node.TEXT_NODE)
			{
				if (characterNode != null)
				{
					throw new CoXmlReadException("A character wrapper must not contain more than one TEXT section");
				}
				else
				{
					characterNode = (CharacterData) childNode;
				}
			}
			else
			{
				throw new CoXmlReadException("A character wrapper must not contain anything but one TEXT section");
			}
		}

		if (characterNode == null)
		{
			throw new CoXmlReadException("A character wrapper must contain exactly one TEXT section");
		}

		// Parse an escaped character as generated by CoXmlBuilder.exportChar
		characterNode.setData(parseEscapedCharacter(characterNode.getData()));

		if (characterNode.getData().length() != 1)
		{
			throw new CoXmlReadException("A character wrapper TEXT section must contain exactly one character");
		}

		namedObject = new CoNamedObject(parentModel, parameter);
		namedObject.setObject(new Character((characterNode.getData()).charAt(0)));
	}
	else
	{
		throw new CoXmlReadException("Unsupported wrapper flavor \"" + flavor + "\"!");
	}

	Object[] returnMe = new Object[2];
	returnMe[CURRENT_NODE] = currentNode;
	returnMe[NAMED_OBJECT] = namedObject;

	return returnMe;
}
/**
 * Registers a parser class for a certain XML element.
 */
public void registerBuilder(String element, Class aBuilder) {
	// Verify that the class implements either CoXmlImportEnabledIF or CoModelBuilderIF
	if (!(CoXmlImportEnabledIF.class.isAssignableFrom(aBuilder) ||
		   CoModelBuilderIF.class.isAssignableFrom(aBuilder)))
	{
		throw new IllegalArgumentException("The builder must be a Class implementing either CoXmlImportEnabledIF or CoModelBuilderIF. That is not the case for: " + aBuilder);
	}

	m_builderClasses.put(element, aBuilder);
}
	/**
	Registers a model in a hash map so that later it can be looked up using
	the <code>lookupModel</code> method.
	*/
	public void registerModel(Node node, Object model) {

		// FIXME: Is registering null models OK?
		// FIXME:   //Johan Walles (2001-05-22 10:36:13)
		// CoAssertion.assert(model != null, "Internal error: Registering a null model");
		
		Integer objId = getObjectId(node);
		if(lookupModel(node) == null) {
			hardRegisterModel(node, model);
		}
	}
	public void setBuilder(CoModelBuilderIF aBuilder) {
		builder = aBuilder;
	}
/**
 * Advance the tree walker, skipping past any text nodes.
 * @return first non-text node
 */
public static Node skipTextNodes(TreeWalker walker)
{
	Node returnMe = null;
	do {
		returnMe = walker.getNext();

		if (returnMe == null)
			return null;
	} while (returnMe.getNodeType() == Node.TEXT_NODE);

	return returnMe;
}
/**
 * Traverse the XML document node using the DOM, preorder depth first.
 * This implementation has the ability to spawn other parsers.
 *
 * @return The model that the XML represented
 */
public Object traverse(Object parentModel, Node startNode, CoXmlContext context)
	throws
		SAXParseException,
		CoXmlReadException
{
	currentNode = startNode;

	// Check if the node has already been built. In that case, return the model
	Object model = lookupModel(currentNode);
	if (model != null) {
		return model;
	}

	// Does the class to be built implement CoXmlImportLazyCreationIF?
	Class classToBuild = builder.classToBuild(parentModel, currentNode, context);

	if ((classToBuild != null) &&
		CoXmlImportLazyCreationIF.class.isAssignableFrom(classToBuild))
	{
		// Yes, build the new object in a lazy fashion
		return lazyTraverse(parentModel, startNode, context);
	}
	else
	{
		// No, build the new object the good old way
		return classicTraverse(parentModel, startNode, context);
	}
}

	private class XmlMapper implements CoXmlMappingSPI.Mapper {

		/**
		 * Maps a class to a XML-tag and a model-builder
		 */
		public void map( Class cls, String tag, Class modelBuilder) {
			map(cls, tag, modelBuilder, null);
		}

		/**
		 * Maps a class to a XML-tag, model-builder and a xml-builder
		 */
		public void map( Class cls, String tag, Class modelBuilder, Class xmlBuilder) {
			if (modelBuilder != null)
				registerBuilder( tag, modelBuilder );
			else
				map(cls, tag);
		}

		/**
		 * Maps a class to XML-tag. No model-builder is used instead the class
		 * should implement CoXmlImportEnabledIF. A default xml-builder is used.
		 */
		public void map( Class cls, String tag ) {
			if (CoXmlImportEnabledIF.class.isAssignableFrom(cls))
				registerBuilder( tag, cls );
		}
		
		/**
		 * Maps a XML-tag to a model-builder
		 */
		public void map( String tag, Class modelBuilder) {
			registerBuilder( tag, modelBuilder );
		}
		
		/**
		 * Maps a class to a xml-builder
		 */
		public void map( Class cls, Class xmlBuilder) {
			// Not used when importing XML
		}


	}

	private void initDistributedMappings() {
		XmlMapper mapper = new XmlMapper();
		Iterator providers = CoServices.getProviders(CoXmlMappingSPI.class);
		while (providers.hasNext()) {
			CoXmlMappingSPI provider = (CoXmlMappingSPI) providers.next();
			provider.collectMappings(mapper);
		}
	}
}