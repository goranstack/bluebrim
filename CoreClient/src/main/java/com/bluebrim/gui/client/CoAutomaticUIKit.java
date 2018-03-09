package com.bluebrim.gui.client;
import java.awt.datatransfer.*;
import java.util.*;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.extensibility.shared.*;

/**
 * Small utility class to automatically select proper
 * UI:s for any type of object, more or less.
 * (Formerly known as CoSourceClientKit.)
 *
 * PENDING: Remake again to use reflection like the bookmarks
 * and avoid all these integers? May be tricky to avoid loading
 * all classes and yet keep the class names correct. Check the
 * specifications! (Unlike with bookmarks, we have access to
 * the classes.)
 *
 * PENDING: Make provisions for selecting different UIs for the
 * same domain object based on additional parameters?
 *
 * @author Markus Persson 2000-01-13
 * @author Markus Persson 2000-05-10
 * @author Markus Persson 2001-10-15
 */
public class CoAutomaticUIKit  {
	// Make it possible to show a default UI when there's no domain object
	// by preloading the map with that UI using this special key. /Markus
	public final static String NO_ITEM_KEY = "NO_ITEM_KEY";
	public final static String UNKNOWN_ITEM_KEY = "UNKNOWN_ITEM_KEY";

	public final static Class[] PARAM_COOBJ_CONTEXT = new Class[] { CoObjectIF.class, CoUIContext.class };
	public final static Class[] PARAM_COOBJ = new Class[] { CoObjectIF.class };

	private final static Map INDEX_MAP = new HashMap();
	private static Set INDEX_SET = new HashSet();
	private final static Map FLAVOR_CACHE = new HashMap();
	private static Map KEY_TO_GUI_CLASS_MAP;
	private static Map KEY_TO_FLAVORS_MAP;

	private final static int DEFAULT = 0;


	static {
		initIntMap();
		initDistributedMappings();
	}

	// Instance state
	private Map m_uiMap = new HashMap();
	private CoUIContext m_context;

	public CoAutomaticUIKit() {
		this(null);
	}

	public CoAutomaticUIKit(CoUIContext context) {
		m_context = context;
	}

	private static void add(String key, int index) {
		Integer indexObj = new Integer(index);
		if (INDEX_SET.add(indexObj)) {
			INDEX_MAP.put(key, indexObj);
		} else {
			System.err.println("Duplicate keys mapping to " + index + ". One is " + key + ".");
		}
	}

	private static DataFlavor[] createFlavorsFor(String key) {
		return (DataFlavor[]) KEY_TO_FLAVORS_MAP.get(key);
	}

	/**
	 * PENDING: Always return UI with <i>item</i> inserted?
	 * Doing so would be consistent. We're already inserting context.
	 * The problem is that some UI:s doesn't seem to handle it. Fix 'em!
	 *
	 * NOTE: The code below looks like this to avoid excessive inner classes.
	 */
	private static CoDomainUserInterface createUIFor(int index, CoObjectIF obj, CoUIContext context) {
		CoDomainUserInterface itemUI;

		switch (index) {

			default :
				return null;

		}
	}

	/**
	 * PENDING: Always return UI with <i>item</i> inserted?
	 * Doing so would be consistent. We're already inserting context.
	 * The problem is that some UI:s doesn't seem to handle it. Fix 'em!
	 */
	private static CoDomainUserInterface tryCreateUIFor(String key, CoObjectIF obj, CoUIContext context) {
		CoDomainUserInterface itemUI = null;
		String guiClassName = (String) KEY_TO_GUI_CLASS_MAP.get(key);
		if (guiClassName != null) {
			try {
				Class guiClass = Class.forName(guiClassName);
				CoConstructorResolver resolver = new CoConstructorResolver(guiClass);
				Object[] args = new Object[] { obj, context };
				itemUI = (CoDomainUserInterface) resolver.createWith(args, PARAM_COOBJ_CONTEXT);
				if (itemUI == null) {
					itemUI = (CoDomainUserInterface) resolver.createWith(args, PARAM_COOBJ);
					if (itemUI == null) {
						itemUI = (CoDomainUserInterface) guiClass.newInstance();
//						if (itemUI != null) {
//							itemUI.setDomain(obj);
//						}
					}
//					if (itemUI instanceof CoContextAcceptingUI) {
//						((CoContextAcceptingUI) itemUI).setUIContext(context);
//						itemUI.setDomain(obj);
//					}
				}

			} catch (ClassNotFoundException cnfe) {
				System.err.println("Could not find GUI class " + guiClassName + " for " + obj + ".");
			} catch (IllegalAccessException iae) {
			} catch (InstantiationException ie) {
			}
		}

		return itemUI;
	}

	/**
	 * Normal way to obtain flavor for an object.
	 *
	 * NOTE: It is probably better to directly obtain a Transferable
	 * from a place like this, but such support isn't here yet.
	 *
	 * @author Markus Persson 2001-09-17
	 */
	public static DataFlavor[] getFlavorsFor(Object item) {
		if (item instanceof CoFactoryElementIF) {
			return getFlavorsForKey(((CoFactoryElementIF) item).getFactoryKey());
		} else {
			return null;
		}
	}

