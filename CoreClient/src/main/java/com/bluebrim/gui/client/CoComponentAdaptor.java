package com.bluebrim.gui.client;

import java.awt.Component;

/**
	An abstract superclass for classes connecting a value model with an ui component.
	Their main responsibility is to function as a protocol transformer between the value model which
	understands <code>getValue</code> and </code>setValue</code> and the ui component which understands
	for example <code>setText</code> and <code>getText</code>.
	An adptor is also listening for <code>CoEnableDisableEvents</code> and enables/disables the component
	accordingly.
 */
public abstract class CoComponentAdaptor  extends CoUserInterfaceAdapter implements CoEnableDisableListener {
	
/**
	Enabla/disabla min gränssnittskomponent.
 */
public void enableDisable(CoEnableDisableEvent e) {
	getComponent().setEnabled(e.enable);
	getComponent().repaint();
}
/**
 * Abstrakt metod somi subklassen skall svara med min gränssnittskomponent.
 * Används i #enableDisable().
 */
protected abstract Component getComponent();
}
