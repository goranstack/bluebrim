package com.bluebrim.layout.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.xml.shared.*;


/**
 * A <code>CoLayoutPrototype</code> is a wrapper that attach a name and 
 * a description to a layout that can serve as template when creating new layouts.
 * <br>
 * This is an attempt to create an interface that can replace 
 * <code>CoPageItemPrototypeIF</code> when refered from outside the layout domain. The
 * reason for doing this is to minimize what is exposed in the core part of the
 * layout domain. 
 * <br>
 * The working strategi is to replace all external references to code>CoPageItemPrototypeIF</code>
 * with this interface and add requested methods until everyone is happy.  
 * <br>
 * 
 * @author: Göran Stäck 2002-04-12
 */

public interface CoLayoutTemplate extends CoRenameable, CoXmlExportEnabledIF {
	/**
	 * Returns a copy of the layout that the <code>CoLayoutTemplate</code> owns.
	 * @return CoLayout
	 */
	public CoLayout getLayoutCopy();
}
