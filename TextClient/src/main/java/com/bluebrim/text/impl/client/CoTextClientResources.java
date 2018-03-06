package com.bluebrim.text.impl.client;

import java.util.*;

import com.bluebrim.resource.shared.*;

public class CoTextClientResources extends CoOldResourceBundle {
	public static CoOldResourceBundle rb = null;
	static final Object[][] contents = { 
		
		{ CoFormattedTextHolderDialog.SCALE, "Scale" },
		{ CoFormattedTextHolderUI.SCALE, "scale" }, 
		{ CoTextContentUI.DOCUMENT_TAB, "Text" },

 };
	/**
	  Sätter om rb när Locale har ändrats. 
	 */
	protected static CoOldResourceBundle getBundle() {
		if (rb == null)
			rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoTextClientResources.class.getName());
		return rb;
	}
	/**
	  Svara med det tecken som hör till nyckeln aKey.
	  Saknas en resurs för aKey så svara med denna.
	 */
	public static char getChar(String aKey) {
		try {
			return getBundle().getString(aKey).charAt(0);
		} catch (MissingResourceException e) {
			return aKey.charAt(0);
		}
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
		rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoTextClientResources.class.getName());
		rb.resetLookup();
	}
}