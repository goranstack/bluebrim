package com.bluebrim.gemstone.shared;
import java.util.*;

import com.bluebrim.resource.shared.*;

/**
 	Resursklass som inneh�ller lokaliserade str�ngar som bl a anv�nds 
 	av de klasser vars instanser beh�ver anv�nda typnamn n�r
 	de visar upp sig.<br>
 	<blockquote><pre>
 		CoGemstoneStringResources.getName(USER);
 	</pre>
 	</blockquote>
	
 */
public class CoGemstoneStringResources extends CoOldResourceBundle implements com.bluebrim.gemstone.shared.CoGemstoneConstants {
	private static CoOldResourceBundle rb	= null;
	static final Object[][] contents =
	{
		{USER,"User"},
		{USER_CATALOG,"User catalog"},
		{USERS,"Users"},
		{CHOOSE_RUNTIME_MODEL,"Choose test environment"},
		{NO_RUNTIME,"Cancel"},
		{SIMULATED_RUNTIME,"GemStone simulation"},
		{GEMSTONE_RUNTIME,"Client & server"},
		{GEMSTONE_SERVER,"GemStone VM"},
	};	
/**
  S�tter om rb n�r Locale har �ndrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)getBundle(CoGemstoneStringResources.class.getName());
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
	rb = (CoOldResourceBundle)getBundle(CoGemstoneStringResources.class.getName());
	rb.resetLookup();
}
}
