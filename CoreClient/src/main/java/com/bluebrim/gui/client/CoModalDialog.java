package com.bluebrim.gui.client;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.WindowEvent;

/**
 * A subclass to <code>CoDialog</code> for modal dialogs.
 * Handles modality by a workaround to fix  
 * bug Id 4243866 in Sun's bug database.
 * This bug shows itself by clipping heavy-weight components.
 * This bug is said to be fixed in Kestrel (JDK 1.3)
 * Creation date: (2000-08-09 11:20:52)
 * @author: Lasse S
 */
public class CoModalDialog extends CoDialog {
private boolean m_opened;
public CoModalDialog(Dialog owner) {
	super(owner,null, false);
}
public CoModalDialog(Dialog owner, String title) {
	super(owner, title, false);
}
public CoModalDialog(Dialog owner, String title, CoUserInterface ui) {
	super(owner, title, false,ui);
}
public CoModalDialog(Dialog owner, CoUserInterface ui) {
	super(owner, false, ui);
}
/**
 * CoModalDialog constructor comment.
 * @param frame java.awt.Frame
 */
public CoModalDialog(java.awt.Frame frame) {
	super(frame,null, false);
}
/**
 * CoModalDialog constructor comment.
 * @param frame java.awt.Frame
 * @param title java.lang.String
 */
public CoModalDialog(Frame frame, String title) {
	super(frame, title, false);
}
/**
 * CoModalDialog constructor comment.
 * @param frame java.awt.Frame
 * @param title java.lang.String
 * @param ui com.bluebrim.base.client.CoUserInterface
 */
public CoModalDialog(Frame frame, String title, CoUserInterface ui) {
	super(frame, title, false,ui);
}
/**
 * CoModalDialog constructor comment.
 * @param frame java.awt.Frame
 * @param ui com.bluebrim.base.client.CoUserInterface
 */
public CoModalDialog(Frame frame, CoUserInterface ui) {
	super(frame, false, ui);
}
public boolean isModal()
{
	return !m_opened;
}
protected void processWindowEvent(WindowEvent e)
{
	super.processWindowEvent(e);
	switch(e.getID())
	{
		case WindowEvent.WINDOW_OPENED:
		{
			m_opened = true;
			break;
		}
		case WindowEvent.WINDOW_ACTIVATED:
		{
			m_opened = true;
			break;
		}
		case WindowEvent.WINDOW_CLOSED:
		{
			m_opened = false;
			break;
		}
		case WindowEvent.WINDOW_DEACTIVATED:
		{
			m_opened = false;
			break;
		}
	}
}
public void setVisible(boolean state)
{
	if (!state)
		m_opened = false;
	super.setVisible(state);
}
}
