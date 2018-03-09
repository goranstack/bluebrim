package com.bluebrim.layout.impl.shared.layoutmanager;

import java.util.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.resource.shared.*;

// Dennis, 2000-09-05
 
public class CoPageItemLayoutResources extends CoOldResourceBundle 
{
	public static CoOldResourceBundle rb = null;

	static final Object[][] contents =
	{
		{ CoNoLayoutManagerIF.NO_LAYOUT_MANAGER, "None" },
		{ CoRowLayoutManagerIF.ROW_LAYOUT_MANAGER, "Rowwise" },
		{ CoColumnLayoutManagerIF.COLUMN_LAYOUT_MANAGER, "Columnwise" },
		{ CoReversedColumnLayoutManagerIF.REVERSED_COLUMN_LAYOUT_MANAGER, "Reversed columnwise" },
		{ CoReversedRowLayoutManagerIF.REVERSED_ROW_LAYOUT_MANAGER, "Reversed rowwise" },

	};	
/**
  S�tter om rb n�r Locale har �ndrats. 
 */
public static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoPageItemLayoutResources.class.getName());
	return rb;
}
/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
/**
  Svara med det namn som h�r till nyckeln aKey.
  Saknas en resurs f�r aKey s� svara med denna.
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
  S�tter om rb n�r Locale har �ndrats. 
 */
public static void resetBundle ( ) {
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoPageItemLayoutResources.class.getName());
	rb.resetLookup();
}
}
