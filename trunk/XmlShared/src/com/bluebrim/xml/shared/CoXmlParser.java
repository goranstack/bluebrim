package com.bluebrim.xml.shared;

import java.util.*;

import org.apache.crimson.tree.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * A CoXmlParser parses a XmlDocument through the DOM and produces a
 * corresponding business object by traversing the XmlDocument in a
 * "clever" order while sending a series of messages to a builder object which
 * in turn instantiates business objects and assembles those into a business
 * object.
 *
 * CoXmlParser is an abstract class encompassing all the different parsers.
 * It has primarily a reference to the XmlDocument being parsed, the current node
 * being parsed and a reference to the builder being used.
 *
 * The place where most of the "parsing action" is carried out is in the base
 * implementation class CoBasicXmlParser which most other concrete parsers subclass.
 * 
 * The CoBasicModelBuilder is the default builder which
 * can spawn other builders for certain elements.
 *
 * The pullWalker is a treewalker that is used when the builder "counters" with
 * "pulling" messages. The pullwalker is then used on the current node (which means
 * it traverses that nodes subnodes).
 *
 * Subclasses are meant to be specific parsers for specific business objects. 
 */
public abstract class CoXmlParser implements com.bluebrim.xml.shared.CoXmlParserIF {
	protected XmlDocument document;
	protected CoModelBuilderIF builder = new CoBasicModelBuilder();
	protected Node currentNode;
	protected TreeWalker pullWalker;

public String toString() {
	if (currentNode != null) {
		return "\"" + currentNode.getNodeName() + "\" parser";
	} else {
		String className = getClass().toString();
		
		// Strip the package junk from the class name to make it more readable
		className =
			className.substring(
				className.lastIndexOf(".") + 1,
				className.length());

		if (getBuilder() != null) {
			return className + "(" + getBuilder().toString() + ")";
		} else {
			return className;
		}
	}
}

/**
 * CoXmlParser constructor comment.
 */
public CoXmlParser() {

}


/**
 * CoXmlParser constructor comment.
 */
public CoXmlParser(XmlDocument doc) {
	this();
	document = doc;
}


/**
 * Get the parser's builder. Not really used yet but could be useful.
 */
public CoModelBuilderIF getBuilder() {
	return builder;
}


	public abstract Object go(Object parentModel, Node node, com.bluebrim.xml.shared.CoXmlContext context)
		throws SAXParseException, com.bluebrim.xml.shared.CoXmlReadException;


/**
 * Parse the node. Default just calls the builder.addNode(Node).
 * Override this for a simple builder.
 */
public void parseNode(Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	builder.addNode(node, context);
}


/**
 * Starts off the parsing process, handles errors and returns a model.
 * Calls the traverse method (which is implemented by subclasses)
 * which does the actual job.
 */
public Object produceModel(com.bluebrim.xml.shared.CoXmlContext context) throws CoXmlReadException {
	Object model = null;
	try {
		model = go(null, document.getDocumentElement(), context);
	} catch (SAXParseException err) {
		throw new CoXmlReadException("Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId()+ " " + err.getMessage());
//		System.out.println("*** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
//		System.out.println("   " + err.getMessage());
 
	} catch (SAXException e) {
		throw new CoXmlReadException(e);
//		Exception x = e.getException();
//		((x == null) ? e : x).printStackTrace();
	} catch (Exception e) {
		throw new CoXmlReadException(e);
//		t.printStackTrace();
	}
	return model;
}


/**
 * Pull a Node from the parser. This is a callback for the builder
 * to use during parsing. It is restricted in such a way that it
 * will only pull nodes residing "within" the last Node sent to the builder.
 * After that we return null so that the builder does not go past the node
 * it was told to build.
 */
public Node pullNode() {
	if (pullWalker == null) {
		pullWalker = new TreeWalker(currentNode);
	}
	return pullWalker.getNext();
}


/**
 * Register the parser in a cache, should
 * only do it if this parser can be "reused".
 * Default implementation does this.
 */
public void registerInCache(HashMap cache, String elementName) {
	cache.put(elementName, this);
}


/**
 * Set the document to parse.
 */
public void setDocument(XmlDocument doc) {
	document = doc;
}


/**
 * Set the parsercache.
 * The default implementation does nothing.
 */
public void setParserCache(HashMap cache) {

}
}