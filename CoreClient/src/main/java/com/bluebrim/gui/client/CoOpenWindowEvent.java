package com.bluebrim.gui.client;

import java.awt.AWTEvent;

import com.bluebrim.base.client.command.CoEvent;

/**
 * En subklass till AWTEvent som används för att öppna ett fönster. 
 * @see CoOpenWindowCommand 
 */
public class CoOpenWindowEvent extends CoEvent {
	protected CoUserInterface userInterface;

	/**
	 * CoCloseWindowEvent constructor comment.
	 * @param source java.lang.Object
	 * @param id int
	 */
	public CoOpenWindowEvent(CoUserInterface source) {
		super(source, AWTEvent.RESERVED_ID_MAX + 101);
		userInterface = source;
	}

	public void dispatch() {
		CoFrame tFrame = new CoFrame(userInterface);
		tFrame.show();
	}
}