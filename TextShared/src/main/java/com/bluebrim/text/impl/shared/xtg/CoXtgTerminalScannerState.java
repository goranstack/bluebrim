package com.bluebrim.text.impl.shared.xtg;

/**
 * Terminal xtg scanner state (see CoXtgScanner)
 * 
 * @author: Dennis
 */
 
abstract class CoXtgTerminalScannerState extends CoXtgScannerState
{
public CoXtgTerminalScannerState( String name )
{
	super( name );
}
public CoXtgTerminalScannerState( String name, boolean keep, boolean consume )
{
	super( name, keep, consume );
}
protected CoXtgToken doScan( CoXtgScanner sc )
{
	CoXtgToken t = getToken( sc );
	
	trace( "%% " + m_name + ": \"" + sc.m_token + "\" -> " + t );
	
	return t;
}
protected abstract CoXtgToken getToken( CoXtgScanner sc );
}
