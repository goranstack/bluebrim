package com.bluebrim.base.client.datatransfer;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTabbedPane;

/**
 * A drop target listener for "opening" tabs in a JTabbedPane during a drag.
 * Creation date: (2001-08-31 07:15:48)
 * @author: Dennis Malmström
 */

public class CoTabbedPaneDropTargetListener extends CoDropTargetListener {
	private List m_titles;

	/**
	 * @param tp JTabbedPane The tabbed pane that is to be "opened"
	 * @param flavors java.awt.datatransfer.DataFlavor[] The flavors that are recognized by this listener
	 * @param titles String[] The titles of the tabs that can be opened
	 */
	public CoTabbedPaneDropTargetListener(JTabbedPane tp, java.awt.datatransfer.DataFlavor[] flavors, String[] titles) {
		super(tp, flavors);

		m_titles = Arrays.asList(titles);
	}

	public boolean canEndDrag(DropTargetDragEvent ev) {
		return false;
	}

	public void dragOver(DropTargetDragEvent ev) {
		int I = getTabbedPane().getComponentCount();
		for (int i = 0; i < I; i++) {
			if (getTabbedPane().getBoundsAt(i).contains(ev.getLocation().getX(), ev.getLocation().getY())) {
				if (m_titles.contains(getTabbedPane().getTitleAt(i))) {
					getTabbedPane().setSelectedIndex(i);
					break;
				}
			}
		}
	}

	private JTabbedPane getTabbedPane() {
		return (JTabbedPane) m_component;
	}

	protected void handleDrop(DropTargetDropEvent ev) {
	}
}