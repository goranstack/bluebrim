package com.bluebrim.gui.client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.bluebrim.menus.client.CoPopupMenu;

/**
 * @author Göran Stäck 2002-12-11
 *
 */
public abstract class CoAbstractPopupGestureListener extends MouseAdapter {
	
		
	protected void showPopup(MouseEvent e) {
		if (e.isPopupTrigger())
			getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
	}

	public void mouseReleased(MouseEvent e) {
		showPopup(e);
	}

	public void mouseClicked(MouseEvent e) {
		showPopup(e);
	}
	
	public void mousePressed(MouseEvent e) {
		showPopup(e);
	}
	
	protected abstract CoPopupMenu getPopupMenu();

}
