package com.bluebrim.layout.shared;
/**
 * A <code>CoDesktopLayout</code> is a layout that is the background
 * in a layout editor.
 * This is an attempt to create an interface that can replace 
 * <code>CoDesktopLayoutAreaIF</code> when refered from outside the layout domain. The
 * reason for doing this is to minimize what is exposed in the core part of the
 * layout domain. 
 * <br>
 * The working strategi is to replace all external references to code>CoDesktopLayoutAreaIF</code>
 * with this interface and add requested methods until everyone is happy.  
 * 
 * @author: Göran Stäck 2002-04-22
 */

public interface CoDesktopLayout extends CoLayout {

	public static String DESKTOP_LAYOUT = "DESKTOP_LAYOUT";

	public CoLayoutParameters getLayoutParameters();
	
}
