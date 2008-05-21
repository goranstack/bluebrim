package com.bluebrim.gui.client;

/**
  * Interface för de klasser som vill lyssna efter
 * ändringar av värdet i en CoValueModel
 *
 * @author Lars Svadängs 971010
 *
*/
public interface CoValueListener extends java.util.EventListener {
/**
 * @param anEvent CoValueChangeEvent
 */
public void valueChange( CoValueChangeEvent anEvent);
}
