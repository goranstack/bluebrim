package com.bluebrim.gui.client;

import javax.swing.*;
import javax.swing.event.*;

/**
 	En subklass till CoValueModel vars värde är en lista. 
 	Hela protokollet för manipulering av listan ligger i den CoListModel som 
 	CoListValueModel använder som ListModel. <br>
	Uppdatering av den vy som visar upp listan sker på två sätt:
	<ul>
	<li> om listan får ett nytt värde - dvs om #setValue anropas med en ny lista - så
	sker uppdateringen via CoListBoxAdaptor, det objekt som kopplar ihop en CoListAspectAdaptor
	med en listvy.
	<li> om element läggs till eller tas bort så sker uppdatering direkt via vyns listmodel 
	eftersom vyn lyssnar efter denna typ av ändringar hos modellen .
	</ul>
 	Andra objekt kan registrerar sig som lyssnare på ListDataEvent eller CoSelectionEvent.
 	@see CoListAspectAdaptor
 */
public abstract class CoAbstractListValueModel extends CoValueModel implements CoListValueable {
	private CoAbstractListModel m_listModel;
	public static abstract class Mutable extends CoAbstractListValueModel implements CoListValueable.Mutable {
		public Mutable()
		{
		}
		public Mutable (String name ) {
			super(name);
		}
		public void addElements(Object elements[])
		{
			getMutableListModel().addElements(elements);
		}
		public void addElement(Object element)
		{
			getMutableListModel().addElement(element);
		}
		public void removeElement(Object element)
		{
			getMutableListModel().removeElement(element);
		}
		public void removeElements(Object elements[])
		{
			getMutableListModel().removeElements(elements);
		}
		public final CoAbstractListModel.Mutable getMutableListModel()
		{
			return (CoAbstractListModel.Mutable)this.getListModel();
		}
		public final void setMutableListModel(CoAbstractListModel.Mutable model)
		{
			this.setListModel(model);
		}
		public CoAbstractListModel getDefaultListModel()
		{
			return createMutableListModel();
		}
		protected abstract CoAbstractListModel.Mutable createMutableListModel();
	};
public CoAbstractListValueModel()
{
}
public CoAbstractListValueModel (String name ) {
	super(name);
}
protected void _setListModel(CoAbstractListModel model)
{
	CoAbstractListModel tOldModel = listModel();
	if (tOldModel != null)
	{
		removeValueListener(tOldModel);
	}
	m_listModel = model;
	if (model != null)
	{
		addValueListener(model);
	}
}
/**
 * @param l CoSelectionListener
 */
public void addSelectionListener ( CoSelectionListener l)
{
	listenerList.add(CoSelectionListener.class, l);
}
public boolean contains(Object element) {
 return (m_listModel != null) ? m_listModel.contains(element) : false;
}
public void elementHasChanged(Object source,Object element)
{
	getListModel().elementHasChanged(source,element);
}
/**
 * getSize method comment.
 */
public abstract CoAbstractListModel getDefaultListModel();
public Object getElementAt(int index) {
	return getListModel().getElementAt(index);
}
/**
 * getSize method comment.
 */
public CoAbstractListModel getListModel() {
	if (m_listModel == null)
		setListModel(getDefaultListModel());
	return m_listModel;
}
public int getSize()
{
	return getListModel().getSize();
}
/**
 */
public int indexOf(Object element) {
 return getListModel().indexOf(element);
}
public boolean isHandlingTransactions() {
	return false;
}
public boolean isMutable() 
{
	return false;
}
public void listHasChanged(Object source) 
{
	getListModel().listHasChanged(source);
}
/**
 * getSize method comment.
 */
protected CoAbstractListModel listModel() {
	return m_listModel;
}
private void propagateSelectionChanged(CoSelectionEvent e)
{

	Object tListeners[] 		= listenerList.getListenerList();
	Class tListenerClass		= CoSelectionListener.class;
	CoSelectionEvent tEvent 	= null;
	for (int k = tListeners.length - 2; k >= 0; k -= 2)
	{
	   if (tListeners[k] == tListenerClass)
	   {
				if (tEvent == null)
					tEvent = new CoSelectionEvent(this, e.getListSelectionModel());
				((CoSelectionListener)tListeners[k + 1]).selectionChange(tEvent);
	   }
	}
}
private void propagateValueChanged(ListSelectionEvent e)
{

	Object tListeners[] 		= listenerList.getListenerList();
	Class tListenerClass		= CoSelectionListener.class;
	CoSelectionEvent tEvent 	= null;
	for (int k = tListeners.length - 2; k >= 0; k -= 2)
	{
	   if (tListeners[k] == tListenerClass)
	   {
				if (tEvent == null)
					tEvent = new CoSelectionEvent(this, (ListSelectionModel )e.getSource());
				((CoSelectionListener)tListeners[k + 1]).selectionChange(tEvent);
	   }
	}
}
/**
 * @param l CoSelectionListener
 */
public void removeSelectionListener (CoSelectionListener l)
{
	listenerList.add(CoSelectionListener.class, l);
}
/**
 * getSize method comment.
 */
public void setListModel(CoAbstractListModel model) {
	if (model != m_listModel)
	{
		_setListModel(model != null ? model : getDefaultListModel());
	}
}
/**
 */
public void setModelFor(JList list) {
	list.setModel(getListModel());
}
/**
 * setValue method comment.
 */
public abstract void setValue(Object newValue);
public void valueChanged(ListSelectionEvent e) 
{
	propagateValueChanged(e);
}
}
