package com.bluebrim.layoutmanager;

import java.util.Enumeration;

/**
 * An interface for any collection of objects. Defines the methods for
 * operating on the Collection of Objects. The elements in the collection
 * need not be ordered in any specific way with respect to each other
 * @author Murali Ravirala
 * @version 1.0
 */

public interface MrCollection {
	/**
	 * Adds an element to the Collection at the end of the list
	 * @param  obj   the object to be added into the list
	 */

	void addElement(Object obj);
	/**
	 * Returns whether the collection contains the given element
	 * @param obj the object that is to be searched for in the list
	 * @return  boolean  a boolean value indicating whether the element
	 *                     is contained in the list or not
	 */

	boolean contains(Object obj);
	/**
	 * Returns the number of elements in the Collection
	 * @return int  an integer representing the count of the elements
	 */

	int count();
	/**
	 * Returns the elements in the Collection as an Enumeration which may
	 * be used for sequencing through all the elements.
	 * @return Enumeration  an enumeration of the components of this collection
	 */

	Enumeration enumerate();
	/**
	 * Returns the element at the specified index. To be used if direct access
	 * is required. Returns null if the element at the specified index does
	 * not exist
	 * @param index  the index of the element into the list
	 * @return Object  the object present at the given index, null if none
	 */

	Object getElement(int index);
	/**
	 * Returns the element that matches the given input. If there is no matching
	 * element in the list, then it returns null
	 * @param  obj  the object that is to be matched
	 * @return Object  the object that matched the given input
	 */

	Object getElement(Object obj);
	/**
	 * Returns whether the collection is empty or has any elements
	 */

	boolean isEmpty();
	/**
	 * Returns the elements of the Collection as an array of Objects. This
	 * may be useful if a some operation like sorting is to be implemented
	 * on the list
	 * @return Object[]  array representation of the elements in the list
	 */

	Object[] list();
	/**
	 * Removes an element from the specified location from the collection and
	 * returns the element. Returns if the specified index does not exist or
	 * the corresponding element is null
	 * @param  index  the index of the element that is to be removed
	 * @return Object  the object that was removed
	 */

	Object removeElement(int index);
} // Collection
