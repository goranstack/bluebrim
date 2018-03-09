package com.bluebrim.formula.impl.client;

import java.util.*;

import com.bluebrim.resource.shared.*;

// Contains the error messages used by CoFormulaErrorException
public class CoFormulaUIResources extends CoOldResourceBundle {
	private static CoOldResourceBundle rb = null;

	public static final String VARIABLE_NAME_LABEL = "VARIABLE_NAME_LABEL";
	public static final String VARIABLE_NAME_ALREADY_USED = "VARIABLE_NAME_ALREADY_USED";
	public static final String VARIABLES_MENU = "VARIABLES_MENU";
	public static final String RESERVED_WORD_IF = "RESERVED_WORD_IF";
	public static final String RESERVED_WORD_TRUE = "RESERVED_WORD_TRUE";
	public static final String RESERVED_WORD_FALSE = "RESERVED_WORD_FALSE";
	public static final String RESERVED_WORD_AND = "RESERVED_WORD_AND";
	public static final String RESERVED_WORD_OR = "RESERVED_WORD_OR";
	public static final String RESERVED_WORD_MM = "RESERVED_WORD_MM";

	static final Object[][] contents = {
		{ VARIABLES_MENU, "Variables"},
		{ RESERVED_WORD_IF, "IF"},
		{ RESERVED_WORD_TRUE, "TRUE"},
		{ RESERVED_WORD_FALSE, "FALSE"},
		{ RESERVED_WORD_AND, "AND"},
		{ RESERVED_WORD_OR, "OR"},
		{ RESERVED_WORD_MM, "MM"},

		// CoAbstractVariableUI
		{VARIABLE_NAME_ALREADY_USED, 		"The name is already used. The name must be unique."},
		{VARIABLE_NAME_LABEL, 				"Variable name"},
	};	
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)getBundle(CoFormulaUIResources.class.getName());
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
	    str = CoFormulaUIResources.getName(s);
	} catch (MissingResourceException mre) {
	    str = null;
	}
	return str;
}
public static void resetBundle ( ) {
	rb = (CoOldResourceBundle)getBundle(CoFormulaUIResources.class.getName());
	rb.resetLookup();
}
}
