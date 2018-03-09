package com.bluebrim.text.impl.shared;

import java.util.*;

/**
 * Node in a tree containing patterns used for finding breakpoints in a word according to the Liang algorithm..
 * 
 * @author: Dennis Malmström
 */

public class CoLiangNode implements com.bluebrim.text.impl.shared.CoHyphenationConstants
{
	private static MutableCharacter m_key = new MutableCharacter();

	private MutableCharacter m_character;
	private int [] m_breakpoints;

	private Map m_children = new HashMap();


	// mutable class holding a char, used as hash key
	private static class MutableCharacter
	{
		private char m_character;

		public MutableCharacter()
		{
			this( '\0' );
		}

		public MutableCharacter( char c )
		{
			set( c );
		}

		public void set( char c )
		{
			m_character = c;
		}

		public int hashCode()
		{
			return (int) m_character;
		}
	
		public String toString()
		{
			char buf[] = { m_character };
			return String.valueOf( buf );
		}
		
		public boolean equals( Object o )
		{
			if
				( ( o != null ) && ( o instanceof MutableCharacter ) )
			{
		    return m_character == ( (MutableCharacter) o ).m_character;
			} 
			return false;
		}
		
	};



	// position-weight pair
	private static class Breakpoint
	{
		public int m_position;
		public int m_weight;

		public Breakpoint( int position, int weight )
		{
			m_position = position;
			m_weight = weight;
		}
	};

protected CoLiangNode( MutableCharacter c )
{
	m_character = c;
}
private CoLiangNode add( char c )
{
	MutableCharacter C = new MutableCharacter( c );

	CoLiangNode n = (CoLiangNode) m_children.get( C );
	
	if
		( n == null )
	{
		n = new CoLiangNode( C );
		m_children.put( C, n );
	}

	return n;
}
protected void addPattern( String pattern, int depth, List breakpoints )
{
	if
		( pattern.length() == 0 )
	{
		int I = breakpoints.size();
		m_breakpoints = new int [ I * 2 ];
		for
			( int i = 0; i < I; i++ )
		{
			Breakpoint p = (Breakpoint) breakpoints.get( i );
			m_breakpoints[ i * 2 ] = p.m_position;
			m_breakpoints[ i * 2 + 1 ] = p.m_weight;
		}
		return;
	}

	char c = pattern.charAt( 0 );

	if
		( Character.isDigit( c ) )
	{
		breakpoints.add( new Breakpoint( depth, (int) ( c - '0' ) ) );
		addPattern( pattern.substring( 1 ), depth, breakpoints );
	} else {
		CoLiangNode n = add( c );
		n.addPattern( pattern.substring( 1 ), depth + 1, breakpoints );
	}
}
protected void match( char [] word, int offset, int count, int [] breakWeights, int depth )
{
//	System.err.println( offset + " " + count + "    #" + word + "#" );
	char c = '\0';
	if ( count > 0 ) c = word[ offset + 0 ];

	if
		( ( c == HYPHENATION_POINT_CHARACTER ) || ( c == ANTI_HYPHENATION_POINT_CHARACTER ) )
	{
		match( word, offset + 1, count - 1, breakWeights, depth );
		return;
	}
	
	if
		( ( count == 0 ) || ( c != '.' ) )
	{
		if
			( m_breakpoints != null )
		{
			for
				( int i = 0; i < m_breakpoints.length / 2; i++ )
			{
				int n = depth + m_breakpoints[ i * 2 ] - 1;
				int w = m_breakpoints[ i * 2 + 1 ];
				if
					( w > breakWeights[ n ] )
				{
					breakWeights[ n ] = w;
				}
			}
		}
	}
	
	if
		( count == 0 )
	{
		return;
	}

	m_key.set( c );
	CoLiangNode n = (CoLiangNode) m_children.get( m_key );
	
	if
		( n != null )
	{
		n.match( word, offset + 1, count - 1, breakWeights, depth );
	} else {
		// no match
	}
}
public String toString()
{
	return m_character.toString();
}
}