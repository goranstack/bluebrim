package com.bluebrim.layout.shared;

import java.util.*;

/**
 * A collection of layouts that can serve as templates.
 * <br>
 * This is an attempt to create an interface that can replace 
 * <code>CoPageItemPrototypeCollectionIF</code> when refered from outside the layout domain. The
 * reason for doing this is to minimize what is exposed in the core part of the
 * layout domain. 
 * <br>
 * The working strategi is to replace all external references to code>CoPageItemPrototypeCollectionIF</code>
 * with this interface and add requested methods until everyone is happy.  
 * <br>
 * 
 * @author Göran Stäck 2002-04-22
 */

public interface CoLayoutTemplateCollection {
	
	/**
	 * This method exist only to please xml-import code. Feels bad.
	 */
	public void add(CoLayoutTemplate layoutTemplate);
	
	public List getLayoutTemplates();
	
	public CoLayoutTemplate createAndAddLayoutTemplate( String name, String description, CoLayout layout );

	public void renameLayoutTemplate( CoLayoutTemplate template, String newName );

	public void removeLayoutTemplates(List templates);
}
