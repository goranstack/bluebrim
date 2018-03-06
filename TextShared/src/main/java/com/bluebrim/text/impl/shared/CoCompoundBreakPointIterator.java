package com.bluebrim.text.impl.shared;


/**
 * Abstract class for breakpoint iterators that are defined by combining two other breakpoint iterators.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoCompoundBreakPointIterator extends CoAbstractBreakPointIterator
{
	protected CoLineBreakerIF.BreakPointIteratorIF m_i1;
	protected CoLineBreakerIF.BreakPointIteratorIF m_i2;
public CoCompoundBreakPointIterator()
{
}
public void set( CoLineBreakerIF.BreakPointIteratorIF i1, CoLineBreakerIF.BreakPointIteratorIF i2 )
{
	m_i1 = i1;
	m_i2 = i2;
}
}
