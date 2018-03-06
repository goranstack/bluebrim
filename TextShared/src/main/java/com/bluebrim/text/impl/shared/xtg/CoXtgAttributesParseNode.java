package com.bluebrim.text.impl.shared.xtg;

import java.io.*;
import java.util.*;

/**
 * Xtg attributes parse tree node
 * 
 * @author: Dennis
 */
 
public class CoXtgAttributesParseNode extends CoXtgParseNode
{
	private CoXtgAttributeParseNode m_attributeNode;
	private CoXtgAttributesParseNode m_nextNode;
/**
 * CoXtgRootParseNode constructor comment.
 */
public CoXtgAttributesParseNode() {
	super();
}
void collect( StringBuffer s )
{
	if ( m_attributeNode != null ) m_attributeNode.collect( s );
	if ( m_nextNode != null ) m_nextNode.collect( s );
}
public void createXtg( PrintStream s )
{
	if ( m_attributeNode != null ) m_attributeNode.createXtg( s );
	if ( m_nextNode != null ) m_nextNode.createXtg( s );
}
public void dump( String indent )
{
//	System.err.println( indent + this );
	if ( m_attributeNode != null ) m_attributeNode.dump( indent + "  " );
	if ( m_nextNode != null ) m_nextNode.dump( indent + "  " );
}
void extract( com.bluebrim.text.shared.CoCharacterStyleIF cs, CoXtgLogger l )
{
	if ( m_attributeNode != null ) m_attributeNode.extract( cs, l );
	if ( m_nextNode != null ) m_nextNode.extract( cs, l );
}
void extract( com.bluebrim.text.shared.CoParagraphStyleIF ps, CoXtgLogger l )
{
	if ( m_attributeNode != null ) m_attributeNode.extract( ps, l );
	if ( m_nextNode != null ) m_nextNode.extract( ps, l );
}
public void extract( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l )
{
}
public void extract( com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger l )
{
}
List getTags( List l )
{
	if ( l == null ) l = new ArrayList();
	
	if
		( m_attributeNode != null )
	{
		l.add( m_attributeNode );
	}
	
	if
		( m_nextNode != null )
	{
		m_nextNode.getTags( l );
	}

	return l;
}
boolean parse( CoXtgParser p, CoXtgLogger l ) throws CoXtgParseException
{
	m_attributeNode = new CoXtgAttributeParseNode();

	if
		( m_attributeNode.parse( p, l ) )
	{
		m_nextNode = new CoXtgAttributesParseNode();
		if ( ! m_nextNode.parse( p, l ) ) m_nextNode = null;
		
	} else {
		m_attributeNode = null;
	}
	
	return true;
}
}
