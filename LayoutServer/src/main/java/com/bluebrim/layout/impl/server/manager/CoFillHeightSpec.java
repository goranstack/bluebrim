package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.Node;

import com.bluebrim.layout.impl.shared.CoFillHeightSpecIF;
import com.bluebrim.layout.impl.shared.CoPageItemStringResources;
import com.bluebrim.layout.shared.*;

/**
 * Sätter höjden på sidelement så att de fyller ut ledigt utrymme
 */
 
public class CoFillHeightSpec extends CoFillSizeSpec implements CoFillHeightSpecIF
{

	// xml tag constants
	public static final String XML_TAG = "fill-height-spec";

/*
 * Used at XML import
 * Helena Rankegård 2001-10-26
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoFillHeightSpec( node, context );
}

CoFillHeightSpec()
{
	super();
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-26
 */
 
protected CoFillHeightSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	super( node, context );
	
	// xml init
}


public String getDescription()
{
	return CoPageItemStringResources.getName(FILL_HEIGHT_SPEC);
}


public String getFactoryKey()
{
	return FILL_HEIGHT_SPEC;
}


public static CoFillHeightSpec getInstance()
{
	return (CoFillHeightSpec) getFactory().getFillHeightSpec();
}


public String getXmlTag()
{
	return XML_TAG;
}


public void setSizeAfterLocation ( CoLayoutableIF layoutable, CoLocationSpec locationSpec )
{
	double h = m_originalValue;
	
	if
		( hasValidParent( layoutable, true ) && layoutable.getLayoutParent().getLayoutHeight() < Double.MAX_VALUE )
	{
		h = locationSpec.getHeightAfterLocation( layoutable);
	}
	
	if
		( h < 0 )
	{
		// sätt ny topEdge
		if
			( h < layoutable.getLayoutHeight() )
		{
			layoutable.setLayoutY( layoutable.getTopEdge() + (layoutable.getLayoutHeight() -(-h)) );
		} else {
			layoutable.setLayoutY( layoutable.getTopEdge() - ((-h ) - layoutable.getLayoutHeight()) );
		}
		setLayoutableHeight( layoutable, -h );
	} else {
		setLayoutableHeight( layoutable, h );
	}
}


public void setSizeBeforeLocation( CoLayoutableIF layoutable )
{
	m_originalValue = layoutable.getLayoutHeight();
	layoutable.invalidateHeight();
//	setLayoutableHeight( layoutable, 0 );
}
}