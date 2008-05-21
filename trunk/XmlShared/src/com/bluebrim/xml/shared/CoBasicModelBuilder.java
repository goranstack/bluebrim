package com.bluebrim.xml.shared;

import org.w3c.dom.*;

/**
 * This is the default model builder which just writes information on
 * System.out. It can be used when debugging parsers.
 */
public class CoBasicModelBuilder extends com.bluebrim.xml.shared.CoModelBuilder {
	String n;
/**
 * CoBasicModelBuilder constructor comment.
 */
public CoBasicModelBuilder() {
	this(null);
}
/**
 * CoBasicModelBuilder constructor comment.
 */
public CoBasicModelBuilder(com.bluebrim.xml.shared.CoXmlParserIF parser) {
	super(parser);
	if (parser!=null) {
		n = parser.toString();
	} else {
		n = "null";
	}
}




/**
 * Add the element to the model.
 */
public void addNode(Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	System.out.println("addNode{"+n+"}:"+node.getNodeName());
}



/**
 * Add the submodel to the model. 
 */
public void addSubModel(String name, Object model, com.bluebrim.xml.shared.CoXmlContext context) {
	System.out.println("addSubModel{"+n+"}:"+model);
}

/**
 * Create the model.
 */
public void createModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	System.out.println("createModel{"+n+"}:"+node.getNodeName());
}
}