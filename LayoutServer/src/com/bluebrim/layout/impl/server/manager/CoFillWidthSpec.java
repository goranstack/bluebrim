package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.Node;

import com.bluebrim.layout.impl.shared.CoFillWidthSpecIF;
import com.bluebrim.layout.impl.shared.CoPageItemStringResources;
import com.bluebrim.layout.shared.*;

/**
 * Sätter bredden på sidelement så att de fyller ut ledigt utrymme
 */
 
public class CoFillWidthSpec extends CoFillSizeSpec implements CoFillWidthSpecIF
{

	// xml tag constants
	public static final String XML_TAG = "fill-width-spec";
/*
 * Used at XML import
 * Helena Rankegård 2001-10-26
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoFillWidthSpec( node, context );
}

CoFillWidthSpec()
{
	super();
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-26
 */
 
protected CoFillWidthSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	super( node, context );
	
	// xml init
}


public String getDescription()
{
	return CoPageItemStringResources.getName(FILL_WIDTH_SPEC);
}


public String getFactoryKey()
{
	return FILL_WIDTH_SPEC;
}


public static CoFillWidthSpec getInstance()
{
	return (CoFillWidthSpec) getFactory().getFillWidthSpec();
}


public String getXmlTag()
{
	return XML_TAG;
}


public void setSizeAfterLocation ( CoLayoutableIF layoutable, CoLocationSpec locationSpec )
{
	double w = m_originalValue;
	
	if
		( hasValidParent( layoutable, true ) && ( layoutable.getLayoutParent().getLayoutWidth() < Double.MAX_VALUE ) )
	{
		w = locationSpec.getWidthAfterLocation( layoutable );
	}
	
	if
		( w < 0 )
	{
		// sätt ny leftEdge
		if
			( w < layoutable.getLayoutWidth() )
		{
			layoutable.setLayoutX( layoutable.getLeftEdge() + ( layoutable.getLayoutWidth() - ( - w ) ) );
		} else {
			layoutable.setLayoutX( layoutable.getLeftEdge() - ( ( - w ) - layoutable.getLayoutWidth() ) );
		}
		setLayoutableWidth( layoutable, -w );
	} else {
		setLayoutableWidth( layoutable, w );
	}
}


public void setSizeBeforeLocation( CoLayoutableIF layoutable )
{
	m_originalValue = layoutable.getLayoutWidth();
	layoutable.invalidateWidth();
//	setLayoutableWidth( layoutable, 0 );
}
}