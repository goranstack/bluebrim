package com.bluebrim.text.impl.shared.xtg;

import java.io.*;

/**
 * Xtg definition specification parse tree node
 * 
 * @author: Dennis
 */
 
public class CoXtgDefspecParseNode extends CoXtgParseNode
{
	private CoXtgQstringParseNode m_qstring0Node;
	private CoXtgQstringParseNode m_qstring1Node;
	private CoXtgQstringParseNode m_qstring2Node;
	private CoXtgQstringParseNode m_qstring3Node;
/**
 * CoXtgRootParseNode constructor comment.
 */
public CoXtgDefspecParseNode() {
	super();
}
public void createXtg( PrintStream s )
{
	s.print( "[S" );

	if ( m_qstring0Node != null ) s.print( "\"" + m_qstring0Node.getQstring() + "\"" );
	if ( m_qstring1Node != null ) s.print( ",\"" + m_qstring1Node.getQstring() + "\"" );
	if ( m_qstring2Node != null ) s.print( ",\"" + m_qstring2Node.getQstring() + "\"" );
	if ( m_qstring3Node != null ) s.print( ",\"" + m_qstring3Node.getQstring() + "\"" );
	s.print( "]" );
}
public void dump( String indent )
{
	System.err.print( indent + this + " [ " );
	if ( m_qstring0Node != null ) System.err.print( "\"" + m_qstring0Node.getQstring() + "\"" );
	if ( m_qstring1Node != null ) System.err.print( ", \"" + m_qstring1Node.getQstring() + "\"" );
	if ( m_qstring2Node != null ) System.err.print( ", \"" + m_qstring2Node.getQstring() + "\"" );
	if ( m_qstring3Node != null ) System.err.print( ", \"" + m_qstring3Node.getQstring() + "\"" );
	System.err.println( " ]" );
}
public void extract( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l )
{
}
public void extract( com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger logger )
{
}
CoXtgQstringParseNode getString( int i )
{
	switch
		( i )
	{
		case 0 : return m_qstring0Node;
		case 1 : return m_qstring1Node;
		case 2 : return m_qstring2Node;
		case 3 : return m_qstring3Node;
		default : return null;
	}
}
boolean parse( CoXtgParser p, CoXtgLogger l ) throws CoXtgParseException
{
	CoXtgToken t = p.getToken();

	if
		( ! ( t instanceof CoXtgNameToken ) )
	{
		return false;
	}

	if ( ! ( (CoXtgNameToken) t ).getString().equals( "S" ) ) throw new CoXtgParseException( "Expected S" );
	
	t = p.nextToken();
	if
		( t instanceof CoXtgQuotedStringToken )
	{
		m_qstring0Node = new CoXtgQstringParseNode( ( (CoXtgQuotedStringToken) t ).getString() );
		t = p.nextToken();
		if
			( t instanceof CoXtgCommaToken )
		{
			t = p.nextToken();
			checkToken( t, CoXtgQuotedStringToken.class );
			
			m_qstring1Node = new CoXtgQstringParseNode( ( (CoXtgQuotedStringToken) t ).getString() );
			t = p.nextToken();
			if
				( t instanceof CoXtgCommaToken )
			{
				t = p.nextToken();
				checkToken( t, CoXtgQuotedStringToken.class );
				m_qstring2Node = new CoXtgQstringParseNode( ( (CoXtgQuotedStringToken) t ).getString() );
				t = p.nextToken();
				if
					( t instanceof CoXtgCommaToken )
				{
					t = p.nextToken();
					checkToken( t, CoXtgQuotedStringToken.class );
					m_qstring3Node = new CoXtgQstringParseNode( ( (CoXtgQuotedStringToken) t ).getString() );
					t = p.nextToken();
				}
			}
		}
	}

	return true;
}
}
