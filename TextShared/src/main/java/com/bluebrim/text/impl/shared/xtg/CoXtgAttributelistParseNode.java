package com.bluebrim.text.impl.shared.xtg;

import java.io.*;
import java.util.*;

/**
 * Xtg attribute list parse tree node
 * 
 * @author: Dennis
 */
 
public class CoXtgAttributelistParseNode extends CoXtgParseNode
{
	private CoXtgAttributesParseNode m_attributesNode;
/**
 * CoXtgRootParseNode constructor comment.
 */
public CoXtgAttributelistParseNode() {
	super();
}
void collect( StringBuffer s )
{
	s.append( "<" );
	m_attributesNode.collect( s );
	s.append( ">" );
}
public void createXtg( PrintStream s )
{
	s.print( "<" );
	m_attributesNode.createXtg( s );
	s.print( ">" );
}
public void dump( String indent )
{
	System.err.println( indent + this );
	System.err.println( indent + "<" );
	if ( m_attributesNode != null ) m_attributesNode.dump( indent + "  " );
	System.err.println( indent + ">" );
}
void extract( com.bluebrim.text.shared.CoCharacterStyleIF cs, CoXtgLogger l )
{
	if ( m_attributesNode != null ) m_attributesNode.extract( cs, l );
}
void extract( com.bluebrim.text.shared.CoParagraphStyleIF ps, CoXtgLogger l )
{
	if ( m_attributesNode != null ) m_attributesNode.extract( ps, l );
}
void extract( com.bluebrim.text.shared.CoTypographyRuleIF cs, CoXtgLogger l )
{
	if ( m_attributesNode != null ) m_attributesNode.extract( cs, l );
}
public void extract( com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger l )
{
}
List getTags( List l )
{
	return m_attributesNode.getTags( l );
}
boolean parse( CoXtgParser p, CoXtgLogger l ) throws CoXtgParseException
{
	if
		( ! ( p.getToken() instanceof CoXtgLessThanToken ) )
	{
		return false;
	}
	
	p.nextToken();
	
	m_attributesNode = new CoXtgAttributesParseNode();
	m_attributesNode.parse( p, l );

	checkToken( p.getToken(), CoXtgGreaterThanToken.class );
	p.nextToken();
	
	return true;
}
}
