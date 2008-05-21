package com.bluebrim.xml.impl.shared;


import org.apache.crimson.tree.*;

import com.bluebrim.xml.shared.*;

public class CoXmlDocumentBuilder extends CoXmlBuilder {
/**
 * Initializes the builder with an XmlDocument instance
 */
public CoXmlDocumentBuilder(XmlDocument xmlDoc) {
	m_xmlDocument = xmlDoc;
	// This builder has the XmlDocument as its node
	setNode(m_xmlDocument);
}
/**
 * Returns the tag to be used when creating element nodes in the XML representation 
 * Creation date: (1999-09-03 14:24:41)
 * @return java.lang.String
 */
protected String getTag() {
	// This builder represents the outmost node in the tree (the document) and does not ha a tag
	return null;
}
}