package com.bluebrim.collection.shared;

import java.util.*;

/**
 * <p>An extension of the <tt>List</tt> interface that permits reordering of the
 * list elements. Performing this operation thru the <tt>List</tt> interface
 * requires sucessive <tt>add</tt>/<tt>addAll</tt> and
 * <tt>remove</tt>/<tt>removeAll</tt>operations that can be
 * costly in common implementations. Also, certain lists may want to allow
 * reordering but not allow addition or removal of elements.</p>
 *
 * NOTE: Since the size() method is defined in both super interfaces it seems
 * neccessary to cast to either one when using size(), at least in VisualAge.
 * PENDING: Resolve the aforementioned problem, if possible.
 *
 * PENDING: Since this interface no longer extends Remote, remove or
 * rename it so as not to cause confusion.
 *
 * @author Markus Persson 1999-04-08
 */
public interface CoReorderableListIF extends List, CoReorderable {
}
