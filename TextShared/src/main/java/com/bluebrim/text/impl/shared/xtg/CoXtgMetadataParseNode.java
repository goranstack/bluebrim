package com.bluebrim.text.impl.shared.xtg;

import java.io.*;

/**
 * Xtg meta data parse tree node
 * 
 * @author: Dennis
 */

public class CoXtgMetadataParseNode extends CoXtgParseNode
{
	private CoXtgAttributelistParseNode m_attributelistNode;
	private CoXtgMetadataParseNode m_nextNode;
/**
 * CoXtgRootParseNode constructor comment.
 */
public CoXtgMetadataParseNode() {
	super();
}
public void createXtg( PrintStream s )
{
	if
		( m_attributelistNode != null )
	{
		m_attributelistNode.createXtg( s );
	}
	if
		( m_nextNode != null )
	{
		m_nextNode.createXtg( s );
	}
}
public void dump( String indent )
{
	System.err.println( indent + this );
	if ( m_attributelistNode != null ) m_attributelistNode.dump( indent + "  " );
	if ( m_nextNode != null ) m_nextNode.dump( indent + "  " );
}
public void extract( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l )
{
}
public void extract( com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger logger )
{
}
public boolean isEmpty()
{
	return m_attributelistNode == null;
}
boolean parse( CoXtgParser p, CoXtgLogger l ) throws CoXtgParseException
{
	m_attributelistNode = new CoXtgAttributelistParseNode();
	if
		( m_attributelistNode.parse( p, l ) )
	{
		m_nextNode = new CoXtgMetadataParseNode();
		if ( ! m_nextNode.parse( p, l ) ) m_nextNode = null;
		
	} else {
		m_attributelistNode = null;
	}
	
	return true;
}
}
