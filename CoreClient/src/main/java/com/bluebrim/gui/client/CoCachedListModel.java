package com.bluebrim.gui.client;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.bluebrim.base.shared.CoListElementViewIF;

/**
 * List model used by an instance of <code>CoAbstractListAspectAdaptor</code> 
 * when the collection implements
 * the <code>List</code> interface.
 */
public  abstract class CoCachedListModel extends CoAbstractCachedListModel  {

		
	public abstract static class Default extends CoCachedListModel {
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
	
	
	public static abstract class Mutable extends CoAbstractListModel.Mutable implements CoCachedListModelIF{

		public Mutable(CoCachedListModel model)
		{
			super(model);
		}
		private CoCachedListModel listModel()
		{
			return (CoCachedListModel)getListModel();
		}
		public Object getElementAt(int index) {
			return listModel().getElementAt(index);
		}
		public int getSize() {
			return listModel().getSize();
		}
		public CoListElementViewIF getElementViewAt(int index)
		{
			return listModel().getElementViewAt(index);
		}
		public void listHasChanged (Object source)
		{
			listModel().listHasChanged(source);
			super.listHasChanged(source);
		}
		public void elementHasChanged(Object source, Object element)
		{
			listModel().elementHasChanged(source, element);
		}
	};


public CoCachedListModel()
{
}
protected CoListElementViewIF[] createCache()
{
	List list 	= getList();
	return list != null ? new CoListElementViewIF[list.size()] : null;
}
protected abstract List getList();
/**
 * indexOf method comment.
 */
public void sort(Comparator c) {
	List tList = getList();
	if (tList != null)
	{
		Collections.sort(tList, c);
		rebuildCache();
	}
}
}