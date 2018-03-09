package com.bluebrim.layoutmanager;

import java.util.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
/**
 * This class represents a rectangular container, meaning the target object
 * that contains the elements is a rectangle. This maps to the target rectangle
 * in our case
 * @author Murali Ravirala
 * @version 1.0
 *
 */

public class RectangularContainer implements MrContainer,Cloneable {
	MrCollection collection; // collection of the items contained in the container
	com.bluebrim.layout.shared.CoLayoutableIF container; // container contains all the items in the collection

	/**
	 * Creates a rectangular container with the specified values
	 */

	public RectangularContainer(CoLayoutableIF target, RectangularCollection collection) {
		this.container = target;
		this.collection = collection;
	} // RectangularContainer()
	/**
	 * Adds an element to the collection at the end of list. It doesnt preserve
	 * the ordering among elements if any exists
	 * @param  obj  the element that is to be added to the list
	 */

	public void addElement(Object obj) {
		collection.addElement(obj);
	} // addElement()
	/**
	 * Returns whether the given element is contained in the container.
	 * @param obj the object that is to be searched for in the collection
	 * @return boolean  indicating whether the object is contained or not
	 */

	public boolean contains(Object obj) {
		return collection.contains(obj);
	} // contains()
	/**
	 * Returns the collection that is held by this container
	 * @return Collection the collection that is held by this container
	 */

	public MrCollection getCollection() {
		return collection;
	} // getCollection()
	/**
	 * Returns the contained elements as an Enumeration. May be useful
	 * when we need to sequence through all the elements in the collection
	 * @return Enumeration  an enumeration of the elements contained in the
	 *                      collection
	 */

	public Enumeration getContainedElements() {
		return collection.enumerate();
	} // getContainedElements()
	/**
	 * Returns the container or the target object that contains other elements
	 * @return Object  the container ( or target) that contains other elements
	 */
	public Object getContainer() {
		return container;
	} // getContainer()
} // RectangularContainer
