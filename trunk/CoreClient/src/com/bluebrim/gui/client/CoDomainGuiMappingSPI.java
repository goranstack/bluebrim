package com.bluebrim.gui.client;

import java.awt.datatransfer.DataFlavor;

/**
 * SPI for mapping domain objects to GUIs.
 * 
 * Temporary protocol until client/server is properly separated.
 * 
 * @author Markus Persson 2002-08-16
 */
public interface CoDomainGuiMappingSPI {

	public static interface Mapper {
		public void addKey(String factoryKey, String uiClassName);
		public void addKey(String factoryKey, DataFlavor[] flavors);
	}

	public void collectMappings(Mapper mapper);
}
