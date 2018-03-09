package com.bluebrim.swing.client;

import java.util.EventListener;
/**
 	Interface för de klasser som vill lyssna på CoChooserEvent från en CoChooserPanel.
 */
public interface CoChooserEventListener extends EventListener{
/**
 */
public void handleChooserEvent (CoChooserEvent e);
}
