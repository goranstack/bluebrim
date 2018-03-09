package com.bluebrim.layout.shared;

import java.util.*;

import com.bluebrim.resource.shared.*;

//

public class CoPageItemToolResources extends CoOldResourceBundle
{
	public static CoOldResourceBundle rb = null;

	static final Object[][] contents =
	{
		// Tool description is composed from prototype name and the word tool
		{CoPageItemToolConstants.CREATE_TOOL_NAME,	"{0} tool"},  // Tool name is composed from prototype name and the word tool

	};
	
/**
  Sätter om rb när Locale har ändrats. 
 */
public static CoOldResourceBundle getBundle()
{
	rb = (CoOldResourceBundle) ResourceBundle.getBundle( CoPageItemToolResources.class.getName() );
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
	rb = (CoOldResourceBundle) ResourceBundle.getBundle( CoPageItemToolResources.class.getName() );
	rb.resetLookup();
}
}