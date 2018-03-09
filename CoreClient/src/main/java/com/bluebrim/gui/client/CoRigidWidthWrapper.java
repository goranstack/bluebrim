package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.Dimension;

public class CoRigidWidthWrapper extends CoRigidWrapper {

	public CoRigidWidthWrapper(Component c) {
		super(c);
	}

	public Dimension getMaximumSize() {
		Dimension d = m_component.getMaximumSize();
		d.width = m_component.getPreferredSize().width;
		return d;
	}

	public Dimension getMinimumSize() {
		Dimension d = m_component.getMinimumSize();
		d.width = m_component.getPreferredSize().width;
		return d;
	}
}