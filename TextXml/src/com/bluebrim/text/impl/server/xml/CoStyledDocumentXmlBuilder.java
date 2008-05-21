package com.bluebrim.text.impl.server.xml;
import org.apache.crimson.tree.*;
import org.w3c.dom.*;

import com.bluebrim.base.shared.debug.*;
import com.bluebrim.xml.shared.*;

public class CoStyledDocumentXmlBuilder extends CoXmlBuilder {
public CoStyledDocumentXmlBuilder() {
	super();
}
public CoStyledDocumentXmlBuilder(org.apache.crimson.tree.XmlDocument xmlDoc) {
	super(xmlDoc);
}
public CoStyledDocumentXmlBuilder(org.apache.crimson.tree.XmlDocument xmlDoc, com.bluebrim.xml.shared.CoXmlBuilderIF builder) {
	super(xmlDoc, builder);
}
protected String getTag() {
	return com.bluebrim.text.shared.CoStyledDocument.XML_TAG;
}
/**
 * This method adds a Node hierarchy representing a <code>com.bluebrim.text.shared.CoStyledDocument</code>
 * to the current tree.
 *
 * @param node A node representing a <code>com.bluebrim.text.shared.CoStyledDocument</code>
 */
public void exportNode(final Node node)
{
	CoAssertion.assertTrue(
		node.getNodeName() == com.bluebrim.text.shared.CoStyledDocument.XML_TAG,
		"Internal error exporting styled text");
	CoAssertion.assertTrue(
		getNode().getNodeName() == com.bluebrim.text.shared.CoStyledDocument.XML_TAG,
		"Internal error exporting styled text (2)");

	// Add the new node's children to the current tree
	int i;
	NodeList childNodes = node.getChildNodes();

	for (i = 0; i < childNodes.getLength(); i++)
	{	
		getNode().appendChild(m_xmlDocument.importNode(childNodes.item(i), true));
	}

	// Copy all attributes from node to the current Node.
	NamedNodeMap attributes = ((ElementNode)node).getAttributes();

	for (i = 0; i < attributes.getLength(); i++)
	{
		Attr attribute = (Attr)(attributes.item(i));
		((ElementNode)getNode()).setAttribute(attribute.getName(), attribute.getValue());
	}
}
}