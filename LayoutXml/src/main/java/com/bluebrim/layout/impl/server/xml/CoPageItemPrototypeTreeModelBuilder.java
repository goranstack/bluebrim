package com.bluebrim.layout.impl.server.xml;

import org.w3c.dom.*;

import com.bluebrim.layout.impl.server.*;
import com.bluebrim.xml.shared.*;

/**
 * Creation date: (2000-09-27 11:00:09)
 * @author: Monika
 */
 
public class CoPageItemPrototypeTreeModelBuilder extends CoModelBuilder
{
public CoPageItemPrototypeTreeModelBuilder() {
	super();
}


public CoPageItemPrototypeTreeModelBuilder(CoXmlParserIF parser) {
	super(parser);
}


public void addNode(Node node, CoXmlContext context) {
}


public void addSubModel( String name, Object submodel, CoXmlContext context )
{
	( (CoPageItemPrototypeTreeNode) m_model ).xmlAddSubModel( null, submodel, context );
}


public void createModel( Object superModel, Node node, CoXmlContext context ) throws CoXmlReadException
{
	CoPageItemPrototypeTreeNode n = null;
	
	if
		( node.getNodeName().equals( CoPageItemPrototypeTreeNode.XML_TAG ) )
	{
		n = new CoPageItemPrototypeTreeNode( null );
	} else {
		n = new CoPageItemPrototypeTreeRoot( "xml", null );
	}
	
	n.xmlInit( getAllAttrAsMap( node ), context );
	m_model = n;
}


public void xmlImportFinished(Node node, CoXmlContext context)
{
	( (CoPageItemPrototypeTreeNode) m_model ).xmlDone();
}
}