package com.bluebrim.swing.client;

import javax.swing.JTabbedPane;

/**
 	Subklass till JTabbedPane. Subklassad f�r att f� en egen typ och 
 	inte beh�va exponera Swing i klientklasserna.
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