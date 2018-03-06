package com.bluebrim.xml.shared;

import java.lang.reflect.*;
import java.util.*;

import org.apache.crimson.tree.*;
import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.extensibility.shared.*;

/**	   	
 * CoXmlBuilder instances are responsible for building XML tree nodes. The XML-api used is the
 * classes in the org.w3c.dom class library.
 * A builder is fed information from a CoXmlVisitor, which in turn gets its information from business objects.
 * The information is used to produce tagged elements and corresponding attributes according to
 * the XML standard. Note that the order and layout (i.e. representation of business object attributes as
 * XML elements or attributes) is the responsibility of the different business object classes. 
 * Which methods that are called by the business object and in what order decides the layout of the final XML representation.
 * Subclasses of this class specializes the behavior for different business object classes.
 *
 * A typical flow of events is this:
 * <ul>
 * <li> A visitor visits an object which feeds the visitor state information
 * <li> The visitor asks its current builder to resolve a builder for the business object at hand
 * <li> The visitor feeds its current builder information which is used to build nodes in the XML-representation
 * </ul>
 * 
 */
public abstract class CoXmlBuilder implements CoXmlBuilderIF {

	/**
	 *	The node associated with the builder. Note that this should only be referenced using its
	 *  get-method (getNode) as it may otherwise be null
	 */
	private Node m_node = null;
	protected XmlDocument m_xmlDocument;
	/**
	 *	Contains mappings between business object classes and the corresponding xml builder classes
	 */
	private final static HashMap m_builderMap = new HashMap();
	private final static HashMap m_tagNamesForClass = new HashMap();
	// Reference to the current visitor
	private CoXmlVisitorIF m_visitor;


	static {
		// Initialize builder map. Note that the XML representation for some classes
		// are built by default XML builders (implementors of CoDefaultXmlBuilderIF).
		// This is true for objects that can be built using the default behavior of
		// CoXmlBuilder. Other objects need specialized builders and these are built
		// with subclassed (from CoXmlBuilder) builders. Note that default builders do
		// not have _any_ context information and must be initialized with a tagname before
		// they are used.
		// Note also that if a specific business object class (or any of its super classes) 
		// is not registered in the builder map, no builder will be created, and the
		// 'parent' builder will be used for XML-ifying the object. This can be used in certain
		// situations when the XML respresentation is flatter than the object structure.
		initDistributedMappings();

		m_builderMap.put(Iterator.class, CoCollectionXmlBuilder.class);
		m_builderMap.put(CoStringWrapper.class, CoDefaultXmlBuilder.class);
		m_builderMap.put(CoListWrapper.class, CoDefaultXmlBuilder.class);
		m_tagNamesForClass.put(CoStringWrapper.class, "str");
		m_tagNamesForClass.put(CoListWrapper.class, "list");

	}

	public CoXmlBuilder() {
		super();
	}

	public CoXmlBuilder(XmlDocument xmlDoc) {
		super();
		setXmlDocument(xmlDoc);
	}

	public CoXmlBuilder(XmlDocument xmlDoc, CoXmlBuilderIF builder) {
		this(xmlDoc);
		connectBuilderNodes(builder);
	}

	public void addComment(String comment) {
		getNode().appendChild(m_xmlDocument.createComment(comment));
	}

	/**
	 * Adds a named subnode under which any subsequent nodes, data and attributes are exported.
	 *
	 * @deprecated in favor of {@link addSubNode(String, String[])}
	 */
	public void addSubNode(String nodeName) {
		Node subNode = m_xmlDocument.createElement(nodeName);
		getNode().appendChild(subNode);
		m_node = subNode;
	}

