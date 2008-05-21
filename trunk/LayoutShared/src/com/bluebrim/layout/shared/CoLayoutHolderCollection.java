package com.bluebrim.layout.shared;

import java.util.*;

/**
 * A <code>CoLayoutHolderCollection</code> is implemented by objects that has a
 * collection of <code>CoLayout</code>'s.
 * 
 * This is an attempt to create an interface that can replace 
 * <code>CoPageItemHolderCollectionIF</code> when refered from outside the layout domain. The
 * reason for doing this is to minimize what is exposed in the core part of the
 * layout domain. 
 * <br>
 * The working strategi is to replace all external references to code>CoPageItemHolderCollectionIF</code>
 * with this interface and add requested methods until everyone is happy.  
 * <br>
 * 
 * @author: Göran Stäck 2002-05-07
 */

public interface CoLayoutHolderCollection {
	public List getLayoutHolders();
	public int indexOf( CoLayoutHolder layoutHolder );
}
