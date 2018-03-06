package com.bluebrim.font.impl.shared;

import java.util.*;

import com.bluebrim.resource.shared.*;

/**
 * @author : Dennis
 */

public class CoFontResources extends CoOldResourceBundle
{
	public static CoOldResourceBundle rb = null;

	static final Object[][] contents =
	{
		{ "FACES", "Faces" },
		{ "FONTS", "Fonts" },
		{ "SERVER", "Server" },
		{ "ADD", "+" },
		{ "DELETE", "-" },
		{ "COMMIT", "Save" },
		{ "ROLLBACK", "Reload" },
		{ "LOAD", "Load ..." },
		{ "CREATE FONTS FOR SELECTED FACES", "Create font" },
		
		{ "FACE", "Face" },
		{ "FAMILY", "Family" },
		{ "NAME", "Name" },
		{ "WEIGHT", "Weight" },
		{ "STYLE", "Style" },
		{ "VARIANT", "Variant" },
		{ "STRETCH", "Stretch" },
		{ "SOURCE", "Source" },
		{ "FONT NAME", "Font name" },
		{ "BOLD", "Bold" },

		{ "NORMAL", " " },
		{ "DEFAULT", "All" },
		{ "OBLIQUE", "Oblique" },
		{ "ITALIC", "Italic" },
		{ "SMALL_CAPS", "Small caps" },
		
		{ "ULTRA_CONDENSED", "Ultra Condensed" },
		{ "EXTRA_CONDENSED", "Extra Condensed" },
		{ "CONDENSED", "Condensed" },
		{ "SEMI_CONDENSED", "Semi Condensed" },
		{ "SEMI_EXPANDED", "Semi Expanded" },
		{ "EXPANDED", "Expanded" },
		{ "EXTRA_EXPANDED", "Extra Expanded" },
		{ "ULTRA_EXPANDED", "Ultra Expanded" },
	};

/**
  Sätter om rb när Locale har ändrats. 
 */
public static CoOldResourceBundle getBundle()
{
	rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoFontResources.class.getName());
	return rb;
}
/**
  Svara med det tecken som hör till nyckeln aKey.
  Saknas en resurs för aKey så svara med denna.
 */
public static char getChar(String aKey ) {
	try
	{
		return getBundle().getString(aKey).charAt(0);
	}
	catch (MissingResourceException e)
	{
		return aKey.charAt(0);
	}			
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
public static Integer getInteger(String aKey ) {
	try
	{
		return (Integer) getBundle().getObject(aKey);
	}
	catch (MissingResourceException e)
	{
		return null;
	}			
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
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoFontResources.class.getName());
	rb.resetLookup();
}
}