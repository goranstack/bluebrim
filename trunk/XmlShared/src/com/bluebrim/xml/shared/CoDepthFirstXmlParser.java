package com.bluebrim.xml.shared;


import org.apache.crimson.tree.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * CoDepthFirstXmlParser is a general base implementation of a parser
 * that uses a preorder depth first traversal.
 * 
 * It essentially implements traverse(Node).
 */
public abstract class CoDepthFirstXmlParser extends CoXmlParser {
	protected TreeWalker walker;
/**
 * CoXmlParser constructor comment.
 */
public CoDepthFirstXmlParser() {
	super();
}
/**
 * CoXmlParser constructor comment.
 */
public CoDepthFirstXmlParser(XmlDocument doc) {
	super(doc);
}


/**
 * Traverse the XML document element using the DOM, preorder depth first.
 * Calls parseNode(Node) for each element. Override this for more
 * intricate traversal.
 */
public Object traverse(Node startNode, com.bluebrim.xml.shared.CoXmlContext context) throws SAXParseException, com.bluebrim.xml.shared.CoXmlReadException {
	TreeWalker walker;
	Node node;
	com.bluebrim.xml.shared.CoXmlParserIF newParser;
	Object subModel;

	currentNode = startNode;	
	// Uses a TreeWalker to traverse preorder depth first.
	walker = new TreeWalker(currentNode);
	// Let the builder create an "empty" model.
	builder.createModel(null, currentNode, context);

	currentNode = walker.getCurrent();
	while (currentNode != null) {
		pullWalker = null;
		parseNode(currentNode, context);
		currentNode = walker.getNext();
	}
	// Let the builder finalize the model and return it.
	return builder.getModel(startNode, context);
}
}