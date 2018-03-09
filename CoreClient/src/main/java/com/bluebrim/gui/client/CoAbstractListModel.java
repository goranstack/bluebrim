package com.bluebrim.gui.client;

import java.util.Comparator;

import javax.swing.ListModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Hjälpklass i CoListValueModel och CoListAspectAdaptor som implementerar 
 * ListModel och fungerar som modell för listvyn. Listan hämtas från
 * den instans av CoListValueModel och CoListAspectAdaptor som den hjälper. <br>
 * Kopplingen mellan CoListModel och listvyn sköts av CoListBoxAdaptor.
 * @see CoListBoxAdaptor 
 */
public abstract class CoAbstractListModel implements CoValueListener, ListModel {

	protected 	EventListenerList 	m_listenerList 	= new EventListenerList();

	public static abstract class Mutable extends CoAbstractListModel {
		private CoAbstractListModel m_listModel;

		public Mutable(CoAbstractListModel model)
		{
			m_listModel = model;
		}
		protected final CoAbstractListModel getListModel()
		{
			return m_listModel;
		}
		public final int indexOf (Object element)
		{
			return m_listModel.indexOf(element);
		}
		public final void intervalAdded (Object source,int firstIndex, int lastIndex)
		{
			fireIntervalAdded(source, firstIndex, lastIndex);
		}	
		public final void intervalRemoved (Object source,int firstIndex, int lastIndex)
		{
			fireIntervalRemoved(source, firstIndex, lastIndex);
		}	
		private void fireIntervalRemoved(Object object, int i, int j)
		{
		  Object listeners[] 				= m_listenerList.getListenerList();
		  ListDataEvent listDataEvent 	= null;
		  Class listenerClass				= ListDataListener.class;
		  for (int k = listeners.length - 2; k >= 0; k -= 2)
		  {
			if (listeners[k] == listenerClass)
			{
			  if (listDataEvent == null)
				listDataEvent = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, i, j);
			  ((ListDataListener)listeners[k + 1]).intervalRemoved(listDataEvent);
			}
		  }
		}
		private void fireIntervalAdded(Object object, int i, int j)
		{
		  Object listeners[] 				= m_listenerList.getListenerList();
		  ListDataEvent listDataEvent 	= null;
		  Class listenerClass				= ListDataListener.class;
		  for (int k = listeners.length - 2; k >= 0; k -= 2)
		  {
			if (listeners[k] == listenerClass)
			{
			  if (listDataEvent == null)
				listDataEvent = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, i, j);
			  ((ListDataListener)listeners[k + 1]).intervalAdded(listDataEvent);
			}
		  }
		}
		public void sort(Comparator c)
		{
			m_listModel.sort(c);
		}
		public final synchronized void addElement(Object object)
		{
			int i = this.getSize();
			doAddElement(object);
			intervalAdded(this, i, i);
		}
		public final synchronized void addElements(Object objects[])
		{
			if (objects == null)
				return;
			if (objects.length == 1)
				addElement(objects[0]);
			else
			{
				int count 	= objects.length;
				int i 		= this.getSize();
				for (int j=0; j<objects.length; j++)
				{
					doAddElement(objects[j]);
				}
				intervalAdded(this, i, i+count);
			}
		}
		public final synchronized boolean removeElement(Object element)
		{
			int tIndex = indexOf(element);
			if (tIndex != -1 && doRemoveElement(element))
			{
				intervalRemoved(this, tIndex, tIndex);
				return true;
			}
			else
				return false;
		}
		public final synchronized void removeElements(Object objects[])
		{
			if (objects.length == 1)
				removeElement(objects[0]);
			else
			{
				int indices[] 	= new int[objects.length];
				for (int i = objects.length - 1; i >= 0; i--)
				{
					Object tObject 	= objects[i];
					int index 		= indexOf(tObject);
					indices[i] 		= index;
					doRemoveElement(tObject);
				}
				int lastIndex = indices[indices.length - 1];
				int prevIndex = lastIndex;
				for (int k = indices.length - 1; k >= 0; k--)
				{
					int j = indices[k];
					if (j < prevIndex - 1 || k == 0)
					{
						intervalRemoved(this, j, lastIndex);
						lastIndex = j;
					}
					prevIndex = j;
				}
			}
		}
		/**
			The list model listents to changes of the value
			in the list valuemodel. If this method is called
			there's a new list that can be accessed via
			<code>anEvent.getNewValue()</code>
		*/
		public void valueChange( CoValueChangeEvent anEvent)
		{
			m_listModel.valueChange(anEvent);
			super.valueChange(anEvent);
		}	
		protected abstract boolean doRemoveElement(Object element);
		protected abstract void doAddElement(Object element);
	}

public CoAbstractListModel()
{
}
/**
 * @param l CoSelectionListener
 */
public void addListDataListener(ListDataListener listDataListener)
{
   m_listenerList.add(ListDataListener.class, listDataListener);
}
public boolean contains(Object object)
{
	return indexOf(object) >= 0;
}
public void elementHasChanged(Object source, Object element)
{
	int tIndex = indexOf(element);
	fireContentsChanged(source, tIndex, tIndex);
}
protected void fireContentsChanged(Object object, int i, int j)
{
	Object 			listeners[] 	= getListenerList().getListenerList();
	ListDataEvent 	listDataEvent 	= null;
	Class 			listenerClass 	= ListDataListener.class;
	for (int k = listeners.length - 2; k >= 0; k -= 2)
	{
		if (listeners[k] == listenerClass)
		{
			if (listDataEvent == null)
				listDataEvent = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, i, j);
			((ListDataListener) listeners[k + 1]).contentsChanged(listDataEvent);
		}
	}
}
protected final EventListenerList getListenerList()
{
	return m_listenerList;
}
public abstract int indexOf (Object element);
public void listHasChanged(Object source) {
	// In case the list has grown. /Markus 2000-01-11
	fireContentsChanged(source, 0, Integer.MAX_VALUE);

//	fireContentsChanged(source, 0, getSize());
}
/**
 * @param l CoSelectionListener
 */
public void removeListDataListener(ListDataListener listDataListener)
{
  m_listenerList.remove(ListDataListener.class, listDataListener);
}
public abstract void sort(Comparator c);
/**
	The list model listents to changes of the value
	in the list valuemodel. If this method is called
	there's a new list that can be accessed via
	<code>anEvent.getNewValue()</code>
*/
public void valueChange( CoValueChangeEvent anEvent)
{
	listHasChanged(anEvent.getSource());
}
}
