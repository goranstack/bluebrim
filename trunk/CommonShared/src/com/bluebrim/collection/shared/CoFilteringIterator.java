package com.bluebrim.collection.shared;

import java.util.*;

/**
 * Simple filtering iterator
 *
 * @author Markus Persson 1999-09-01
 */
public abstract class CoFilteringIterator implements CoRemoteIteratorIF {
	private Iterator m_delegate;
	private Object m_next;
	private boolean m_nextIsValid;
	
	public CoFilteringIterator(Iterator delegate) {
		m_delegate = delegate;
	}
	
	public boolean hasNext() {
		while (!m_nextIsValid && m_delegate.hasNext()) {
			m_nextIsValid = keep(m_next = m_delegate.next());
		}

		return m_nextIsValid;
	}
	
	protected abstract boolean keep(Object obj);
	
	public Object next() {
		if (hasNext()) {
			m_nextIsValid = false;
			return m_next;
		} else {
			throw new NoSuchElementException();
		}
	}
	
	/**
	 * This could be supported, but that would require more state.
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}
}