package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.Node;

import com.bluebrim.layout.impl.shared.CoContentWidthSpecIF;
import com.bluebrim.layout.impl.shared.CoPageItemStringResources;
import com.bluebrim.layout.shared.*;

//

public class CoContentWidthSpec extends CoContentSize  implements CoContentWidthSpecIF
{

	// xml tag constants
	public static final String XML_TAG = "content-width-spec";
/*
 * Used at XML import
 * Helena Rankegård 2001-10-26
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoContentWidthSpec( node, context );
}

/**
 * CoContentHeightSpec constructor comment.
 */
public CoContentWidthSpec()
{
	super();
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-26
 */
 
protected CoContentWidthSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	super( node, context );
	
	// xml init
}


public String getDescription()
{
	return CoPageItemStringResources.getName(CONTENT_WIDTH_SPEC);
}


public String getFactoryKey()
{
	return CONTENT_WIDTH_SPEC;
}


public String getXmlTag()
{
	return XML_TAG;
}


public void setSizeBeforeLocation( CoLayoutableIF layoutable )
{
	double w = layoutable.getContentWidth() * ( 1 + m_relativeOffset ) + m_absoluteOffset;
	
	layoutable.reshapeToContentWidth();
	setLayoutableWidth( layoutable, w );
}
}