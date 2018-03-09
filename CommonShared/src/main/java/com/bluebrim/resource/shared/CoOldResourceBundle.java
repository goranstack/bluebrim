package com.bluebrim.resource.shared;

import java.util.*;
/**
 	Superklass för de resursklasser vi kommer att använda.
 	Är just nu en ren imitiation av ResourceBundle men implementerad så att
 	'lookup' nollställs när Locale ändras.
 *
 * NOTE: This lookup resetting that is mentioned above,
 * does not seem to be invoked ...
 * Also, what is required for the subclasses of this class to
 * handle a changed locale is to reset the static ResourceBundle
 * variable. Reseting lookup may free some memory but this should
 * hardly be needed as the ResourceBundles are held in a soft
 * (memory sensitive) cache. (At least now, in JDK1.4. SoftReferences
 * didn't exist prior to JDK1.2) /Markus 2002-06-03
 */
public class CoOldResourceBundle extends ResourceBundle {
	protected Hashtable lookup;

	protected Object[][] getContents() {
		return null;
	}

	/**
	 * Implementation of ResourceBundle.getKeys.
	 */
	public Enumeration getKeys() {
		// lazily load the lookup hashtable.
		if (lookup == null) {
			loadLookup();
		}
		Enumeration result = null;
		if (parent != null) {
			Hashtable temp = new Hashtable();
			for (Enumeration parentKeys = parent.getKeys(); parentKeys.hasMoreElements(); /* nothing */
				) {
				temp.put(parentKeys.nextElement(), this);
			}
			for (Enumeration thisKeys = lookup.keys(); thisKeys.hasMoreElements(); /* nothing */
				) {
				temp.put(thisKeys.nextElement(), this);
			}
			result = temp.keys();
		} else {
			result = lookup.keys();
		}
		return result;
	}

	/**
	 * Return localized resource object using given base resource class.
	 *
	 * Note: The current implementation does not use any performance
	 * enhancements, such as those used in the resource classes themselves.
	 *
	 * @return java.lang.Object
	 * @param resourceClass java.lang.Class
	 * @param key java.lang.String
	 * @see getString(Class,String)
	 * @author Markus Persson 2000-03-03
	 */
	public static Object getObject(Class resourceClass, String key) {
		CoOldResourceBundle rb = (CoOldResourceBundle) ResourceBundle.getBundle(resourceClass.getName());
		return rb.getObject(key);
	}

	/**
	 * Return localized resource string using given base resource class.
	 *
	 * The name getString was chosen over getName since there is, in general,
	 * nothing name-ish with these strings. Rather, if there's any language
	 * element that's commonly NOT translated, it's names.
	 *
	 * Note: The current implementation does not use any performance
	 * enhancements, such as those used in the resource classes themselves.
	 *
	 * @return java.lang.String
	 * @param resourceClass java.lang.Class
	 * @param key java.lang.String
	 * @see getObject(Class,String)
	 * @author Markus Persson 1999-05-25
	 */

	public static String getString(Class resourceClass, String key) {
		try {
			return (String) getObject(resourceClass, key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Override of ResourceBundle, same semantics
	 */
	public final Object handleGetObject(String key) {
		// lazily load the lookup hashtable.
		if (lookup == null) {
			loadLookup();
		}
		return lookup.get(key); // this class ignores locales
	}

	private void loadLookup() {
		Object[][] contents = getContents();
		lookup = new Hashtable(contents.length);
		for (int i = 0; i < contents.length; ++i) {
			// Signals in standard out if entries already exist
			// in the hashtable (if identical keys are used);

			//			String existing = (String)lookup.get(contents[i][0]);
			// 2000-01-11, Dennis, some resource values are not strings.			
			Object existing = lookup.get(contents[i][0]);
			if (existing != null && !existing.equals(contents[i][1])) {
				System.out.println("WARNING(" + getClass().getName() + "): Duplicate entries for key '" + contents[i][0] + "'. Old entry: '" + existing.toString() + "'. New entry: '" + contents[i][1] + "'");
			}
			lookup.put(contents[i][0], contents[i][1]);
		}
	}

	public void resetLookup() {
		lookup = null;
	}
}