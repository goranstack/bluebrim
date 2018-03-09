package com.bluebrim.text.impl.shared;

import javax.swing.text.*;

/**
 * Abstract class for breakpoint iterators that calculate the breakpoints from a text.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoAbstractSegmentBreakPointIterator extends CoAbstractBreakPointIterator
{
	protected Segment m_text;
	
	protected int m_index;
protected abstract void findNext();
public boolean hasNext()
{
	return m_index < m_text.offset + m_text.count;
}
public void init( Segment text )
{
	m_text = text;
	m_index = m_text.offset;
	findNext();
}
public int next()
{
	int i = m_index - m_text.offset;
	findNext();

	return i;
}
}
