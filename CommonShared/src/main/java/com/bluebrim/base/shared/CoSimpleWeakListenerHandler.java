package com.bluebrim.base.shared;

import java.lang.ref.*;
import java.util.*;

/**
 * Simple event listener handler which uses
 * weak references to the listeners.
 *
 * The "simple" part stands for handling only one type of listener,
 * without type checking.
 *
 * PENDING: Clear out removed entries. ReferenceQueue:s?
 *
 * PENDING: Consider synchronization.
 *
 * NOTE: Now GemStone adapted by removing silly static Swing remnant.
 *
 * @author Markus Persson 1999-10-03
 */
public class CoSimpleWeakListenerHandler {
	private WeakReference[] m_listeners = null;
	private ReuseableWeakIterator m_iterator = new ReuseableWeakIterator();

	private class ReuseableWeakIterator implements Iterator {
		int i = -1;
		Object m_next;

		public void reset() {
			i = (m_listeners == null) ? -1 : m_listeners.length-1;
			m_next = null;
		}

		public boolean hasNext() {
			// Keep strong reference to next object, so it remains when doing next().
			while ((i >= 0) && (m_next = m_listeners[i].get()) == null)
				i--;
			return (i >= 0);
		}

		public Object next() {
			if (hasNext()) {
				// Careful not to loose it ...
				Object next = m_next;
				// ... when we release the strong reference.
				m_next = null;
				i--;
				return next;
			} else
				throw new NoSuchElementException();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
/**
 * Add the listener as a listener of the specified type.
 * @param l the listener to be added
 */
public synchronized void add(EventListener l) {
	if (l == null)
	    throw new IllegalArgumentException("Listener " + l + " is null.");

	WeakReference weakRef = new WeakReference(l);
	if (m_listeners == null) {
		// If this is the first listener added, 
		// initialize the array.
		m_listeners = new WeakReference[] { weakRef };
	} else {
		// Otherwise copy the array and add the new listener.
		int len = m_listeners.length;
		WeakReference[] tmp = new WeakReference[len+1];
		System.arraycopy(m_listeners, 0, tmp, 0, len);

		tmp[len] = weakRef;

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
 * @param l the listener to be removed
 */
public synchronized void remove(EventListener l) {
	if (l == null)
		throw new IllegalArgumentException("Listener " + l + " is null");

	// Do we have a list?
	if (m_listeners != null) {
		// Is l on the list?
		int index = -1;
		for (int i = m_listeners.length - 1; i>=0; i--) {
			if (m_listeners[i].equals(l)) {
				index = i;
				break;
			}
		}

		// If so, remove it.
		if (index != -1) {
			if (m_listeners.length > 1) {
				WeakReference[] tmp = new WeakReference[m_listeners.length-1];
				// Copy the array up to index.
				System.arraycopy(m_listeners, 0, tmp, 0, index);
				// Copy from one past the index, up to
				// the end of tmp (which is one element
				// shorter than the old list)
				if (index < tmp.length)
					System.arraycopy(m_listeners, index+1, tmp, index, tmp.length - index);

				m_listeners = tmp;
			} else {
				m_listeners = null;
			}
		}
	}
}
}
