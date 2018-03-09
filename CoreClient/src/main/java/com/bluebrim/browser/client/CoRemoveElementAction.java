package com.bluebrim.browser.client;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
/**
 	Actionklass f�r menyval som anv�nds f�r att 
 	ta bort element ur en tr�dstruktur.
 */
public class CoRemoveElementAction extends AbstractAction {
	CoAbstractCatalogEditor 	editor 			= null;
/**
 */
public CoRemoveElementAction(String name, Icon icon, CoAbstractCatalogEditor editor) {
	super(name, icon);
	this.editor 			= editor;
}
/**
 */
public CoRemoveElementAction(String name, CoAbstractCatalogEditor editor) {
	super(name);
	this.editor 			= editor;
}
/**
 */
public void actionPerformed(ActionEvent event) {
	editor.preHandleRemoveElementAction(); 
}
}
