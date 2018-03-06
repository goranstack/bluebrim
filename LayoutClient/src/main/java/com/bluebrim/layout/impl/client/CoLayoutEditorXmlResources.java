package com.bluebrim.layout.impl.client;
import java.util.*;

import com.bluebrim.resource.shared.*;

/**
 */ 
public class CoLayoutEditorXmlResources extends CoOldResourceBundle //implements CoLayoutEditorConstants
{
	public static CoOldResourceBundle rb = null;

	static final Object[][] contents =
	{

		// menu system structure
		{ "MENU_ITEMS", new String[] { 
				"MENU.FILE",
		} },

		
		// file menu
			{ "MENU.FILE_ITEMS", new String[] {
					"MENU.FILE.SAVE_AS_XML",
					"MENU.FILE.LOAD_XML",
					"MENU.FILE.TEST_SAVE_BUSINESS_XML",
					"MENU.FILE.TEST_LOAD_BUSINESS_XML"
			}, 
		}


	};
/**
  S�tter om rb n�r Locale har �ndrats. 
 */
public static CoOldResourceBundle getBundle()
{
	rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoLayoutEditorXmlResources.class.getName());
	return rb;
}
/**
  Svara med det tecken som h�r till nyckeln aKey.
  Saknas en resurs f�r aKey s� svara med denna.
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
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoLayoutEditorXmlResources.class.getName());
	rb.resetLookup();
}
}
