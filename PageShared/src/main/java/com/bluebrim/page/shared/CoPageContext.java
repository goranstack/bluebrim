package com.bluebrim.page.shared;

import com.bluebrim.layout.shared.CoDesktopLayout;
import com.bluebrim.layout.shared.CoLayoutParameters;

/**
 * Implemted by objects that arrange pages in structures for example
 * a sequence of pages where even pages are left pages and odd pages are right pages.
 * A <code>CoPageContext</code> also provide a desktop and the layout parameters 
 * that is nessecary for the layout of the page.
 * 
 * @author Göran Stäck 2002-09-17
 *
 */
public interface CoPageContext {

	/**
	 * Returns the name of the structure for example the name of a 
	 * publication format for a newspaper. This method is only used
	 * in <code>CoInsertionRequestPageLocation.getLocationString</code>
	 * and would be nice to get rid of it.
	 */	
	public String getName();
	
	/**
	 * Layout parameters has an override mechanism that makes it possible
	 * to express parameters that is local for certain page context.
	 */
	public CoLayoutParameters getLayoutParameters();

	/**
	 * It's not unlikely that pages with the same context share the same desktop.
	 */
	public CoDesktopLayout getDesktop();
	
	/**
	 * Answer true ef the specified page is the page to left on a spread. <br>
	 * The object model concerning left and right pages and spreads
	 * is not complete. The concept of spreads is not working toghether
	 * with the concept of left and right pages in the present implementation
	 */
	public boolean isLeftSide(CoPage page);
	
	
	public String getContextualNameFor(CoPage page);

}
