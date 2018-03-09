package com.bluebrim.gui.client;

import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.bluebrim.base.shared.CoObjectIF;
/**

 	Abstract superclass for valuemodels whose values is a collection of objects that should be displayed
 	and optionally edited in a table view. The collection is held by a list valuemodel that's is sent as an
 	argument in the constructor of the table valuemodel.
 	<br>
 	The table valuemodel is listening for changes in the list valuemodel and updates its table view 
 	(via the component adaptor) when the contents of the list is changed. This means that there are
 	two ways to update the table when the list changes:
 	<ul>
 	<li>if the object owning the collection is a persistent businessobject on the server the ui model should
 	listen for changes to this serverobject and call <code>listHasChanged</code> for the list 
 	valuemodel (or just valueHasChanged for the ui model) when it gets notified that the serverobject has changed.
 	<li>otherwise the actions used to change the contents of the list should end with a call to <code>listHasChanged</code>.
 	</ul>
 	<pre><code>
 
 	Variant 1:
 		protected void createListeners()
 		{
 			super.createListeners();
 			...
 			(new com.bluebrim.gemstone.client.CoServerObjectListener(this) {
				protected Object getServerObjectFrom(CoObjectIF domain)
				{
					return domain;
				}
				public void changedServerObject(com.bluebrim.gemstone.shared.CoChangedObjectEvent e)
				{
					if (e.getChangedObject() == getServerObjectFrom(getDomain()))
					{
						valueHasChanged();	// or theListAspectAdaptor.listHasChanged(this);		
					}

				}
			}).initialize();

			
 			...
		}

	Variant 2:
		...
		...
		
		iAddAction = new AbstractAction(CoStringResources.getName(CoConstants.ADD_ITEM)) {
			public void actionPerformed(ActionEvent e) {
				((com.bluebrim.properties.shared.CoEnumerationIF) getDomain()).createValue(CoStringResources.getName(CoConstants.UNTITLED));
				iValueListAdaptor.listHasChanged(CoEnumerationEditor.this);
			}
		};
		...
		...
		
 	</code></pre>
 	The following method must be implemented in the subclass:
 	<ul>
 	<li><code>getValueForAt</code> which answers with the object displayed in the cell corresponding to the row an column given in
 	the arguments.
 	</ul>
 	The following methods has a default implementation in <code>CoTableAspectAdaptor</code> which makes it not editable as default.
 	They need to be reimplemented in a subclass if the table should be possible to edit.
 	<ul>
 	<li><code>isCellEditable</code>, answers false as default
 	<li><code>setValueForAt</code>, which should be implemented to update the persistent object with the new value.
 	</ul>
 	If you wamt to edit the values of individual cells and make those changes persistent in GemStone/J you should subclass
 	<code>CoGsTableAspectAdaptor</code> instead. This class wraps the call to <code>setValueForAt</code> in a transaction.

 	000118 Lasse Implemented <code>valueHasChanged</code> as NOP because everything i taken care of by the
 	list value model. This reduces the roundtrips to the business object by 1.
 	
 */
