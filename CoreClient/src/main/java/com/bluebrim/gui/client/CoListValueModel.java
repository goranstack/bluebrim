package com.bluebrim.gui.client;

import java.util.*;

/**
 */
public class CoListValueModel extends CoAbstractListValueModel { 
	private List m_list;

	public static class Mutable extends CoAbstractListValueModel.Mutable {
		private List m_list;
		
		public Mutable (String name ) {
			this(name, new Vector());
		}		
		public Mutable (String name, List list ) {
			super(name);
			setList(list);
		}		
		public Object getValue() {
			return m_list;
		}
		public void setValue(Object aValue) {
			setList((List )aValue);
			valueHasChanged();
		}
		private void setList(List aList) {
			m_list = aList;
		}
		public CoAbstractListModel.Mutable createMutableListModel() {
			return new CoCollectionListModel.Mutable(this) {
				protected void doAddElement(Object element)
				{
					getList().add(element);
				}
				protected boolean doRemoveElement(Object element)
				{
					getList().remove(element);
					return true;
				}
			};
		}	
	}
public CoListValueModel () {
	this("");
}
public CoListValueModel (String name ) {
	this(name, new ArrayList());
}
public CoListValueModel (String name, List list ) {
	super(name);
	m_list = list;
}
public CoAbstractListModel getDefaultListModel() {
	return new CoCollectionListModel.Default(this);
}
/**
 * getValue method comment.
 */
public Object getValue() {
	return m_list;
}
private void setList(List aList) {
	m_list = aList;
}
/**
 * setValue method comment.
 */
public void setValue(Object newValue) {
	setList((List )newValue);
	valueHasChanged();
}
}
