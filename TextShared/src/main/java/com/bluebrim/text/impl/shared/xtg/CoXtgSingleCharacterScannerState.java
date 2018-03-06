package com.bluebrim.text.impl.shared.xtg;

/**
 * Xtg scanner state (see CoXtgScanner)
 * 
 * @author: Dennis
 */
 
class CoXtgSingleCharacterScannerState extends CoXtgTerminalScannerState
{
	private CoXtgToken m_token;
public CoXtgSingleCharacterScannerState( String name, CoXtgToken token )
{
	super( name );

	m_token = token;
}
protected CoXtgToken getToken( CoXtgScanner sc )
{
	return m_token;
}
}
