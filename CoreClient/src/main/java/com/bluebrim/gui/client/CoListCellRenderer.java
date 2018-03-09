package com.bluebrim.gui.client;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.border.Border;

import com.bluebrim.base.shared.CoNamed;
/**
 	Superklass till BasicListCellRenderer som laddas med gränssnittets CoUIDefaults.
 	Måste ha en egen statisk variabel 'hasFocusBorder' eftersom dess värde sätts 
 	i konstruktorn.
 */
public class CoListCellRenderer extends DefaultListCellRenderer {
	protected static Border hasFocusBorder;
/**
 * CoListCellRenderer constructor comment.
 */
public CoListCellRenderer() {
	super();
}
public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
{
	setColorAndFont(list, value, index, isSelected, cellHasFocus);
	setValue(value, index, isSelected, cellHasFocus);
	setEnabled(list.isEnabled());
	setBorder((cellHasFocus) ? hasFocusBorder : noFocusBorder);
	return this;
}
protected void setColorAndFont(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
{
	if (isSelected)
	{
		setBackground(list.getSelectionBackground());
		setForeground(list.getSelectionForeground());
	}
	else
	{
		setBackground(list.getBackground());
		setForeground(list.getForeground());
	}
	setFont(list.getFont());
	
}
protected void setValue(Object value, int index, boolean isSelected, boolean cellHasFocus) {
	if (value instanceof Icon) {
		setIcon((Icon) value);
	} else if (value instanceof CoNamed) {
		String name = ((CoNamed)value).getName();
		setText((name != null) ? name : "");
	} else {
		setText((value == null) ? "" : value.toString());
	}
}
}
