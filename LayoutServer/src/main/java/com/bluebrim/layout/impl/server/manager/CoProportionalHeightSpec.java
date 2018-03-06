package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

//

public class CoProportionalHeightSpec extends CoProportionalSizeSpec implements CoProportionalHeightSpecIF
{

	// xml tag constants
	public static final String XML_TAG = "proportional-height-spec";

/*
 * Used at XML import
 * Helena Rankegård 2001-10-26
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoProportionalHeightSpec( node, context );
}

/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-26
 */
 
protected CoProportionalHeightSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	super( node, context );
	
	// xml init
}


/**
 * CoProportionalHeightSpec constructor comment.
 */
public CoProportionalHeightSpec( boolean useTopLevelAncestorAsBase, double maxProportionValue )
{
	super( useTopLevelAncestorAsBase, maxProportionValue );
}


/**
 * CoProportionalHeightSpec constructor comment.
 */
public CoProportionalHeightSpec( boolean useTopLevelAncestorAsBase, double maxProportionValue, double proportion )
{
	super( useTopLevelAncestorAsBase, maxProportionValue, proportion );
}


public void configure( CoShapePageItemIF pi )
{
	CoLayoutableIF p = getProportionBaseSupplyingParent( pi );

	if
		( p != null )
	{
		m_proportion = m_maxProportionValue * pi.getCoShape().getHeight() / p.getInteriorHeight();
	}
}


public String getDescription()
{
	return CoPageItemStringResources.getName(PROPORTIONAL_HEIGHT_SPEC);
}


public String getFactoryKey()
{
	return PROPORTIONAL_HEIGHT_SPEC;
}


public String getXmlTag()
{
	return XML_TAG;
}


public void setSizeBeforeLocation( CoLayoutableIF layoutable )
{
	CoLayoutableIF p = getProportionBaseSupplyingParent( layoutable );

	if
		( p != null )
	{
		double H = p.getInteriorHeight();
		if ( H < Double.MAX_VALUE ) H *= m_proportion / m_maxProportionValue; // Double.MAX_VALUE is sort of the same thing as infinity here.
	
		setLayoutableHeight( layoutable, H );
	}
}
}