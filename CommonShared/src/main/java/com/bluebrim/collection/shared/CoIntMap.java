package com.bluebrim.collection.shared;

import java.util.*;

/**
 * An interface similar to java.util.Map, with the difference that a CoIntMap
 * maps an int to an object.<p>
 *
 * PENDING: Fix comments stolen from Map!
 *
 * The <tt>CoIntMap</tt> interface provides two <i>collection views</i>, which
 * allow a map's contents to be viewed as a collection of values,
 * or iteration of key-value mappings.  The <i>order</i> of a map is defined as
 * the order in which the iterators on the map's collection views return their
 * elements.  Some map implementations, like the <tt>TreeMap</tt> class, make
 * specific guarantees as to their order; others, like the <tt>HashMap</tt>
 * class, do not.<p>
 *
 * All general-purpose map implementation classes should provide two
 * "standard" constructors: a void (no arguments) constructor which creates an
 * empty map, and a constructor with a single argument of type <tt>Map</tt>,
 * which creates a new map with the same key-value mappings as its argument.
 * In effect, the latter constructor allows the user to copy any map,
 * producing an equivalent map of the desired class.  There is no way to
 * enforce this recommendation (as interfaces cannot contain constructors) but
 * all of the general-purpose map implementations in the JDK comply.
 *
 * @see java.util.Map
 * @author Markus Persson 2001-01-04
 */
public interface CoIntMap {
	/**
	 * A map entry (key-value pair).  The <tt>Map.entrySet</tt> method returns
	 * a collection-view of the map, whose elements are of this class.  The
	 * <i>only</i> way to obtain a reference to a map entry is from the
	 * iterator of this collection-view.  These <tt>Map.Entry</tt> objects are
	 * valid <i>only</i> for the duration of the iteration; more formally,
	 * the behavior of a map entry is undefined if the backing map has been
	 * modified after the entry was returned by the iterator, except through
	 * the iterator's own <tt>remove</tt> operation, or through the
	 * <tt>setValue</tt> operation on a map entry returned by the iterator.
	 *
	 * @see Map#entrySet()
	 */
	public interface Entry {
		/**
		 * Returns the key corresponding to this entry.
		 *
		 * @return the key corresponding to this entry.
		 */
		int getKey();

		/**
		 * Returns the value corresponding to this entry.  If the mapping
		 * has been removed from the backing map (by the iterator's
		 * <tt>remove</tt> operation), the results of this call are undefined.
		 *
		 * @return the value corresponding to this entry.
		 */
		Object getValue();

		/**
		 * Replaces the value corresponding to this entry with the specified
		 * value (optional operation).  (Writes through to the map.)  The
		 * behavior of this call is undefined if the mapping has already been
		 * removed from the map (by the iterator's <tt>remove</tt> operation).
		 *
		 * @param value new value to be stored in this entry.
		 * @return old value corresponding to the entry.
		 * 
		 * @throws UnsupportedOperationException if the <tt>put</tt> operation
		 *	      is not supported by the backing map.
		 * @throws ClassCastException if the class of the specified value
		 * 	      prevents it from being stored in the backing map.
		 * @throws    IllegalArgumentException if some aspect of this value
		 *	      prevents it from being stored in the backing map.
		 * @throws NullPointerException the backing map does not permit
		 *	      <tt>null</tt> values, and the specified value is
		 *	      <tt>null</tt>.
		 */
		Object setValue(Object value);

		/**
		 * Compares the specified object with this entry for equality.
		 * Returns <tt>true</tt> if the given object is also a map entry and
		 * the two entries represent the same mapping.  More formally, two
		 * entries <tt>e1</tt> and <tt>e2</tt> represent the same mapping
		 * if<pre>
		 *     (e1.getKey() == e2.getKey())  &&
		 *     (e1.getValue() == null ?
		 *      e2.getValue() == null : e1.getValue().equals(e2.getValue()))
		 * </pre>
		 * This ensures that the <tt>equals</tt> method works properly across
		 * different implementations of the <tt>Map.Entry</tt> interface.
		 *
		 * @param o object to be compared for equality with this map entry.
		 * @return <tt>true</tt> if the specified object is equal to this map
		 *         entry.
		 */
		boolean equals(Object o);

