package com.bluebrim.xml.shared;

import org.apache.crimson.tree.*;

/**
 * This XML builder serves as a default builder, which does not alter the
 * behavior of attribute, element and data export to the XML representation.
 * This class has been added to keep down the number of builder classes that
 * are equivalent except for the tag it returns when the getTag method is called.
 * A CoDefaultXmlBuilder must be initialized with a tag after it has been created
 * Creation date: (1999-10-01 11:54:05)
 * @author: Mikael Printz
 */
public class CoDefaultXmlBuilder extends CoXmlBuilder implements CoDefaultXmlBuilderIF {
	private String m_tag = "none";

/**
 * CoDefaultXmlBuilder constructor comment.
 */
public CoDefaultXmlBuilder() {
	super();
}
/**
 * CoDefaultXmlBuilder constructor comment.
 * @param xmlDoc XmlDocument
 */
public CoDefaultXmlBuilder(XmlDocument xmlDoc) {
	super(xmlDoc);
}
/**
 * CoDefaultXmlBuilder constructor comment.
 * @param xmlDoc com.sun.xml.tree.XmlDocument
 * @param builder com.bluebrim.xml.shared.CoXmlBuilderIF
 */
public CoDefaultXmlBuilder(XmlDocument xmlDoc, com.bluebrim.xml.shared.CoXmlBuilderIF builder) {
	super(xmlDoc, builder);
}
protected String getTag() {
	return m_tag;
}
	public void setTag(String tag) {
		m_tag = tag;
	}
}