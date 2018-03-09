package com.bluebrim.text.impl.shared.xtg;

/**
 * Subclass of CoXtgScanner for scanning xtg bracketed list expressions
 * Used internally by the xtg scanning mechanism.
 * 
 * @author: Dennis Malmström
 */

class CoXtgBracketScanner extends CoXtgScanner
{
	private CoXtgScannerState m_initialState = new CoXtgScannerState( "START", false, false );
	
	{
		CoXtgScannerState COMMA = new CoXtgSingleCharacterScannerState( "COMMA", new CoXtgCommaToken() );
		CoXtgScannerState LEFT_BRACKET = new CoXtgSingleCharacterScannerState( "LEFT_BRACKET", new CoXtgLeftBracketToken() );
		CoXtgScannerState RIGHT_BRACKET = new CoXtgSingleCharacterScannerState( "RIGHT_BRACKET", new CoXtgRightBracketToken() );
		CoXtgScannerState S_TAG = new CoXtgNameTerminalScannerState( "S_TAG", true, true );

		CoXtgScannerState QUOTE = new CoXtgScannerState( "QUOTE" );
		CoXtgScannerState ENDQUOTE = new CoXtgQuotedStringTerminalScannerState( "ENDQUOTE" );
		
		CoXtgScannerState END = new CoXtgSingleCharacterScannerState( "END", null );
		
		m_initialState.add( ",", COMMA );
		m_initialState.add( "[", LEFT_BRACKET );
		m_initialState.add( "]", RIGHT_BRACKET );
		m_initialState.add( "sS", S_TAG );
		m_initialState.add( "\"", QUOTE );
		m_initialState.add( "\0", END );

		QUOTE.add( "\"", ENDQUOTE );
		QUOTE.add( QUOTE );
	}

public CoXtgBracketScanner( java.io.Reader r, CoXtgLogger l )
{
	super( r, l );
}
protected CoXtgScannerState getInitialState()
{
	return m_initialState;
}
}
