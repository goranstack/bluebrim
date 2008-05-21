package com.bluebrim.text.impl.shared.xtg;

import java.io.*;
import java.util.*;

/**
 * Xtg parameters parse tree node
 * 
 * @author: Dennis
 */
 
public class CoXtgParamsParseNode extends CoXtgParseNode
{
	private CoXtgParamParseNode m_paramNode;
	private CoXtgParamsParseNode m_nextNode;
/**
 * CoXtgRootParseNode constructor comment.
 */
public CoXtgParamsParseNode() {
	super();
}
void collect( StringBuffer s )
{
	m_paramNode.collect( s );
	
	if
		( m_nextNode != null )
	{
		s.append( "," );
		m_nextNode.collect( s );
	}
}
public void createXtg( PrintStream s )
{
	m_paramNode.createXtg( s );
	
	if
		( m_nextNode != null )
	{
		s.print( "," );
		m_nextNode.createXtg( s );
	}
}
public void dump()
{
	if ( m_paramNode != null ) m_paramNode.dump();
	if
		( m_nextNode != null )
	{
		System.err.print( ", " );
		m_nextNode.dump();
	}
}
public void dump( String indent )
{
	System.err.println( indent + this );
	if ( m_paramNode != null ) m_paramNode.dump( indent + "  " );
	if ( m_nextNode != null ) m_nextNode.dump( indent + "  " );
}
public void extract( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l )
{
}
public void extract( com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger l )
{
}
List getParameters()
{
	return getParameters( new ArrayList() );
}
List getParameters( List l )
{
	if ( l == null ) l = new ArrayList();
	
	if
		( m_paramNode != null )
	{
		l.add( m_paramNode );
	}
	
	if
		( m_nextNode != null )
	{
		m_nextNode.getParameters( l );
	}

	return l;
}
boolean parse( CoXtgParser p, CoXtgLogger l ) throws CoXtgParseException
{
	m_paramNode = new CoXtgParamParseNode();
	m_paramNode.parse( p, l );

	if
		( p.getToken() instanceof CoXtgCommaToken )
	{
		p.nextToken();
		m_nextNode = new CoXtgParamsParseNode();
		m_nextNode.parse( p, l );
	}

	return true;
}
}
