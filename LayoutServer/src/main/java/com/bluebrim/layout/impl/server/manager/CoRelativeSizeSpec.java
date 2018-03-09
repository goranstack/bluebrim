package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.*;

/**
 * Abstrakt superklass för klasser som uttrycker höjd eller bredd 
 * i relation till layout-arean.
 * 
 */
public abstract class CoRelativeSizeSpec extends CoSizeSpec
{
/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-26
 */
 
protected CoRelativeSizeSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	super( node, context );
	
	// xml init
}

/**
 * CoRelativeSizeSpec constructor comment.
 */
public CoRelativeSizeSpec()
{
	super();
}
}