package com.bluebrim.collection.shared;

import java.util.*;

/**
 * Development suspended ...
 * Not fully functional yet.
 *
 * @author Markus Persson (1999-07-16 11:05:50)
 */
public class CoReorderableMap extends HashMap implements CoReorderableMapIF {
	private CoReorderableList m_list;
	private Collection m_values;
/**
 * CoReorderableMap constructor comment.
 */
public CoReorderableMap() {
	super();
	m_list = new CoReorderableList();
}
/**
 * CoReorderableMap constructor comment.
 * @param initialCapacity int
 */
public CoReorderableMap(int initialCapacity) {
	super(initialCapacity);
	m_list = new CoReorderableList(initialCapacity);
}
/**
 * CoReorderableMap constructor comment.
 * @param initialCapacity int
 * @param loadFactor float
 */
public CoReorderableMap(int initialCapacity, float loadFactor) {
	super(initialCapacity, loadFactor);
	m_list = new CoReorderableList(initialCapacity);
}
/**
 * CoReorderableMap constructor comment.
 * @param t java.util.Map
 */
public CoReorderableMap(Map t) {
	super(t);
	m_list = new CoReorderableList(t.values());
}
public void move(int fromIndex, int toIndex, int newIndex) {
	m_list.move(fromIndex, toIndex, newIndex);
}
public Object put(Object key, Object value) {
	return super.put(key, value);
}
public Collection values() {
	if (m_values==null) {
	    m_values = new AbstractCollection() {
				public Iterator iterator() {
					return m_list.iterator();
				}
				public int size() {
					return m_list.size();
				}
				public boolean contains(Object obj) {
					return m_list.contains(obj);
				}
				public void clear() {
					CoReorderableMap.this.clear();
				}
			};
		}
	return m_values;
}
}
