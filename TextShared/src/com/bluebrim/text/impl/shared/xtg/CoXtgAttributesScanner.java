package com.bluebrim.text.impl.shared.xtg;

/**
 * Subclass of CoXtgScanner for scanning xtg text style attribute expressions.
 * Used internally by the xtg scanning mechanism.
 * 
 * @author: Dennis Malmström
 */

class CoXtgAttributesScanner extends CoXtgScanner
{
	private CoXtgScannerState m_initialState = new CoXtgScannerState( "START", false, false );
	
	{
		CoXtgScannerState INT = new CoXtgScannerState( "INT" );
		CoXtgScannerState POINT = new CoXtgScannerState( "POINT" );
		CoXtgScannerState FRAC = new CoXtgScannerState( "FRAC" );
		CoXtgScannerState NEG = new CoXtgScannerState( "NEG" );
		CoXtgScannerState NUM = new CoXtgNumberTerminalScannerState( "NUM" );

		CoXtgScannerState QUOTE = new CoXtgScannerState( "QUOTE" );
		CoXtgScannerState ENDQUOTE = new CoXtgQuotedStringTerminalScannerState( "ENDQUOTE" );

		CoXtgScannerState COMMA = new CoXtgSingleCharacterScannerState( "COMMA", new CoXtgCommaToken() );
		CoXtgScannerState PERCENT = new CoXtgSingleCharacterScannerState( "PERCENT", new CoXtgPercentToken() );
		CoXtgScannerState DOLLAR = new CoXtgSingleCharacterScannerState( "DOLLAR", new CoXtgDollarToken() );
		CoXtgScannerState LEFT_PAREN = new CoXtgSingleCharacterScannerState( "LEFT_PAREN", new CoXtgLeftParenToken() );
		CoXtgScannerState RIGHT_PAREN = new CoXtgSingleCharacterScannerState( "RIGHT_PAREN", new CoXtgRightParenToken() );
		CoXtgScannerState LESS_THAN = new CoXtgSingleCharacterScannerState( "LESS_THAN", new CoXtgLessThanToken() );
		CoXtgScannerState GREATER_THAN = new CoXtgSingleCharacterScannerState( "GREATER_THAN", new CoXtgGreaterThanToken() );

		CoXtgScannerState COLOR = new CoXtgScannerState( "COLOR" );
		
		CoXtgScannerState TAG = new CoXtgScannerState( "TAG" );
		CoXtgScannerState ENDTAG = new CoXtgNameTerminalScannerState( "ENDTAG", false, false );
		
		CoXtgScannerState END = new CoXtgSingleCharacterScannerState( "END", null );
		
		CoXtgScannerState C3 = new CoXtgScannerState( "C3" );
		CoXtgScannerState C2 = new CoXtgScannerState( "C2" );
		CoXtgScannerState C1 = new CoXtgScannerState( "C1" );
		CoXtgScannerState C0 = new CoXtgNameTerminalScannerState( "C0" );

		m_initialState.add( "-", NEG );
		m_initialState.add( "0123456789", INT );
		m_initialState.add( ".", POINT );
		m_initialState.add( "\"", QUOTE );
		m_initialState.add( ",", COMMA );
		m_initialState.add( "%", PERCENT );
		m_initialState.add( "$", DOLLAR );
		m_initialState.add( "(", LEFT_PAREN );
		m_initialState.add( ")", RIGHT_PAREN );
		m_initialState.add( "<", LESS_THAN );
		m_initialState.add( ">", GREATER_THAN );
		m_initialState.add( "\0", END );
		m_initialState.add( "c", COLOR );
		m_initialState.add( "*", C3 );
		m_initialState.add( "PBIOSUW/KH+V$vegGfzshktby", C1 );
		m_initialState.add( "@", TAG );

		NEG.add( "0123456789", INT );
		NEG.add( ".", POINT );
		NEG.add( C0 );
		INT.add( ".", POINT );
		INT.add( "0123456789", INT );
		INT.add( NUM );
		POINT.add( "0123456789", FRAC );
		POINT.add( NUM );
		FRAC.add( "0123456789", FRAC );
		FRAC.add( NUM );
		
		QUOTE.add( "\"", ENDQUOTE );
		QUOTE.add( QUOTE );
		
		COLOR.add( "\"", C0 );
		COLOR.add( C1 );
		
		TAG.add( ">", ENDTAG );
		TAG.add( TAG );

		C1.add( C0 );
		C2.add( C1 );
		C3.add( "LCRJFtphd", C1 );
		C3.add( C2 );
	}

public CoXtgAttributesScanner( java.io.Reader r, CoXtgLogger l )
{
	super( r, l );
}
protected CoXtgScannerState getInitialState()
{
	return m_initialState;
}
}
