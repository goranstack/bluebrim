package com.bluebrim.content.impl.shared;

import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.resource.shared.*;

public class CoContentResources extends CoOldResourceBundle implements CoContentConstants {
	public static CoOldResourceBundle rb = null;

	// Class that can deliver a localized name to be displayed to the user.
	public static class Name extends CoAbstractLocalizedName {
		public Name(String nameKey) {
			super(nameKey);
		}

		public String getName() {
			return CoContentResources.getName(getKey());
		}
	}

	static final Object[][] contents =
	{

		// CoEditorialPrefs
		{EDITORIAL_PREFS,						"Editorial preferences"},
		
		// Property dividers
		{ORIGIN,		"Origin"},
		{COPYRIGHT,		"Copyright"},
		{CAPTION,		"Caption"},
		{BYLINE,		"Byline"},
		{KEYWORDS,		"Keywords"},
		{CATEGORIES,	"Categories"},

		// Text/Article properties
		{CHAR_COUNT,								"Character count"},
		{WORD_COUNT,								"Word count"},
		{COLUMN_MM,								"Column mm"},
		{WRITER,								"Writer"},
		{REGISTERED_BY,							"Registered by"},
		{IS_ARCHIVED,							"archived"},

		// CoHypheantionJustification
		{HYPHENATION_JUSTIFICATION,			"Hyphenation and Justification"},
					
		// CoArticle
		{CREATED_BY,								"Created by"},
		{TEXT_EXTRACT,						 		"Text extract"},
	};
/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoContentResources.class.getName());
	return rb;
}
/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
/**
  Svara med det namn som hör till nyckeln aKey.
  Saknas en resurs för aKey så svara med denna.
 */
public static String getName(String aKey ) {
	try
	{
		return getBundle().getString(aKey);
	}
	catch (MissingResourceException e)
	{
		return aKey;
	}			
}
/**
  Sätter om rb när Locale har ändrats. 
 */
public static void resetBundle ( ) {
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoContentResources.class.getName());
	rb.resetLookup();
}
}