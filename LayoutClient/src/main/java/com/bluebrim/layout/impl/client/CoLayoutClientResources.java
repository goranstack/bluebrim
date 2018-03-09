package com.bluebrim.layout.impl.client;

import java.util.*;

import com.bluebrim.layout.client.*;
import com.bluebrim.resource.shared.*;

/**
 * @author Göran Stäck 2002-09-16
 *
 */
public class CoLayoutClientResources extends CoOldResourceBundle implements CoLayoutClientConstants {
	public static CoOldResourceBundle rb = null;
	static final Object[][] contents = {
		{LAYOUT_PARAMETERS,	"Style sheets, colors, line styles etc"},
	    {LAYOUT_PROTOTYPES,	        "Layout templates" },
		// CoLayoutContentUI
		{ CoLayoutContentUI.LAYOUT_TAB, "Layout" },
	};

	/**
	  Sätter om rb när Locale har ändrats. 
	 */
	protected static CoOldResourceBundle getBundle() {
		if (rb == null)
			rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoLayoutClientResources.class.getName());
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
		rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoLayoutClientResources.class.getName());
		rb.resetLookup();
	}
}
