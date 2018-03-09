package com.bluebrim.text.impl.shared;

import javax.swing.text.*;

/**
 * Breakpoint iterator containing breakpoint before words.
 * 
 * @author: Dennis Malmström
 */

public class CoBetweenWordsBreakPointIterator extends CoAbstractSegmentBreakPointIterator
{
public CoBetweenWordsBreakPointIterator()
{
}
protected void findNext()
{
	m_index++;
	while
		( m_index < m_text.offset + m_text.count )
	{
		if ( ( m_index - 1 >= m_text.offset ) && Character.isWhitespace( m_text.array[ m_index - 1 ] ) && ! Character.isWhitespace( m_text.array[ m_index ] ) ) break;
		m_index++;
	}
}
public void set( Segment text )
{
	init( text );
}
}
