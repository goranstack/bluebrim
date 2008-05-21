package com.bluebrim.gui.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComponent;

public class CoRigidWrapper extends JComponent {
	protected Component m_component;

	public CoRigidWrapper(Component c) {
		super();
		setLayout(new BorderLayout());
		add(c);
		m_component = c;
	}

	public Dimension getMaximumSize() {
		return m_component.getPreferredSize();
	}

	public Dimension getMinimumSize() {
		return m_component.getPreferredSize();
	}
}