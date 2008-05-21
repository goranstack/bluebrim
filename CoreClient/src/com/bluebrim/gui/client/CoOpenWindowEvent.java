package com.bluebrim.gui.client;

import java.awt.AWTEvent;

import com.bluebrim.base.client.command.CoEvent;

/**
 * En subklass till AWTEvent som anv�nds f�r att �ppna ett f�nster. 
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