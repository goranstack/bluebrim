package com.bluebrim.base.shared;

import java.io.*;
import java.util.*;

// Copy of javax.swing.event.EventListenerList with nom-transient array of listeners.

public class CoEventListenerList implements Serializable
{
	/* A null array to be shared by all empty listener lists*/
	private final static Object[] NULL_ARRAY = new Object[0];
	
	/* The list of ListenerType - Listener pairs */
	protected Object[] listenerList = NULL_ARRAY;
	/**
	 * Add the listener as a listener of the specified type.
	 * @param t the type of the listener to be added
	 * @param l the listener to be added
	 */
	public synchronized void add(Class t, EventListener l) {
	if (l==null) {
	    // In an ideal world, we would do an assertion here
	    // to help developers know they are probably doing
	    // something wrong
	    return;
	}
	if (!t.isInstance(l)) {
	    throw new IllegalArgumentException("Listener " + l +
					 " is not of type " + t);
	}
	if (listenerList == NULL_ARRAY) {
	    // if this is the first listener added, 
	    // initialize the lists
	    listenerList = new Object[] { t, l };
	} else {
	    // Otherwise copy the array and add the new listener
	    int i = listenerList.length;
	    Object[] tmp = new Object[i+2];
	    System.arraycopy(listenerList, 0, tmp, 0, i);

	    tmp[i] = t;
	    tmp[i+1] = l;

	    listenerList = tmp;
	}
	}
	/**
	 * Return the total number of listeners for this listenerlist
	 */
	public int getListenerCount() {
	return listenerList.length/2;
	}
	/**
	 * Return the total number of listeners of the supplied type 
	 * for this listenerlist.
	 */
	public int getListenerCount(Class t) {
	int count = 0;
	Object[] lList = listenerList;
	for (int i = 0; i < lList.length; i+=2) {
	    if (t == (Class)lList[i])
		count++;
	}
	return count;
	}
	/**
	 * This passes back the event listener list as an array
	 * of ListenerType - listener pairs.  Note that for 
	 * performance reasons, this implementation passes back 
	 * the actual data structure in which the listner data
	 * is stored internally!  
	 * This method is guaranteed to pass back a non-null
	 * array, so that no null-checking is required in 
	 * fire methods.  A zero-length array of Object should
	 * be returned if there are currently no listeners.
	 * 
	 * WARNING!!! Absolutely NO modification of
	 * the data contained in this array should be made -- if
	 * any such manipulation is necessary, it should be done
	 * on a copy of the array returned rather than the array 
	 * itself.
	 */
	public Object[] getListenerList() {
	return listenerList;
	}
	/**
	 * Remove the listener as a listener of the specified type.
	 * @param t the type of the listener to be removed
	 * @param l the listener to be removed
	 */
	public synchronized void remove(Class t, EventListener l) {
	if (l ==null) {
	    // In an ideal world, we would do an assertion here
	    // to help developers know they are probably doing
	    // something wrong
	    return;
	}
	if (!t.isInstance(l)) {
	    throw new IllegalArgumentException("Listener " + l +
					 " is not of type " + t);
	}
	// Is l on the list?
	int index = -1;
	for (int i = listenerList.length-2; i>=0; i-=2) {
	    if ((listenerList[i]==t) && (listenerList[i+1].equals(l) == true)) {
		index = i;
		break;
	    }
	}
	
	// If so,  remove it
	if (index != -1) {
	    Object[] tmp = new Object[listenerList.length-2];
	    // Copy the list up to index
	    System.arraycopy(listenerList, 0, tmp, 0, index);
	    // Copy from two past the index, up to
	    // the end of tmp (which is two elements
	    // shorter than the old list)
	    if (index < tmp.length)
		System.arraycopy(listenerList, index+2, tmp, index, 
				 tmp.length - index);
	    // set the listener array to the new array or null
	    listenerList = (tmp.length == 0) ? NULL_ARRAY : tmp;
	    }
	}
	/**
	 * Return a string representation of the EventListenerList.
	 */
	public String toString() {
	Object[] lList = listenerList;
	String s = "EventListenerList: ";
	s += lList.length/2 + " listeners: ";
	for (int i = 0 ; i <= lList.length-2 ; i+=2) {
	    s += " type " + ((Class)lList[i]).getName();
	    s += " listener " + lList[i+1];
	}
	return s;
	}
}
