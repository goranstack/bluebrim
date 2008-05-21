package com.bluebrim.xml.shared;

import org.apache.crimson.tree.*;

/**
 * Is used to export a collection of items. Builds an enclosing node
 * under which the content of a top collection is exported
 * <p>
 * Creation date: (1999-10-26 10:06:03)
 *
 * @author: Mikael Printz
 *
 * @deprecated {@link com.bluebrim.xml.shared.CoXmlVisitorIF.export(String name, Collection exportMe)} should be used for
 * exporting <code>Collection</code>s.
 */
public class CoCollectionXmlBuilder extends CoXmlBuilder {
/**
 * CoCollectionXmlBuilder constructor comment.
 */
public CoCollectionXmlBuilder() {
	super();
}
/**
 * CoCollectionXmlBuilder constructor comment.
 */
public CoCollectionXmlBuilder(XmlDocument xmlDoc) {
	super(xmlDoc);
}
/**
 * CoCollectionXmlBuilder constructor comment.
 * @param xmlDoc com.sun.xml.tree.XmlDocument
 * @param builder com.bluebrim.xml.shared.CoXmlBuilderIF
 */
public CoCollectionXmlBuilder(XmlDocument xmlDoc, com.bluebrim.xml.shared.CoXmlBuilderIF builder) {
	super(xmlDoc, builder);
}
/**
 * getTag method comment.
 */
protected String getTag() {
	// The name of the tag is based on the context
	return getVisitor().getContext().getTopNodeName();
}
}