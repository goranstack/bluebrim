package com.bluebrim.gui.client;

import com.bluebrim.menus.client.CoPopupMenu;

/**
 * @author Göran Stäck 2002-12-11
 *
 */
public class CoUIpopupGestureListener extends CoAbstractPopupGestureListener {
	
	private CoUserInterface m_ui;
	
	public CoUIpopupGestureListener(CoUserInterface ui) {
		super();
		m_ui = ui;
	}
		
	protected CoPopupMenu getPopupMenu() {
		return m_ui.getObjectMenu();
	}

}
