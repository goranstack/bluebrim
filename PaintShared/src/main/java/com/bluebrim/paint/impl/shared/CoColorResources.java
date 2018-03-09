package com.bluebrim.paint.impl.shared;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.resource.shared.*;

public class CoColorResources extends CoOldResourceBundle {
	public static CoOldResourceBundle rb = null;

	// Class that can deliver a localized name to be displayed to the user.
	public static class Name extends CoAbstractLocalizedName {
		public Name(String nameKey) {
			super(nameKey);
		}

		public String getName() {
			return CoColorResources.getName(getKey());
		}
	}

	static final Object[][] contents =
	{
		// Color types
		{com.bluebrim.paint.impl.shared.CoNoColorIF.NO_COLOR,						"No Color"},
		{com.bluebrim.paint.impl.shared.CoSpotColorIF.SPOT_COLOR,					"Spot Color"},
		{com.bluebrim.paint.impl.shared.CoWhiteColorIF.WHITE_COLOR,				"White Color"},							
		{com.bluebrim.paint.impl.shared.CoProcessColorIF.PROCESS_COLOR,			"Process Color"},		
		{com.bluebrim.paint.impl.shared.CoMultiInkColorIF.MULTI_INK_COLOR,			"Multi Ink Color"},
		{com.bluebrim.paint.impl.shared.CoUserDefinedColorIF.USER_DEFINED_COLOR,	"User Defined Color"},
		{com.bluebrim.paint.impl.shared.CoRegistrationColorIF.REGISTRATION_COLOR,	"Registration Color"},

		// Process Colors
		{com.bluebrim.paint.impl.shared.CoProcessCyanIF.PROCESS_CYAN,				"Cyan"},		
		{com.bluebrim.paint.impl.shared.CoProcessBlackIF.PROCESS_BLACK,			"Black"},
		{com.bluebrim.paint.impl.shared.CoProcessYellowIF.PROCESS_YELLOW,			"Yellow"},
		{com.bluebrim.paint.impl.shared.CoProcessMagentaIF.PROCESS_MAGENTA,		"Magenta"},
	};
/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoColorResources.class.getName());
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
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoColorResources.class.getName());
	rb.resetLookup();
}
}
