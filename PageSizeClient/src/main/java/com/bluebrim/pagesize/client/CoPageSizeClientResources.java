package com.bluebrim.pagesize.client;

import java.util.*;

import com.bluebrim.resource.shared.*;

/**
 * @author G�ran St�ck 2002-11-06
 *
 */
public class CoPageSizeClientResources extends CoOldResourceBundle {
	public static CoOldResourceBundle rb = null;
	static final Object[][] contents = {
		
		{ CoPageSizeClientConstants.WIDTH, "Width"},
		{ CoPageSizeClientConstants.HEIGHT, "Height"},
	};

	/**
	  S�tter om rb n�r Locale har �ndrats. 
	 */
	public static CoOldResourceBundle getBundle() {
		if (rb == null)
			rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoPageSizeClientResources.class.getName());
		return rb;
	}
	/**
	  * @return java.lang.Object[]
	 */
	public Object[][] getContents() {
		return contents;
	}
	/**
	  Svara med det namn som h�r till nyckeln aKey.
	  Saknas en resurs f�r aKey s� svara med denna.
	 */
	public static String getName(String aKey) {
		try {
			return getBundle().getString(aKey);
		} catch (MissingResourceException e) {
			return aKey;
		}
	}
	/**
	  S�tter om rb n�r Locale har �ndrats. 
	 */
	public static void resetBundle() {
		rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoPageSizeClientResources.class.getName());
		rb.resetLookup();
	}

}
