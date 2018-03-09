package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.Node;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoNoSizeSpecIF;
import com.bluebrim.layout.impl.shared.CoPageItemStringResources;
import com.bluebrim.layout.shared.*;

/**
Denna sizespec används för sidelement vars storlek manipuleras via gränssnitt i editorn
som direkt sätter bredd och höjd med reshapemetoden, exempelvis vid editering via musen.
 */
 
public class CoNoSizeSpec extends CoSizeSpec implements CoNoSizeSpecIF
{
	// xml tag constants
	public static final String XML_TAG = "no-size-spec";

/*
 * Used at XML import
 * Helena Rankegård 2001-10-26
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoNoSizeSpec( node, context );
}

CoNoSizeSpec()
{
	super();

	m_isSticky = true;
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-26
 */
 
protected CoNoSizeSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	super( node, context );
	
	// xml init
}


public void configure( CoShapePageItemIF pi )
{
}


public CoSizeSpecIF deepClone()
{
	return this;
}


public boolean equals( Object o )
{
	if ( this == o ) return true;
	if
		( o instanceof CoNoSizeSpec )
	{
		CoNoSizeSpec s = (CoNoSizeSpec) o;
		
		return m_isSticky == s.m_isSticky;
	} else {
		return super.equals( o );
	}
}


public String getDescription()
{
	return CoPageItemStringResources.getName(NO_SIZE_SPEC);
}


public String getFactoryKey()
{
	return NO_SIZE_SPEC;
}


public String getXmlTag()
{
	return XML_TAG;
}


public boolean isNull()
{
	return true;
}
}