package com.bluebrim.gui.client;

import java.util.EventListener;

/**
 * Interface implemented by classes interested in changes
 * of the value in a <code>CoValueModel</code>.
 * A <code>CoPostValueListener</code> gets notified after 
 * all <code>CoValueListeners</code>.
 *
 * @author Lars Svadängs 971122
 *
 */
public interface CoPostValueListener extends EventListener {
/**
 */
public void postValueChange( CoValueChangeEvent anEvent);
}
