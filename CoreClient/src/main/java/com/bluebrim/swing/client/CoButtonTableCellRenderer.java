package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
/**
 * This type was created in VisualAge.
 */
public class CoButtonTableCellRenderer extends JButton implements TableCellRenderer
{
	protected Color unselectedForeground;
	protected Color unselectedBackground;
/**
 * CoButtonTableCellRenderer constructor comment.
 */
public CoButtonTableCellRenderer() {
	super();
}
/**
 * CoButtonTableCellRenderer constructor comment.
 * @param text java.lang.String
 */
public CoButtonTableCellRenderer(String text) {
	super(text);
}
/**
 * CoButtonTableCellRenderer constructor comment.
 * @param text java.lang.String
 * @param icon javax.swing.Icon
 */
public CoButtonTableCellRenderer(String text, Icon icon) {
	super(text, icon);
}
/**
 * CoButtonTableCellRenderer constructor comment.
 * @param icon javax.swing.Icon
 */
public CoButtonTableCellRenderer(Icon icon) {
	super(icon);
}
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		if (isSelected)
		{
			super.setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		}
		else
		{
			super.setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
			super.setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
		}
		setFont(table.getFont());
		if (hasFocus)
		{
			if (table.isCellEditable(row, column))
			{
				super.setForeground(UIManager.getColor("Table.focusCellForeground"));
				super.setBackground(UIManager.getColor("Table.focusCellBackground"));
			}
		}
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
