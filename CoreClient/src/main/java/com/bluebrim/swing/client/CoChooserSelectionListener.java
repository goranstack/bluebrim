package com.bluebrim.swing.client;

import java.util.EventListener;
/**
 	Interface för de klasser som vill lyssna på CoChooserEvent från en CoChooserPanel.
 */
public interface CoChooserSelectionListener extends EventListener{
/**
 */
public void valueChanged (CoChooserSelectionEvent e);
}
