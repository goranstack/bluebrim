package com.bluebrim.collection.shared;

import java.util.*;

/**
 * Utility class for iterators.
 * 
 * NOTE: Similar classes for collections are
 * CoCollections and java.util.Collections.
 * 
 * @see CoFilteringIterator
 * @see CoCollections
 * @see java.util.Collections
 * @author Markus Persson 2002-04-19
 */
public final class CoIterators {
	private static final Iterator EMPTY_ITERATOR = new EmptyIter();

	/**
	 * Empty iterator.
	 * @author Markus Persson 2002-04-19
	 */
	private static class EmptyIter implements Iterator {
		public boolean hasNext() {
			return false;
		}
		public Object next() {
			throw new NoSuchElementException();
		}
		public void remove() {
			throw new IllegalStateException();
		}
	}

	/**
	 * Single element iterator.
	 * @author Markus Persson 2002-04-19
	 */
	private static class SingleIter implements Iterator {
		private Object m_element;
		private boolean m_used;
		
		public SingleIter(Object element) {
			m_element = element;
		}
		public boolean hasNext() {
			return !m_used;
		}
		public Object next() {
			if (m_used) {
				throw new NoSuchElementException();
			} else {
				m_used = true;
				return m_element;
			}
		}
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}


	/**
	 * Concatenating iterator.
	 * PENDING: Remove the Remote stuff?
	 * @author Markus Persson 1999-04-21
	 * @author Markus Persson 2002-04-19
	 */
	private static class ConcatIter implements CoRemoteIteratorIF {
		private final Iterator m_first;
		private final Iterator m_last;

		public ConcatIter(Iterator first, Iterator last) {
			m_first = first;
			m_last = last;
		}
		public boolean hasNext() {
			return m_first.hasNext() || m_last.hasNext();
		}
		public Object next() {
			return m_first.hasNext() ? m_first.next() : m_last.next();
		}
		public void remove() {
			// Could be fixed properly too, if wanted.
			throw new UnsupportedOperationException();
		}
	}


	/**
	 * Concatenating array iterator.
	 * PENDING: Remove the Remote stuff?
	 * @author Markus Persson 2002-04-19
	 */
	private static class ConcatArrIter implements CoRemoteIteratorIF {
		private final Iterator[] m_iterators;
		private Iterator m_current;
		private int m_pos;

		public ConcatArrIter(Iterator[] iterators) {
			m_iterators = iterators;
			m_current = EMPTY_ITERATOR;
		}
		public boolean hasNext() {
			if (m_current.hasNext()) {
				return true;
			}
			while (++m_pos < m_iterators.length) {
				if (m_iterators[m_pos].hasNext()) {
					m_current = m_iterators[m_pos];
					return true;
				}
			}
			return false;
		}
		public Object next() {
			hasNext();
			// Let the last iterator throw the exception ...
			return m_current.next();
		}
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}


	/**
	 * Return an Iterator with no elements.
	 */
	public static Iterator empty() {
		return EMPTY_ITERATOR;
	}

	/**
	 * Return an Iterator with a single element.
	 */
	public static Iterator single(Object element) {
		return new SingleIter(element);
	}

	/**
	 * Return an Iterator concatenating the two given.
	 */
	public static Iterator concat(Iterator first, Iterator last) {
		return new ConcatIter(first, last);
	}

	/**
	 * Return an Iterator concatenating those in the Iterator
	 * array.
	 */
	public static Iterator concat(Iterator[] iterators) {
		return new ConcatArrIter(iterators);
	}

	/**
	 * Private constructor to prevent instantiantion.
	 */
	private CoIterators() {
	}

}