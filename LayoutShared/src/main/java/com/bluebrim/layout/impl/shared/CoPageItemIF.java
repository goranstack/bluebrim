package com.bluebrim.layout.impl.shared;

import java.io.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.transact.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * RMI-enabling interface for class CoPageItem.
 * See class for method details.
 * 
 * @author Dennis Malmström
 */

public interface CoPageItemIF extends Cloneable, CoFactoryElementIF, CoXmlEnabledIF, java.rmi.Remote
{	
	/**
		The class <code>CoPageItemIF.State</code> and all its subclasses (which are all inner classes in the subclasses of <code>CoPageItemIF</code>)
		are basically structs for transporting state from page items to page item views. By transporting the state in such a struct
		the entire state (that is relevant to the view) can be delivered in one call (in most cases a page item view and its page item
		reside in different VMs, hence the call is distributed).
	*/
	public static class State implements Serializable
	{
	};
	
	/**
		The class <code>CoPageItemIF.ViewState</code> and all its subclasses (which are all inner classes in the subclasses of <code>CoPageItemIF</code>)
		are basically structs for transporting state from page item views to page item when fetching page item state.
	*/
	public static class ViewState implements Serializable
	{
	};

	// "Infinite" dimension value
	// Double.MAX_VALUE would be a nice value to use but it causes java2D to behave strange
	public static double INFINITE_DIMENSION = 1e10;


	public CoPageItemIF deepClone();
	
	void destroy();
	
	State getState( ViewState viewState );
	
	CoRef getId();
}