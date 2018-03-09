package com.bluebrim.paint.impl.client;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.bluebrim.resource.shared.CoOldResourceBundle;

public class CoPaintUIResources extends CoOldResourceBundle
{
	public static CoOldResourceBundle rb = null;

	static final Object[][] contents =
	{
		{ CoColorCollectionUI.ADD, "Add" }, 
		{ CoColorCollectionUI.REMOVE, "Remove" }, 
		{ CoColorCollectionUI.ADD_RGB_COLORS, "Add RGB colors" }, 
		{ CoColorCollectionUI.SPOT_COLOR, "Spot color" }, 
		{ CoColorCollectionUI.MULTI_INK_COLOR, "Multi ink color" }, 

	};	
	
/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoPaintUIResources.class.getName());
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
public static void resetBundle ( ) {
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoPaintUIResources.class.getName());
	rb.resetLookup();
}
}