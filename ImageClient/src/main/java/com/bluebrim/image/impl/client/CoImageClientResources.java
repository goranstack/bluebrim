package com.bluebrim.image.impl.client;

import java.util.*;

import com.bluebrim.resource.shared.*;

public class CoImageClientResources extends CoOldResourceBundle 
{
	public static CoOldResourceBundle rb = null;
	static final Object[][] contents = { 
		

		// CoImageContentUI
		{ CoImageContentUI.CAPTION_TAB, "Caption" }, 
		{ CoImageContentUI.IMAGE_TAB, "Image" }, 

 };
	/**
	  S�tter om rb n�r Locale har �ndrats. 
	 */
	protected static CoOldResourceBundle getBundle() {
		if (rb == null)
			rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoImageClientResources.class.getName());
		return rb;
	}
	/**
	  Svara med det tecken som h�r till nyckeln aKey.
	  Saknas en resurs f�r aKey s� svara med denna.
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
		rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoImageClientResources.class.getName());
		rb.resetLookup();
	}
}