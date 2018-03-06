package com.bluebrim.gui.client;

import java.util.Comparator;

import com.bluebrim.base.shared.CoObjectIF;
/**
 */
public class CoSingleElementListModel extends CoAbstractListModel {
	private CoObjectIF m_element;
public CoSingleElementListModel(CoObjectIF element)
{
	super();
	m_element = element;
}
public boolean contains(Object object)
{
	return m_element == object;
}
public Object getElementAt(int index)
{
	return m_element;
}
public int getSize()
{
	return 1;
}
/**
 */
public int indexOf(Object object) {
	return (object == m_element)? 0 : -1;
}
public void sort(Comparator c) {
}
}
