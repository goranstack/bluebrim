package com.bluebrim.gui.client;

import com.bluebrim.menus.client.*;

/**
 * @author G�ran St�ck 2002-12-11
 *
 */
public class CoPopupGestureListener extends CoAbstractPopupGestureListener {
	
	private CoPopupMenu m_menu;
	
	public CoPopupGestureListener(CoPopupMenu menu) {
		super();
		m_menu = menu;
	}
	
	protected CoPopupMenu getPopupMenu() {
		return m_menu;
	}

}
