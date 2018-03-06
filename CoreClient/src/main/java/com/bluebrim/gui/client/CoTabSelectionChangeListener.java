package com.bluebrim.gui.client;

import java.util.EventListener;

/**
 * For listening to tab selection changes in userinterfaces 
 * that consists of tabbed panes.
 *
 * @author Ali Abida 2001-01-11.
 */
public interface CoTabSelectionChangeListener extends EventListener {
	public void tabSelectionChange(CoTabSelectionChangeEvent event);
}
