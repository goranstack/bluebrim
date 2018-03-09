package com.bluebrim.xml.shared;

import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.debug.*;

/**
 * The CoModelBuilder is an object that given a series of "instruction messages"
 * constructs a business object.
 * <p>
 * It is used primarily by CoXmlParser and subclasses for building business objects
 * from XML documents, but could be driven by other objects as well.
 * <p>
 * The instruction messages are normally relatively few and it is up to the builder
 * to choose the correct "building action" given the information received.
 * <p>
 * The builder keeps track of the owning parser (passed in constructor) in case it has
 * to send messages to the parser - for example pulling extra specific information
 * the builder knows must be coming next.
 * <p>
 * It also holds the current node.
 */
public abstract class CoModelBuilder implements CoModelBuilderIF {
	protected CoXmlParserIF owner;
	protected Node currentNode;
	protected Object m_model;

	/**
	 *	Class that holds data for models that must be lazy instantiated.
	 */
	public abstract class ModelData {
		private Object m_instance = null;
		public  Object getInstance() {
			return m_instance;
		}
		public void setInstance(Object instance) {
			m_instance = instance;
		}
	}

public void createModel(Object superModel, Collection subModels, Node node, CoXmlContext context)
	throws CoXmlReadException
{
	// Fall back on the default variant on default
	createModel(superModel, node, context);
}

/**
 * CoModelBuilder constructor comment.
 */
public CoModelBuilder() {
	this(null);
}


/**
 * CoModelBuilder constructor comment.
 */
public CoModelBuilder(CoXmlParserIF parser) {
	owner = parser;
}


/**
 * @deprecated in favor of {@link addNode(Node, CoXmlContext)}
 */
protected final void addNode(Node node, Object context) {
	CoAssertion.assertTrue(false,
		"You must use CoXmlContext for the context type, not Object!!");
}


/**
 * @deprecated in favor of addSubModel(String, Object, CoXmlContext)
 */
protected final void addSubModel(Object model, CoXmlContext context)
	throws CoXmlReadException
{
	throw new 
		UnsupportedOperationException("This method has been deprecated in favor of addSubModel(String, Object, CoXmlContext)");
}


/**
 * @deprecated in favor to {@link addSubModel(String, Object, CoXmlContext)}
 */
protected final void addSubModel(Object model, Object context)
{
	CoAssertion.assertTrue(false,
		"You must use CoXmlContext for the context type, not Object!!");
}


public Class classToBuild(
	final Object superModel,
	final Node node,
	final CoXmlContext context) 
{
	return null;
}

/**
 * Make sure nobody tries to implement this (deprecated) method.  What
 * <em>should</em> be implemented is
 * <code>createModel(Object, Node, CoXmlContext)</code>.
 */
protected final void createModel(Node node, CoXmlContext context) {
}


protected List getAllAttr(Node aNode){

	List list = new ArrayList();
	NamedNodeMap map = aNode.getAttributes();
	for(int i=0; i<map.getLength(); i++)
		list.add((Attr) map.item(i));
		
	return list; 
}


protected Map getAllAttrAsMap( Node aNode )
{
	Map m = new HashMap();
	NamedNodeMap map = aNode.getAttributes();
	for
		(int i = 0; i < map.getLength(); i++)
	{
		Attr a = (Attr) map.item(i);
		m.put( a.getName(), a.getValue() );
	}

	return m;
}


	public static String getAttrVal(NamedNodeMap map, String name) {
		return getAttrVal(map, name, "");
	}


	public static String getAttrVal(NamedNodeMap map, String name, String defaultVal) {
		if(map == null) {
			return defaultVal;
		}
		Attr attr = (Attr)map.getNamedItem(name);
		if(attr != null) {
			String val = attr.getValue();
			if(val == null) {
				return defaultVal;
			} else {
				return val;
			}
		}
		return defaultVal;
	}


	public static String getAttrVal(Node node, String name) {
		return getAttrVal(node.getAttributes(), name);
	}


	public static String getAttrVal(Node node, String name, String defaultVal) {
		return getAttrVal(node.getAttributes(), name, defaultVal);
	}


public static boolean getBoolAttrVal(NamedNodeMap map, String name, boolean defaultVal) {
	return Boolean.valueOf(getAttrVal(map, name, new Boolean(defaultVal).toString())).booleanValue();
}



/**
 * Get the model.
 */
public Object getModel(Node node, CoXmlContext context) {
	return m_model;
}


