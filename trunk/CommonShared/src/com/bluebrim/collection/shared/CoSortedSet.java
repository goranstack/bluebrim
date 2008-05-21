package com.bluebrim.collection.shared;

import java.util.*;

/**
 * @author Markus Persson 2002-08-21
 */
public interface CoSortedSet extends SortedSet {

	public Iterator iterator(Object fromInclusive);

	public Iterator iteratorByKey(Comparable fromInclusiveKey);

	public Iterator iteratorByKey(Comparable fromInclusiveKey, Comparable toExclusiveKey);

	public CoSortedSet subSetByKey(Comparable fromInclusiveKey, Comparable toExclusiveKey);

}
