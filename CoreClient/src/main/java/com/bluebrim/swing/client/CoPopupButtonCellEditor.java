package com.bluebrim.swing.client;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
/**
  */
public class CoPopupButtonCellEditor extends DefaultCellEditor
{
		Object currentValue	= null;
		JLabel emptyLabel 	= new JLabel("");
public CoPopupButtonCellEditor(CoPopupMenuButton button)
{
	this(button, 2);
}
public CoPopupButtonCellEditor(CoPopupMenuButton button, int clickCountsToStart)
{
	super(new JCheckBox());
	editorComponent = button;
	setClickCountToStart(clickCountsToStart);

}
	public void fireEditingStopped()
	{
		super.fireEditingStopped();
	}
	public Object getCellEditorValue()
	{
		return currentValue;
	}
public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
{
	currentValue = value;
	if (value != null)
	{
		((CoPopupMenuButton) editorComponent).setText(value.toString());
		return editorComponent;
	}
	else
		return emptyLabel;
}
}
