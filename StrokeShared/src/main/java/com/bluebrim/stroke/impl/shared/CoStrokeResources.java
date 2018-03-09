package com.bluebrim.stroke.impl.shared;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.resource.shared.*;
import com.bluebrim.stroke.shared.*;

public class CoStrokeResources extends CoOldResourceBundle {
	public static CoOldResourceBundle rb = null;

	// Class that can deliver a localized name to be displayed to the user.
	public static class Name extends CoAbstractLocalizedName {
		public Name(String nameKey) {
			super(nameKey);
		}

		public String getName() {
			return CoStrokeResources.getName(getKey());
		}
	}

	static final Object[][] contents =
	{
		// Strokes
		{CoStrokeCollectionIF.ALL_DOTS,			"All Dots"},
		{CoStrokeCollectionIF.DASH_DOT,			"Dash Dot"},
		{CoStrokeCollectionIF.DOTTED,				"Dotted"},
		{CoStrokeCollectionIF.DOTTED2,			"Dotted2"},
		{CoStrokeCollectionIF.DOUBLE,				"Double"},
		{CoStrokeCollectionIF.THICK_THIN,			"Thick-Thin"},
		{CoStrokeCollectionIF.THICK_THIN_THICK,	"Thick-Thin-Thick"},
		{CoStrokeCollectionIF.THIN_THICK,			"Thin-Thick"},
		{CoStrokeCollectionIF.THIN_THICK_THIN,	"Thin-Thick-Thin"},
		{CoStrokeCollectionIF.TRIPLE,				"Triple"},
		{CoStrokeIF.SOLID,				"Solid"},
		
	};



/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoStrokeResources.class.getName());
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
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoStrokeResources.class.getName());
	rb.resetLookup();
}
}