package com.bluebrim.gui.client;

import java.awt.Frame;

/**
 * General sub window used to show user interfaces.
 * Uses a standard CoSubcanvasAdaptor to handle UI:s
 * and domain objects.
 *
 * PENDING: Subject to change. Many questions remain.
 *
 * @author Markus Persson 1999-10-13
 */
public class CoSubcanvasDialog extends CoDialog {
	private CoSubcanvas m_subcanvas;
public CoSubcanvasDialog(Frame frame) {
	this(frame, null, null);
}
public CoSubcanvasDialog(Frame frame, String title) {
	this(frame, title, null);
}
public CoSubcanvasDialog(Frame frame, String title, CoUserInterface ui) {
	super(frame, title);
	// PENDING: Use CoUserInterfaceBuilder?
	m_subcanvas = new CoSubcanvas();
	m_subcanvas.setUserInterface(ui);
	getContentPane().add(m_subcanvas);
}
public CoSubcanvasDialog(Frame frame, CoUserInterface ui) {
	this(frame, null, ui);
}
public CoSubcanvas getSubcanvas() {
	return m_subcanvas;
}
public CoUserInterface getUI() {
	return m_subcanvas.getUserInterface();
}
public void setUI(CoUserInterface ui) {
	m_subcanvas.setUserInterface(ui);
}
}
