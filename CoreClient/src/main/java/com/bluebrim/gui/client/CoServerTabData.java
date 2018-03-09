package com.bluebrim.gui.client;

import java.util.HashMap;
import java.util.Map;


/**
 * Abstract superclass for classes holding the tab data 
 * for subclasses of <code>CoServerTabUI</code>.
 * This class is also used by worksheets showing the same tabs
 * as the corresponding tab ui.
 * Creation date: (2000-05-19 11:16:48)
 * @author: Lasse
 */
public abstract class CoServerTabData {
	private CoDomainUserInterface m_clientUI;
	protected Map m_tabData;

	public CoServerTabData(CoDomainUserInterface ui) {
		super();
		m_clientUI = ui;
		m_tabData = new HashMap();
	}

	protected void addTabData(CoAbstractUserInterfaceData tabData) {
		m_tabData.put(tabData.getKey(), tabData);
	}

	public final CoDomainUserInterface getClient() {
		return m_clientUI;
	}

	public Map getTabData() {
		return m_tabData;
	}

	public final CoAbstractUserInterfaceData getTabDataFor(Object key) {
		return (CoAbstractUserInterfaceData) m_tabData.get(key);
	}

	public abstract void initializeTabData();
}
