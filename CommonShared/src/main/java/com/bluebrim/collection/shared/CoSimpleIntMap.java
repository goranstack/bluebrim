package com.bluebrim.collection.shared;

import java.util.*;

/**
 * Lazy implementation until I have time for something better. <p>
 *
 * @author Markus Persson 2001-02-08
 */
public class CoSimpleIntMap extends CoAbstractIntMap {
	private Map m_hashMap = new HashMap();
	public void clear() {
		m_hashMap.clear();
	}
	public boolean containsKey(int key) {
		return m_hashMap.containsKey(new Integer(key));
	}
	public Iterator entryIterator() {
		throw new UnsupportedOperationException();
	}
	public Object get(int key) {
		return m_hashMap.get(new Integer(key));
	}
	public Object put(int key, Object value) {
		return m_hashMap.put(new Integer(key), value);
	}
	public Object remove(int key) {
		return m_hashMap.remove(new Integer(key));
	}
	public int size() {
		return m_hashMap.size();
	}
	/**
	 * Overridden to Object default since entryIterator is unsupported.
	 */
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode());
	}
}
