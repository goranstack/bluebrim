package com.bluebrim.swing.client;

import java.util.EventListener;
/**
 Interface f�r de klasser som vill lyssna p� CoSelectedButtonEvent fr�n en CoButtonGroup.
 */
public interface CoSelectedButtonListener extends EventListener{
/**
 * @param e SE.corren.calvin.userinterface.CoSelectedButtonEvent
 */
public void selectedButtonChanged (CoSelectedButtonEvent e);
}
