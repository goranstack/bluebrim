package com.bluebrim.gui.client;

/**
	Interface f�r de klasser som vill lyssna efter �ndringar av 
	v�rdet i ett gr�nssnitts v�rdeobjekt.
*/
public interface CoValueModelListener extends java.util.EventListener {
/**
 */
public void valueModelChange( CoValueModelChangeEvent anEvent);
}
