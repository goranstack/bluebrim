package com.bluebrim.text.impl.shared.xtg;

/**
 * Xtg scanner state (see CoXtgScanner)
 * 
 * @author: Dennis
 */
 
class CoXtgTextTerminalScannerState extends CoXtgTerminalScannerState
{
public CoXtgTextTerminalScannerState( String name )
{
	super( name, false, true );
}
public CoXtgTextTerminalScannerState( String name, boolean keep, boolean consume )
{
	super( name, keep, consume );
}
protected String getText( CoXtgScanner sc )
{
	return sc.m_token.toString();
}
protected CoXtgToken getToken( CoXtgScanner sc )
{
	return new CoXtgTextToken( getText( sc ) );
}
}
