package com.bluebrim.gui.client;

/**
  * Interface f�r de klasser som vill lyssna efter
 * �ndringar av v�rdet i en CoValueModel
 *
 * @author Lars Svad�ngs 971010
 *
*/
public interface CoValueListener extends java.util.EventListener {
/**
 * @param anEvent CoValueChangeEvent
 */
public void valueChange( CoValueChangeEvent anEvent);
}