	/**
	 * Start a sub-node.
	 *
	 * @param nodeName The name of the new node.
	 *
	 * @param nodeAttributes An array containing attribute names and their values.
	 *       The first string in the array is an attribute name, the second a value,
	 *       the third a name and so on.  Passing this method an array with an uneven
	 *       number of members will throw an exception.
	 */
	public void addSubNode(String nodeName, String[] nodeAttributes) {
		if ((nodeAttributes != null) && ((nodeAttributes.length % 2) != 0)) {
			throw new IllegalArgumentException("Must get an even number of attribute/values");
		}

		Element subNode = m_xmlDocument.createElement(nodeName);
		if (nodeAttributes != null) {
			for (int i = 0; i < nodeAttributes.length; i += 2)
				subNode.setAttribute(nodeAttributes[i], nodeAttributes[i + 1]);
		}

		getNode().appendChild(subNode);
		m_node = subNode;
	}

	public void connectBuilderNodes(CoXmlBuilderIF builder) {
		builder.getNode().appendChild(getNode());
	}

	/**
	 * Creates an instance of the appropriate builderClass. The m_builderMap HashMap has
	 * builder classes keyed on model classes for lookup.
	 * <p>
	 * Should be static.
	 * <p>
	 * Creation date: (1999-09-07 15:08:48)
	 * <p>
	 * @param builder CoXmlBuilderIF
	 */
	protected CoXmlBuilderIF createBuilderForClass(Class modelClass) throws CoXmlWriteException {
		Class originalModelClass = modelClass;
		Class builderClass = null;
		String defaultTag = null;
		while (!modelClass.equals(Object.class) && builderClass == null) {
			builderClass = (Class) m_builderMap.get(modelClass);
			// We use the first tag we find on the way up the inheritance hierarchy
			if (defaultTag == null) {
				defaultTag = (String) m_tagNamesForClass.get(modelClass);
			}
			if (builderClass == null) {
				modelClass = modelClass.getSuperclass();
			}
		}
		if (builderClass == null) {
			// No builder class found. This is not an error, the builder for the
			// parent object will receive attributes and such from the model object
			return null;
		}

		CoXmlBuilder builder = null;
		try {
			builder = (CoXmlBuilder) builderClass.newInstance();
			if (builder instanceof CoDefaultXmlBuilderIF) {
				// Must initialize the builder with the correct tag
				if (defaultTag == null) {
					defaultTag = (String) m_tagNamesForClass.get(originalModelClass);
				}
				// If default tag still is null set default to avoid exceptions
				if (defaultTag == null) {
					defaultTag = "no_tag_name_defined";
				}
				((CoDefaultXmlBuilderIF) builder).setTag(defaultTag);
			}
		} catch (Throwable t) {
			t.printStackTrace(System.out);
			throw new CoXmlWriteException("Could not create builder for class " + originalModelClass.getName() + ": " + t.toString());
		}
		return builder;

	}

	public void exportAttribute(String name, String value) {
		exportAttribute(name, value, getNode());
	}

	protected void exportAttribute(String name, String value, Node node) {
		if (value == null) {
			return;
		}

		// FIXME: The following check should match the W3C recommendation at
		// "http://www.w3.org/TR/REC-xml#sec-common-syn" exactly.
		CoAssertion.assertTrue(
			(name.indexOf(" ") == -1) && (name.indexOf("\"") == -1),
			"Attribute names must not contain spaces or double quotes.");

		// XML entities (like "&amp;" instead of "&") are not expanded
		// within attributes, so we don't call xmlEscape() on value.
		 ((ElementNode) node).setAttribute(name, value);
	}

	final public Node exportChar(String tag, Character exportMe) {
		if (exportMe == null)
			return null;

		Element node = m_xmlDocument.createElement("wrapper");

		node.setAttribute("flavor", CoXmlWrapperFlavors.CHARACTER.getXmlName());
		node.setAttribute("parameter", tag); // "parameter" is a required attribute

		StringBuffer xmlExportableCharacter;

		if (exportMe.hashCode() >= 32) {
			//		xmlExportableCharacter = new StringBuffer(xmlEscape(exportMe.toString()));
			xmlExportableCharacter = new StringBuffer(exportMe.toString());
		} else {
			xmlExportableCharacter = new StringBuffer(Integer.toHexString(exportMe.hashCode()));

			while (xmlExportableCharacter.length() < 4) {
				xmlExportableCharacter.insert(0, '0');
			}

			xmlExportableCharacter.insert(0, "0x");
		}

		node.appendChild(m_xmlDocument.createTextNode(xmlExportableCharacter.toString()));

		getNode().appendChild(node);

		return node;
	}

