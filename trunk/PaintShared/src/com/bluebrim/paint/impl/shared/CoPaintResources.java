package com.bluebrim.paint.impl.shared;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.resource.shared.*;

public class CoPaintResources extends CoOldResourceBundle {
	public static CoOldResourceBundle rb = null;

	// Class that can deliver a localized name to be displayed to the user.
	public static class Name extends CoAbstractLocalizedName {
		public Name(String nameKey) {
			super(nameKey);
		}

		public String getName() {
			return CoPaintResources.getName(getKey());
		}
	}

	static final Object[][] contents =
	{
		{ CoColorCollectionIF.GREEN, "Green" },
		{ CoColorCollectionIF.BLUE, "Blue" },
		{ CoColorCollectionIF.RED, "Red" },

	};







/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoPaintResources.class.getName());
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
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoPaintResources.class.getName());
	rb.resetLookup();
}
}