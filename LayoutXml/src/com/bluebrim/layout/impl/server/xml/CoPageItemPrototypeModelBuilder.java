package com.bluebrim.layout.impl.server.xml;

import org.w3c.dom.*;

import com.bluebrim.layout.impl.server.*;
import com.bluebrim.xml.shared.*;

/**
 * Creation date: (2000-09-27 11:00:09)
 * @author Monika
 */
 
public class CoPageItemPrototypeModelBuilder extends CoModelBuilder {

public CoPageItemPrototypeModelBuilder() {
	super();
}


public CoPageItemPrototypeModelBuilder(CoXmlParserIF parser) {
	super(parser);
}


public void addNode(Node node, CoXmlContext context) {}


public void addSubModel(String name, Object submodel, CoXmlContext context)
{
	( (CoPageItemPrototype) m_model ).xmlAddSubModel( null, submodel, context );
}




public void createModel(Object superModel, Node node, CoXmlContext context ) throws CoXmlReadException
{
	CoPageItemPrototype p = new CoPageItemPrototype( null, null, null );
	m_model = p;
	p.xmlInit( getAllAttrAsMap( node ), context );
}
}