package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.menus.client.*;

/**
 * Implemented by objects that is supplied in the layout editor configuration
 * for the purpose of extending the layout editor menus with more menu items.
 * 
 * @author Göran Stäck 2002-09-14
 *
 */
public interface CoMenuExtender {
	public void extendMenu( CoMenuBar menuBar, CoMenuBuilder builder);  

}
