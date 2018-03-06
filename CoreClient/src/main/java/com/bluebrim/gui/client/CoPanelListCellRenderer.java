package com.bluebrim.gui.client;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 */
public abstract class CoPanelListCellRenderer extends JPanel implements ListCellRenderer, Serializable {
	
	private boolean m_selected = false;
	
	protected static Border hasFocusBorder;
	protected static Border noFocusBorder;

/**
  * Paints the value.  The background is filled based on selected.
  */
public void paint(Graphics g)
{
	Color bColor = getBackgroundColor();
	if (bColor != null)
	{
		g.setColor(bColor);
		g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
	}
	super.paint(g);

}

/**
  * Return the background color based 
  * upon the selection state.
  */
public Color getBackgroundColor() {
	
	if(m_selected) 
	{
	   return  UIManager.getColor(CoUIConstants.LIST_SELECTION_BACKGROUND);
	} 
	else if (isOpaque())
	{
	   Color bColor = UIManager.getColor(CoUIConstants.LIST_BACKGROUND);
	   return (bColor == null)
				? getBackground()
				: bColor;
	}
	else
		return null;
}


/**
 * CoListCellRenderer constructor comment.
 */
public CoPanelListCellRenderer() {
	super();
	setOpaque(false);
}


public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
{
	m_selected = isSelected;
	
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


protected abstract void setIcon(Icon icon);


protected abstract void setText(String text);


protected void setValue(Object value, int index, boolean isSelected, boolean cellHasFocus)
{
	
	if (value instanceof Icon)
	{
		setIcon((Icon) value);
	}
	else
	{
		setText((value == null) ? "" : value.toString());
	}
}
}