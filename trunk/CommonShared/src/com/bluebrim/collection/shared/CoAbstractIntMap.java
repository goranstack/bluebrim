package com.bluebrim.collection.shared;

import java.util.*;

/**
 * This class provides a skeletal implementation of the <tt>CoIntMap</tt>
 * interface, to minimize the effort required to implement this interface. <p>
 *
 * @author Markus Persson 2001-01-04
 */
public abstract class CoAbstractIntMap implements CoIntMap {
	private transient Collection m_values = null;
	/**
	 * Sole constructor. (For invocation by subclass constructors, typically
	 * implicit.)
	 */
	protected CoAbstractIntMap() {
	}
	/**
	 * Removes all mappings from this map (optional operation). <p>
	 *
	 * @throws UnsupportedOperationException clear is not supported
	 * by this map.
	 */
	public void clear() {
		throw new UnsupportedOperationException();
	}
	/**
	 * Returns <tt>true</tt> if this map maps one or more keys to the
	 * specified value.  More formally, returns <tt>true</tt> if and only if
	 * this map contains at least one mapping to a value <tt>v</tt> such that
	 * <tt>(value==null ? v==null : value.equals(v))</tt>.  This operation
	 * will probably require time linear in the map size for most
	 * implementations of the <tt>CoIntMap</tt> interface.
	 *
	 * @param value value whose presence in this map is to be tested.
	 * @return <tt>true</tt> if this map maps one or more keys to the
	 *         specified value.
	 */
	public boolean containsValue(Object value) {
		Iterator entries = entryIterator();
		if (value == null) {
			while (entries.hasNext()) {
				Entry entry = (Entry) entries.next();
				if (entry.getValue() == null)
					return true;
			}
		} else {
			while (entries.hasNext()) {
				Entry entry = (Entry) entries.next();
				if (value.equals(entry.getValue()))
					return true;
			}
		}
		return false;
	}
	/**
	 * Returns the value to which this map maps the specified key.  Returns
	 * <tt>null</tt> if the map contains no mapping for this key.  A return
	 * value of <tt>null</tt> does not <i>necessarily</i> indicate that the
	 * map contains no mapping for the key; it's also possible that the map
	 * explicitly maps the key to <tt>null</tt>.  The <tt>containsKey</tt>
	 * operation may be used to distinguish these two cases.
	 *
	 * @param key key whose associated value is to be returned.
	 * @return the value to which this map maps the specified key, or
	 *	       <tt>null</tt> if the map contains no mapping for this key.
	 * 
	 * @see #containsKey(int)
	 */
	public Object get(int key) {
		Iterator entries = entryIterator();
		while (entries.hasNext()) {
			Entry entry = (Entry) entries.next();
			if (key == entry.getKey())
				return entry.getValue();
		}
		return null;
	}
	/**
	 * Returns <tt>true</tt> if this map contains no key-value mappings. <p>
	 *
	 * This implementation returns <tt>size() == 0</tt>.
	 *
	 * @return <tt>true</tt> if this map contains no key-value mappings.
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	/**
	 * @see CoIntMap
	 */
	public Object put(int key, Object value) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Copies all of the mappings from the specified map to this map
	 * (optional operation).  These mappings will replace any mappings that
	 * this map had for any of the keys currently in the specified map.
	 *
	 * This implementation iterates over the specified map's
	 * <tt>entrySet()</tt> collection, and calls this map's <tt>put</tt>
	 * operation once for each entry returned by the iteration.
	 *
	 * @param t Mappings to be stored in this map.
	 * 
	 * @throws UnsupportedOperationException if the <tt>putAll</tt> method is
	 * 		  not supported by this map.
	 * 
	 * @throws ClassCastException if the class of a value in the
	 * 	          specified map prevents it from being stored in this map.
	 * 
	 * @throws IllegalArgumentException some aspect of a value in the
	 *	          specified map prevents it from being stored in this map.
	 * 
	 * @throws NullPointerException this map does not permit <tt>null</tt>
	 *            values, and the specified value is <tt>null</tt>.
	 */
	public void putAll(CoIntMap t) {
		Iterator iter = t.entryIterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			put(entry.getKey(), entry.getValue());
		}
	}
	/**
	 * @see CoIntMap
	 */
	public Object remove(int key) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Returns a string representation of this map.  The string representation
	 * consists of a list of key-value mappings in the order returned by the
	 * map's <tt>entrySet</tt> view's iterator, enclosed in braces
	 * (<tt>"{}"</tt>).  Adjacent mappings are separated by the characters
	 * <tt>", "</tt> (comma and space).  Each key-value mapping is rendered as
	 * the key followed by an equals sign (<tt>"="</tt>) followed by the
	 * associated value.  Values are converted to strings as by
	 * <tt>String.valueOf(Object)</tt>.<p>
	 *
	 * This implementation creates an empty string buffer, appends a left
	 * brace, and iterates over the map's <tt>entrySet</tt> view, appending
	 * the string representation of each <tt>map.entry</tt> in turn.  After
	 * appending each entry except the last, the string <tt>", "</tt> is
	 * appended.  Finally a right brace is appended.  A string is obtained
	 * from the stringbuffer, and returned.
	 *
	 * @return a String representation of this map.
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer("{");
		Iterator entries = entryIterator();

		while (entries.hasNext()) {
			Entry entry = (Entry) (entries.next());
			buf.append(entry.getKey() + "=" + entry.getValue());
			if (entries.hasNext())
				buf.append(", ");
		}
		buf.append("}");
		return buf.toString();
	}
	/**
	 * Returns a collection view of the values contained in this map.  The
	 * collection is backed by the map, so changes to the map are reflected in
	 * the collection, and vice-versa.  (If the map is modified while an
	 * iteration over the collection is in progress, the results of the
	 * iteration are undefined.)  The collection supports element removal,
	 * which removes the corresponding entry from the map, via the
	 * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
	 * <tt>removeAll</tt>, <tt>retainAll</tt> and <tt>clear</tt> operations.
	 * It does not support the <tt>add</tt> or <tt>addAll</tt> operations.<p>
	 *
	 * This implementation returns a collection that subclasses abstract
	 * collection.  The subclass's iterator method returns a "wrapper object"
	 * over this map's <tt>entrySet()</tt> iterator.  The size method
	 * delegates to this map's size method and the contains method delegates
	 * to this map's containsValue method.<p>
	 *
	 * The collection is created the first time this method is called, and
	 * returned in response to all subsequent calls.  No synchronization is
	 * performed, so there is a slight chance that multiple calls to this
	 * method will not all return the same Collection.
	 *
	 * @return a collection view of the values contained in this map.
	 */
	public Collection values() {
		if (m_values == null) {
			m_values = new AbstractCollection() {
				public Iterator iterator() {
					return new Iterator() {
						private Iterator i = entryIterator();

						public boolean hasNext() {
							return i.hasNext();
						}

						public Object next() {
							return ((Entry) i.next()).getValue();
						}

						public void remove() {
							i.remove();
						}
					};
				}

				public int size() {
					return CoAbstractIntMap.this.size();
				}

				public boolean contains(Object v) {
					return CoAbstractIntMap.this.containsValue(v);
				}
			};
		}
		return m_values;
	}
}
