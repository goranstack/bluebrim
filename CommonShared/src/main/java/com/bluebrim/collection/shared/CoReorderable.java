package com.bluebrim.collection.shared;

/**
 * <p>An interface for "collections" that permits reordering of its
 * elements. The "collection" need not implement any other interface,
 * in particular none of the <tt>java.util</tt> collection interfaces.</p>
 *
 * @author Markus Persson 1999-04-08
 * @see CoReorderableListIF
 */
public interface CoReorderable {
/**
 * <p>Reorders the elements of this collection by moving the portion of
 * between the specified
 * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive, to the
 * position specified by <tt>newIndex</tt>. (If <tt>fromIndex</tt> and
 * <tt>toIndex</tt> are equal, the collection is not affected.)<p>
 *
 * <p>Recently, long after this interface was created, I became aware of
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
 * @see java.util.List#subList()
 * @see javax.swing.DefaultTableModel#moveRow()
 */
void move(int fromIndex, int toIndex, int newIndex);
/**
 * Returns the number of elements in this collection.  If this collection contains
 * more than <tt>Integer.MAX_VALUE</tt> elements, returns
 * <tt>Integer.MAX_VALUE</tt>.
 *
 * @return the number of elements in this collection.
 */
int size();
}
