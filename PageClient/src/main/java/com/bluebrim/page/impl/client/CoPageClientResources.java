package com.bluebrim.page.impl.client;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.bluebrim.resource.shared.CoOldResourceBundle;

/**
 * @author Göran Stäck 2002-11-06
 *
 */
public class CoPageClientResources extends CoOldResourceBundle {
	public static CoOldResourceBundle rb = null;
	static final Object[][] contents = {
		
		{ CoPageImplClientConstants.ADD_TREE_NODE, "Add group"},

		{ CoPageImplClientConstants.WIDTH, "Width"},
		{ CoPageImplClientConstants.HEIGHT, "Height"},
	};

	/**
	  Sätter om rb när Locale har ändrats. 
	 */
	public static CoOldResourceBundle getBundle() {
		if (rb == null)
			rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoPageClientResources.class.getName());
		return rb;
	}
	/**
	  * @return java.lang.Object[]
	 */
	public Object[][] getContents() {
		return contents;
	}
	/**
	  Svara med det namn som hör till nyckeln aKey.
	  Saknas en resurs för aKey så svara med denna.
	 */
	public static String getName(String aKey) {
		try {
			return getBundle().getString(aKey);
		} catch (MissingResourceException e) {
			return aKey;
		}
	}
	/**
	  Sätter om rb när Locale har ändrats. 
	 */
	public static void resetBundle() {
		rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoPageClientResources.class.getName());
		rb.resetLookup();
	}

}
