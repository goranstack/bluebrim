package com.bluebrim.content.impl.server.xml;

import org.apache.crimson.tree.*;

import com.bluebrim.xml.shared.*;


/**
 * Super class of all page item XML builders.
 * Has common behavior for specific types of attributes and elements
 * Creation date: (1999-09-17 15:37:57)
 * @author: Mikael Printz
 */
 
public abstract class CoContentXmlBuilder extends CoXmlBuilder {

/**
 * CoPageItemXmlBuilder constructor comment.
 */
public CoContentXmlBuilder() {
	super();
}


/**
 * CoPageItemXmlBuilder constructor comment.
 * @param xmlDoc com.sun.xml.tree.XmlDocument
 */
public CoContentXmlBuilder(XmlDocument xmlDoc) {
	super(xmlDoc);
}

/**
 * CoPageItemXmlBuilder constructor comment.
 * @param xmlDoc XmlDocument
 * @param builder com.bluebrim.xml.shared.CoXmlBuilderIF
 */
public CoContentXmlBuilder(XmlDocument xmlDoc, CoXmlBuilderIF builder) {
	super(xmlDoc, builder);
}
}