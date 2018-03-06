package com.bluebrim.browser.client;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import com.bluebrim.browser.shared.CoCatalogElementIF;
import com.bluebrim.gui.client.CoPanelTreeCellRenderer;
import com.bluebrim.resource.shared.CoResourceLoader;

public class CoCatalogPanelTreeCellRenderer extends CoPanelTreeCellRenderer {
	private JLabel m_label;
	
	public abstract static class CheckBoxRenderer extends CoCatalogPanelTreeCellRenderer {
		private JCheckBox 	m_checkBox;
public CheckBoxRenderer()
{
	super();
	add(Box.createRigidArea(new Dimension(2, 0)));
	add(m_checkBox = new JCheckBox());
	m_checkBox.setOpaque(false);
	m_checkBox.setAlignmentY(Component.CENTER_ALIGNMENT);
}
		protected void setValue(Object value, int row, boolean isSelected,boolean expanded, boolean leaf, boolean hasFocus)
		{
			super.setValue(value, row, isSelected, expanded, leaf, hasFocus);
			m_checkBox.setBackground(getBackgroundColor());
			if (value instanceof CoCatalogElementIF )
			{
				m_checkBox.setSelected(isSelected((CoCatalogElementIF )value));
			}
		}
		protected void setCheckBoxVisibility(boolean show)
		{
			m_checkBox.setVisible(show);
		}
		protected abstract boolean isSelected(CoCatalogElementIF element);
	};

/**
 * CoCatalogPanelTreeCellRenderer constructor comment.
 */
public CoCatalogPanelTreeCellRenderer() {
	super();
	setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	add(m_label 	= new JLabel());
	m_label.setOpaque(false);
}


public void setIcon(Icon icon)
{
	m_label.setIcon(icon);
}


public void setText(String text)
{
	m_label.setText(text);
}


protected void setValue(Object value, int row, boolean isSelected,boolean expanded, boolean leaf, boolean hasFocus)
{
	Color color = getBackgroundColor();
	System.out.println("Changing background from "+m_label.getBackground()+" to "+color);
	m_label.setBackground(color);
	if (value instanceof CoCatalogElementIF )
	{
		CoCatalogElementIF tElement	= (CoCatalogElementIF )value;
		setText(tElement.getIdentity());
		String tIconName			= (tElement.getSmallIconName());
		if (tIconName != null && tIconName.length() > 0)
			setIcon(CoResourceLoader.loadIcon(tElement.getIconResourceAnchor(), tIconName));
		else
			setIcon(null);
	}
}
}