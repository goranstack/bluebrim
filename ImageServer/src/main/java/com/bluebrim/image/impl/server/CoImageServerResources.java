package com.bluebrim.image.impl.server;

import java.util.*;

import com.bluebrim.image.shared.*;
import com.bluebrim.resource.shared.*;

/**
 * @author Göran Stäck 2002-09-02
 *
 */
public class CoImageServerResources extends CoOldResourceBundle implements CoImageServerConstants {
	public static CoOldResourceBundle rb = null;
	static final Object[][] contents = {
		{CoImageContentIF.IMAGE_CONTENT,	"Image"},
		{UNTITLED_IMAGE,	"New image"},
	};

	/**
	  Sätter om rb när Locale har ändrats. 
	 */
	protected static CoOldResourceBundle getBundle() {
		if (rb == null)
			rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoImageServerResources.class.getName());
		return rb;
	}

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
		rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoImageServerResources.class.getName());
		rb.resetLookup();
	}
}
