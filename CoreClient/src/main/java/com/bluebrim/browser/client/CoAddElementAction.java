package com.bluebrim.browser.client;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.bluebrim.browser.shared.CoAddElementData;
/**
 	Actionklass för menyval i "Lägg till" menyn som används för att 
 	lägga till element i en trädstruktur.
 */
public class CoAddElementAction extends AbstractAction {
	protected CoAbstractCatalogEditor 	editor 			= null;
	protected CoAddElementData 				elementData	= null;
/**
 */
public CoAddElementAction(String name, Icon icon, CoAbstractCatalogEditor editor, CoAddElementData elementData) {
	super(name, icon);
	this.editor 			= editor;
	this.elementData	= elementData;
}
/**
 */
public CoAddElementAction(String name, CoAbstractCatalogEditor editor, CoAddElementData elementData) {
	super(name);
	this.editor 			= editor;
	this.elementData	= elementData;
}
/**
 */
public void actionPerformed(ActionEvent event) {
	editor.handleAddElementAction(elementData); 
}
}
