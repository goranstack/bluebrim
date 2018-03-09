package com.bluebrim.gui.client;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;

import com.bluebrim.browser.shared.CoCatalogElementIF;
import com.bluebrim.resource.shared.CoResourceLoader;

public abstract class CoCatalogPanelListCellRenderer extends CoPanelListCellRenderer {
	public abstract static class CheckBoxRenderer extends CoCatalogPanelListCellRenderer {
		private JLabel		m_label;
		private JCheckBox 	m_checkBox;
		public CheckBoxRenderer()
		{
			super();
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(m_label = new JLabel());
			m_label.setAlignmentY(Component.CENTER_ALIGNMENT);
			add(Box.createHorizontalGlue());
			add(m_checkBox = new JCheckBox());
			m_checkBox.setOpaque(false);
			m_checkBox.setAlignmentY(Component.CENTER_ALIGNMENT);
		}
		protected void setIcon(Icon icon)
		{
			m_label.setIcon(icon);
		}
		protected void setText(String text)
		{
			m_label.setText(text);
		}
		protected void setValue(Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			super.setValue(value, index, isSelected, cellHasFocus);
			m_checkBox.setSelected(isSelected(index));
		}
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			((JComponent )renderer).revalidate();
			return renderer;
		}
		protected abstract boolean isSelected(int index);
	};

/**
 * CoCatalogPanelListCellRenderer constructor comment.
 * @param defaults com.bluebrim.base.client.CoUIDefaults
 */
public CoCatalogPanelListCellRenderer() {
	super();
}


protected void setValue(Object value, int index, boolean isSelected, boolean cellHasFocus){
	if (value != null)
	{	
		CoCatalogElementIF tElement	= (CoCatalogElementIF )value;
		setText(tElement.getIdentity());
		String tIconName			= (tElement.getSmallIconName());
		setIcon(CoResourceLoader.loadIcon(tElement.getIconResourceAnchor(), tIconName));
	}
	else
	{
		setText("");
		setIcon(null);
	}	
}
}