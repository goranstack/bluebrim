package com.bluebrim.menus.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

import com.bluebrim.resource.shared.CoMenuItemResource;
/**
 */
public class CoMenuItem extends JMenuItem implements PropertyChangeListener {

	private CoMenuItemResource m_resource;

	public CoMenuItem() {
		super();
	}

	public CoMenuItem(String label) {
		super(label);
	}

	public CoMenuItem(String text, int mnemonic) {
		super(text, mnemonic);
	}

	public CoMenuItem(CoMenuItemResource item) {
		super(item.text(), item.mnemonic());
		setAccelerator(item.accelerator());
		m_resource = item;
	}

	public CoMenuItem(String label, Action action) {
		super(action);
		setText(label);
	}

	public CoMenuItem(String text, Icon icon) {
		super(text, icon);
		setHorizontalTextPosition(SwingConstants.RIGHT);
	}

	public CoMenuItem(Action action) {
		super(action);
	}

	public CoMenuItem(Icon icon) {
		super(icon);
		setHorizontalTextPosition(SwingConstants.RIGHT);
	}

	public CoMenuItemResource getResource() {
		return m_resource;
	}

	public void propertyChange(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();
		if (e.getPropertyName().equals(Action.NAME)) {
			String text = (String) e.getNewValue();
			setText(text);
		} else
			if (propertyName.equals("enabled")) {
				Boolean enabledState = (Boolean) e.getNewValue();
				setEnabled(enabledState.booleanValue());
			}
	}


}
