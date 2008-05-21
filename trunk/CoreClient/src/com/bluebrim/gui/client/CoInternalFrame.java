package com.bluebrim.gui.client;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class CoInternalFrame extends JInternalFrame {
/**
 * CoInternalFrame constructor comment.
 * @param title java.lang.String
 */
public CoInternalFrame(String title, CoUserInterface ui) {
	this(title, false, ui);
}
/**
 * CoInternalFrame constructor comment.
 * @param title java.lang.String
 * @param resizable boolean
 */
public CoInternalFrame(String title, boolean resizable, CoUserInterface ui) {
	this(title, resizable, false, ui);
}
/**
 * CoInternalFrame constructor comment.
 * @param title java.lang.String
 * @param resizable boolean
 * @param closable boolean
 */
public CoInternalFrame(String title, boolean resizable, boolean closable, CoUserInterface ui) {
	this(title, resizable, closable, false, ui);
}
/**
 * CoInternalFrame constructor comment.
 * @param title java.lang.String
 * @param resizable boolean
 * @param closable boolean
 * @param maximizable boolean
 */
public CoInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, CoUserInterface ui) {
	this(title, resizable, closable, maximizable, false, ui);
}
/**
 * CoInternalFrame constructor comment.
 * @param title java.lang.String
 * @param resizable boolean
 * @param closable boolean
 * @param maximizable boolean
 * @param iconifiable boolean
 */
public CoInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable, CoUserInterface ui) {
	super(title, resizable, closable, maximizable, iconifiable);
	installUI(ui);
}
/**
 * CoInternalFrame constructor comment.
 */
public CoInternalFrame( CoUserInterface ui) {
	this("",ui);
}
protected void installUI(CoUserInterface ui) {

	final CoUserInterface tUI	= ui;
	ui.prepareInternalFrame(this);

	addInternalFrameListener(new InternalFrameAdapter() {
		public void internalFrameClosed(InternalFrameEvent e)
		{
			removeUI(tUI);
		}
	});
	pack();
}
protected void removeUI(CoUserInterface ui) {
}
}
