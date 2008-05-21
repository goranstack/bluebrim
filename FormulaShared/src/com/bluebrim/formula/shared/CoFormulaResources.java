package com.bluebrim.formula.shared;

import java.util.*;

import com.bluebrim.resource.shared.*;

// Contains the error messages used by CoFormulaErrorException
public class CoFormulaResources extends CoOldResourceBundle {
	
	public static final String DEFAULT_VARIABLE_NAME = "default_variable_name";
	public static final String RESERVED_WORD_IF = "RESERVED_WORD_IF";
	public static final String RESERVED_WORD_TRUE = "RESERVED_WORD_TRUE";
	public static final String RESERVED_WORD_FALSE = "RESERVED_WORD_FALSE";
	public static final String RESERVED_WORD_AND = "RESERVED_WORD_AND";
	public static final String RESERVED_WORD_OR = "RESERVED_WORD_OR";

	private static CoOldResourceBundle rb = null;

	static final Object[][] contents = {
		{ DEFAULT_VARIABLE_NAME, "Variable_name_"},
		{ RESERVED_WORD_IF, "IF"},
		{ RESERVED_WORD_TRUE, "TRUE"},
		{ RESERVED_WORD_FALSE, "FALSE"},
		{ RESERVED_WORD_AND, "AND"},
		{ RESERVED_WORD_OR, "OR"},
	};	
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)getBundle(CoFormulaResources.class.getName());
	return rb;
}
public Object[][] getContents ( ) {
	return contents;
}
/**
  Svara med det namn som hör till nyckeln aKey.
  Saknas en resurs för aKey så svara med denna.
 */
public static String getName(String aKey ) {
	try {
		return getBundle().getString(aKey);
	} catch (MissingResourceException e) {
		return aKey;
	}			
}
public static String getUIString (String s) {
	// get the translated message
	String str;
	try {
	    str = CoFormulaResources.getName(s);
	} catch (MissingResourceException mre) {
	    str = null;
	}
	return str;
}
public static void resetBundle ( ) {
	rb = (CoOldResourceBundle)getBundle(CoFormulaResources.class.getName());
	rb.resetLookup();
}
}
