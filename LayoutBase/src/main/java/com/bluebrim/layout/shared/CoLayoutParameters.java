package com.bluebrim.layout.shared;

import com.bluebrim.page.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.text.shared.*;

/**
 * A <code>CoLayoutParameters</code> is a set of properties whose values determine 
 * the characteristics and behavior of <code>CoLayout</code>'s. Since layouts contains
 * text a <code>CoTextParameters</code> is a part of a <code>CoLayoutParameters</code>.
 * <br>
 * This is an attempt to create an interface that can replace 
 * <code>CoPageItemPreferencesIF</code> when refered from outside the layout domain. The
 * reason for doing this is to minimize what is exposed in the core part of the
 * layout domain. 
 * <br>
 * The working strategi is to replace all external references to code>CoPageItemPreferencesIF</code>
 * with this interface and add requested methods.  
 * <br>
 * 
 * @author: Göran Stäck 2002-04-10
 */


public interface CoLayoutParameters extends CoTextParameters {

	/**
	 * <code>CoLayoutParameters</code> has the ability to form chains since
	 * each <code>CoLayoutParameters</code> can have a super <code>CoLayoutParameters</code>.
	 * A resolving mechanism compiles parameters from the chain in a way that enables
	 * overriding and concatenation of parameters in the chain. <br>
	 * You can use this method when creating a <code>CoLayoutParameters</code> owned by
	 * an object that is owned by an object that also owns a <code>CoLayoutParameters</code>.
	 * By using this method you create a <code>CoLayoutParameters</code> that inherits all
	 * parameters from the super <code>CoLayoutParameters</code>.
	 * @return CoLayoutParameters has the called <code>CoLayoutParameters</code> as super.
	 */
	public CoLayoutParameters createSubParameters();
			
	public CoPageSizeCollectionIF getPageSizeCollection();
	
	/**
	 * Owners of <code>CoLayoutParameters</code>'s must call this method before
	 * deleting a reference to a <code>CoLayoutParameters</code>. 
	 */
	public void dispose();
	
	public CoColorCollectionIF getColorCollection();
	
	public CoDesktopLayout getDesktop();
	
}
