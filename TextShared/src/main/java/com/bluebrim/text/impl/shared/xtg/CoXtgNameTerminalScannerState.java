package com.bluebrim.text.impl.shared.xtg;

/**
 * Xtg scanner state (see CoXtgScanner)
 * 
 * @author: Dennis
 */
 
class CoXtgNameTerminalScannerState extends CoXtgTerminalScannerState
{
public CoXtgNameTerminalScannerState( String name )
{
	super( name, false, false );
}
public CoXtgNameTerminalScannerState( String name, boolean keep, boolean consume )
{
	super( name, keep, consume );
}
protected String getText( CoXtgScanner sc )
{
	return sc.m_token.toString();
}
protected CoXtgToken getToken( CoXtgScanner sc )
{
	return new CoXtgNameToken( getText( sc ) );
}
}
