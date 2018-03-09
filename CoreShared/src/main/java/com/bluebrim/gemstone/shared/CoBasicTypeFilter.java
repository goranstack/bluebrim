package com.bluebrim.gemstone.shared;

import java.util.*;

import com.bluebrim.browser.shared.*;
import com.bluebrim.collection.shared.*;

/**
	Implementation of <code>CoTreeCatalogFilterIF</code> which only filters on the the type,
	i.e <code>applyOn</code> answers true iff the factory key of the filtered element is amongst the
	types defined in the filter. <code>CoBasicTypeFilter</code> is also implementing the cache mechanism 
	that is defined in the interface. 
	<br>
	This is implemented so that the value model responsible for 
	handling the tree first checks to see if there's a cached result from a previous filtering.
	If so, this is used , if not a filtering is performed and the result is put in the cache with
	the filtered element used as a key.
	<br>
	When changes are made to the tree structure the cache must be emptied for the 
	elements affected by the change.
 */
public class CoBasicTypeFilter implements CoTreeCatalogFilterIF {
	private Hashtable cachedElements = new Hashtable();
	private String types[];
	/**
	 * This method was created by a SmartGuide.
	 */
	public CoBasicTypeFilter() {
		this(new String[] {
		});
	}
	/**
	 * This method was created by a SmartGuide.
	 */
	public CoBasicTypeFilter(String types[]) {
		this.types = types;
	}
	/**
	  */
	public void addElements(List elements, CoTreeCatalogElementIF element) {
		cachedElements.put(elements, elements);
	}
	/**
	 */
	public boolean applyOn(CoTreeCatalogElementIF element) {
		String tType = element.getFactoryKey();
		for (int i = 0; i < types.length; i++) {
			if (types[i].equals(tType))
				return true;
		}
		return false;
	}
	/**
	  */
	public String getDescription() {
		return "";
	}
	/**
	  */
	public List getElements(CoTreeCatalogElementIF element) {
		return (List) cachedElements.get(element);
	}
	/**
	  */
	public void removeElements(CoTreeCatalogElementIF element) {
		cachedElements.remove(element);
	}
	/**
	  */
	public void resetElements() {
		cachedElements = new Hashtable();
	}
	/**
	  */
	public void resetElementsFrom(CoTreeCatalogElementIF element) {
		if (element == null)
			resetElements();
		else {
			List tElements = getElements(element);
			if (tElements != null) {
				removeElements(element);
				CoCollections.each(tElements, new CoCollections.EachDo() {
					public void doTo(Object treeElement) {
						resetElementsFrom((CoTreeCatalogElementIF) treeElement);
					}
				});
			}
		}
	}
}
