package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.Node;

import com.bluebrim.layout.impl.shared.CoContentHeightSpecIF;
import com.bluebrim.layout.impl.shared.CoPageItemStringResources;
import com.bluebrim.layout.shared.*;

/**
 * CoContentHeight kan till exempel användas för att anpassa höjden på
 * en CoTextShape så att hela texten syns.
 */
 
public class CoContentHeightSpec extends CoContentSize implements CoContentHeightSpecIF
{

	// xml tag constants
	public static final String XML_TAG = "content-height-spec";
/*
 * Used at XML import
 * Helena Rankegård 2001-10-26
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoContentHeightSpec( node, context );
}

/**
 * CoContentHeightSpec constructor comment.
 */
public CoContentHeightSpec()
{
	super();
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-26
 */
 
protected CoContentHeightSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	super( node, context );
	
	// xml init
}


public String getDescription()
{
	return CoPageItemStringResources.getName(CONTENT_HEIGHT_SPEC);
}


public String getFactoryKey()
{
	return CONTENT_HEIGHT_SPEC;
}


public String getXmlTag()
{
	return XML_TAG;
}


public void setSizeBeforeLocation( CoLayoutableIF layoutable )
{
	double h = layoutable.getContentHeight() * ( 1 + m_relativeOffset ) + m_absoluteOffset;

	layoutable.reshapeToContentHeight();
	setLayoutableHeight( layoutable, h );
}
}