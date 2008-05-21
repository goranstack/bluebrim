package com.bluebrim.paint.impl.client;
import java.util.*;

import com.bluebrim.resource.shared.*;

public class CoColorUIResources extends CoOldResourceBundle {
	public static CoOldResourceBundle rb = null;

	static final Object[][] contents =
	{
		// com.bluebrim.paint.impl.client.CoColorCollectionUI
		{com.bluebrim.paint.impl.client.CoColorCollectionUI.COLORS, "Colors"},
		
		{com.bluebrim.paint.impl.client.CoMultiInkColorUI.CYAN,		"Cyan"},
		{com.bluebrim.paint.impl.client.CoMultiInkColorUI.BLACK,		"Black"},		
		{com.bluebrim.paint.impl.client.CoMultiInkColorUI.MAGENTA,		"Magenta"},
		{com.bluebrim.paint.impl.client.CoMultiInkColorUI.YELLOW,		"Yellow"},

		{"SWATCHES",					"Swatches"},
		{"RED",							"Red"},
		{"GREEN",						"Green"},
		{"BLUE",						"Blue"},
		{"PREVIEW",						"Preview"},
		{"RECENT",						"Recent"},

		{com.bluebrim.paint.impl.client.CoColorCollectionUI.ADD,				"Add"},
		{com.bluebrim.paint.impl.client.CoColorCollectionUI.REMOVE,				"Remove"},
		{com.bluebrim.paint.impl.client.CoColorCollectionUI.COLORS,	"Colors"},

		{com.bluebrim.paint.impl.client.CoSpotColorUI.SPOT_COLOR,							"Spot Color"},
		{com.bluebrim.paint.impl.client.CoWhiteColorUI.WHITE_COLOR,						"White Color"},
		{com.bluebrim.paint.impl.client.CoProcessColorUI.PROCESS_COLOR,					"Process Color"},						
		{com.bluebrim.paint.impl.client.CoMultiInkColorUI.MULTI_INK_COLOR,					"Multi Ink Color"},
		{com.bluebrim.paint.impl.client.CoRegistrationColorUI.REGISTRATION_COLOR,			"Registration Color"},

		{com.bluebrim.paint.impl.client.CoWhiteColorUI.AS_LIGHT_AS_POSSIBLE,				"The lightest possible color"},	
		
		{com.bluebrim.paint.impl.client.CoMultiInkColorUI.COLOR, "Color"},
		{com.bluebrim.paint.impl.client.CoMultiInkColorUI.SHADE, "Shade"},	
		
		{com.bluebrim.paint.impl.client.CoMultiInkColorUI.APPLY, "Apply"},
		{com.bluebrim.paint.impl.client.CoMultiInkColorUI.RESET, "Reset"},			
	};
/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoColorUIResources.class.getName());
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
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoColorUIResources.class.getName());
	rb.resetLookup();
}
}