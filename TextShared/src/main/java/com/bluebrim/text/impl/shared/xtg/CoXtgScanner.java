package com.bluebrim.text.impl.shared.xtg;

import java.io.*;

/**
 * Xtg file scanner.
 * Due to the characteristicts of the xtg language, a simple context free scanner isn't enough.
 * Therefore a CoXtgScanner sometimes delegates the scanning to a subscanner (the subclasses of this class).
 * 
 * @author: Dennis Malmström
 */
 
public class CoXtgScanner implements CoXtgLogger
{
	private Reader m_reader;
	Character m_nextCharacter;
	private int m_readBuffer = -2;
	
	private CoXtgScanner m_scanner; // subscanner
	
	StringBuffer m_token;

	CoXtgLogger m_logger;


	
	private CoXtgScannerState m_initialState = new CoXtgScannerState( "START", false, false );
	
	{
		CoXtgScannerState COLON = new CoXtgSingleCharacterScannerState( "COLON", new CoXtgColonToken() );
		CoXtgScannerState EQUALS = new CoXtgSingleCharacterScannerState( "EQUALS", new CoXtgEqualsToken() );

		CoXtgScannerState NULL = new CoXtgSingleCharacterScannerState( "NULL", new CoXtgNullToken() );

		CoXtgScannerState NAME = new CoXtgScannerState( "NAME" );
		CoXtgScannerState ENDNAME = new CoXtgNameTerminalScannerState( "ENDNAME" );

		CoXtgScannerState BRACKET = new CoXtgScannerState( "BRACKET" );
		CoXtgScannerState ENDBRACKET = new CoXtgBracketTerminalScannerState( "ENDBRACKET" );

		CoXtgScannerState TAGS = new CoXtgScannerState( "TAGS" );
		CoXtgScannerState ENDTAGS = new CoXtgAttributesTerminalScannerState( "ENDTAGS" );
	
		CoXtgScannerState TEXT = new CoXtgScannerState( "TEXT" );
		CoXtgScannerState ENDTEXT = new CoXtgTextTerminalScannerState( "ENDTEXT" );
		
		CoXtgScannerState END = new CoXtgSingleCharacterScannerState( "END", null );
		
		m_initialState.add( "=", EQUALS );
		m_initialState.add( ":", COLON );
		m_initialState.add( "\n\r\t ", NULL );
		m_initialState.add( "<", TAGS );
		m_initialState.add( "[", BRACKET );
		m_initialState.add( "@", NAME );
		m_initialState.add( "\0", END );
		m_initialState.add( TEXT );

		TEXT.add( "\n\r\0", ENDTEXT );
		TEXT.add( TEXT );

		TAGS.add( ">", ENDTAGS );
		TAGS.add( "\\", TEXT );
		TAGS.add( TAGS );

		BRACKET.add( "]", ENDBRACKET );
		BRACKET.add( BRACKET );

		NAME.add( ":=", ENDNAME );
		NAME.add( NAME );
		

	}

public CoXtgScanner( Reader r, CoXtgLogger logger )
{
	m_reader = r;
	m_logger = logger;

	if
		( m_logger == null )
	{
		m_logger = new CoXtgLogger.DefaultLogger();
	}
}
protected CoXtgScannerState getInitialState()
{
	return m_initialState;
}
public void log( String msg )
{
	m_logger.log( msg );
}
public static void main( String [] args )
{
	String str = "-123.456<-><*kn1>@@@:detta är en text\n@$[]<f\"qwerty fgh 567 w45 3\">,=:en flerradig text:\n som fortsätter här\n";

	Reader is = new StringReader( str );

	try
	{
		is = new FileReader( "D:\\xtg\\saab_text.xtg" );
	}
	catch ( FileNotFoundException ex )
	{
	}


	CoXtgScanner sc = new CoXtgScanner( is, null );

	CoXtgToken t = sc.scan();
	while
		( t != null )
	{
		System.err.println( t );
		t = sc.scan();
	}

}
public CoXtgToken scan()
{
	if
		( m_scanner != null )
	{
		CoXtgToken t = m_scanner.scan();
		if
			( t != null )
		{
			return t;
		}
		
		m_scanner = null;
	}

	
	if
		( m_nextCharacter == null )
	{
		scanNextCharacter();
		if ( m_nextCharacter == null ) return null;
	}
	
	m_token = new StringBuffer();
	CoXtgToken t = getInitialState().scan( this );
	
	while
		( t instanceof CoXtgNullToken )
	{
		m_token = new StringBuffer();
		t = getInitialState().scan( this );
	}

	if
		( t != null )
	{
		m_scanner = t.getScanner( m_logger );
		if
			( m_scanner != null )
		{
			return scan();
		}
	}
	
	return t;
}
void scanNextCharacter()
{
	try
	{
		int i;
		
		if
			( m_readBuffer != -2 )
		{
			i = m_readBuffer;
			m_readBuffer = -2;
		} else {
			i = m_reader.read();
			if
				( i == ':' )
			{
				m_readBuffer = m_reader.read();
				if
					( ( m_readBuffer == '\n' ) || ( m_readBuffer == '\r' ) )
				{
					i = m_readBuffer;
					m_readBuffer = -2;
					while
						( ( i == '\n' ) || ( i == '\r' ) )
					{
						i = m_reader.read();
					}
				}
			}
		}
		
		if
			( i == -1 )
		{	
			m_nextCharacter = new Character( '\0' );
		} else {
			m_nextCharacter = new Character( (char) i );
		}
	}
	catch ( IOException ex )
	{
		log( "Scanner exception: " + ex );
		m_nextCharacter = null;
	}

//	System.err.println( "#"+m_nextCharacter+"#" );
}
}
