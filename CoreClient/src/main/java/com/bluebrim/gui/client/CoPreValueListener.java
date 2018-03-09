package com.bluebrim.gui.client;

import java.util.*;

/**
 * Interface implemented by classes interested in changes
 * of the value in a <code>CoValueModel</code>.
 * A <code>CoPreValueListener</code> gets notified before 
 * all <code>CoValueListeners</code>.
 *
 * @author Lars Svadängs 971010
 *
 */
public interface CoPreValueListener extends EventListener {
/**
 */
public void preValueChange( CoValueChangeEvent anEvent);
}
