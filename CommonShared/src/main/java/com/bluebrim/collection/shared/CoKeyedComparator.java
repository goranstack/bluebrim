package com.bluebrim.collection.shared;

import java.util.*;

/**
 * Copy of the Gemstone variant.
 * 
 * @author Markus Persson 2002-08-20
 */
public interface CoKeyedComparator extends Comparator {

	public Comparable sortKey(Object value);
	
	/**
	 * PENDING: Remove this method? /Markus
	 */
//	public void typeCheck(Comparable key);
}
