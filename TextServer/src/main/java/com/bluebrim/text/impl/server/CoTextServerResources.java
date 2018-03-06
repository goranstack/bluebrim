package com.bluebrim.text.impl.server;

import java.util.*;

import com.bluebrim.resource.shared.*;
import com.bluebrim.text.shared.*;

/**
 * @author G�ran St�ck 2002-09-02
 *
 */
public class CoTextServerResources extends CoOldResourceBundle implements CoTextServerConstants {
	public static CoOldResourceBundle rb = null;
	static final Object[][] contents = {
		{UNTITLED_TEXT,	"New text"},
		{CoTextContentIF.TEXT_CONTENT,	"Text"},
	};

	/**
	  S�tter om rb n�r Locale har �ndrats. 
	 */
	protected static CoOldResourceBundle getBundle() {
		if (rb == null)
			rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoTextServerResources.class.getName());
		return rb;
	}

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
		rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoTextServerResources.class.getName());
		rb.resetLookup();
	}
}
