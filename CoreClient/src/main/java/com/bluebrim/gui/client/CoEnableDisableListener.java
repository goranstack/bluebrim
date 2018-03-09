package com.bluebrim.gui.client;

import java.util.EventListener;

/**
 *	Interface to be implemented by those classes wishing to listen for
 	<code>CoEnableDisableEvent</code> from for example a <code>CoUserInterfaceBuilder</code>.
 */
public interface CoEnableDisableListener extends EventListener{
/**
 * @param e SE.corren.calvin.userinterface.CoEnableDisableEvent
 */
public void enableDisable (CoEnableDisableEvent e);
}
