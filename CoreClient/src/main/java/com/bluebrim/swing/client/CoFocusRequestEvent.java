package com.bluebrim.swing.client;

import java.awt.*;

/**
 * @see CoFocusRequestPanel
 * @author Karin
 */
public class CoFocusRequestEvent extends AWTEvent {
	private Component m_focusedComponent;

	public CoFocusRequestEvent(Object source, Component comp) {
		super(source, AWTEvent.RESERVED_ID_MAX + 17);
		m_focusedComponent = comp;
	}

	public Component getFocusedComponent() {
		return m_focusedComponent;
	}
}