package com.bluebrim.menus.client;

import javax.swing.MenuSelectionManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
/**
	Bugfix från Swing team.
 	Lyssnar på en popupmeny och ser till att en submeny 
 	tas bort om jag klickar utanför densamme.
 */
class CoClosePopupSubmenu implements PopupMenuListener {
/**
 * popupMenuCanceled method comment.
 */
public void popupMenuCanceled(PopupMenuEvent e) {
	MenuSelectionManager.defaultManager().clearSelectedPath();
}
/**
 * popupMenuWillBecomeInvisible method comment.
 */
public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
}
/**
 * popupMenuWillBecomeVisible method comment.
 */
public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
}
}
