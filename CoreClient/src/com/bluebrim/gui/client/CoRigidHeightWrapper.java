package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.Dimension;

public class CoRigidHeightWrapper extends CoRigidWrapper {

	public CoRigidHeightWrapper(Component c) {
		super(c);
	}

	public Dimension getMaximumSize() {
		Dimension d = m_component.getMaximumSize();
		d.height = m_component.getPreferredSize().height;
		return d;
	}

	public Dimension getMinimumSize() {
		Dimension d = m_component.getMinimumSize();
		d.height = m_component.getPreferredSize().height;
		return d;
	}
}