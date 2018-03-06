package com.bluebrim.text.impl.shared;

import javax.swing.text.*;

/**
 * Breakpoint iterator containing all "bad" breakpoints in a text.
 * 
 * @author: Dennis Malmström
 */

public class CoBadBreakPointIterator extends CoAbstractSegmentBreakPointIterator
{
public CoBadBreakPointIterator()
{
}
protected void findNext()
{
	int range = 3;
	
	m_index++;
	
	outer:
	while
		( m_index < m_text.offset + m_text.count )
	{
		// before space is bad
		if ( ( m_index >= m_text.offset ) && Character.isWhitespace( m_text.array[ m_index ] ) ) break outer; 

		for
			( int i = 1; i <= range; i++ )
		{
		  // before last range or less characters in a word is bad
			int n = m_index - i - 2;
			if ( ( n >= m_text.offset ) && Character.isWhitespace( m_text.array[ n + 1 ] ) && ! Character.isWhitespace( m_text.array[ n ] ) ) break outer;

		  // after first range or less characters in a word is bad
			n = m_index + i;
			if ( ( n < m_text.offset + m_text.count ) && Character.isWhitespace( m_text.array[ n ] ) && ! Character.isWhitespace( m_text.array[ n - 1 ] ) ) break outer;
		}
		m_index++;
	}

}
public void set( Segment text )
{
	init( text );
}
}
