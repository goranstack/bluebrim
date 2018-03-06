package com.bluebrim.page.shared;
import java.util.*;

import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Implemented by objects that represents pages in a page based media 
 * such as newspaper. Pages makes it possible to work with pages that is independent from the
 * physical media where the pages is reproduced in the final product. Pages has an object that
 * contains the graphic on the page. <br>
 * Pages can be part of a page stack where 
 * one page in the stack is shared among several stacks i.e. pagination and one page contains the body of the page.
 * A page can be small as a business card or large as a wall poster.
 *
 * Creation date: (2001-11-21 12:07:45)
 * @author: Göran Stäck 
 */
public interface CoPage extends CoLayoutHolder, CoXmlEnabledIF {

	public void addPageChangeListener(CoPageChangeListener listener);

	public void removePageChangeListener(CoPageChangeListener listener);
	
	/**
	 * Put values for the text variables in the specified Map. 
	 */	
	public void bindTextVariableValues( Map values );

	public boolean satisfyPagePlaceRequest(CoPage page);

	/**
	 * Returns the name of the page. In most cases a page number
	 */	
	public String getName();
	
	public CoPageContext getPageContext();
	
	public CoLayoutParameters getLayoutParameters();
	
	public CoDesktopLayout getDesktop();
	
	/**
	 * Answer true if the page is the left page on a spreed
	 */
	public boolean isLeftSide();


}