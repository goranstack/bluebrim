package com.bluebrim.base.server;
import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import java.util.*;
/**
 * Abstract super class of selections (note, this is a server only class)
 * An abstract selection can be created in two ways:
 * - either by supplying two collections which it should manage (use constructor that takes two lists)
 * - or by using default constructor, which created two empty lists
 * Creation date: (2001-10-01 08:34:03)
 * @author: Mikael Printz
 */
public abstract class CoAbstractSelection extends CoObject implements CoSelectionIF {
	public static final String FACTORY_KEY = "abstract_selection";
	private List m_available;
	private List m_selected;
public CoSelectionIF getClone() {
	try {
		CoAbstractSelection sel = (CoAbstractSelection) super.clone();
		sel.m_available = new ArrayList();
		sel.m_available.addAll(m_available);
		sel.m_selected = new ArrayList();
		sel.m_selected.addAll(m_selected);
		return sel;
	} catch (CloneNotSupportedException cnse) {
		System.err.println("Could not clone selection");
		cnse.printStackTrace();
	}
	return null;
}	

public CoAbstractSelection() {
	super();
	m_available = new ArrayList();
	m_selected  = new ArrayList();
}


public CoAbstractSelection(List available, List selected) {
	m_available = available;
	m_selected  = selected;
}


public void addAvailableItem(Object item) {
	CoAssertion.assertTrue(getAddAssertCondition(item), getAddAssertMessage());
	m_available.add(item);
}


public void deselectItem(Object item) {
	m_selected.remove(item);
}


public void deselectItems(Object[] items) {
	for(int idx = 0; idx < items.length; idx++) {
		deselectItem(items[idx]);
	}
}


protected abstract boolean getAddAssertCondition(Object item);


protected abstract String getAddAssertMessage();


public List getAvailableItems() {
	return m_available;
}


public String getFactoryKey() {
	return FACTORY_KEY;
}


public List getSelectedItems() {
	return m_selected;
}


public void removeAvailableItem(Object item) {
	m_available.remove(item);
}


public void selectItem(Object item) {
	m_selected.add(item);
}


public void selectItems(Object[] items) {
	for(int idx = 0; idx < items.length; idx++) {
		selectItem(items[idx]);
	}
}
}