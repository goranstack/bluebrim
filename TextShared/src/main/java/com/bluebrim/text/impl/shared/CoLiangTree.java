package com.bluebrim.text.impl.shared;

import java.io.*;
import java.util.*;

import javax.swing.text.*;

/**
 * Root of a tree containing patterns used for finding breakpoints in a word according to the Liang algorithm..
 * 
 * @author: Dennis Malmström
 */

public class CoLiangTree extends CoLiangNode
{

	
	private static Segment m_segment; // working area used when finding breakpoints
public CoLiangTree( InputStream patternStream, Collection customPatterns ) throws IOException
{
	super( null );

	readPatterns( patternStream );

	Iterator i = customPatterns.iterator();
	while
		( i.hasNext() )
	{
		addPattern( (String) i.next() );
	}
}
private void addPattern( String pattern )
{
	addPattern( pattern, 0, new ArrayList() );
}
public int [] match( char [] word, int offset, int count )
{
	return match( word, offset, count, null );
}
public int [] match( char [] word, int offset, int count, int breakpoints [] /* length > count */ )
{
	final boolean TRACE = false;
	
	int I = count;

	if
		( breakpoints == null )
	{
		breakpoints = new int [ I + 1 ];
	} else {
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue( breakpoints.length > count, "Breakpoint array to small" );
	}
	
	for
		( int i = 0; i < count + 1; i++ )
	{
		if
			( ( i < I ) && ( word[ offset + i ] == HYPHENATION_POINT_CHARACTER ) )
		{
			breakpoints[ i ] = 99;
			i++;
			breakpoints[ i ] = 99;
		} else if
			( ( i < I ) && ( word[ offset + i ] == ANTI_HYPHENATION_POINT_CHARACTER ) )
		{
			breakpoints[ i ] = 98;
			i++;
			breakpoints[ i ] = 98;
		} else {
			breakpoints[ i ] = 0;
		}
	}

	prepareSegment( count + 2 );
	m_segment.array[ 0 ] = '.';
	System.arraycopy( word, offset, m_segment.array, 1, count );
	m_segment.array[ count + 1 ] = '.';

	int n = 0;
	match( m_segment.array, m_segment.offset, m_segment.count, breakpoints, n );

	m_segment.offset += 2;
	m_segment.count -= 2;
	n += 2;
	
	while
		( m_segment.count > 1 )
	{
		match( m_segment.array, m_segment.offset, m_segment.count, breakpoints, n );
		m_segment.offset++;
		m_segment.count--;
		n++;
	}

	return breakpoints;
}
private static void prepareSegment( int length )
{
	if
		( m_segment == null )
	{
		m_segment = new Segment( new char [ length ], 0, length );
	} else {
		if
			( m_segment.array.length < length )
		{
			m_segment = new Segment( new char [ length ], 0, length );
		}
	}

	m_segment.offset = 0;
	m_segment.count = length;

}
private void readPatterns( InputStream s )
{
	try
	{
		BufferedReader br = new BufferedReader( new InputStreamReader( s ) );
		String pattern = null;
		while
			( ( ( pattern = br.readLine() ) != null ) && ( pattern.length() > 0 ) )
		{
			if ( pattern.charAt( 0 ) == '%' ) continue;
			if ( pattern.charAt( 0 ) == '\\' ) continue;
			if ( pattern.charAt( 0 ) == '{' ) continue;
			if ( pattern.charAt( 0 ) == '}' ) continue;

			addPattern( pattern );
		}
	}
	catch ( IOException ex )
	{
		System.err.println( "Hyphenation pattern file could not be read :" + ex.getMessage() );
	}
}
public String toString()
{
	return "CoLiangTree";
}
}