public abstract class CoTableAspectAdaptor extends CoAspectAdaptor implements TableModel, ListDataListener {
	private CoListValueable				m_listValueable;
	private String 						m_columnNames[];
	private EventListenerList 			m_listenerList = new EventListenerList();
public CoTableAspectAdaptor(CoValueable context, String name, CoListValueable listValueable, String columnNames[]) {
	this(context, name, listValueable, columnNames, false);
}
public CoTableAspectAdaptor(CoValueable context, String name, CoListValueable listValueable, String columnNames[], boolean subjectFiresChange) {
	super(context, name, subjectFiresChange);
	setListValueable(listValueable);
	setColumnNames(columnNames);
}
/**
	This method is meant to be overriden in subclasses that needs to wrap to actual
	set-method, i e by calling setValueForAt inside a transaction.
 */
protected void _setValueForAt(CoObjectIF row,Object aValue,int rowIndex, int columnIndex)
{
	setValueForAt(row, aValue, rowIndex, columnIndex);
}
/**
 * add method comment. added by GorFo
 */
public void addColumnNames(String addedNames[]) {
	String [] newNames = new String [m_columnNames.length + addedNames.length];
	for (int i=0; i<m_columnNames.length; i++)
		newNames[i] = m_columnNames[i];
	for (int i=0; i<addedNames.length; i++)
		newNames[i+m_columnNames.length] = addedNames[i];
		
	m_columnNames = newNames;
	fireTableModelEvent(new TableModelEvent(this,TableModelEvent.HEADER_ROW));

}
/**
 * addTableModelListener method comment.
 */
public void addTableModelListener(TableModelListener l) {
	listenerList.add(TableModelListener.class, l);
}
public void contentsChanged(ListDataEvent event) {
	fireTableModelEvent(new TableModelEvent(this,
		event.getIndex0(), event.getIndex1(), TableModelEvent.UPDATE));
	
//	Clearly wrong, eh? /Markus 2000-01-11
//	fireTableModelEvent(new TableModelEvent(this,TableModelEvent.HEADER_ROW));
}
protected void fireTableModelEvent(TableModelEvent e)
{
	// Guaranteed to return a non-null array
	Object[] listeners = listenerList.getListenerList();

	for (int i = listeners.length - 2; i >= 0; i -= 2)
	{
		if (listeners[i] == TableModelListener.class)
		{
			((TableModelListener) listeners[i + 1]).tableChanged(e);
		}
	}
}
protected Object get(CoObjectIF subject)
{
	return getListValueable().getValue();
}
/**
 	Implementeras i subklassen för att svara med klassen för det
 	objekt som ligger i kolumn med index 'columnIndex'.
 */
public Class getColumnClass(int columnIndex)
{
	return getValueAt(0, columnIndex).getClass();
}
/**
 * getColumnCount method comment.
 */
public int getColumnCount() {
	return m_columnNames.length;
}
/**
 * getColumnName method comment.
 */
public String getColumnName(int columnIndex) {
	return m_columnNames[columnIndex];
}
/**
 * get method comment.
 */
public Object getElementAt(int index) {
	return getListValueable().getElementAt(index);
}
/**
 */
protected final CoListValueable getListValueable()
{
	return m_listValueable;
}
/**
 * getRowCount method comment.
 */
public int getRowCount() {
	return getListValueable().getSize();
}
/**
 * getValueAt method comment.
 */
public Object getValueAt(int rowIndex, int columnIndex) {
	return getValueForAt((CoObjectIF )getElementAt(rowIndex), rowIndex,columnIndex);
}
/**
 	Implementeras i subklassen för att svara med objektet i 'rowSubject'
 	som skall visas i kolumnen med index 'columnIndex'.
 */
protected abstract Object getValueForAt(CoObjectIF rowSubject,int rowIndex, int columnIndex);
protected void handleUpdate(CoValueChangeEvent e) 
{
	valueHasChanged();
}
public void intervalAdded(ListDataEvent event) {
	fireTableModelEvent(new TableModelEvent(this,
		event.getIndex0(), event.getIndex1(), TableModelEvent.INSERT));
	
//	Clearly wrong, eh? /Markus 2000-01-11
//	fireTableModelEvent(new TableModelEvent(this,TableModelEvent.UPDATE));
}
public void intervalRemoved(ListDataEvent event) {
	fireTableModelEvent(new TableModelEvent(this,
		event.getIndex0(), event.getIndex1(), TableModelEvent.DELETE));
	
//	Clearly wrong, eh? /Markus 2000-01-11
//	fireTableModelEvent(new TableModelEvent(this,TableModelEvent.UPDATE));
}
/**
 	Denna metod svarar med false så att användaren 
 	inte behöver göra något om det inte går att editera data.
 */
public boolean isCellEditable(int rowIndex, int columnIndex) {
	return false;
}
/**
	New method created by KarGy
	Clear all Column names (except the name and type) but no changes are fired.
	Must be followed by addColumnNames()
**/
public void removeAllColumnNames() {
	String [] newNames = new String[0];
	for (int i=0; i<newNames.length; i++)
		newNames[i] = m_columnNames[i];
		
	m_columnNames = newNames;
//	fireTableModelEvent(new TableModelEvent(this,TableModelEvent.HEADER_ROW));

}
/**
 * removeTableModelListener method comment.
 */
public void removeTableModelListener(TableModelListener l) {
	listenerList.remove(TableModelListener.class, l);
}
/**
	Not used as the table is displaying date held by
	another value model.
 */
public void set(CoObjectIF subject, Object value)
{
}
/**
 * set method comment.
 */
private void setColumnNames(String columnNames[]) {
	if (columnNames == null)
		throw new IllegalArgumentException("columnNames == null i CoTableAspectAdaptor.setColumnNames");
	m_columnNames = columnNames;
}
/**
 * set method comment.
 */
private void setListValueable(CoListValueable listValueable) {
	if (listValueable == null)
		throw new IllegalArgumentException("listValueable == null i CoTableAspectAdaptor.setListValueable");
	m_listValueable = listValueable;
	m_listValueable.getListModel().addListDataListener(this);
}
/**
 * setValueAt method comment.
 */
public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	CoObjectIF 	tRowObject	= (CoObjectIF )getElementAt(rowIndex);
	Object		tOldValue	= getValueForAt(tRowObject, rowIndex, columnIndex);
	if (hasValueChanged(tOldValue, aValue))
		_setValueForAt(tRowObject, aValue, rowIndex,columnIndex);
}
/**
 	Denna metod är implementerad tom så att användaren 
 	inte behöver göra något om det inte går att editera data.
 */
protected void setValueForAt(CoObjectIF row,Object aValue,int rowIndex, int columnIndex)
{
}
public void tableHasChanged() {
	// WARNING: This event probably does NOT what the author intended. /Markus 990412
	//fireTableModelEvent(new TableModelEvent(this,0, getRowCount(),TableModelEvent.UPDATE));

	// This is slightly drastic ... /Markus 990412
	//fireTableModelEvent(new TableModelEvent(this,TableModelEvent.HEADER_ROW));

	// Comments in TableModelEvent indicate that this may work ... /Markus 2000-01-11
	fireTableModelEvent(new TableModelEvent(this));
}
public void valueHasChanged()
{
	// NOP This is taken care of by the list value model;
}
}
