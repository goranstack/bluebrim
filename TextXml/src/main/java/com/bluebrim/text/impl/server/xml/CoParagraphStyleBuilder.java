package com.bluebrim.text.impl.server.xml;

/**
 * Insert the type's description here.
 * Creation date: (2001-04-20 10:46:12)
 * @author: 
 */
public class CoParagraphStyleBuilder extends com.bluebrim.text.impl.server.xml.CoCharacterStyleBuilder
{
/**
 * CoParagraphStyleBuilder constructor comment.
 */
public CoParagraphStyleBuilder() {
	super();
}
/**
 * CoParagraphStyleBuilder constructor comment.
 * @param xmlDoc org.apache.crimson.tree.XmlDocument
 */
public CoParagraphStyleBuilder(org.apache.crimson.tree.XmlDocument xmlDoc) {
	super(xmlDoc);
}
/**
 * CoParagraphStyleBuilder constructor comment.
 * @param xmlDoc org.apache.crimson.tree.XmlDocument
 * @param builder com.bluebrim.xml.shared.CoXmlBuilderIF
 */
public CoParagraphStyleBuilder(org.apache.crimson.tree.XmlDocument xmlDoc, com.bluebrim.xml.shared.CoXmlBuilderIF builder) {
	super(xmlDoc, builder);
}
/**
 * getTag method comment.
 */
protected String getTag()
{
	return com.bluebrim.text.impl.server.CoParagraphStyle.XML_TAG;
}
}
