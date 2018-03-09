package com.bluebrim.xml.shared;

import org.w3c.dom.*;

/**
 * Insert the type's description here.
 * Creation date: (2000-08-16 12:55:06)
 * @author: 
 */
public class CoStringWrapperModelBuilder extends CoSimpleModelBuilder{

/**
 * CoStringWrapperModelBuilder constructor comment.
 * @param parser CoXmlParserIF
 */
public CoStringWrapperModelBuilder(CoXmlParserIF parser) {
	super(parser);
}


public void createModel(Object superModel, Node aNode, CoXmlContext context){

	m_model = new String(getAttrVal(aNode, "string"));
}
}