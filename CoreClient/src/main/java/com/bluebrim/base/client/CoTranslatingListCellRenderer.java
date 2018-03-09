package com.bluebrim.base.client;

import java.awt.Component;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @author Dennis (?)
 */
public class CoTranslatingListCellRenderer extends DefaultListCellRenderer {
	private ListCellRenderer m_delegate;
	private ResourceBundle m_bundle;

	public CoTranslatingListCellRenderer(ResourceBundle bundle) {
		m_bundle = bundle;
	}
	public CoTranslatingListCellRenderer(ResourceBundle bundle, ListCellRenderer delegate) {
		this(bundle);
		m_delegate = delegate;
	}
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (m_delegate != null) {
			return m_delegate.getListCellRendererComponent(list, getName(value), index, isSelected, cellHasFocus);
		} else {
			return super.getListCellRendererComponent(list, getName(value), index, isSelected, cellHasFocus);
		}
	}
	private Object getName(Object key) {
		if (key == null)
			return null;

		try {
			return m_bundle.getString(key.toString());
		} catch (MissingResourceException ex) {
			return key;
		}
	}
}