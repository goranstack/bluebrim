package com.bluebrim.text.impl.shared;


/**
 * Breakpoint iterator defined as the union of two breakpoint iterators.
 * 
 * @author: Dennis Malmström
 */

public class CoUnionBreakPointIterator extends CoCompoundBreakPointIterator
{
	int m_v1;
	int m_v2;
public CoUnionBreakPointIterator()
{
	super();
}
public boolean hasNext()
{
	return ( m_v1 != -1 ) || ( m_v2 != -1 );
}
public int next()
{
	if
		( m_v1 == -1 )
	{
		int i = m_v2;
		m_v2 = m_i2.hasNext() ? m_i2.next() : -1;
		return i;
	}
	
	if
		( m_v2 == -1 )
	{
		int i = m_v1;
		m_v1 = m_i1.hasNext() ? m_i1.next() : -1;
		return i;
	}

	if
		( m_v2 == m_v1 )
	{
		int i = m_v1;
		m_v1 = m_i1.hasNext() ? m_i1.next() : -1;
		m_v2 = m_i2.hasNext() ? m_i2.next() : -1;
		return i;
	}

	if
		( m_v1 < m_v2 )
	{
		int i = m_v1;
		m_v1 = m_i1.hasNext() ? m_i1.next() : -1;
		return i;
	} else {
		int i = m_v2;
		m_v2 = m_i2.hasNext() ? m_i2.next() : -1;
		return i;
	}
}
public void set( CoLineBreakerIF.BreakPointIteratorIF i1, CoLineBreakerIF.BreakPointIteratorIF i2 )
{
	super.set( i1, i2 );

	m_v1 = m_i1.hasNext() ? m_i1.next() : -1;
	m_v2 = m_i2.hasNext() ? m_i2.next() : -1;
}
}
