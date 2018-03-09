package com.bluebrim.collection.shared;

/**
 * @author Markus Persson 2002-09-12
 */
public abstract class CoAbstractStringComparator implements CoKeyedComparator {

	public Comparable sortKey(Object value) {
		return stringKey(value);
	}

	public abstract String stringKey(Object value);

	public int compare(Object first, Object second) {
		return stringKey(first).compareTo(stringKey(second));
	}
}
