package com.bluebrim.solitarylayouteditor;

import java.util.MissingResourceException;

import javax.swing.KeyStroke;

import com.bluebrim.resource.shared.CoMenuItemResource;
import com.bluebrim.resource.shared.CoResources;

/**
 * @author Göran Stäck 2002-12-05
 */
public class CoSolitaryLayoutEditorResources extends CoResources implements CoSolitaryLayoutEditorConstants {
	public static CoResources RB = null;

	
	static final Object[][] contents =
	{
		{ MENU_HELP_RELEASE_NOTES, menuItem("Release notes", 'R') },
		{ CoSolitaryLayoutEditor.RELEASE_NOTES_URL, "release_notes.html" },
			
	};

	public Object[][] getContents() {
		return contents;
	}

	public static CoResources getBundle() {
		if (RB == null) {
			RB = getBundle(CoSolitaryLayoutEditorResources.class);
		}
		return RB;
	}

	public static String getName(String key) {
		try {
			return getBundle().getString(key);
		} catch (MissingResourceException mre) {
			return key;
		}
	}

	public static CoMenuItemResource getMenuItem(Object key) {
		return (CoMenuItemResource) getBundle().get(key);
	}

	public static char getChar(String key) {
		try {
			return getBundle().getString(key).charAt(0);
		} catch (MissingResourceException e) {
			return key.charAt(0);
		}
	}
	
	public static Integer getInteger(String key) {
		try {
			return (Integer) getBundle().getObject(key);
		} catch (MissingResourceException e) {
			return null;
		}
	}

	public static KeyStroke getKeyStroke(String key) {
		try {
			return (KeyStroke) getBundle().get(key);
		} catch (MissingResourceException e) {
			return null;
		}
	}

}