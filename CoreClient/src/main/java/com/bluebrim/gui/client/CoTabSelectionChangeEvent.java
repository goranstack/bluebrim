package com.bluebrim.gui.client;

/**
 * Events fired when there is a tab selection change in a CoTabUI.
 * A CoTabSelectionChangeEvent object is sent as an argument
 * to a CoTabSelectionChangeListener tabSelectionChange method.
 *
 * @author Ali Abida 2001-01-11.
 */
public class CoTabSelectionChangeEvent {
	private CoTabUI m_source;
	private boolean m_firedBeforeChange;
	
	private int m_newIndex = -1;
	private int m_oldIndex = -1;
	
	public int getNewIndex() {
		return m_newIndex;
	}
	
	public int getOldIndex() {
		return m_oldIndex;
	}
	
	public CoTabSelectionChangeEvent(CoTabUI source, int newIndex, int oldIndex, boolean firedBeforeChange) {
		m_source = source;
		m_newIndex = newIndex;
		m_oldIndex = oldIndex;
		m_firedBeforeChange = firedBeforeChange;
	}
	
	public boolean firedBeforeChange() {
		return m_firedBeforeChange;
	}
	
	public CoTabUI getSource() {
		return m_source;
	}
}