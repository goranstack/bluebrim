package com.bluebrim.layoutmanager;

import java.util.*;

import com.bluebrim.resource.shared.*;

// Dennis, 2000-09-05
 
public class CoExJobbLayoutManagerResources extends CoOldResourceBundle 
{
	public static CoOldResourceBundle rb = null;

	static final Object[][] contents =
	{
		{ CoExjobbLayoutManagerIF.COLUMNALG_LAYOUT_MANAGER, "Convex layout" },
		{ CoAdPlacementLayoutManagerIF.AD_PLACEMENT_LAYOUT_MANAGER, "Ad placement" },
		{ CoRectangleLayoutManagerIF.RECTANGLE_LAYOUT_MANAGER, "Rectangle" },
		{ CoLineLayoutManagerIF.LINE_LAYOUT_MANAGER, "Line" },

	};	
/**
  S�tter om rb n�r Locale har �ndrats. 
 */
public static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoExJobbLayoutManagerResources.class.getName());
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
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoExJobbLayoutManagerResources.class.getName());
	rb.resetLookup();
}
}