		/**
		 * Returns the hash code value for this map entry.  The hash code
		 * of a map entry <tt>e</tt> is defined to be: <pre>
		 *     e.getKey() ^ (e.getValue()==null ? 0 : e.getValue().hashCode())
		 * </pre>
		 * This ensures that <tt>e1.equals(e2)</tt> implies that
		 * <tt>e1.hashCode() == e2.hashCode()</tt> for any two Entries
		 * <tt>e1</tt> and <tt>e2</tt>, as required by the general
		 * contract of <tt>Object.hashCode</tt>.
		 *
		 * @return the hash code value for this map entry.
		 * @see Object#hashCode()
		 * @see Object#equals(Object)
		 * @see #equals(Object)
		 */
		int hashCode();
	}

	/**
	 * Removes all mappings from this map (optional operation).
	 *
	 * @throws UnsupportedOperationException clear is not supported by this
	 * 		  map.
	 */
	void clear();
/**
 * Returns <tt>true</tt> if this map contains a mapping for the specified
 * key.
 *
 * @param key key whose presence in this map is to be tested.
 * @return <tt>true</tt> if this map contains a mapping for the specified
 * key.
 */
boolean containsKey(int key);
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
boolean containsValue(Object value);
/**
 * PENDING: Look the use of an operation like this over. /Markus
 *
 * Returns a iterator view of the mappings contained in this map. Each element
 * in the returned iterator is a <tt>CoIntMap.Entry</tt>.  The iterator is backed by the
 * map, so changes to the map are reflected in the iterator, and vice-versa.
 * If the map is modified while an iteration is in progress,
 * the results of the iteration are undefined.  The iterator supports element
 * removal, which removes the corresponding mapping from the map, via the
 * <tt>Iterator.remove</tt> operation.
 *
 * @return a set view of the mappings contained in this map.
 */
public Iterator entryIterator();
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
Object get(int key);
	/**
	 * Returns <tt>true</tt> if this map contains no key-value mappings.
	 *
	 * @return <tt>true</tt> if this map contains no key-value mappings.
	 */
	boolean isEmpty();
/**
 * Associates the specified value with the specified key in this map
 * (optional operation).  If the map previously contained a mapping for
 * this key, the old value is replaced.
 *
 * @param key key with which the specified value is to be associated.
 * @param value value to be associated with the specified key.
 * @return previous value associated with specified key, or <tt>null</tt>
 *	       if there was no mapping for key.  A <tt>null</tt> return can
 *	       also indicate that the map previously associated <tt>null</tt>
 *	       with the specified key, if the implementation supports
 *	       <tt>null</tt> values.
 * 
 * @throws UnsupportedOperationException if the <tt>put</tt> operation is
 *	          not supported by this map.
 * @throws ClassCastException if the class of the specified value
 * 	          prevents it from being stored in this map.
 * @throws IllegalArgumentException if some aspect of this key or value
 *	          prevents it from being stored in this map.
 * @throws NullPointerException this map does not permit <tt>null</tt>
 *            values, and the specified value is <tt>null</tt>.
 */
Object put(int key, Object value);
/**
 * Copies all of the mappings from the specified map to this map
 * (optional operation).  These mappings will replace any mappings that
 * this map had for any of the keys currently in the specified map.
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
void putAll(CoIntMap t);
/**
 * Removes the mapping for this key from this map if present (optional
 * operation).
 *
 * @param key key whose mapping is to be removed from the map.
 * @return previous value associated with specified key, or <tt>null</tt>
 *	       if there was no mapping for key.  A <tt>null</tt> return can
 *	       also indicate that the map previously associated <tt>null</tt>
 *	       with the specified key, if the implementation supports
 *	       <tt>null</tt> values.
 * @throws UnsupportedOperationException if the <tt>remove</tt> method is
 *         not supported by this map.
 */
Object remove(int key);
/**
 * Returns the number of key-value mappings in this map.  If the
 * map contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
 * <tt>Integer.MAX_VALUE</tt>.
 *
 * @return the number of key-value mappings in this map.
 */
int size();
/**
 * PENDING: Look the use of an operation like this over. /Markus
 *
 * Returns a collection view of the values contained in this map.  The
 * collection is backed by the map, so changes to the map are reflected in
 * the collection, and vice-versa.  If the map is modified while an
 * iteration over the collection is in progress, the results of the
 * iteration are undefined.  The collection supports element removal,
 * which removes the corresponding mapping from the map, via the
 * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
 * <tt>removeAll</tt>, <tt>retainAll</tt> and <tt>clear</tt> operations.
 * It does not support the add or <tt>addAll</tt> operations.
 *
 * @return a collection view of the values contained in this map.
 */
public Collection values();
}
