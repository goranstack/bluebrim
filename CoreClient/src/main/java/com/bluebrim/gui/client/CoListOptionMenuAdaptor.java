package com.bluebrim.gui.client;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 * A copy of  for use with CoOptionMenu.
 * Creation date: 2001-09-10
 * @author: Dennis Malmström
 */
 
public class CoListOptionMenuAdaptor extends CoOptionMenuAdaptor
{
	private CoListValueable m_itemSource;
	private Model 			m_model;
	
	private class Model implements ComboBoxModel {
		private CoAbstractListModel m_listModel;
		private Object				m_selectedItem;
		public Model(CoAbstractListModel listModel)
		{
			m_listModel = listModel;
		}
		public Object getSelectedItem()
		{
			return m_selectedItem;
		}
		public void setSelectedItem(Object item)
		{
			if (item != m_selectedItem)
			{
				m_selectedItem = item;
				m_listModel.fireContentsChanged(m_listModel, -1,-1);
			}
		}
		public int getSize()
		{
			return m_listModel.getSize();
		}
		public void addListDataListener(ListDataListener l)
		{
			m_listModel.addListDataListener(l);
		}
		public void removeListDataListener(ListDataListener l)
		{
			m_listModel.removeListDataListener(l);
		}
		public Object getElementAt(int index)
		{
			return m_listModel.getElementAt(index);
		}
		public void listHasChanged()
		{
			m_listModel.fireContentsChanged(m_listModel, -1,-1);
		}
	}

private void setItemSource(CoListValueable listValueable)
{
	m_itemSource	= listValueable;
	m_model 		= new Model(m_itemSource.getListModel());
	getOptionMenu().setModel(m_model);
	m_itemSource.addValueListener(this);
}
public void valueChange(CoValueChangeEvent e) {
	if (e.getSource() == m_itemSource )
	{
		m_model.listHasChanged();
		updateOptionMenu();
	}
	else
		super.valueChange(e);
}

public CoListOptionMenuAdaptor( CoListValueable lv, CoValueModel vm, com.bluebrim.swing.client.CoOptionMenu m )
{
	super( vm, m );
	setItemSource( lv );
}
}