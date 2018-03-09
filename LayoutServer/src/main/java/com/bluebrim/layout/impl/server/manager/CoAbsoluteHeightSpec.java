package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.*;

import com.bluebrim.base.shared.debug.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Specificerar ett sidelements bredd.
 */
public class CoAbsoluteHeightSpec extends CoAbsoluteSizeSpec implements CoAbsoluteHeightSpecIF
{

	// xml tag constants
	public static final String XML_TAG = "absolute-height-spec";

/*
 * Used at XML import
 * Helena Rankegård 2001-10-26
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoAbsoluteHeightSpec( node, context );
}

public CoAbsoluteHeightSpec()
{
	super();
}


public CoAbsoluteHeightSpec( double distance )
{
	super( distance );
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-26
 */
 
protected CoAbsoluteHeightSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	super( node, context );
	
	// xml init
}


public void configure( CoShapePageItemIF pi )
{
	m_distance = pi.getCoShape().getHeight();
}


public String getDescription()
{
	return CoPageItemStringResources.getName(ABSOLUTE_HEIGHT_SPEC);
}


public String getFactoryKey()
{
	return ABSOLUTE_HEIGHT_SPEC;
}


public String getXmlTag()
{
	return XML_TAG;
}


/*
 * Sätter höjden i pixels på pageItem baserat på mottagarens distance
 */
public void setSizeBeforeLocation( CoLayoutableIF layoutable )
{
	if
		( CoAssertion.PRE )
	{
//			CoAssertion.preCondition( ( m_distance != null ), "distance == null in CoAbsoluteHeightSpec.setSizeBeforeLocation");
		CoAssertion.preCondition( ( m_distance >= 0 ), "negative distance in CoAbsoluteHeightSpec.setSizeBeforeLocation");
	}
	
	setLayoutableHeight( layoutable, m_distance );

}
}