	public void exportNode(final Node node) {
		// Add the node (and its children) to the current tree
		m_node.appendChild(m_xmlDocument.importNode(node, true));
	}

	/**
	 *	Exports an object as a property element
	 */
	public void exportProperty(String key, Object val) {
		exportProperty(getNode(), key, val);
	}

	/**
	 *	Exports an object as a property element
	 */
	private void exportProperty(Node node, String key, Object val) {
		ElementNode eNode = (ElementNode) m_xmlDocument.createElement("property");
		if (key == null) {
			key = "nullKey";
		}

		eNode.setAttribute("name", key);
		eNode.setAttribute("value", formatOutput(val));
		node.appendChild(eNode);
	}

	final public Node exportString(String tag, String exportMe) {
		if (exportMe == null)
			return null;

		Element node = m_xmlDocument.createElement("wrapper");

		node.setAttribute("flavor", CoXmlWrapperFlavors.STRING.getXmlName());
		node.setAttribute("parameter", tag); // "parameter" is a required attribute
		node.setAttribute("xml:space", "preserve");

		Text tNode = m_xmlDocument.createTextNode(xmlEscape(exportMe));
		node.appendChild(tNode);

		getNode().appendChild(node);

		return node;
	}

	/**
	 * Formats output for instances of certain classes.
	 *
	 * @deprecated Since the XML parsing framework doesn't provide any help converting
	 *            from {@link java.lang.String String} to any other class, the XML
	 *            generation framework shouldn't do that the other way either.  If you
	 *            need to output a string while generating XML, output the string and
	 *            not an {@link java.lang.Object Object}.
	 */
	protected static String formatOutput(Object xObj) {
		if (xObj == null) {
			return "";
		} else
			if (xObj instanceof CoNamed) {
				return ((CoNamed) xObj).getName();
			} else
				if (xObj instanceof CoStringExportable) {
					return ((CoStringExportable) xObj).stringExport();
				} else
					if (xObj instanceof CoFormattable) {
						return ((CoFormattable) xObj).format();
					} else {
						return xObj.toString();
					}
	}

	protected CoXmlContext getContext() {
		return getVisitor().getContext();
	}

	public Node getNode() {
		if (m_node == null) {
			m_node = m_xmlDocument.createElement(getTag());
		}
		return m_node;
	}

	protected abstract String getTag();

	public CoXmlVisitorIF getVisitor() {
		return m_visitor;
	}
	/**
	 * Registers a builder class to be used for building a specific class of models
	 * Creation date: (1999-09-07 15:08:48)
	 * Should be static
	 * @param builder CoXmlBuilderIF
	 */
	public static void registerBuilderClassForClass(Class modelClass, Class builderClass) {
		m_builderMap.put(modelClass, builderClass);
	}

	public static void registerTagNamesForClass(Class c, String tag) {
		m_tagNamesForClass.put(c, tag);
	}

	/**
	 * Resolves which builder to use for the current business object and connects the builders' nodes
	 * <p>
	 * Creation date: (1999-09-02 13:10:44)
	 */
	public CoXmlBuilderIF resolveBuilder(Object model, boolean connectNodes) throws CoXmlWriteException {

		if (model == null) {
			return null;
		}
		// If the model class is instance of Iterator, return a collection builder
		CoXmlBuilderIF builder;
		if (model instanceof Iterator) {
			builder = new CoCollectionXmlBuilder();
		} else {
			builder = createBuilderForClass(model.getClass());
		}
		if (builder == null) {
			builder = this;
		} else {
			builder.setXmlDocument(m_xmlDocument);
			builder.setVisitor(getVisitor());
			if (connectNodes) {
				builder.connectBuilderNodes(this);
			}
		}
		if (builder == this) {
			System.out.println("Did not resolve builder for: " + model + " in CoXmlBuilder.resolveBuilder");
		}
		return builder;
	}

