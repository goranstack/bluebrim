package com.bluebrim.collection.shared;

import java.util.*;

/**
 * Simple implementation for not too big non-negative indices.
 *
 * NOTE: size() and entryIterator() is not implemented ...
 *
 * @author Markus Persson 2001-03-23
 */
public class CoArrayIntMap extends CoAbstractIntMap {
	protected Object[] m_elements;
	public CoArrayIntMap() {
		this(16);
	}
	public CoArrayIntMap(int initialSize) {
		m_elements = new Object[Math.max(4, initialSize)];
	}
	public void clear() {
		// To be lazy, allocate new.
		m_elements = new Object[m_elements.length];
	}
	public boolean containsKey(int key) {
		return (key >= 0) && (key < m_elements.length) && (m_elements[key] != null);
	}
	public Iterator entryIterator() {
		throw new UnsupportedOperationException();
	}
	public Object get(int key) {
		return (key >= 0) && (key < m_elements.length) ? m_elements[key] : null;
	}
	public Object put(int key, Object value) {
		if (key < 0)
			throw new IllegalArgumentException();

		if (key >= m_elements.length) {
			int newLen = Math.max(key + 1, m_elements.length * 2);
			Object[] newArr = new Object[newLen];
			System.arraycopy(m_elements, 0, newArr, 0, m_elements.length);
			m_elements = newArr;
		}
		Object oldVal = m_elements[key];
		m_elements[key] = value;
		return oldVal;
	}
	public Object remove(int key) {
		if ((key >= 0) && (key < m_elements.length)) {
			Object oldVal = m_elements[key];
			m_elements[key] = null;
			return oldVal;
		} else {
			return null;
		}
	}
	public int size() {
		throw new UnsupportedOperationException();
	}
	/**
	 * Overridden to Object default since entryIterator() is unsupported.
	 */
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode());
	}
}
