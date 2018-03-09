package com.bluebrim.base.client;

import java.awt.AWTEvent;
import java.awt.Window;

import com.bluebrim.base.client.command.CoEvent;
import com.bluebrim.gui.client.CoUserInterface;
import com.bluebrim.gui.client.CoWindowList;
/**
 En subklass till AWTEvent som används för att stänga ett fönster. 
 Detta event postas i min SystemEventQueue från ett CoCloseWindowCommand.
 @see CoCloseWindowCommand 
*/
public class CoCloseWindowEvent extends CoEvent {
	CoUserInterface userInterface;
/**
 * CoCloseWindowEvent constructor comment.
 * @param source java.lang.Object
 * @param id int
 */
public CoCloseWindowEvent(CoUserInterface source) {
	super(source, AWTEvent.RESERVED_ID_MAX+100);
	userInterface = source;
}
/**
  */
public void dispatch() {
	Window tWindow = CoWindowList.findWindowFor(userInterface);
	if (tWindow != null)
		tWindow.setVisible(false);
}
}
