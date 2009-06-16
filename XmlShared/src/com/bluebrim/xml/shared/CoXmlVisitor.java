package com.bluebrim.xml.shared;
import java.io.*;
import java.util.*;

import org.apache.crimson.tree.*;

import com.bluebrim.system.shared.*;
import com.bluebrim.xml.impl.shared.*;

/**
 * Abstract super class of xml visitors. The visitor initiates traversal of
 * objects to be XML-ified. The objects then feed the visitor with state information
 * and sets off the visitor on its journey to related objects using the export(CoXmlExportEnabledIF)
 * method.
 * Creation date: (1999-09-02 10:42:15)
 * @author: Mikael Printz
 */
public class CoXmlVisitor implements CoXmlVisitorIF {
	/**
	 *	Stack used to keep track of which builders are used
	 */
	 protected Stack m_builders = new Stack();
	/**
	 *	The XmlDocument instance which is the base for all nodes generated by builders
	 */
	protected XmlDocument m_xmlDoc;


	protected String m_currentChildName = null	;

	protected CoXmlContext m_context;
	/**
	 *	Hashmap holding already visited objects
	 */
	protected HashMap m_visited = new HashMap();

	/**
	 *	Sequence number used for already visited objects
	 */
	protected int m_seq;

	/**
	 *	Stack used to keep track of which builders are used
	 */
	private Stack m_models = new Stack();

