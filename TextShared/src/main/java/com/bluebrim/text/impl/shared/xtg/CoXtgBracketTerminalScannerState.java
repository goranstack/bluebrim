package com.bluebrim.text.impl.shared.xtg;

/**
 * Xtg scanner state (see CoXtgScanner)
 * 
 * @author: Dennis
 */
 
class CoXtgBracketTerminalScannerState extends CoXtgTerminalScannerState
{
public CoXtgBracketTerminalScannerState( String name )
{
	super( name );
}
protected String getText( CoXtgScanner sc )
{
	return sc.m_token.toString();
}
protected CoXtgToken getToken( CoXtgScanner sc )
{
	return new CoXtgBracketToken( getText( sc ) );
}
}
