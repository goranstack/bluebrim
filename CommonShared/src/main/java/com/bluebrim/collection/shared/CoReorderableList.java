package com.bluebrim.collection.shared;

import java.util.*;

/**
 * Implementation of the CoReorderableListIF interface.
 *
 * @see CoReorderableListIF
 * @author Markus Persson 1999-04-08
 */
public class CoReorderableList extends CoArrayList implements CoReorderableListIF {
/**
 * CoReorderableList constructor comment.
 */
public CoReorderableList() {
	super();
}
/**
 * CoReorderableList constructor comment.
 * @param initialCapacity int
 */
public CoReorderableList(int initialCapacity) {
	super(initialCapacity);
}
/**
 * CoReorderableList constructor comment.
 * @param c java.util.Collection
 */
public CoReorderableList(java.util.Collection c) {
	super(c);
}
/**
 * Since this List is remote, the iterator should be too.
 *
 * @author Markus Persson 1999-11-08
 */
public Iterator iterator() {
	return CoRemoteIterator.wrap(super.iterator());
}
/**
 * <p>Reorders the elements of this list by moving the portion of
 * between the specified
 * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive, to the
 * position specified by <tt>newIndex</tt>. (If <tt>fromIndex</tt> and
 * <tt>toIndex</tt> are equal, the list is not affected.)<p>
 *
 * <p>Recently, long after this class was created, I became aware of
 * the existence of a similar method in javax.swing.DefaultTableModel,
 * named moveRow(), which uses a different convention for its parameters.
 * Maybe one should adapt to that convention.</p>
 *
 * @param fromIndex low endpoint (inclusive) of the sublist.
 * @param toKey high endpoint (exclusive) of the sublist.
 * @param newIndex desired new index of the first element of the sublist.
 * 
 * @throws IndexOutOfBoundsException for an illegal endpoint index value
 *     (fromIndex &lt; 0 || toIndex &gt; size || fromIndex &gt; toIndex || newIndex &gt; size-(toIndex-fromIndex)).
 *
 * @see CoReorderable
 * @see java.util.List#subList()
 * @see javax.swing.DefaultTableModel#moveRow()
 */
public void move(int fromIndex, int toIndex, int newIndex) {
	int size = size();
	if (fromIndex < 0 || toIndex > size || fromIndex > toIndex || newIndex+(toIndex-fromIndex) > size)
	    throw new IndexOutOfBoundsException(
		"fromIndex: "+fromIndex+", toIndex: "+toIndex+", newIndex: "+newIndex+", Size: "+size);

	if (fromIndex == newIndex) return;

	modCount++;
	
	int selSize = toIndex - fromIndex;
	int restSrc, restDst, restSize;

	if (newIndex < fromIndex) {
		restSrc = newIndex;
		restDst = newIndex + selSize;
		restSize = fromIndex - newIndex;
	} else {
		restSrc = toIndex;
		restDst = fromIndex;
		restSize = (newIndex + selSize) - toIndex;
	}

/*
	// Do these two operations without interference. That is, store away
	// the data for the operation to be performed last temporarily somewhere
	// before the first operation is performed.
	// Could optimize by making the smaller interval the one stored away.
	
	System.arraycopy(elementData, fromIndex, elementData, newIndex, selSize);
	System.arraycopy(elementData, restSrc, elementData, restDst, restSize);
*/

	if (restSize < selSize) {
		Object[] tempBuf = new Object[restSize];
		System.arraycopy(elementData, restSrc, tempBuf, 0, restSize);
		System.arraycopy(elementData, fromIndex, elementData, newIndex, selSize);
		System.arraycopy(tempBuf, 0, elementData, restDst, restSize);
	} else {
		Object[] tempBuf = new Object[selSize];
		System.arraycopy(elementData, fromIndex, tempBuf, 0, selSize);
		System.arraycopy(elementData, restSrc, elementData, restDst, restSize);
		System.arraycopy(tempBuf, 0, elementData, newIndex, selSize);
	}
}
}
