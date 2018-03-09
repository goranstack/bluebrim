package com.bluebrim.text.impl.shared;


/**
 * Breakpoint iterator containing all points in a given interval.
 * 
 * @author: Dennis Malmström
 */

public class CoIntervalBreakPointIterator extends CoAbstractBreakPointIterator
{
	// interval
	private int m_first;
	private int m_last;

	// counter
	private int m_next;
public boolean hasNext()
{
	return m_next <= m_last;
}
public int next()
{
	return m_next++;
}
public void set( int first, int last )
{
	m_first = first;
	m_last = last;
	m_next = m_first;
}
}
