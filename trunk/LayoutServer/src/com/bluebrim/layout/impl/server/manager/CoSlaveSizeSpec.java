package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.Node;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoPageItemStringResources;
import com.bluebrim.layout.impl.shared.CoSlaveSizeSpecIF;
import com.bluebrim.layout.shared.*;

/**
 * Size spec that is assigned to page items that are slaves.
 * Since slave geoemtry depends on its master, this size spec does nothing.
 *
 * @author: Dennis
 */
 
public class CoSlaveSizeSpec extends CoSizeSpec implements CoSlaveSizeSpecIF
{
	// xml tag constants
	public static final String XML_TAG = "slave-size-spec";
/*
 * Used at XML import
 * Helena Rankegård 2001-10-26
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoSlaveSizeSpec( node, context );
}

CoSlaveSizeSpec()
{
	super();

	m_isSticky = true;
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-26
 */
 
protected CoSlaveSizeSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context )
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
		( o instanceof CoSlaveSizeSpec )
	{
		CoSlaveSizeSpec s = (CoSlaveSizeSpec) o;
		
		return m_isSticky == s.m_isSticky;
	} else {
		return super.equals( o );
	}
}


public String getDescription()
{
	return CoPageItemStringResources.getName(SLAVE_SIZE_SPEC);
}


public String getFactoryKey()
{
	return SLAVE_SIZE_SPEC;
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