	public static DataFlavor[] getFlavorsForKey(String key) {
		DataFlavor[] flavors = (DataFlavor[]) FLAVOR_CACHE.get(key);

		if ((flavors == null) && !FLAVOR_CACHE.containsKey(key)) {
			// This works around the problem with drop first found in VAJ3.5.
			flavors = CoDataTransferKit.fixFlavors(createFlavorsFor(key));
			FLAVOR_CACHE.put(key, flavors);
		}

		return flavors;
	}

	public CoDomainUserInterface getUIFor(Object item) {
		return getUIForUsing(item, m_uiMap, m_context);
	}

	/**
	 * Resolve to suitable UI for given object using map as cache.
	 *
	 * Handling mappings to null makes it possible to disable
	 * unwanted UIs by preloading the map.
	 *
	 * PENDING: Always return UI with <i>item</i> inserted?
	 * Doing so would be consistent. We're already inserting context.
	 * The problem is that some UI:s doesn't seem to handle it. Fix 'em!
	 */
	public static CoDomainUserInterface getUIForUsing(Object item, Map uiMap) {
		return getUIForUsing(item, uiMap, null);
	}

	/**
	 * Resolve to suitable UI for given object using map as cache.
	 *
	 * Handling mappings to null makes it possible to disable
	 * unwanted UIs by preloading the map.
	 *
	 * PENDING: Always return UI with <i>item</i> inserted?
	 * Doing so would be consistent. We're already inserting context.
	 * The problem is that some UI:s doesn't seem to handle it. Fix 'em!
	 */
	public static CoDomainUserInterface getUIForUsing(Object item, Map uiMap, CoUIContext context) {
		String key;

		if (item == null) {
			// Make it possible to show a default UI when there's no domain object
			// by preloading the map with that UI using this special key. /Markus
			return (CoDomainUserInterface) uiMap.get(NO_ITEM_KEY);
		} else if (item instanceof CoFactoryElementIF) {
			key = ((CoFactoryElementIF) item).getFactoryKey();
		} else {
			key = UNKNOWN_ITEM_KEY;
		}

		return getUIForUsing(key, item, uiMap, context);
	}

	/**
	 * Resolve to suitable UI for given object using map as cache.
	 *
	 * Handling mappings to null makes it possible to disable
	 * unwanted UIs by preloading the map.
	 *
	 * PENDING: Always return UI with <i>item</i> inserted?
	 * Doing so would be consistent. We're already inserting context.
	 * The problem is that some UI:s doesn't seem to handle it. Fix 'em!
	 */
	private static CoDomainUserInterface getUIForUsing(String key, Object item, Map uiMap, CoUIContext context) {
		CoDomainUserInterface itemUI = (CoDomainUserInterface) uiMap.get(key);

		if ((itemUI == null) && !uiMap.containsKey(key)) {
			int index = indexFor(key);
			if ((index == DEFAULT) && !UNKNOWN_ITEM_KEY.equals(key)) {
				// Try our new distributed way ...
				itemUI = tryCreateUIFor(key, (CoObjectIF) item, context);
				if (itemUI == null) {
					// Recurse, but also cache under our key below ...
					itemUI = getUIForUsing(UNKNOWN_ITEM_KEY, item, uiMap, context);
				}
			} else {
				itemUI = createUIFor(index, (CoObjectIF) item, context);
				if (itemUI == null) {
					// Recurse, but also cache under our key below ...
					itemUI = getUIForUsing(UNKNOWN_ITEM_KEY, item, uiMap, context);
				}
			}
			uiMap.put(key, itemUI);
		} else if ((context != null) && (itemUI instanceof CoContextAcceptingUI)) {
			((CoContextAcceptingUI) itemUI).setUIContext(context);
		}

		return itemUI;
	}

	private static int indexFor(String key) {
		Integer indexObj;
		if ((indexObj = (Integer) INDEX_MAP.get(key)) != null) {
			return indexObj.intValue();
		} else {
			return DEFAULT;
		}
	}

	public static void init() {
		INDEX_MAP.clear();
		INDEX_SET.clear();
		initIntMap();
	}

	private static void initIntMap() {
		// Default
		add(UNKNOWN_ITEM_KEY, DEFAULT);


	}

	public void release() {
		release(m_uiMap);
	}

	public static void release(Map uiMap) {
		// Released cached UI:s.
		Iterator iter = uiMap.values().iterator();
		while (iter.hasNext())
			 ((CoUserInterface) iter.next()).closed();
	}

	private static class GuiMapper implements CoDomainGuiMappingSPI.Mapper {
		private Map m_keyMapping = new HashMap();
		private Map m_flavorMap = new HashMap();

		public void addKey(String factoryKey, String uiClassName) {
			m_keyMapping.put(factoryKey, uiClassName);
		}

		public void addKey(String factoryKey, DataFlavor[] flavors) {
			m_flavorMap.put(factoryKey, flavors);
		}

		public Map getFactoryKeyMapping() {
			return m_keyMapping;
		}

		public Map getFlavorMapping() {
			return m_flavorMap;
		}
	}

	private static void initDistributedMappings() {
		GuiMapper mapper = new GuiMapper();
		Iterator providers = CoServices.getProviders(CoDomainGuiMappingSPI.class);
		while (providers.hasNext()) {
			CoDomainGuiMappingSPI provider = (CoDomainGuiMappingSPI) providers.next();
			provider.collectMappings(mapper);
		}

		KEY_TO_GUI_CLASS_MAP = mapper.getFactoryKeyMapping();
		KEY_TO_FLAVORS_MAP = mapper.getFlavorMapping();
	}
}