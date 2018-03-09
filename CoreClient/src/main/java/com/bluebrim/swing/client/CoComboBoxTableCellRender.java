package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
/**
 * This type was created in VisualAge.
 */
public class CoComboBoxTableCellRender extends JComboBox implements javax.swing.table.TableCellRenderer
{
	protected Color unselectedForeground;
	protected Color unselectedBackground;
	StringBuffer item;
/**
 * CoComboBoxTableCellRender constructor comment.
 */
public CoComboBoxTableCellRender() {
	super();
	setEditable(false);
	//setEnabled(false);
	setOpaque(true);
	item = new StringBuffer();
	addItem(item);
}
public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
{
	item.setLength(0);
	item.append(value);

	if (hasFocus)
	{
		if (table.isCellEditable(row, column))
		{
			super.setForeground(UIManager.getColor("Table.focusCellForeground"));
			super.setBackground(UIManager.getColor("Table.focusCellBackground"));
		}
	}
	else
	{
		super.setForeground(UIManager.getColor("ComboBox.foreground"));
		super.setBackground(UIManager.getColor("ComboBox.background"));
	}
	Color tBorderColor = isSelected ? table.getSelectionBackground():table.getBackground();
	setBorder(new LineBorder(tBorderColor, 2));
	return this;
}
	public void setBackground(Color c)
	{
		super.setBackground(c);
		unselectedBackground = c;
	}
	public void setForeground(Color c)
	{
		super.setForeground(c);
		unselectedForeground = c;
	}
	public void updateUI()
	{
		super.updateUI();
		setForeground(null);
		setBackground(null);
	}
}
