package com.bluebrim.gui.client;

/**
 * Common interface for ui's consisting of a tabbed pane.
 *
 * @author Ali Abida 2001-01-15.
 */
public interface CoTabUI {
public CoAbstractUserInterfaceData getTabDataAt(int index);
public void addTabSelectionChangeListener(CoTabSelectionChangeListener listener);
public void removeTabSelectionChangeListener(CoTabSelectionChangeListener listener);
}