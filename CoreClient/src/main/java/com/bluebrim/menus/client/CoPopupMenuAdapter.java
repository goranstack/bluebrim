package com.bluebrim.menus.client;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
/**
	En implementering av PopupMenuListener som fyller samma funktion som 
	MouseAdapter g�r f�r MouseListener, dvs implementerar tomma metoder s�
	att subklasser (eller inre klasser) bara beh�ver omimplementera de metoder
	som �r relevanta.
 */
public abstract class CoPopupMenuAdapter implements PopupMenuListener {
/**
 * CoPopupMenuAdaptor constructor comment.
 */
public CoPopupMenuAdapter() {
	super();
}
/**
 * popupMenuCanceled method comment.
 */
public void popupMenuCanceled(PopupMenuEvent e) {
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