	public final static int FILE_COPY_BUFFER_SIZE = 4096;







public void export(String tag, Character exportMe) {
	if (exportMe == null)
		return;
	
	getCurrentBuilder().exportChar(tag, exportMe);
}


/**
 * Initializes the visitor with an XmlDocument instance
 * <p>
 * Creation date: (1999-09-03 11:52:29)
 */
public CoXmlVisitor(XmlDocument xmlDoc, CoXmlContext context) {
	m_xmlDoc = xmlDoc;
	m_context = context;

	// Setup the default builder
	CoXmlBuilderIF builder = new CoXmlDocumentBuilder(m_xmlDoc);
	builder.setVisitor(this);
	pushBuilder(builder);
}


/**
 * @param name The name is restricted to what can be stored in an XML attribute,
 *             which means basically no double quotes (") or newlines.
 */
public void export(String name, Number exportMe)
{
	org.w3c.dom.Element numberNode = m_xmlDoc.createElement(CoJavaNumberModelBuilder.XML_TAG);
	numberNode.setAttribute("name", name);
	numberNode.setAttribute("number", exportMe.toString());

	getCurrentBuilder().exportNode(numberNode);
}


/**
 * @param name The name is restricted to what can be stored in an XML attribute,
 *             which means basically no double quotes (") or newlines.
 */
public void export(String name, Collection exportMe)
{
	String[] attributes;
	
	if (name != null) {
		attributes = new String[2];
		attributes[0] = "name";
		attributes[1] = name;
	} else {
		attributes = new String[0];
	}

	// Create the Collection node
	getCurrentBuilder().addSubNode(CoJavaCollectionModelBuilder.XML_TAG, attributes);

	// Add the collection elements as children of the collectionNode
	Iterator i = exportMe.iterator();
	Object member;
	while (i.hasNext())
	{
		exportObject("", i.next());
	}

	// Get out of the Collection node
	getCurrentBuilder().revertToParentNode();
}


public void export(String name, Map exportMe)
{
	String[] attributes;
	
	if (name != null) {
		attributes = new String[2];
		attributes[0] = "name";
		attributes[1] = name;
	} else {
		attributes = new String[0];
	}

	// Create the Collection node
	getCurrentBuilder().addSubNode("java-map", attributes);

	// Add the map elements as children of the collectionNode
	Set mapKeys = exportMe.keySet();
	Iterator i = mapKeys.iterator();
	Object currentKey;
	while (i.hasNext())
	{
		currentKey = i.next();

		getCurrentBuilder().addSubNode(CoJavaMapEntry.XML_TAG, attributes);
		exportObject("key", currentKey);
		exportObject("value", exportMe.get(currentKey));
		getCurrentBuilder().revertToParentNode();
	}

	// Get out of the Collection node
	getCurrentBuilder().revertToParentNode();
}


/**
 * @deprecated Use {@link export(CoXmlWrapperFlavor, String, CoXmlExportEnabledIF)} instead.
 */
public void export(String childName, CoXmlExportEnabledIF xObj)
{
	getCurrentBuilder().addSubNode(childName, null);
	export(xObj);
	getCurrentBuilder().revertToParentNode();
}


public void export(Iterator iter) {
	while(iter.hasNext()) {
		exportObject("", iter.next());
	}
}


public void export(org.w3c.dom.Node node) {
 	getCurrentBuilder().exportNode(node);
}


public CoXmlBuilderIF export(CoXmlExportEnabledIF exportMe) {

	if(exportMe == null) {
		return null;
	}
	// Check if the object is of a class where recursion should stop (for the current model)
	if(m_context.stopAt(getCurrentModel(), exportMe)) {
		return null;
	}

	try {
		// Keep track of the current model being exported
		m_models.push(exportMe);
		CoXmlBuilderIF builder = getCurrentBuilder().resolveBuilder(exportMe, CoXmlBuilderIF.CONNECT);
		boolean sameBuilder = builder.equals(peekBuilder());
		boolean didPushBuilder = pushBuilder(builder);
		// If we are not continuing building xml using the same builder, we should output an identifier
		boolean encountered = false;
		if(!sameBuilder) {
			encountered = manageIdentityFor(exportMe);
		}
		if(!encountered) {
			exportMe.xmlVisit(this);
		} 
		popBuilder(didPushBuilder);
		m_models.pop();
		return builder;
	} catch(Exception e) {
		throw new CoXmlRuntimeException(e.toString());
	}
}


public void export(CoXmlWrapperFlavor wrapperFlavor, String wrapperParameter, Iterator iterator)
{
	if (wrapperFlavor == CoXmlWrapperFlavors.NAMED)
	{
		String[] attributes = { "flavor", "named", "parameter", wrapperParameter };

		getCurrentBuilder().addSubNode("wrapper", attributes);
		export(iterator);
		getCurrentBuilder().revertToParentNode();
	}
	else
	{
		throw new
			UnsupportedOperationException("Unimplemented Xml Wrapper Flavor");
	}
}


public void export(CoXmlWrapperFlavor wrapperFlavor, String wrapperParameter, CoXmlExportEnabledIF xObj)
{
	if (wrapperFlavor == CoXmlWrapperFlavors.NAMED)
	{
		String[] attributes = { "flavor", "named", "parameter", wrapperParameter };

		getCurrentBuilder().addSubNode("wrapper", attributes);
		export(xObj);
		getCurrentBuilder().revertToParentNode();
	}
	else
	{
		throw new
			UnsupportedOperationException("Unimplemented Xml Wrapper Flavor");
	}
}


public void exportAttribute(String name, String value) {
	getCurrentBuilder().exportAttribute(name, value);
}


public void exportGOIAttribute(String name, CoGOI goi) {
	if (m_context.useGOI())
		exportAttribute(name, goi.toString());
}


/**
 * Export the objects GOI as string or export the object it self
 * depending on the useGOI flag.
 */
public void exportAsGOIorObject(String name,  CoXmlExportEnabledIF object) {
//	if (m_context.useGOI())
//		exportAttribute(name, ((CoDistinguishable)object).getGOI().toString());		
//	else
		export(CoXmlWrapperFlavors.NAMED, name, object);
}


public void exportAttachementFile(File attachement, String identifier) {
	getContext().getAttachements().add(attachement);
	// Create the wrapper leaf pointing out the file
	String[] attributes =
	{
		"flavor", CoXmlWrapperFlavors.BINARY.getXmlName(),
		"parameter", identifier,
		"location", attachement.getName()  
	};

	getCurrentBuilder().addSubNode("wrapper", attributes);
	getCurrentBuilder().revertToParentNode();
	
}

public void exportBinary(InputStream binarySource, String identifier, String suffix)
	throws IOException
{
	/*
	FIXME: This method uses a wrapper type for binary data.  That is probably
	       not necessary though, it would probably be just as well to make a
	       new "binary" type with its own model builder (that builds an
	       InputStream).
	  //Johan Walles (2001-05-23 10:54:14)
	*/

	// Make up a file name
	if (suffix == null)
	{
		suffix = ".bin";
	}
	
	String filename = getContext().getFileNamePrefix();
	String dir = getContext().getFilePathWithoutFileName();
	filename = filename + "-" + nextSequenceNumber() + suffix;
	
	// Copy the contents of the input stream to that file name
	FileOutputStream out = new FileOutputStream(dir + File.separator + filename);
	byte copyBuffer[] = new byte[FILE_COPY_BUFFER_SIZE];
	int bytes_copied;

	while (true)
	{
		int bytesRead = binarySource.read(copyBuffer);

		if (bytesRead == -1)
		{
			break;	
		}

		out.write(copyBuffer, 0, bytesRead);
	}

	out.close();
	binarySource.close();
	
	// Create the wrapper leaf pointing out the file
	String[] attributes =
	{
		"flavor", "binary",
		"parameter", identifier,
		// "location", dir + File.separator + filename
		"location", filename  // Use a relative file name
	};

 	getCurrentBuilder().addSubNode("wrapper", attributes);
	getCurrentBuilder().revertToParentNode();
}


/**
 * Export the given object with the given description.  Internal use only.
 * <p>
 * Creation date: (2001-06-20 10:41:29)
 * 
 * @author Johan Walles
 */
private void exportObject(String name, Object exportMe) {
	if (name == null) {
		name = "";
	}

	if (exportMe instanceof CoXmlExportEnabledIF) {
		if (name.equals("")) {
			export((CoXmlExportEnabledIF) exportMe);
		} else {
			export(CoXmlWrapperFlavors.NAMED, name, (CoXmlEnabledIF) exportMe);
		}
	} else if (exportMe instanceof String) {
		exportString(name, (String) exportMe);
	} else if (exportMe instanceof Number) {
		export(name, (Number) exportMe);
	} else if (exportMe instanceof Character) {
		export(name, (Character) exportMe);
	} else if (exportMe instanceof Collection) {
		export(name, (Collection) exportMe);
	} else if (exportMe instanceof Map) {
		export(name, (Map) exportMe);
	} else {
		throw new IllegalArgumentException("Cannot export Object of type " + exportMe.getClass());
	}
}


public void exportString(String tag, String exportMe) {
	// FIXME: Is there any reason why this method couldn't be named just "export"?
	// FIXME:   //Johan Walles (2001-05-29 09:45:15)

	if (exportMe == null)
		return;
	
	getCurrentBuilder().exportString(tag, exportMe);
}


public void exportTop(Object obj) {
	try {
		// FIXME: This method is broken in its handling of Java Collections.  The Right Way (TM) of
		//              implementing this method would be to have it just call exportObject(obj).  However, that
		//              would break some classes that depend on the current behaviour, so that would probably
		//              take a week (which I don't have currently) to do.
		    //Johan Walles (2001-06-20 10:53:53)

		// Is it multiple objects?
		if (obj instanceof Collection) {
			obj = ((Collection) obj).iterator();
		}
		if (obj instanceof Iterator) {
			Iterator iter = (Iterator) obj;
			// Add a top enclosing node
			CoCollectionXmlBuilder cBuilder = (CoCollectionXmlBuilder) getCurrentBuilder().resolveBuilder(iter, true);
			pushBuilder(cBuilder);
			export(iter);
			popBuilder(true);
		} else {
			export((CoXmlExportEnabledIF) obj);
		}
	} catch (Exception e) {
		throw new CoXmlRuntimeException(e.toString());
	}
}


