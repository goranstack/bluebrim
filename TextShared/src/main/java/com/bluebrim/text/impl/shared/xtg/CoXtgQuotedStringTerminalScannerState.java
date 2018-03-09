package com.bluebrim.text.impl.shared.xtg;

/**
 * Xtg scanner state (see CoXtgScanner)
 * 
 * @author: Dennis
 */
 
class CoXtgQuotedStringTerminalScannerState extends CoXtgTerminalScannerState
{
public CoXtgQuotedStringTerminalScannerState( String name )
{
	super( name, false, true );
}
protected String getText( CoXtgScanner sc )
{
	return sc.m_token.substring( 1 );
}
protected CoXtgToken getToken( CoXtgScanner sc )
{
	return new CoXtgQuotedStringToken( getText( sc ) );
}
}
