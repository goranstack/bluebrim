package com.bluebrim.swing.client;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonListener;

/**
 * @author Markus Persson 2001-07-23
 */
public class CoCheckBoxRendererListener extends BasicButtonListener {
	private Point m_tmpPoint = new Point();

public CoCheckBoxRendererListener(AbstractButton b) {
	super(b);
}


private boolean inHitArea(MouseEvent e) {
	CoCheckBoxRenderer comp = (CoCheckBoxRenderer) e.getSource();
	Rectangle hitArea = comp.getLiveHitArea();
	Point pos = m_tmpPoint;
	pos.x = e.getX();
	pos.y = e.getY();
	return hitArea.contains(pos);
}

public void mouseDragged(MouseEvent e) {
	AbstractButton b = (AbstractButton) e.getSource();

	// HACK! We're forced to do this since mouseEnter and mouseExit aren't
	// reported while the mouse is down.
	ButtonModel model = b.getModel();

	if (model.isPressed()) {
		if (inHitArea(e)) {
			model.setArmed(true);
		} else {
			model.setArmed(false);
		}
	}
}


public void mousePressed(MouseEvent e) {
	if (SwingUtilities.isLeftMouseButton(e)) {
		AbstractButton b = (AbstractButton) e.getSource();
		ButtonModel model = b.getModel();
		if (!model.isEnabled()) {
			// Disabled buttons ignore all input...
			return;
		}

		// But because of the mouseDragged hack above, we can't do setArmed
		// in mouseEnter. As a workaround, set it here just before setting
		// focus.
		if (inHitArea(e)) {
			model.setArmed(true);
			model.setPressed(true);
		}
		if (!b.hasFocus()) {
			b.requestFocus();
		}
	}
}


public void mouseReleased(MouseEvent e) {
	if (SwingUtilities.isLeftMouseButton(e)) {
		AbstractButton b = (AbstractButton) e.getSource();
		ButtonModel model = b.getModel();
		model.setPressed(false);
		model.setArmed(false);
	}
}
}