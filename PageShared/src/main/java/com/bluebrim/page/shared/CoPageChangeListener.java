package com.bluebrim.page.shared;

import java.util.EventListener;
import java.util.EventObject;

/**
 * A listener for receiving page change events.
 *
 * @author Göran Stäck 2002-10-03
 *
 */
public interface CoPageChangeListener extends EventListener {
	void pageChange(EventObject evt);
}
