package com.bluebrim.text.impl.shared;

import javax.swing.text.*;

/**
 * Breakpoint iterator containing breakpoint according to the Liang algorithm.
 * 
 * @author: Dennis Malmström
 */

public class CoLiangBreakPointIterator extends CoAbstractSegmentBreakPointIterator
{
	private CoLiangTree m_tree; // Liang pattern tree
	
	private int [] m_wordPoints; // points in current word
	private int m_wordPointCount; // -1 means m_wordPoints is not valid
	
	private int m_wordIndex;
	private int m_wordPosition;
	private int m_wordLength;

	// constraints
	private int m_minimumPrefixLength = 0;
	private int m_minimumSuffixLength = 0;
	private int m_minimumWordLength = 0;
public CoLiangBreakPointIterator()
{
}
private boolean checkConstraints()
{
	if ( m_index - 1 >= m_text.offset ) if ( Character.isWhitespace( m_text.array[ m_index - 1 ] ) ) return true;
	
	int i0 = m_index - m_minimumPrefixLength;
	if ( i0 < m_text.offset ) return false;
	
	int i1 = m_index + m_minimumSuffixLength - 1;
	if ( i1 >= m_text.offset + m_text.count - 1 ) return false;
	
	for
		( int i = i0; i <= i1; i++ )
	{
		if ( i == m_index ) continue;
		if ( Character.isWhitespace( m_text.array[ i ] ) ) return false;
	}
	
	return true;
}
private void doFindNext()
{
	if
		( m_wordPointCount < 0 )
	{
		m_wordPosition = m_index;
		prepareWord();
		m_index = m_wordPosition;

		if
			( m_wordLength < m_minimumWordLength )
		{
			m_wordIndex = m_wordPointCount;
		} else {
			m_wordIndex++;
		}
		return;
	}

	while
		( m_wordIndex < m_wordPointCount )
	{
		if
			( m_wordPoints[ m_wordIndex ] % 2 == 1 )
		{
			m_index = m_wordIndex + m_wordPosition;
			m_wordIndex++;
			return;
		}
		m_wordIndex++;
	}

	if
		( m_wordPosition >= m_text.offset + m_text.count )
	{
		m_index = m_text.offset + m_text.count;
		return;
	}

	m_wordPointCount = -1;
	m_index = m_wordPosition + m_wordLength;
	doFindNext();
}
protected void findNext()
{
	doFindNext();

	if
		( m_index >= m_text.offset + m_text.count )
	{
		return; // end of text
	}

	if ( ! checkConstraints() ) findNext();
}
private void prepareWord()
{
	while
		( ( m_wordPosition < m_text.offset + m_text.count ) && Character.isWhitespace( m_text.array[ m_wordPosition ] ) )
	{
		m_wordPosition++;
	}
	int i = m_wordPosition;
	while
		( ( i < m_text.offset + m_text.count ) && ! Character.isWhitespace( m_text.array[ i ] ) )
	{
		i++;
	}
	m_wordLength = i - m_wordPosition;

	if
		( ( m_wordPoints == null ) || ( m_wordPoints.length <= m_wordLength ) )
	{
		m_wordPoints = new int [ m_wordLength + 1 ];
	}
	
	m_tree.match( m_text.array, m_wordPosition, m_wordLength, m_wordPoints );
	m_wordPointCount = m_wordLength + 1;
	
	m_wordIndex = 0;
}
public void set( CoLiangTree tree, Segment text, int minimumPrefixLength, int minimumSuffixLength, int minimumWordLength )
{
	m_wordIndex = m_wordPosition = m_wordLength = 0;
	m_wordPointCount = -1;

	m_minimumPrefixLength = minimumPrefixLength;
	m_minimumSuffixLength = minimumSuffixLength;
	m_minimumWordLength = minimumWordLength;
	m_tree = tree;
	init( text );
}
}