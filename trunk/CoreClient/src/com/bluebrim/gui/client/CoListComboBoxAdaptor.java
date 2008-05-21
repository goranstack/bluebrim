package com.bluebrim.gui.client;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;

/**
 * A <code>CoComboBoxAdaptor</code> that sets the model for the combo box to 
 * an implementation of <code>ComboBoxModel</code> that dispatches all listmodel
 * request to the listmodel of a delivered <code>CoListValueable</code>.
 * Creation date: (2000-01-07 22:28:05)
 * @author: Lasse Svadängs
 */
public class CoListComboBoxAdaptor extends CoComboBoxAdaptor {
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
public CoListComboBoxAdaptor(CoListValueable listValueable, CoValueModel aValueModel, JComboBox aComboBox) {
	super(aValueModel, aComboBox);
	setItemSource(listValueable);
}
private void setItemSource(CoListValueable listValueable)
{
	m_itemSource	= listValueable;
	m_model 		= new Model(m_itemSource.getListModel());
	getComboBox().setModel(m_model);
	m_itemSource.addValueListener(this);
}
public void valueChange(CoValueChangeEvent e) {
	if (e.getSource() == m_itemSource )
	{
		m_model.listHasChanged();
		updateComboBox();
	}
	else
		super.valueChange(e);
}
}
