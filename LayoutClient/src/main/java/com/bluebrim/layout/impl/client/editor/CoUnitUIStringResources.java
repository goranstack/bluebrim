package com.bluebrim.layout.impl.client.editor;

import java.util.*;

import com.bluebrim.resource.shared.*;


public class CoUnitUIStringResources extends CoOldResourceBundle
{
	public static CoOldResourceBundle rb = null;
	
	static final Object[][] contents =
	{
		{ CoLengthUnitSetUI.UNIT, "Unit" },
		{ CoLengthUnitSetUI.VIEW_DECIMALS, "View decimals" },
		{ CoLengthUnitSetUI.SNAP_DECIMALS, "Snap decimals" },
	};
/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoUnitUIStringResources.class.getName());
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
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoUnitUIStringResources.class.getName());
	rb.resetLookup();
}
}
