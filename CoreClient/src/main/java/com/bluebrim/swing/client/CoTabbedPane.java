package com.bluebrim.swing.client;

import javax.swing.JTabbedPane;

/**
 	Subklass till JTabbedPane. Subklassad för att få en egen typ och 
 	inte behöva exponera Swing i klientklasserna.
 */
public class CoTabbedPane extends JTabbedPane {
/**
 * CoTabbedPane constructor comment.
 */
public CoTabbedPane() {
	super();

}
/**
 * CoTabbedPane constructor comment.
 * @param tabPlacement int
 */
public CoTabbedPane(int tabPlacement) {
	super(tabPlacement);
}
}