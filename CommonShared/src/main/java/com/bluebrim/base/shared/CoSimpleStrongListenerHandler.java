package com.bluebrim.base.shared;

import java.util.*;

/**
 * Simple event listener handler which uses
 * strong (normal) references to the listeners.
 *
 * The "simple" part stands for handling only one type of listener,
 * without type checking.
 *
 * PENDING: Consider synchronization.
 *
 * NOTE: Now GemStone adapted by removing silly static Swing remnant.
 *
 * @see CoSimpleWeakListenerHandler
 * @author Markus Persson 1999-10-17
 */
public class CoSimpleStrongListenerHandler {
	private Object[] m_listeners = null;
	private ReuseableIterator m_iterator = new ReuseableIterator();

	private class ReuseableIterator implements Iterator {
		int i = -1;

		public void reset() {
			i = (m_listeners == null) ? -1 : m_listeners.length - 1;
		}

		public boolean hasNext() {
			return (i >= 0);
		}

		public Object next() {
			if (hasNext()) {
				return m_listeners[i--];
			} else
				throw new NoSuchElementException();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	/**
	 * Add the listener as a listener of the specified type.
	 * @param listener the listener to be added
	 */
	public synchronized void add(EventListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("Listener " + listener + " is null.");

		if (m_listeners == null) {
			// If this is the first listener added, 
			// initialize the array.
			m_listeners = new Object[] { listener };
		} else {
			// Is listener on the list? If so, return.
			int index = -1;
			for (int i = m_listeners.length - 1; i >= 0; i--) {
				if (m_listeners[i] == listener) {
					return;
				}
			}

			// Otherwise copy the array and add the new listener.
			int len = m_listeners.length;
			Object[] tmp = new Object[len + 1];
			System.arraycopy(m_listeners, 0, tmp, 0, len);

			tmp[len] = listener;

			m_listeners = tmp;
		}
	}
	/**
	 * Doesn't need Remote wrapping, since the objects are intended to be used
	 * only on the same side (server or client) they are created at. This class
	 * is not Remote nor Serializable.
	 */
	public Iterator getListeners() {
		m_iterator.reset();
		return m_iterator;
	}
	/**
	 * Remove the listener as a listener of the specified type.
	 * @param listener the listener to be removed
	 */
	public synchronized void remove(EventListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("Listener " + listener + " is null");

		// Do we have a list?
		if (m_listeners != null) {
			// Is listener on the list?
			int index = -1;
			for (int i = m_listeners.length - 1; i >= 0; i--) {
				if (m_listeners[i] == listener) {
					index = i;
					break;
				}
			}

			// If so, remove it.
			if (index != -1) {
				if (m_listeners.length > 1) {
					Object[] tmp = new Object[m_listeners.length - 1];
					// Copy the array up to index.
					System.arraycopy(m_listeners, 0, tmp, 0, index);
					// Copy from one past the index, up to
					// the end of tmp (which is one element
					// shorter than the old list)
					if (index < tmp.length)
						System.arraycopy(m_listeners, index + 1, tmp, index, tmp.length - index);

					m_listeners = tmp;
				} else {
					m_listeners = null;
				}
			}
		}
	}
}