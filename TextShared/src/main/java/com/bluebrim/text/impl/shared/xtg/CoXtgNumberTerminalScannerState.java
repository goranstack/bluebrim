package com.bluebrim.text.impl.shared.xtg;

/**
 * Xtg scanner state (see CoXtgScanner)
 * 
 * @author: Dennis
 */
 
class CoXtgNumberTerminalScannerState extends CoXtgTerminalScannerState
{
public CoXtgNumberTerminalScannerState( String name )
{
	super( name, false, false );
}
protected CoXtgToken getToken( CoXtgScanner sc )
{
	String str = sc.m_token.toString();
	try
	{
		return new CoXtgNumberToken( str, Float.parseFloat( str ) );
	}
	catch ( NumberFormatException ex )
	{
		sc.log( "Scanner exception: could not parse \"" + str + "\" to a number." );
		return new CoXtgNumberToken( str, Float.NaN );
	}
}
}
