package com.bluebrim.gui.client;

/**
	Interface för de klasser som vill lyssna efter ändringar av 
	värdet i ett gränssnitts värdeobjekt.
*/
public interface CoValueModelListener extends java.util.EventListener {
/**
 */
public void valueModelChange( CoValueModelChangeEvent anEvent);
}
