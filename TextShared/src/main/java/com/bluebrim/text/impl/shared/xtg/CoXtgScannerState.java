package com.bluebrim.text.impl.shared.xtg;

import java.util.*;

/**
 * Class used to build a finite state machine in CoXtgScanner.
 * 
 * @author: Dennis
 */
 
class CoXtgScannerState
{
	protected String m_name;
	protected boolean m_keep = true;
	protected boolean m_consume = true;
	
	protected Map m_nextStates = new HashMap();
	protected CoXtgScannerState m_restState;

	protected final boolean TRACE = !true;
public CoXtgScannerState( String name )
{
	m_name = name;
}
public CoXtgScannerState( String name, boolean keep, boolean consume )
{
	this( name );
	m_keep = keep;
	m_consume = consume;
}
public void add( String c, CoXtgScannerState s )
{
	for
		( int i = 0; i < c.length(); i++ )
	{
		m_nextStates.put( new Character( c.charAt( i ) ), s );
	}
}
public void add( CoXtgScannerState s )
{
	m_restState = s;
}
protected CoXtgToken doScan( CoXtgScanner sc )
{
	CoXtgScannerState nextState = null;

	trace( "## " + m_name + " : " + sc.m_nextCharacter + " (" + (int) sc.m_nextCharacter.charValue() + ")" );

	if
		( sc.m_nextCharacter != null )
	{	
		nextState = (CoXtgScannerState) m_nextStates.get( sc.m_nextCharacter );
		if ( nextState == null ) nextState = m_restState;
		if
			( nextState == null )
		{
			sc.log( "Scanning exception: unexpected character at \"" + sc.m_token + sc.m_nextCharacter + "\" (" + (int) sc.m_nextCharacter.charValue() + ")" );
			return null;
		}
	}

	trace( " -> " + nextState );

	if
		( sc.m_nextCharacter == null )
	{
		return null; // end of stream
	}

	return nextState.scan( sc );
}
public final CoXtgToken scan( CoXtgScanner sc )
{
	if
		( m_keep )
	{
		sc.m_token.append( sc.m_nextCharacter.charValue() );
	}

	if
		( m_consume )
	{
		sc.scanNextCharacter();
	}

	return doScan( sc );
}
public String toString()
{
	return m_name;
}
protected void trace( String msg )
{
	if ( TRACE ) System.err.println( msg );
}
}
