package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

//

public class CoProportionalWidthSpec extends CoProportionalSizeSpec implements CoProportionalWidthSpecIF
{

	// xml tag constants
	public static final String XML_TAG = "proportional-width-spec";
/*
 * Used at XML import
 * Helena Rankegård 2001-10-26
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoProportionalWidthSpec( node, context );
}

/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-26
 */
 
protected CoProportionalWidthSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	super( node, context );
	
	// xml init
}


/**
 * CoProportionalHeightSpec constructor comment.
 */
public CoProportionalWidthSpec( boolean useTopLevelAncestorAsBase, double maxProportionValue )
{
	super( useTopLevelAncestorAsBase, maxProportionValue );
}


/**
 * CoProportionalHeightSpec constructor comment.
 */
public CoProportionalWidthSpec( boolean useTopLevelAncestorAsBase, double maxProportionValue, double proportion )
{
	super( useTopLevelAncestorAsBase, maxProportionValue, proportion );
}


public void configure( CoShapePageItemIF pi )
{
	CoLayoutableIF p = getProportionBaseSupplyingParent( pi );

	if
		( p != null )
	{
		m_proportion = m_maxProportionValue * pi.getCoShape().getWidth() / p.getInteriorWidth();
	}
}


public String getDescription()
{
	return CoPageItemStringResources.getName(PROPORTIONAL_WIDTH_SPEC);
}


public String getFactoryKey()
{
	return PROPORTIONAL_WIDTH_SPEC;
}


public String getXmlTag()
{
	return XML_TAG;
}


public void setSizeBeforeLocation( CoLayoutableIF layoutable )
{
	CoLayoutableIF p = getProportionBaseSupplyingParent( layoutable );

	if
		( p != null ) //( hasValidParent( layoutable, true ) )
	{
		double W = p.getInteriorWidth();
		if ( W < Double.MAX_VALUE ) W *= m_proportion / m_maxProportionValue; // Double.MAX_VALUE is sort of the same thing as infinity here.
		
		setLayoutableWidth( layoutable, W );
	}

}
}