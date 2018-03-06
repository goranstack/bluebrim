package com.bluebrim.layoutmanager;

/**
 * Defines an Ordered collection of elements, wherein the elements in the list
 * are ordered based on a certain criterion. The application should use
 * this method whenever it wants the input to be in certain order
 * @author Murali Ravirala
 * @version 1.0
 */

public interface MrOrderedCollection extends MrCollection {

	/**
	 * Orders the elements in the list based on a certain ordering criterion,
	 * for e.g., the area of the shape in our case. This method should be
	 * invoked whenever the user wants the input to be arranged in order.
	 * By default the elements in the list are not ordered. Also the ordering
	 * may get disturbed after adding an element to the list (which adds
	 * an item to the end ). So, if necessary, ordering may have to be called
	 * after every insertion to preserve the ordering.
	 */

	void order();
}
