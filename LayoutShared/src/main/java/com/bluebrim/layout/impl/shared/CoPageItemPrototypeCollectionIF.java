package com.bluebrim.layout.impl.shared;

import java.rmi.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;


/**
 * Protocol for a collection of page item prototypes.
 * 
 * @author: Dennis Malmström
 */

public interface
	CoPageItemPrototypeCollectionIF
extends
	CoPageItemHolderCollectionIF,
	CoLayoutTemplateCollection,
	CoFactoryElementIF,
	Remote
{
	String FACTORY_KEY = "CoPageItemPrototypeCollectionIF.FACTORY_KEY";

	void addPageItemPrototype( CoPageItemPrototypeIF p );

	CoPageItemPrototypeIF getPageItemPrototype( String name );

	List getPageItemPrototypes();
	
	void removePageItemPrototypes( List ps ); // Might contain objects that are not CoPageItemPrototypeIF's, they should be ignored.
	
	CoPageItemPrototypeIF addPageItemPrototype(String name, String description, CoShapePageItemIF pageItem);
	
	void removePageItemPrototype( CoPageItemPrototypeIF p );
	
	// create copy of p, insert it after p if p is member of collection
	CoPageItemPrototypeIF copyPageItemPrototype( CoPageItemPrototypeIF p, String nameOfCopy );
	
	void renamePageItemPrototype( CoPageItemPrototypeIF prototype, String name );
}