	public CoXmlContext getContext() {
		return m_context;
	}


/**
 * Creation date: (9/6/99 3:47:19 PM)
 * @return the builder on top of the stack
 */
protected CoXmlBuilderIF getCurrentBuilder() {
	return (CoXmlBuilderIF)m_builders.peek();
}


	public String getCurrentChildName() {
		return m_currentChildName;
	}


	public Object getCurrentModel() {
		if(m_models.isEmpty()) {
			return null;
		} else {
			return m_models.peek();
		}
	}





	/**
	 *	Generates and outputs a unique (within an export) id for
	 *  a specific node. This would be a good place to implement
	 *  a mechanism which would limit the set of classes of business objects
	 *  for which id-s are output.
	 * <p>
	 *  @return true if the model has previously been encountered, otherwise false
	 */
	private boolean manageIdentityFor(CoXmlExportEnabledIF xObj) {
		boolean returnMe = false;

		// Check if object has already been visited
		Integer objId = (Integer)m_visited.get(xObj);
		if(objId == null) {
			objId = new Integer(m_seq);
			m_visited.put(xObj, objId);
			m_seq++;
		} else {
			returnMe = true;
		}

		// Output the id!			
		getCurrentBuilder().exportAttribute("id", objId.toString());

		return returnMe;
	}


	protected CoXmlBuilderIF peekBuilder() {
		return (CoXmlBuilderIF)m_builders.peek();
	}


/**
 * Pops the top builder
 * <p>
 * Creation date: (9/6/99 3:50:34 PM)
 * @param builder CoXmlBuilderIF
 */
protected CoXmlBuilderIF popBuilder(boolean shouldPop) {
	if(shouldPop) {
		return (CoXmlBuilderIF)m_builders.pop();
	} else {
		return (CoXmlBuilderIF)m_builders.peek();
	}
}


/**
 * Pushes the new builder
 * Creation date: (9/6/99 3:50:34 PM)
 * @param builder CoXmlBuilderIF
 */
protected boolean pushBuilder(CoXmlBuilderIF builder) {
	m_builders.push(builder);
	return true;
}


/**
 * @return A number that increases by one each time this method is called
 */
public int nextSequenceNumber() {
	return getContext().nextSequenceNumber();
}
}