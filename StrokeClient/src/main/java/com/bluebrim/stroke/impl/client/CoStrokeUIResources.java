package com.bluebrim.stroke.impl.client;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.bluebrim.resource.shared.CoOldResourceBundle;

public class CoStrokeUIResources extends CoOldResourceBundle
{
	public static CoOldResourceBundle rb = null;

	static final Object[][] contents =
	{

		// CoStrokeUI
	  { CoStrokeUI.NAME, "Name" },
	  { CoStrokeUI.ADD_LAYER, "Add" },
	  { CoStrokeUI.DELETE_LAYER, "Remove" },

		// CoStrokeLayerUI
	  { CoStrokeLayerUI.DASH_PARAMETERS, "Dash parameters" },
	  { CoStrokeLayerUI.DASH_WIDTH_PROPORTION, "Proportional width" },
	  { CoStrokeLayerUI.DASH_COLOR, "Color" },
	  { CoStrokeLayerUI.DASH_COLOR_SHADE, "Shade" },
	  { CoStrokeLayerUI.DASH_FOREGROUND_COLOR, "Foreground color" },
	  { CoStrokeLayerUI.DASH_BACKGROUND_COLOR, "Background color" },
	  { CoStrokeLayerUI.DASH_NO_COLOR, "Transparent" },

		// CoDashUI
		{CoDashUI.SIZES, "Sizes"},		
		{CoDashUI.SEGMENTS, "Segments"},
		{CoDashUI.DASH_ATTRIBUTES, "Dash Attributes"},		
		{CoDashUI.NUMBER_OF_SEGMENTS, "Number of segments"},
		{CoDashUI.CYCLE_LENGTH, "Repeats every"},
		{CoDashUI.CYCLE_LENGTH_IN_WIDTH, "times width"},
		{CoDashUI.CYCLE_LENGTH_IN_POINTS, "points"},
		{CoDashUI.JOIN_STYLE, "Miter style"},		
		{CoDashUI.CAP_STYLE,  "Endcap style"},	
		{CoDashUI.JOIN_MITER, "Miter"},
		{CoDashUI.JOIN_ROUND, "Round"},
		{CoDashUI.JOIN_BEVEL, "Bevel"},
		{CoDashUI.CAP_BUTT, "Butted"},
		{CoDashUI.CAP_ROUND, "Round"},
		{CoDashUI.CAP_SQUARE, "Projected rectangle"},

		{CoStrokeCollectionUI.ADD,						"Add"},
		{CoStrokeCollectionUI.ADD_DEFAULT_STROKES,						"Add default strokes"},
		{CoStrokeCollectionUI.REMOVE,					"Remove"},
		{CoStrokeCollectionUI.STROKES,					"Dashes & Stripes" },


	};	
	
/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoStrokeUIResources.class.getName());
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
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoStrokeUIResources.class.getName());
	rb.resetLookup();
}
}