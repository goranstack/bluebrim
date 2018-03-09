package com.bluebrim.text.impl.server;

import java.util.*;

import com.bluebrim.base.shared.*;

/**
 * Implementation of a collection of custom hyphenation patterns.
 * 
 * @author: Dennis Malmström
 */

public class CoHyphenationPatternCollection extends CoObject implements com.bluebrim.text.shared.CoHyphenationPatternCollectionIF
{
	private List m_patterns = new ArrayList(); // [ CoHyphenationPatternCollection.Pattern ] 

	private boolean m_dirty;
	// Implementation of custom hyphenation patterns.
	private class Pattern implements com.bluebrim.text.shared.CoHyphenationPatternIF
	{
		private String m_word;
		private String m_pattern;

		public Pattern( String pattern )
		{
			m_pattern = pattern;
			normalize();
		}

		public String getPattern()
		{
			return m_pattern;
		}

		public void setPattern( String pattern )
		{
			m_pattern = pattern;
			normalize();
			CoHyphenationPatternCollection.this.markDirty();
		}

		private void normalize()
		{
			if ( ! m_pattern.startsWith( "." ) ) m_pattern = "." + m_pattern;
			if ( ! m_pattern.endsWith( "." ) ) m_pattern = m_pattern + ".";
			m_word = asWord( m_pattern );
		}

		public boolean equals( Object o )
		{
			if
				( o instanceof Pattern )
			{
				return m_word.equals( ( (Pattern) o ).m_word );
			} else if
				( o instanceof String )
			{
				return m_word.equals( asWord( (String) o ) );
			} else {
				return super.equals( o );
			}
		}

		public String toString()
		{
			return m_pattern;
		}
	};
public com.bluebrim.text.shared.CoHyphenationPatternIF addPattern( String s )
{
	Pattern p = new Pattern( s );
	m_patterns.add( p );
	markDirty();
	return p;
}
public static String asWord( String pattern )
{
	String w = "";
	int I = pattern.length();
	for
		( int i = 0; i < I; i++ )
	{
		char c = pattern.charAt( i );
		if ( Character.isLetter( c ) ) w += c;
	}

	return w;
}
public boolean equals( Object o )
{
	if
		( o instanceof com.bluebrim.text.shared.CoHyphenationPatternCollectionIF )
	{
		com.bluebrim.text.shared.CoHyphenationPatternCollectionIF c = (com.bluebrim.text.shared.CoHyphenationPatternCollectionIF) o;
		return m_patterns.equals( c.getPatterns() );
	} else {
		return super.equals( o );
	}
}
public String getFactoryKey()
{
	return null;
}
public com.bluebrim.text.shared.CoHyphenationPatternIF getPattern(int i)
{
	return (com.bluebrim.text.shared.CoHyphenationPatternIF) m_patterns.get( i );
}
public int getPatternCount()
{
	return m_patterns.size();
}
// PENDING: why not just return m_patterns ???

public Collection getPatterns() // [ com.bluebrim.text.shared.CoHyphenationPatternIF ]
{
	List l = new ArrayList();
	
	Iterator i = m_patterns.iterator();
	while
		( i.hasNext() )
	{
		l.add( ( (Pattern) i.next() ).getPattern() );
	}
	
	return l;
}
public int indexOfPattern( String w )
{
	int I = m_patterns.size();
	for
		( int i = 0; i < I; i++ )
	{
		if
			( m_patterns.get( i ).equals( w ) )
		{
			return i;
		}
	}
	
	return -1;
}
public int indexOfPattern( com.bluebrim.text.shared.CoHyphenationPatternIF p )
{
	return m_patterns.indexOf( p );
}
private void markDirty()
{
	m_dirty = ! m_dirty;
}
public void removePattern( int i )
{
	m_patterns.remove( i );
	markDirty();
}
public void removePattern( String w )
{
	int i = m_patterns.indexOf( w );
	if
		( i >= 0 )
	{	
		m_patterns.remove( i );
		markDirty();
	}
}
public void removePattern( com.bluebrim.text.shared.CoHyphenationPatternIF p )
{
	m_patterns.remove( p );
	markDirty();
}

public void removeAllPatterns()
{
	m_patterns.clear();
	markDirty();
}
}