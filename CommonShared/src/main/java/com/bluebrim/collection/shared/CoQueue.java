package com.bluebrim.collection.shared;

import java.util.*;

/**
 * Interface for queues, suitable for delegating to
 * Facets CuQueue. Note that the policy for CuQueue
 * is to have at most one consumer at any given time.
 * 
 * The docs for the methods are copied from CuQueue
 * which is why they mention sessions and such.
 * 
 * @author Markus Persson 2002-09-18
 */
public interface CoQueue {

	/**
	 * Add an object to the input side of the receiver.
	 * 
	 * @param object the Object to add
	 * @throws IllegalArgumentException if argument is null
	 */
	public void add(Object object);

	/**
	 * Add every object in the collection to the input side of the receiver
	 * in the order retrieved from an iterator on the collection.
	 * 
	 * @param collection the Collection containing the objects to add
	 * @throws IllegalArgumentException if an element of the collection is null
	 */
	public void addAll(Collection collection);

	/**
	 * Remove all objects from the output side of the receiver.
	 */	
	public void clear();

	/**
	 * Return true if size is 0 otherwise return false.
	 * 
	 * @return a boolean indicating the receiver is empty (true) or not (false)
	 */
	public boolean isEmpty();
	
	/**
	 * Return the object that would be removed next from the receiver.
	 * If the receiver is empty return null. This can be called by either
	 * producers or consumers. This method is more time and space hungry
	 * for sessions that are not the queue consumer. The extra cost for
	 * non-consumers increases more than linearly with the size of the queue.
	 * 
	 * @return the object that is next to be removed or null if the receiver
	 * is empty
	 */
	public Object peek();
	
	/**
	 * Remove an object from the output side of the receiver, then return it.
	 * If the receiver is empty return null.
	 * 
	 * @return the object that is removed from the output side of the receiver
	 * or null if the receiver is empty
	 */
	public Object remove();
	
	/**
	 * Remove all objects from the output side of the receiver, then return
	 * a List of those objects in removal order. If the receiver is empty
	 * return an empty List.
	 * 
	 * @return an array of the removed objects, empty if the receiver is empty
	 */
	public List removeAll();
	
	/**
	 * Remove <code>n</code> objects from the output side of the receiver then
	 * return a List of those objects in removal order. If the receiver is
	 * empty return an empty List. If the receiver has less than <code>n</code>
	 * objects then remove and return all the objects.
	 * 
	 * @return an array of the removed objects, empty if the receiver is empty
	 */
	public List removeCount(int n);
	
	/**
	 * Return the number of objects in the receiver yet to be removed.
	 * This may require an O(n) algorithm.
	 * 
	 * @return the int number of objects in the receiver yet to be removed
	 */
	public int size();
	
	/**
	 * Return a collection containing every object in the receiver that has
	 * not been removed. This can be called by either producers or consumers.
	 * The collection argument can be empty or non-empty. All of the
	 * non-removed objects in the receiver are added to the collection in
	 * removal order (but they are not removed!) The collection argument can
	 * be null in which case a new List is created to contain the objects in
	 * removal order.
	 * 
	 * @param collection the collection to add objects to or null if a new
	 * collection is to be created
	 * @return the collection to which the objects have been added
	 */
	public Collection toCollection(Collection collection);

}
