package com.bluebrim.content.impl.server;

import java.util.*;

import com.bluebrim.content.shared.*;
import com.bluebrim.resource.shared.*;

/**
 * @author G�ran St�ck 2002-09-02
 *
 */
public class CoCompositeContentServerResources extends CoOldResourceBundle implements CoCompositeContentServerConstants {
	public static CoOldResourceBundle rb = null;
	static final Object[][] contents = {
		{CoWorkPieceIF.WORK_PIECE,	"Work piece"},
		{UNTITLED_WORKPIECE,	"New work piece"},
	};

	/**
	  S�tter om rb n�r Locale har �ndrats. 
	 */
	protected static CoOldResourceBundle getBundle() {
		if (rb == null)
			rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoCompositeContentServerResources.class.getName());
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
		rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoCompositeContentServerResources.class.getName());
		rb.resetLookup();
	}
}
