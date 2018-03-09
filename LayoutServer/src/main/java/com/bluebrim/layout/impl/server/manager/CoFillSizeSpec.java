package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

//
 
public abstract class CoFillSizeSpec extends CoRelativeSizeSpec
{
	protected transient double m_originalValue;

/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-26
 */
 
protected CoFillSizeSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	super( node, context );
	
	// xml init
}

public CoFillSizeSpec()
{
	super();
}


public void configure( CoShapePageItemIF pi )
{
}


public boolean equals( Object o )
{
	if ( this == o ) return true;
	if
		( getClass().isInstance( o ) )
	{
		CoFillSizeSpec s = (CoFillSizeSpec) o;
		
		return m_isSticky == s.m_isSticky;
	} else {
		return super.equals( o );
	}
}
}