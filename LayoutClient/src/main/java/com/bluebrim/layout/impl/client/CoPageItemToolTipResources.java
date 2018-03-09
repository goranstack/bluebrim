package com.bluebrim.layout.impl.client;

import java.util.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.resource.shared.*;

/**
 * @author: Dennis
 */
 
public class CoPageItemToolTipResources extends CoOldResourceBundle 
{
	public static CoOldResourceBundle rb = null;

	static final Object[][] contents =
	{

		{ CoNoLocationIF.NO_LOCATION, "No location rule" },
//		{ CoTopLeftLocationIF.TOP_LEFT_LOCATION, "Top left" },
//		{ CoTopRightLocationIF.TOP_RIGHT_LOCATION, "Top right" },
//		{ CoBottomRightLocationIF.BOTTOM_RIGHT_LOCATION, "Bottom right" },
//		{ CoBottomLeftLocationIF.BOTTOM_LEFT_LOCATION, "Bottom left" },
		{ CoLeftLocationIF.LEFT_LOCATION, "Left" },
		{ CoRightLocationIF.RIGHT_LOCATION, "Right" },
		{ CoTopLocationIF.TOP_LOCATION, "Top" },
		{ CoBottomLocationIF.BOTTOM_LOCATION, "Bottom" },
		{ CoCenterLocationIF.CENTER_LOCATION, "Center" },

		// Height Spec
		{ CoNoSizeSpecIF.NO_HEIGHT_SPEC, "No height specification" },
		{ CoAbsoluteHeightSpecIF.ABSOLUTE_HEIGHT_SPEC, "Absolute height" },
		{ CoFillHeightSpecIF.FILL_HEIGHT_SPEC, "Fill height" },		
		{ CoContentHeightSpecIF.CONTENT_HEIGHT_SPEC, "Content height" },		
		{ CoProportionalHeightSpecIF.PROPORTIONAL_HEIGHT_SPEC, "Proportional height" },		
			
		// Height Spec
		{ CoNoSizeSpecIF.NO_WIDTH_SPEC, "No width specification" },
		{ CoAbsoluteWidthSpecIF.ABSOLUTE_WIDTH_SPEC, "Absolute height" },
		{ CoFillWidthSpecIF.FILL_WIDTH_SPEC, "Fill width" },		
		{ CoContentWidthSpecIF.CONTENT_WIDTH_SPEC, "Content width"},		
		{ CoProportionalWidthSpecIF.PROPORTIONAL_WIDTH_SPEC, "Proportional width" },		
	


	};	
/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoPageItemToolTipResources.class.getName());
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
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoPageItemToolTipResources.class.getName());
	rb.resetLookup();
}
}