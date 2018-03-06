package com.bluebrim.layoutmanager;

import java.util.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * This class represents an ordered collection of rectangles, wherein the
 * rectangles are ordered based on area in the descending order
 * @author Murali Ravirala
 * @version 1.0
 */
public class RectangularCollection implements MrOrderedCollection{
	private Vector list;  // the list that holds the collection

	/**
	 * Initialises the collection to have zero elements
	 */
	public RectangularCollection() {
		list = new Vector();
	}
	/**
	 * Initialises the collection to have elements from the given Vector
	 * @param  Vector  the input list to which the new vector will be
	 *                 initiailized
	 */
	public RectangularCollection(Vector list) {
		this.list = list;
	} // RectangularCollection()
	/**
	 * Adds an element to the collection at the end of the list thereby
	 * increasing the element count by one. This insertion may cause the
	 * elements to become disordered with respect to each. To ensure the
	 * ordering the applicatio may invoke the order() method.
	 * @param obj  the shape to be added
	 */

	public void addElement(Object obj) {
		list.addElement(obj);
	} // addElement()
	/**
	 * Returns the clone of the list of elements in the collection
	 * @return Object  the cloned list
	 */

	public Object clone() {
		return (Vector)list.clone();
	} // clone()
	/**
	 * Returns whether the collection contains the given element
	 * @param obj the object that is to be searched for in the list
	 * @return  boolean  a boolean value indicating whether the element
	 *                     is contained in the list or not
	 */

	public boolean contains(Object obj) {
		// loop through the list checking for presence of the given element
		for(int i = 0; i < list.size(); i++)
			if(list.elementAt(i).equals(obj))
				return true;
		return false;
	} // contains()
	/**
	 * Returns a count of the number of shapes in the Collection
	 * @return int  an integer representing the count of elements in the list
	 */

	public int count()  {
		return list.size();
	} // count()
	/**
	 * Returns the shapes in the Collection as an Enumeration which may be
	 * used when we have to loop through the whole collection sequentially
	 * @return Enumeration the enumeration of the elements contained in
	 *                     the list
	 */

	public Enumeration enumerate() {
		return list.elements();
	} // enumerate()
	/**
	 * Returns the rectangle at a specified index and null if the there
	 * no element in the list at the specified index
	 * @param  index   the index of the element
	 * @return object  the rectangle that is present at the specified, null
	 *                  if none
	 */

	public Object getElement(int index) {
		return list.elementAt(index);
	} // getElement()
	/**
	 * Returns the element that matches the given input. If there is no matching
	 * element in the list, then it returns null
	 * @param  obj  the object that is to be matched
	 * @return Object  the shape that matched the given input
	 */

	public Object getElement(Object obj) {
		// loop through the list checking for presence of the given element
		for(int i = 0; i < list.size(); i++)
			if((list.elementAt(i)).equals(obj))
				return list.elementAt(i);
		return null;
	} // getElement()
	/**
	 * Tests whether the collection is empty or has any shapes
	 * @return boolean  a boolean value indicating whether the list is empty
	 *                  or not
	 */

	public boolean isEmpty() {
		return list.isEmpty();
	} // isEmpty()
	/**
	 * Converts the list and returns it as a array of rectangles. Could be used
	 * in cases where a certain operation is to be applied to the compelete
	 * list, rather than a single element. The original list is kept intact.
	 * @return  object  the array representation of the list
	 */

	public Object[] list() {
		Object[] arr = new Object[list.size()];
		list.copyInto(arr);
		return arr;
	} // list()
	/**
	 * Orders the elements in the collection in a specified order.
	 * Currently the ordering criterion is the area of the shape with
	 * the elements in the list being arranged in the descending order
	 * of priority
	 */

	public void order() {
		//System.out.println("This method is still to be implemented.....");
		// currently bubble sorting is being implemented; could be changed
		// later on if necessary

		// loop through the list
		for(int i = 0; i < list.size()-1; i++) {
			for(int j = i + 1; j < list.size(); j++) {
				// check if the adjacent elements are out of order
				if(((CoLayoutableIF)list.elementAt(j)).getArea() >
				   ((CoLayoutableIF)list.elementAt(i)).getArea() ) {

				   // if so, the swap the shapes
				   CoLayoutableIF temp = (CoLayoutableIF)list.elementAt(j);
				   list.setElementAt(list.elementAt(i), j);
				   list.setElementAt(temp, i);
				} // if
			} // for
		} // for

		/*
		for(int i =0; i < list.size(); i++) {
		   ((Rectangle)list.elementAt(i)).print();
		} */
	} // order()
	/**
	 * Removes an element from the specified location from the collection and
	 * returns the element. Returns if the specified index does not exist or
	 * the corresponding element is null
	 * @param  index  the index of the element that is to be removed
	 * @return Object  the shape that was removed
	 */

	public Object removeElement(int index) {
		// check if the index is present
		if(index < list.size()) {
			Object temp = list.elementAt(index);
			list.removeElementAt(index);
			return temp;
		} // if
		return null;
	} // removeElement()
} // RectangularCollection 
