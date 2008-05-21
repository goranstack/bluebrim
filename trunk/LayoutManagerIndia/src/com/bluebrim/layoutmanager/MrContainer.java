package com.bluebrim.layoutmanager;

import java.util.Enumeration;

public interface MrContainer {

	/**
	 * Adds an element to the collection at the end of list. It doesnt preserve
	 * the ordering among elements if any exists
	 * @param  obj  the element that is to be added to the list
	 */

	void addElement(Object obj);
	/**
	 * Returns whether the given element is contained in the container.
	 * @param obj the object that is to be searched for in the collection
	 * @return boolean  indicating whether the object is contained or not
	 */

	boolean contains(Object obj);
	/**
	 * Returns the collection that is held by this container
	 * @return Collection the collection that is held by this container
	 */

	MrCollection getCollection();
	/**
	 * Returns the contained elements as an Enumeration. May be useful
	 * when we need to sequence through all the elements in the collection
	 * @return Enumeration  an enumeration of the elements contained in the
	 *                      collection
	 */

	Enumeration getContainedElements();
	/**
	 * Returns the container or the target object that contains other elements
	 * @return Object  the container ( or target) that contains other elements
	 */

	Object getContainer();
} // Container
