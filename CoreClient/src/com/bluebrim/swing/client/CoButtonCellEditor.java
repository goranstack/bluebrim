package com.bluebrim.swing.client;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
/**
  */
public class CoButtonCellEditor extends DefaultCellEditor
{
	Object currentValue = null;
public CoButtonCellEditor(JButton button)
{
	this(button, 2);
}
public CoButtonCellEditor(JButton button, int clickCountsToStart)
{
	super(new JCheckBox());
	editorComponent = button;
	setClickCountToStart(clickCountsToStart);

	//Must do this so that editing stops when appropriate.
	button.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			fireEditingStopped();
		}
	});
}
/**
 * Copied from super class as an implementation using
 * super doesn't seem to work here :-(
 */
protected void fireEditingStopped()
{
	Object[] listeners = listenerList.getListenerList();
	for (int i = listeners.length - 2; i >= 0; i -= 2)
	{
		if (listeners[i] == CellEditorListener.class)
		{
			if (changeEvent == null)
				changeEvent = new ChangeEvent(this);
			((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
		}
	}
}
	public Object getCellEditorValue()
	{
		return currentValue;
	}
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		currentValue = value;
		return editorComponent;
	}
}
