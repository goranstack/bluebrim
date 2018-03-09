package com.bluebrim.swing.client;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.metal.MetalCheckBoxUI;

/**
 * @author Markus Persson 2001-07-23
 */
public class CoCheckBoxRendererUI extends MetalCheckBoxUI {
	private final static CoCheckBoxRendererUI checkboxRendererUI = new CoCheckBoxRendererUI();

public CoCheckBoxRendererUI() {
	super();
}


protected BasicButtonListener createButtonListener(AbstractButton b) {
	return new CoCheckBoxRendererListener(b);
}


public static ComponentUI createUI(JComponent button) {
	return checkboxRendererUI;
}


public synchronized void paint(Graphics g, JComponent c) {
	CoCheckBoxRenderer b = (CoCheckBoxRenderer) c;

	// fill background
	if (c.isOpaque()) {
		g.setColor(b.getBackground());
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}

	Icon icon = getDefaultIcon();
	Rectangle hitArea = b.getLiveHitArea();
	icon.paintIcon(c, g, hitArea.x, hitArea.y);
}
}