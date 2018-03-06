package com.bluebrim.base.shared;
import java.rmi.*;
import java.util.*;
/**
 * Interface to be implemented by objects that can select a subset of
 * objects from a possible set
 * Creation date: (2001-09-06 09:21:27)
 * @author: Mikael Printz
 */
public interface CoSelectionIF extends CoObjectIF, Remote, Cloneable {
	
	public void addAvailableItem(Object item);


public void deselectItem(Object item);


public void deselectItems(Object[] items);


public List getAvailableItems();


public CoSelectionIF getClone();


	public List getSelectedItems();


	public void removeAvailableItem(Object item);


public void selectItem(Object item);


public void selectItems(Object[] items);
}