	public void revertToParentNode() {
		m_node = getNode().getParentNode();
	}

	public void setNode(Node aNode) {
		m_node = aNode;
	}

	public void setVisitor(CoXmlVisitorIF visitor) {
		m_visitor = visitor;
	}

	public void setXmlDocument(XmlDocument xmlDoc) {
		m_xmlDocument = xmlDoc;
	}

	/**
	 * Escapes &, > and " according to "http://www.w3.org/TR/REC-xml#syntax".
	 * <p>
	 * Creation date: (2001-04-10 11:05:43)
	 */
	public final static String xmlEscape(String unencoded) {
		if (unencoded == null)
			return null;

		StringBuffer encoded = new StringBuffer();

		for (int i = 0; i < unencoded.length(); i++) {
			if (unencoded.charAt(i) == '&')
				encoded.append("&amp;"); // Replace & with &amp;
			else
				if (unencoded.charAt(i) == '"')
					encoded.append("&quot;"); // Replace " with &quot;
			else
				if (unencoded.charAt(i) == '>')
					encoded.append("&gt;"); // Replace > with &gt;
			else
				if (unencoded.charAt(i) == '<')
					encoded.append("&lt;"); // Replace > with &lt;
			else
				encoded.append(unencoded.charAt(i));
		}

		return encoded.toString();
	}

	private static class XmlMapper implements CoXmlMappingSPI.Mapper {
		
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
			m_tagNamesForClass.put(cls, tag);
			if (xmlBuilder != null)
				m_builderMap.put(cls, xmlBuilder);
			else
				m_builderMap.put(cls, CoDefaultXmlBuilder.class);
		}

		/**
		 * Maps a class to XML-tag. No model-builder is used instead the class
		 * should implement CoXmlImportEnabledIF. A default xml-builder is used.
		 */
		public void map( Class cls, String tag ) {
			m_builderMap.put(cls, CoDefaultXmlBuilder.class);
			m_tagNamesForClass.put(cls, tag);
		}
		
		/**
		 * Maps a XML-tag to a model-builder
		 */
		public void map( String tag, Class modelBuilder) {
		}
		
		/**
		 * Maps a class to a xml-builder
		 */
		public void map( Class cls, Class xmlBuilder) {
			m_builderMap.put(cls, xmlBuilder);
		}
		
		
						
	}

	public static CoXmlMappingSPI.Mapper getXmlMapper()
	{
		return new XmlMapper();
	}
	
	private static void initDistributedMappings() {
		XmlMapper mapper = new XmlMapper();
		Iterator providers = CoServices.getProviderNames(CoXmlMappingSPI.class);
		while (providers.hasNext()) {
		    String className = (String) providers.next();
		    Class cls = null;
		    try {
		        cls = Class.forName(className);
		    } catch (ClassNotFoundException e) {
		        System.out.println("Class not found: " + className);
//		        throw new RuntimeException("Class not found: " + className);
		    }
		    try
            {
		        CoXmlMappingSPI provider = (CoXmlMappingSPI) cls.getConstructor(new Class[]{}).newInstance(new Object[]{});
				provider.collectMappings(mapper);

            } catch (IllegalArgumentException e1)
            {
		        System.out.println("IllegalArgumentException");
		        throw new RuntimeException("IllegalArgumentException");
            } catch (SecurityException e1)
            {
		        System.out.println("SecurityException");
		        throw new RuntimeException("SecurityException");
            } catch (InstantiationException e1)
            {
		        System.out.println("InstantiationException");
		        throw new RuntimeException("InstantiationException");
            } catch (IllegalAccessException e1)
            {
		        System.out.println("IllegalAccessException");
		        throw new RuntimeException("IllegalAccessException");
            } catch (InvocationTargetException e1)
            {
		        System.out.println("InvocationTargetException");
		        throw new RuntimeException("InvocationTargetException");
            } catch (NoSuchMethodException e1)
            {
		        System.out.println("NoSuchMethodException");
		        throw new RuntimeException("NoSuchMethodException");
            }
		    
		}
	}

}