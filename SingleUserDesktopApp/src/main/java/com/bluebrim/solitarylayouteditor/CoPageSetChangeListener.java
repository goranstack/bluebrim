package com.bluebrim.solitarylayouteditor;

import java.util.EventListener;
import java.util.EventObject;

/**
 * A listener for receiving page set change events.
 *
 * @author G�ran St�ck 2002-10-03
 *
 */
public interface CoPageSetChangeListener extends EventListener {
	void pageSetChange(EventObject evt);
}
