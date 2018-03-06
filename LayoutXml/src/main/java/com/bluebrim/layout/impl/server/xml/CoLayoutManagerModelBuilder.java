package com.bluebrim.layout.impl.server.xml;

import org.w3c.dom.*;

import com.bluebrim.layout.impl.server.manager.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layoutmanager.*;
import com.bluebrim.xml.shared.*;

/**
 * 
 * @author: Dennis
 */
 
public class CoLayoutManagerModelBuilder extends CoSimpleModelBuilder
{
/**
 * CoLayoutSpecModelBuilder constructor comment.
 * @param parser CoXmlParserIF
 */
public CoLayoutManagerModelBuilder(CoXmlParserIF parser) {
	super(parser);
}


/**
 * Create the model.
 */
public void createModel(Object superModel, Node node, CoXmlContext context )
{
	String type = node.getNodeName();

	if      ( type.equals( CoAdPlacementLayoutManager.XML_TAG ) ) m_model = new CoAdPlacementLayoutManager();
	else if ( type.equals( CoColumnLayoutManager.XML_TAG ) ) m_model = CoColumnLayoutManager.getInstance();
	else if ( type.equals( CoExjobbLayoutManager.XML_TAG ) ) m_model = new CoExjobbLayoutManager();
	else if ( type.equals( CoNoLayoutManager.XML_TAG ) ) m_model = CoNoLayoutManager.getInstance();
	else if ( type.equals( CoReversedColumnLayoutManager.XML_TAG ) ) m_model = CoReversedColumnLayoutManager.getInstance();
	else if ( type.equals( CoReversedRowLayoutManager.XML_TAG ) ) m_model = CoReversedRowLayoutManager.getInstance();
	else if ( type.equals( CoRowLayoutManager.XML_TAG ) ) m_model = new CoRowLayoutManager();
	else if ( type.equals( CoRectangleLayoutManager.XML_TAG ) ) m_model = new CoRectangleLayoutManager();

	if
		( m_model != null )
	{
		( (CoLayoutManager) m_model ).xmlInit( getAllAttrAsMap( node ) );
	}
}
}