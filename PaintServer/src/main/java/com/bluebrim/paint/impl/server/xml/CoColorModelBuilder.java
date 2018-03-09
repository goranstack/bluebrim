package com.bluebrim.paint.impl.server.xml;
import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.paint.impl.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Builds colors from a cmyk-specification in the XML representation
 * Creation date: (1999-09-27 12:12:05)
 * @author: Mikael Printz

 * Total rewrite, 2001-04-18
 * @author: Dennis
 */
 
public class CoColorModelBuilder extends CoSimpleModelBuilder
{


/**
 * CoColorModelBuilder constructor comment.
 * @param parser CoXmlParserIF
 */
public CoColorModelBuilder(CoXmlParserIF parser) {
	super(parser);
}


public void addSubModel( String name, Object model, CoXmlContext context )
{
	if
		( m_model instanceof CoExtendedMultiInkColor.ShadedTrappableColor )
	{
		( (CoExtendedMultiInkColor.ShadedTrappableColor) m_model ).xmlAddSubModel( null, model, context );
	} else {
		( (CoColor) m_model ).xmlAddSubModel( null, model, context );
	}
}

/**
 * Create the model.
 */
public void createModel(Object superModel, Node node, CoXmlContext context )
{
	if
		( node.getNodeName().equals( CoExtendedMultiInkColor.ShadedTrappableColor.XML_TAG ) )
	{
		CoExtendedMultiInkColor.ShadedTrappableColor c = new CoExtendedMultiInkColor.ShadedTrappableColor();
		c.xmlInit( getAllAttrAsMap( node ), context );
		m_model = c;		
	} else {
		String type = getAttrVal( node, CoColor.XML_TYPE );
		CoColor c = (CoColor) CoFactoryManager.createObject( type );
		c.xmlInit( getAllAttrAsMap( node ), context );
		m_model = c;
	}

}
}