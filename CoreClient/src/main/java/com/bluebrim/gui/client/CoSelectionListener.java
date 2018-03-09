package com.bluebrim.gui.client;


/**
 * Interface f�r de klasser som beh�ver lyssna efter
 * ett CoSelectionEvent.
 *
 * @see SelectionEvent
 * @see CoListValueModel#listSelectionChanged
 *
 */
public interface CoSelectionListener extends java.util.EventListener {
/**
 * @param anEvent CoSelectionEvent
 */
public abstract void selectionChange( CoSelectionEvent anEvent);
}