	public void initializeFrom(CoModelBuilderIF builder) {
	}


/**
 * Parse the boolean given as a string.
 */
public boolean parseBoolean(String bool) {
	return bool.equalsIgnoreCase("true");
}


/**
 * Parse the size given with units into a double representing points.
 *
 * Reference for units: http://www.unc.edu/~rowlett/units/dictI.html
 *
 *  Point:
 *  A unit of length used by typographers and printers. When printing was done from hand-set metal type, one point represented the smallest
 *    element of type that could be handled, roughly 1/64 inch. Eventually, the point was standardized in Britain and America as exactly 0.013
 *    837 inch, which is about 0.35 mm (351.46 micrometers) and a little bit less than 1/72 inch. In continental Europe, typographers
 *    traditionally used a slightly larger point of 0.014 83 inch (roughly 1/67 inch), sometimes called a Didot point after the French typographer
 *    Firmin Didot (1764-1836). The distinction between the British and Didot points is rapidly disappearing, since most digital typesetting and
 *    page design software now define the point to be exactly 1/72 inch (0.013 888 9 inch or 0.352 777 8 millimeters). 
 *
 * In short:
 *
 * 1 in = EXACTLY 2.54 cm
 * 1 pt = EXACTLY 1/72 in
 *
 * Which gives:
 *
 * 1 mm = 1/25.4 in = 72/25.4 pt
 */
public double parsePtSize(String size) {
	int index;
	double pts;
	if ((index = size.indexOf("mm"))!=-1) {
		pts = (new Double((size.substring(0, index)).trim())).doubleValue();
		return (pts*72)/25.4;
	} else if ((index = size.indexOf("pt"))!=-1) {
		pts = (new Double((size.substring(0, index)).trim())).doubleValue();
		return pts;
	} else  {
		pts = (new Double(size.trim())).doubleValue();
		return pts;
	}
}


/**
 * @deprecated in favor of {@link postCreateModel(Node, CoXmlContext)}
 */
public final void postCreateModel(Node n, Object context)
{
		CoAssertion.assertTrue(false,
		"You must use CoXmlContext for the context type, not Object!!");
}


/**
 * Pull the next node and remember it.
 */
public Node pullNode() {
	return (currentNode = owner.pullNode());
}


	public void resetState() {}


	public void setParser(CoXmlParserIF parser) {
		owner = parser;
	}


public String toString() {
	if (m_model == null) {
		String classString = getClass().toString();

		// Strip the package junk from the class name to make it more readable
		classString =
			classString.substring(
				classString.lastIndexOf(".") + 1,
				classString.length());
		
		return classString;
	} else {
		String classString = m_model.getClass().toString();

		// Strip the package junk from the class name to make it more readable
		classString =
			classString.substring(
				classString.lastIndexOf(".") + 1,
				classString.length());
		
		return classString + " builder: " + m_model;
	}
}


/**
 * Implement this method if you wish to get notified after an object has
 * been read from an XML file.
 * <p>
 * Creation date: (2001-04-12 14:12:28)
 *
 * @param attributes The attributes to the XML node representing this object.
 */
public void xmlImportFinished(Node node, CoXmlContext context)
	throws CoXmlReadException
{}

public static double getDoubleAttrVal(NamedNodeMap map, String name, double defaultVal) {
	//return Double.valueOf(getAttrVal(map, name, new Double(defaultVal).toString())).doubleValue();
	
	String s = getAttrVal(map, name, new Double(defaultVal).toString());
	if (s.equals("NaN")) {
		return Double.NaN;
	} else {
		return Double.valueOf(s).doubleValue();
	}
}


public static float getFloatAttrVal(NamedNodeMap map, String name, float defaultVal) {
	//return Float.valueOf(getAttrVal(map, name, new Float(defaultVal).toString())).floatValue();
	
	String s = getAttrVal(map, name, new Float(defaultVal).toString());
	if (s.equals("NaN")) {
		return Float.NaN;
	} else {
		return Float.valueOf(s).floatValue();
	}
}


public static int getIntAttrVal(NamedNodeMap map, String name, int defaultVal) {
	return Integer.valueOf(getAttrVal(map, name, new Integer(defaultVal).toString())).intValue();
}
}