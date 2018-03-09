package com.bluebrim.text.impl.shared.xtg;

/**
 * Subclass of CoXtgScanner for scanning xtg text expressions
 * Used internally by the xtg scanning mechanism.
 * 
 * @author: Dennis Malmström
 */

class CoXtgTextScanner extends CoXtgScanner
{
	private CoXtgScannerState m_initialState = new CoXtgScannerState( "START", false, false );
	
	{
		CoXtgScannerState LT = new CoXtgScannerState( "LT" );
		
		CoXtgScannerState TAGS = new CoXtgScannerState( "TAGS" );
		CoXtgScannerState ENDTAGS = new CoXtgNameTerminalScannerState( "ENDTAGS", true, true );
		
		CoXtgScannerState CHAR = new CoXtgScannerState( "CHAR" );
		CoXtgScannerState ENDCHAR = new CoXtgNameTerminalScannerState( "ENDCHAR", true, true );

		CoXtgScannerState TEXT = new CoXtgScannerState( "TEXT" );
		CoXtgScannerState BACKSLASH = new CoXtgScannerState( "BACKSLASH" );
		CoXtgScannerState ENDTEXT = new CoXtgTextTerminalScannerState( "ENDTEXT" );
		CoXtgScannerState ENDTEXT2 = new CoXtgTextTerminalScannerState( "ENDTEXT2", false, false );
		
		CoXtgScannerState END = new CoXtgSingleCharacterScannerState( "END", null );


		m_initialState.add( "<", LT );
		m_initialState.add( "\0", END );
		m_initialState.add( TEXT );

		LT.add( "\\", CHAR );
		LT.add( TAGS );

		TAGS.add( ">", ENDTAGS );
		TAGS.add( TAGS );

		CHAR.add( ">", ENDCHAR );
		CHAR.add( CHAR );
			
		TEXT.add( "\n\r\0", ENDTEXT );
		TEXT.add( "\\", BACKSLASH );
		TEXT.add( "<", ENDTEXT2 );
		TEXT.add( TEXT );
	}

public CoXtgTextScanner( java.io.Reader r, CoXtgLogger l )
{
	super( r, l );
}
protected CoXtgScannerState getInitialState()
{
	return m_initialState;
}
}
