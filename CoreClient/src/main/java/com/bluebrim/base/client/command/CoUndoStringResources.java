package com.bluebrim.base.client.command;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.bluebrim.resource.shared.CoOldResourceBundle;

public class CoUndoStringResources extends CoOldResourceBundle
{
	public static CoOldResourceBundle rb = null;
	
	static final Object[][] contents =
	{
		{ "Undo", "Undo" },
		{ "UNDO", "Undo" },
		{ "undo", "Undo" },
		
		{ "Redo", "Redo" },
		{ "REDO", "Redo" },
		{ "redo", "Redo" },
	};
/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoUndoStringResources.class.getName());
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
	System.err.println( e );
		return aKey;
	}			
}
/**
  Sätter om rb när Locale har ändrats. 
 */
public static void resetBundle ( ) {
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoUndoStringResources.class.getName());
	rb.resetLookup();
}
}