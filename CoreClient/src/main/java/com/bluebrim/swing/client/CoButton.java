package com.bluebrim.swing.client;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * JButton subclass. Has a protected (and thus probably not used)
 * method for listening to changes in Actions.
 * 
 * @author Lasse
 * @author Markus Persson 2002-05-24 (slimified)
 */
public class CoButton extends JButton {
	private class ActionChangedListener implements PropertyChangeListener {
		CoButton button;

		ActionChangedListener(CoButton button) {
			super();
			this.button = button;
		}
		public void propertyChange(PropertyChangeEvent e) {
			String propertyName = e.getPropertyName();
			if (e.getPropertyName().equals(Action.NAME)) {
				String text = (String) e.getNewValue();
				button.setText(text);
			} else if (propertyName.equals("enabled")) {
				Boolean enabledState = (Boolean) e.getNewValue();
				button.setEnabled(enabledState.booleanValue());
			} else if (e.getPropertyName().equals(Action.SMALL_ICON)) {
				Icon icon = (Icon) e.getNewValue();
				button.setIcon(icon);
				button.invalidate();
				button.repaint();
			}
		}
	}
	public CoButton() {
		this(null, null);
	}
	public CoButton(String title) {
		this(title, null);
	}
	public CoButton(String title, Icon icon) {
		super(title, icon);
	}
	public CoButton(AbstractAction action) {
		this((String) action.getValue(Action.NAME), (Icon) action.getValue(Action.SMALL_ICON));
		addActionListener(action);
		PropertyChangeListener actionPropertyChangeListener = createActionChangeListener();
		action.addPropertyChangeListener(actionPropertyChangeListener);
		setEnabled(action.isEnabled());
	}
	public CoButton(Icon icon) {
		this(null, icon);
	}
	protected PropertyChangeListener createActionChangeListener() {
		return new ActionChangedListener(this);
	}

	/* Slimification /Markus */
	public Dimension getMaximumSize() {
		return getMinimumSize();
	}

	/* Slimification /Markus */
	public Dimension getPreferredSize() {
		return getMinimumSize();
	}
}