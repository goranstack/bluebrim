package com.bluebrim.gemstone.shared;
import java.util.*;

import com.bluebrim.resource.shared.*;

/**
 	Resursklass som innehåller lokaliserade strängar som bl a används 
 	av de klasser vars instanser behöver använda typnamn när
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
  Sätter om rb när Locale har ändrats. 
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
	rb = (CoOldResourceBundle)getBundle(CoGemstoneStringResources.class.getName());
	rb.resetLookup();
}
}
