package com.bluebrim.menus.client;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
/**
	En implementering av PopupMenuListener som fyller samma funktion som 
	MouseAdapter gör för MouseListener, dvs implementerar tomma metoder så
	att subklasser (eller inre klasser) bara behöver omimplementera de metoder
	som är relevanta.
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
