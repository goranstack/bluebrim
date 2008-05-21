package com.bluebrim.gui.client;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
	List model used by an instance of <code>CoAbstractListAspectAdaptor</code> when the collection implements
	the <code>List</code> interface.
 */
public  abstract class CoCollectionListModel extends CoAbstractListModel {

	public static class Default extends CoCollectionListModel {
		private CoValueable m_valueable;

		public Default(CoValueable valueable)
		{
			m_valueable = valueable;
		}
		protected List getList()
		{
			return (List )m_valueable.getValue();
		}
	};
	
	
	public static abstract class Mutable extends CoAbstractListModel.Mutable {

		public Mutable(CoValueable valueable)
		{
			this(new CoCollectionListModel.Default(valueable));
		}
		public Mutable(CoCollectionListModel model)
		{
			super(model);
		}
		private CoCollectionListModel _getListModel()
		{
			return (CoCollectionListModel)getListModel();
		}
		protected List getList()
		{
			return _getListModel().getList();
		}
		public Object getElementAt(int index) {
			return _getListModel().getElementAt(index);
		}
		public int getSize() {
			return _getListModel().getSize();
		}
		public void listHasChanged(Object source)
		{
			 _getListModel().listHasChanged(source);
			 super.listHasChanged(source);
		}
	};
public CoCollectionListModel()
{
}
public Object getElementAt(int index) {
	List tList = getList();
	return tList != null ? tList.get(index) : null;
}
protected abstract List getList();
/**
 * getSize method comment.
 */
public int getSize() {
	List tList = getList();
	return tList != null ? tList.size() : 0;
}
/**
 * indexOf method comment.
 */
public int indexOf(Object object) {
	List tList = getList();
	return tList != null ? tList.indexOf(object) : -1;
}
/**
 * indexOf method comment.
 */
public void sort(Comparator c) {
	List tList = getList();
	if (tList != null)
		Collections.sort(tList, c);
}
}
