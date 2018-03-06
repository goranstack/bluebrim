package com.bluebrim.layout.shared;

import java.util.*;

/**
 * An object that is to be laid out by the layout engine can have children 
 * that also ar to be laid out. If so that object must implement this interface.
 */

public interface CoLayoutableContainerIF extends CoLayoutableIF
{

	/**
	 *  Return that children that are to be laid out.
	 */
	List getLayoutChildren(); // [ CoLayoutableIF ]
	
	/**
	 * Returns true if the <code>CoLayoutableContainerIF</code> represents a page 
	 * that is on the left side of a spread. Also returns true in all cases
	 * where the <code>CoLayoutableContainerIF</code> is not a page.
	 */
	boolean isLeftSide();
	
	boolean hasFiniteDimensions();
}