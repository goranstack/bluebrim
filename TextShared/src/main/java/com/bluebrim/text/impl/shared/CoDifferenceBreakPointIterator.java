package com.bluebrim.text.impl.shared;


/**
 * Breakpoint iterator defined as the difference between two breakpoint iterators.
 * 
 * @author: Dennis Malmström
 */

public class CoDifferenceBreakPointIterator extends CoCompoundBreakPointIterator
{
	int m_v1;
	int m_v2;
public CoDifferenceBreakPointIterator()
{
	super();
}
public boolean hasNext()
{
	return ( m_v1 != -1 );
}
public int next()
{
	int i = m_v1;
	
	m_v1 = m_i1.hasNext() ? m_i1.next() : -1;

	while
		( m_v1 != -1 && m_v2 != -1 && m_v2 <= m_v1 )
	{
		while
			( m_v1 != -1 && m_v2 != -1 && m_v1 == m_v2 )
		{
			m_v1 = m_i1.hasNext() ? m_i1.next() : -1;
			m_v2 = m_i2.hasNext() ? m_i2.next() : -1;
		}
		
		while
			( m_v1 != -1 && m_v2 != -1 && m_v1 > m_v2 )
		{
			m_v2 = m_i2.hasNext() ? m_i2.next() : -1;
		}
	}

	return i;
}
public void set( CoLineBreakerIF.BreakPointIteratorIF i1, CoLineBreakerIF.BreakPointIteratorIF i2 )
{
	super.set( i1, i2 );

	m_v1 = m_i1.hasNext() ? m_i1.next() : -1;
	m_v2 = m_i2.hasNext() ? m_i2.next() : -1;
}
}
