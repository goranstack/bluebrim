package com.bluebrim.layout.impl.client.editor;

import java.util.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.operations.*;
import com.bluebrim.layout.impl.client.view.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * A class that is used to configure the layout editor.
 * It effects the layout editor in the following ways:
 *  - It supplies the custom page item operations that are appended to the "Object" menu..
 *  - It supplies page item view popup menu models that are appended to the default ones.
 *  - It supplies the layout editor application used when new layout editrs are spawned.
 *  - It can add items to the list of external UI's that are made available in the "File" menu.
 *  - It provides the set of page item view renderer factories.
 *
 * @author : Dennis
 */

public class CoLayoutEditorConfiguration implements CoLayoutEditorConstants {

	private CoMenuExtender m_menuEnhancer;
	private List m_operations = new ArrayList(); // [ CoPageItemOperationIF ]
	private List m_popupMenuModels = new ArrayList(); // [ CoPageItemPopupMenuModel ]
	private List m_rendererFactories = new ArrayList(); // [ CoPageItemViewRendererFactory ]
	private static Map m_keyToInstanceMap;
	private List m_externalUIs = new ArrayList(); // [ CoUserInterface ]

	public static CoLayoutEditorConfiguration AD_INSTANCE = new CoLayoutEditorConfiguration();
	public static CoLayoutEditorConfiguration EDITORIAL_INSTANCE = new CoLayoutEditorConfiguration() {
		{
			add(new CoSetEditorialStrokeAndMargins());
		}
	};
	static {
		m_keyToInstanceMap = new HashMap();

		m_keyToInstanceMap.put(EDITORIAL_CONFIGURATION, EDITORIAL_INSTANCE);
		m_keyToInstanceMap.put(AD_CONFIGURATION, AD_INSTANCE);
	}

	protected CoLayoutEditorConfiguration() {
		add(CoDefaultPageItemViewRendererFactory.INSTANCE);
		add(CoNamePageItemViewRendererFactory.INSTANCE);
	}

	public void add(CoPageItemPopupMenuModel m) {
		m_popupMenuModels.add(m);
	}

	public String getFactoryKey() {
		return null;
	}

	public List getPopupMenuModels() {
		return m_popupMenuModels;
	}

	public void add(CoUserInterface ui) {
		m_externalUIs.add(ui);
	}

	public void add(CoPageItemOperationIF op) {
		m_operations.add(op);
	}

	public void add(CoPageItemViewRendererFactory f) {
		m_rendererFactories.add(f);
	}

	public CoLayoutEditorConfiguration copy() {
		CoLayoutEditorConfiguration c = new CoLayoutEditorConfiguration();

		c.m_operations.addAll(m_operations);
		c.m_popupMenuModels.addAll(m_popupMenuModels);
		c.m_rendererFactories.addAll(m_rendererFactories);
		c.m_externalUIs.addAll(m_externalUIs);

		return c;
	}

	/**
	 * Add external UI's to the layout editor.
	 * The following objects can be added:
	 *  - Instances of CoUserInterface
	 *  - Instances of CoPageItemPrototypeTreeRootIF
	 *  - Instances of CoBookmarkIF where the business object is an instace of CoCatalogSourceIF
	 * 
	 * @author: Dennis
	 */

	public List getExternalUIs() // [ CoUserInterface / CoPageItemPrototypeTreeRootIF / CoBookmarkIF ]
	{
		return m_externalUIs;
	}

	public List getOperations() // [ CoPageItemOperationIF ]
	{
		return m_operations;
	}

	public List getRendererFactories() // [ CoPageItemViewRendererFactory ]
	{
		return m_rendererFactories;
	}

	public CoLayoutEditorConfiguration getSpawnedConfiguration(CoPageItemIF model) {
		return this;
	}

	public static CoLayoutEditorConfiguration lookup(String key) {
		return (CoLayoutEditorConfiguration) m_keyToInstanceMap.get(key);
	}
	
	public void setMenuExtender (CoMenuExtender menuEnhancer) {
		m_menuEnhancer = menuEnhancer;
	}
	
	public CoMenuExtender getMenuExtender () {
		return m_menuEnhancer;
	}
}