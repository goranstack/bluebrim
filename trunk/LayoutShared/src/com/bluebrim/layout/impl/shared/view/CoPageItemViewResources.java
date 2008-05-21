package com.bluebrim.layout.impl.shared.view;

import java.util.*;

import com.bluebrim.resource.shared.*;

//

public class CoPageItemViewResources extends CoOldResourceBundle
{
	public static CoOldResourceBundle rb = null;

	static final Object[][] contents =
	{
		{ "POPUP.TOOL.MODIFY", "Modify" },
		{ "POPUP.TOOL.COPY", "Copy" },
		{ "POPUP.TOOL.DELETE", "Delete" },
		{ "POPUP.TOOL.MOVE_UP", "Up" },
		{ "POPUP.TOOL.MOVE_DOWN", "Down" },
	};
/**
  Sätter om rb när Locale har ändrats. 
 */
public static CoOldResourceBundle getBundle()
{
	rb = (CoOldResourceBundle) ResourceBundle.getBundle( CoPageItemViewResources.class.getName() );
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
public static void resetBundle()
{
	rb = (CoOldResourceBundle) ResourceBundle.getBundle( CoPageItemViewResources.class.getName() );
	rb.resetLookup();
}
}