package com.bluebrim.layout.shared;

import java.util.EventListener;
import java.util.EventObject;

/**
 * A listener for receiving layout change events.
 *
 * @author Göran Stäck 2002-10-03
 *
 */
public interface CoLayoutChangeListener extends EventListener {
	void layoutChange(EventObject evt);
}
