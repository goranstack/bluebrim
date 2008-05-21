package com.bluebrim.base.shared;

import java.util.*;

import com.bluebrim.resource.shared.*;

/**
	Localized string resources.
 	<blockquote><pre>
 		CoStringResources.getName(UNTITLED);
 	</pre>
 	</blockquote>
	
 */
public class CoStringResources extends CoOldResourceBundle implements CoConstants{
	private static CoOldResourceBundle rb	= null;

	// Class that can deliver a localized name to be displayed to the user.
	public static class Name extends CoAbstractLocalizedName {
		public Name(String nameKey) {
			super(nameKey);
		}

		public String getName() {
			return CoStringResources.getName(getKey());
		}
	}

	static final Object[][] contents =
	{
		{UNTITLED,"Untitled"},
		{UNDEFINED,"Undefined"},		
		{PAGES,"pages"},
		{NAME, "Name"}, 
		{DESCRIPTION, "Description"}, 
		{ADD_ITEM,"Add"},
		{EDIT_ITEM,"Edit"},
		{ADD_ITEM_ELLIPSIS,"Add ..."},
		{EDIT_ITEM_ELLIPSIS,"Edit ..."},
		{REMOVE_ITEM,"Remove"},
		{MOVE_ITEM,"Move"},
		{ADD_ITEM_LABEL,"Add new {0}"},
		{REMOVE_ITEM_LABEL,"Remove selected {0}"},

		//	Validering
		{VALIDATION_ERROR,"{0} is not an acceptable value for {1}"},
		{EMPTY_VALIDATION_ERROR,"{0} must have a value"},

	
		{PRINTED, "Printed by {0}, {1}"},
		{Boolean.TRUE.toString(),                       "Yes"},
		{Boolean.FALSE.toString(),                      "No"},
	};
/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)getBundle(CoStringResources.class.getName());
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
	rb = (CoOldResourceBundle)getBundle(CoStringResources.class.getName());
	rb.resetLookup();
}
}