package com.bluebrim.text.impl.shared.xtg;

import java.io.*;

/**
 * Xtg parameter parse tree node
 * 
 * @author: Dennis
 */
 
public class CoXtgParamParseNode extends CoXtgParseNode
{
	private CoXtgNameParseNode m_nameNode;
	private CoXtgQstringParseNode m_qstringNode;
	private CoXtgNumberParseNode m_numberNode;
	private boolean m_hasPercent;
	private boolean m_isPositiveDelta;
/**
 * CoXtgRootParseNode constructor comment.
 */
public CoXtgParamParseNode() {
	super();
}
void collect( StringBuffer s )
{
	if      ( m_nameNode != null ) s.append( m_nameNode.getName() );
	else if ( m_qstringNode != null ) s.append( "\"" + m_qstringNode.getQstring() + "\"" );
	else if ( m_numberNode != null ) s.append( ( m_isPositiveDelta ? "+" : "" ) + m_numberNode.getString() );

	if ( m_hasPercent ) s.append( "%" );
}
public void createXtg( PrintStream s )
{
	if      ( m_nameNode != null ) s.print( m_nameNode.getName() );
	else if ( m_qstringNode != null ) s.print( "\"" + m_qstringNode.getQstring() + "\"" );
	else if ( m_numberNode != null ) s.print( ( m_isPositiveDelta ? "+" : "" ) + m_numberNode.getString() );

	if ( m_hasPercent ) s.print( "%" );
}
public void dump()
{
	if ( m_nameNode != null ) System.err.print( m_nameNode.getName() );
	if ( m_qstringNode != null ) System.err.print( "\"" + m_qstringNode.getQstring() + "\"" );
	if ( m_numberNode != null ) System.err.print( m_numberNode.getNumber() );
}
public void dump( String indent )
{
	System.err.println( indent + this );
	if ( m_nameNode != null ) m_nameNode.dump( indent + "  " );
	if ( m_qstringNode != null ) m_qstringNode.dump( indent + "  " );
	if ( m_numberNode != null ) m_numberNode.dump( indent + "  " );
}
public void extract( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l )
{
}
public void extract( com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger l )
{
}
String getName()
{
	return ( m_nameNode == null ) ? null : m_nameNode.getName();
}
float getNumber()
{
	return ( m_numberNode == null ) ? Float.NaN : m_numberNode.getNumber();
}
String getQstring()
{
	return ( m_qstringNode == null ) ? null : m_qstringNode.getQstring();
}
boolean hasPercent()
{
	return m_hasPercent;
}
boolean isPositiveDelta()
{
	return m_isPositiveDelta;
}
boolean parse( CoXtgParser p, CoXtgLogger l ) throws CoXtgParseException
{
	CoXtgToken t = p.getToken();

	if
		( t instanceof CoXtgNameToken )
	{
		if
			( ( (CoXtgNameToken) t ).getString().equals( "+" ) )
		{
			m_isPositiveDelta = true;
			t = p.nextToken();
			checkToken( t, CoXtgNumberToken.class );
			m_numberNode = new CoXtgNumberParseNode( ( (CoXtgNumberToken) t ).getString(), ( (CoXtgNumberToken) t ).getFloat() );
			p.nextToken();
			return true;
			
		} else {
			m_nameNode = new CoXtgNameParseNode( ( (CoXtgNameToken) t ).getString() );
			p.nextToken();
			return true;
		}

	} else if
		( t instanceof CoXtgQuotedStringToken )
	{
		m_qstringNode = new CoXtgQstringParseNode( ( (CoXtgQuotedStringToken) t ).getString() );
		p.nextToken();
		return true;

	} else if
		( t instanceof CoXtgNumberToken )
	{
		m_numberNode = new CoXtgNumberParseNode( ( (CoXtgNumberToken) t ).getString(), ( (CoXtgNumberToken) t ).getFloat() );
		t = p.nextToken();
		if
			( t instanceof CoXtgPercentToken )
		{
			p.nextToken();
			m_hasPercent = true;
		}
		return true;
		
	}
	
	throw new CoXtgParseException( "Expected name, qouted string or number" );
}
}
