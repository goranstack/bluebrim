package com.bluebrim.base.client.command;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

import com.bluebrim.transact.shared.CoCommand;

/**
 * An abstract class used as a command performing something that may take a while to execute.
 * It gives the user feedback by showing a wait cursor over the current frame.
 * Users of this class must implement the abstract getGlassPane and getRootComponent methods.
 *
 * Creation date: 1999-11-24
 * @author: Ali Abida
 */
public abstract class CoWaitFeedbackCommand extends CoCommand {
public CoWaitFeedbackCommand(String name) {
	super(name);
}
public CoWaitFeedbackCommand(String name, Icon icon) {
	super(name, icon);
}
public void execute() {
	SwingUtilities.invokeLater(new Runnable() {
		public void run()
		{
			setBusy(true);
			try
			{
				doExecute();
			}
			finally
			{
				setBusy(false);
			}
		}
	});
}
protected abstract Component getGlassPane();
protected abstract Component getRootComponent();
/**
 * PENDING: Move (or remove) m_frame to be able to reuse this Action ...
 * /Markus 1999-11-24
 */
private void setBusy(boolean busy) {
	Cursor 		cursor 	= Cursor.getPredefinedCursor(busy ? Cursor.WAIT_CURSOR : Cursor.DEFAULT_CURSOR);
	Component 	root 	= getRootComponent();
	Component 	glass 	= getGlassPane();
	
	glass.setCursor(cursor);
	glass.setVisible(busy);
	root.setCursor(cursor);
}
}
