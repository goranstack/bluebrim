package com.bluebrim.text.impl.shared.xtg;

import com.bluebrim.text.shared.*;


/**
 * Xtg file parser.
 * After an xtg file has been parsed, a document or typography rule can be extracted.
 * Formatted text is extracted to the document.
 * Stylesheets are extracted to the typography rule.
 *

The xtg language described is something close to BNF (all caps = terminal, - = nothing):
 
xtg		      := metadata defs paragraphs

metadata	  := taglist metadata | -

taglist		  := < tags >
tags		    := tag tags | -
tag		      := NAME | NAME QSTRING | NAME NUMBER | NAME ( params ) | NAME $
params		  := param | param , params
param		    := NUMBER | QSTRING | NAME | NUMBER % | +(NAME) NUMBER

defs		    := def defs | -
def		      := NAME = taglist | NAME = [ defspec ] taglist
defspec		  := S QSTRING , QSTRING, QSTRING , QSTRING | S QSTRING , QSTRING , QSTRING | S QSTRING , QSTRING | S QSTRING | S

paragraphs	:= paragraph paragraphs | -
paragraph	  := NAME : TEXT | taglist TEXT | taglist taglist TEXT

 *
 * @author: Dennis Malmström
 */

public class CoXtgParser implements CoXtgLogger
{
	private CoXtgParseNode m_root = createRootNode();

	private CoXtgScanner m_scanner;

	// look ahead buffer
	private CoXtgToken m_token;
	private CoXtgToken m_token2;

	private CoXtgLogger m_logger;
	
	private StringBuffer m_errorsAndWarnings = new StringBuffer();
	
	private static boolean TRACE = false;
public CoXtgParser( java.io.Reader r )
{
	this( r, null );
}
protected CoXtgParser( java.io.Reader r, CoXtgLogger l )
{
	m_logger = l;
	if ( m_logger == null ) m_logger = this;
	
	m_scanner = new CoXtgScanner( r, m_logger );
	m_token = m_scanner.scan();
	m_token2 = m_scanner.scan();
}
protected CoXtgParseNode createRootNode()
{
	return new CoXtgRootParseNode();
}
public void extract( CoTypographyRuleIF r )
{
	getRoot().extract( r, this );
}
public void extract( CoStyledDocumentIF d )
{
	getRoot().extract( d, this );
}
public String getLog()
{
	return m_errorsAndWarnings.toString();
}
CoXtgToken getNextToken()
{
	return m_token2;
}
CoXtgParseNode getRoot()
{
	return m_root;
}
CoXtgToken getToken()
{
	return m_token;
}
public void log( String msg )
{
	m_errorsAndWarnings.append( msg );
	m_errorsAndWarnings.append( "\n" );
}
CoXtgToken nextToken()
{
	m_token = m_token2;
	m_token2 = m_scanner.scan();

	trace( "TOKEN: " + m_token  );
	
	return m_token;
}
public boolean parse()
{
	try
	{
		return m_root.parse( this, m_logger );
	}
	catch ( CoXtgParseException ex )
	{
		m_logger.log( "Fatal parser error: " + ex );
		return false;
	}
}
private void trace( String msg )
{
	if ( TRACE ) System.err.println( msg );
}
}
