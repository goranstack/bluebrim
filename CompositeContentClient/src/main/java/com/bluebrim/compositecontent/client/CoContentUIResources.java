package com.bluebrim.compositecontent.client;

import java.util.*;

import com.bluebrim.resource.shared.*;

public class CoContentUIResources extends CoOldResourceBundle implements CoContentUIConstants {
	public static CoOldResourceBundle rb = null;
	static final Object[][] contents = { 
		

		// CoContentCollectionUI
		{ CONTENT_COLLECTION, "Contents" },

		// CoContentCollectionEditor
		{ CoContentCollectionEditor.NEW_TEXT, "New text" }, 
		{ CoContentCollectionEditor.NEW_IMAGE, "New image" }, 
		{ CoContentCollectionEditor.NEW_LAYOUT, "New layout" }, 
		{ CoContentCollectionEditor.NEW_WORKPIECE, "New workpiece" }, 
		{ CoContentCollectionEditor.REMOVE, "Remove" }, 
		{ CoContentCollectionEditor.COPY, "Copy" }, 
		{ CoContentCollectionEditor.PASTE, "Paste" },


		// CoWorkPieceUI
		{ CoWorkPieceUI.ATOMIC_CONTENT_TAB, "Content" },

 };
	/**
	  Sätter om rb när Locale har ändrats. 
	 */
	protected static CoOldResourceBundle getBundle() {
		if (rb == null)
			rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoContentUIResources.class.getName());
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
		rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoContentUIResources.class.getName());
		rb.resetLookup();
	}
}