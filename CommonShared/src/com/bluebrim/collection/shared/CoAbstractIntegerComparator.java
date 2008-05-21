package com.bluebrim.collection.shared;

/**
 * @author Markus Persson 2002-09-12
 */
public abstract class CoAbstractIntegerComparator implements CoKeyedComparator {

	public Comparable sortKey(Object value) {
		return integerKey(value);
	}

	public Integer integerKey(Object value) {
		return new Integer(intKey(value));
	}

	public abstract int intKey(Object value);

	public int compare(Object first, Object second) {
		return compare(intKey(first), intKey(second));
	}

	private static final int compare(int first, int second) {
		return (first < second ? -1 : (first == second ? 0 : 1));
	}
}
