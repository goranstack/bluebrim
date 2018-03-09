package com.bluebrim.swing.client;

import java.util.EventListener;
/**
 Interface för de klasser som vill lyssna på CoSelectedButtonEvent från en CoButtonGroup.
 */
public interface CoSelectedButtonListener extends EventListener{
/**
 * @param e SE.corren.calvin.userinterface.CoSelectedButtonEvent
 */
public void selectedButtonChanged (CoSelectedButtonEvent e);
}
