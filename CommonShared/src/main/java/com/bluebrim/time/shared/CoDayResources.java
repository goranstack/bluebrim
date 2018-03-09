package com.bluebrim.time.shared;

import java.util.*;

import com.bluebrim.resource.shared.*;

public class CoDayResources extends CoOldResourceBundle implements CoTimeConstants {
	public static CoOldResourceBundle rb = null;

	// IMPORTANT: Do NOT change here just like that. The parsing
	// routines unfortunately have a few quirks that makes them
	// very sensitive to changes here. They should be rewritten.
	// /Markus 2001-03-26
	static final Object[][] contents =
	{
		// PENDING: Change to better "english".
		{DAY_FORMAT_PATTERNS,
			new String[] {
				"EEEE MMMM d, yyyy",	// Full
				"MMMM d, yyyy",	// Long
				"MMM d, yyyy",	// Medium (Default)
				"M/d/yyyy",	// Short
			}
		},
		{DAY_PARSE_PATTERNS,
			new String[] {	// In order of preference
				"EEEE MMMM d, yyyy",
				"MMMM d, yyyy",
				"MMM d, yyyy",
				"M/d/yyyy",	// Short
			}
		},
		// Both for formatting and parsing.
		{TIMESTAMP_FORMAT_PATTERNS,
			new String[] {
				"EEEE MMMM d, yyyy h:mm:ss a",	// Full
				"MMMM d, yyyy HH:mm:ss a",	// Long
				"MMM d, yyyy HH:mm:ss",	// Medium
				"M/d/yyyy HH:mm",	// Short
			}
		},

		// Shortcuts when interacting. Currently in lowercase and no whitespaces.
		{TODAY_SHORTED, "today"},
		{TOMORROW_SHORTED, "tomorrow"},
		{YESTERDAY_SHORTED, "yesterday"},

		// Legacy, remove.
		{DATEFORMAT_SEPARATOR, "/"},
	};
/**
 * Resets rb when Locale has changed.
 */
protected static CoOldResourceBundle getBundle() {
	if (rb == null)
		rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoDayResources.class.getName());
	return rb;
}
/**
 * @return java.lang.Object[][]
 */
public Object[][] getContents() {
	return contents;
}
/**
 * Svara med det namn som hör till nyckeln key.
 * Saknas en resurs för key så svara med denna.
 */
public static String getName(String key) {
	try {
		return getBundle().getString(key);
	} catch (MissingResourceException e) {
		return key;
	}
}
/**
 * Get an object from a ResourceBundle.
 * <BR>Convenience method to save casting.
 * @param key see class description.
 */
public final static String[] getNames(String key) {
	try {
		return getBundle().getStringArray(key);
	} catch (MissingResourceException e) {
		return null;
	}
}
/**
 * Resets rb when Locale has changed.
 */
public static void resetBundle() {
	rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoDayResources.class.getName());
	rb.resetLookup();
}
}
