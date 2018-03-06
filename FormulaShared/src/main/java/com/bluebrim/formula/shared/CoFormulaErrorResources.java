package com.bluebrim.formula.shared;

import java.util.*;

import com.bluebrim.resource.shared.*;

// Contains the error messages used by CoFormulaErrorException
public class CoFormulaErrorResources extends CoOldResourceBundle {
	private static CoOldResourceBundle rb = null;
	public static final String FORMULA_INCORRECT_FORMULATED = "FORMULA_INCORRECT_FORMULATED";
	public static final String UNEXPECTED_END = "UNEXPECTED_END";
	public static final String THE_IDENTIFIER_NOT_DEFINED = "THE_IDENTIFIER_NOT_DEFINED";
	public static final String IDENTIFIER_NOT_DEFINED = "IDENTIFIER_NOT_DEFINED";
	public static final String MISSING_LEFTPARANTHESIS = "MISSING_LEFTPARANTHESIS";
	public static final String MISSING_LEFT_BRACKET = "MISSING_LEFT_BRACKET";
	public static final String MISSING_FORMULA = "MISSING_FORMULA";
	public static final String UNEXPECTED_TOKEN = "UNEXPECTED_TOKEN";
	public static final String THE_UNEXPECTED_TOKEN = "THE_UNEXPECTED_TOKEN";
	public static final String CAN_NOT_READ_FORMULA = "CAN_NOT_READ_FORMULA";

	public static final String INCORRECT_USE_OF_OPERATOR_AND = "INCORRECT_USE_OF_OPERATOR_AND";
	public static final String INCORRECT_USE_OF_OPERATOR_DIVIDE = "INCORRECT_USE_OF_OPERATOR_DIVIDE";
	public static final String INCORRECT_USE_OF_OPERATOR_EQUAL = "INCORRECT_USE_OF_OPERATOR_EQUAL";
	public static final String INCORRECT_USE_OF_OPERATOR_GREATER_EQUAL = "INCORRECT_USE_OF_OPERATOR_GREATER_EQUAL";
	public static final String INCORRECT_USE_OF_OPERATOR_GREATER = "INCORRECT_USE_OF_OPERATOR_GREATER";
	public static final String INCORRECT_USE_OF_OPERATOR_LESS_EQUAL = "INCORRECT_USE_OF_OPERATOR_LESS_EQUAL";
	public static final String INCORRECT_USE_OF_OPERATOR_LESS = "INCORRECT_USE_OF_OPERATOR_LESS";
	public static final String INCORRECT_USE_OF_OPERATOR_MINUS = "INCORRECT_USE_OF_OPERATOR_MINUS";
	public static final String INCORRECT_USE_OF_OPERATOR_PLUS = "INCORRECT_USE_OF_OPERATOR_PLUS";
	public static final String INCORRECT_USE_OF_OPERATOR_MULTIPLY = "INCORRECT_USE_OF_OPERATOR_MULTIPLY";
	public static final String INCORRECT_USE_OF_OPERATOR_NOT = "INCORRECT_USE_OF_OPERATOR_NOT";
	public static final String INCORRECT_USE_OF_OPERATOR_NEGATION = "INCORRECT_USE_OF_OPERATOR_NEGATION";
	public static final String INCORRECT_USE_OF_OPERATOR_NOT_EQUAL = "INCORRECT_USE_OF_OPERATOR_NOT_EQUAL";
	public static final String INCORRECT_USE_OF_NUMBER = "INCORRECT_USE_OF_NUMBER";
	public static final String INCORRECT_USE_OF_OPERATOR_OR = "INCORRECT_USE_OF_OPERATOR_OR";
	public static final String INCORRECT_USE_OF_VARIABLE = "INCORRECT_USE_OF_VARIABLE";
	public static final String INCORRECT_USE_OF_UNIT = "INCORRECT_USE_OF_UNIT";
	public static final String DIVISION_BY_0 = "DIVISION_BY_0";
	public static final String INCORRECT_CONDITION_EXPRESSION = "INCORRECT_CONDITION_EXPRESSION";
	public static final String THE_CONDITION_EXPRESSION_MISSING = "THE_CONDITION_EXPRESSION_MISSING";
	public static final String INCORRECT_USE_OF_BOOLEAN = "INCORRECT_USE_OF_BOOLEAN";
	public static final String INCORRECT_USE_OF_STRING = "INCORRECT_USE_OF_STRING";
	public static final String LOOPING_SEQUENCE_FOUND_IN_NODE = "LOOPING_SEQUENCE_FOUND_IN_NODE";
	public static final String INTERVAL_MISSING_FOR_VARIABLE = "INTERVAL_MISSING_FOR_VARIABLE";
	public static final String VALUE_MISSING_FOR_VARIABLE = "VALUE_MISSING_FOR_VARIABLE";
	public static final String THE_MISSING_VARIABLE = "THE_MISSING_VARIABLE";
	public static final String ERROR_NODE = "ERROR_NODE";

	public static final String THE_VARABLE_NAME_NOT_UNIQUE = "THE_VARABLE_NAME_NOT_UNIQUE";
	public static final String THE_VARABLE_NAME_NOT_DEFINED = "THE_VARABLE_NAME_NOT_DEFINED";
	public static final String VARABLE_NAME_EMPTY = "VARABLE_NAME_EMPTY";
	public static final String VARABLE_TYPE_INCORRECT = "VARABLE_TYPE_INCORRECT";
	
	static final Object[][] contents = {
		{ FORMULA_INCORRECT_FORMULATED,"The formula is not correctly formulated."},
		{ UNEXPECTED_END,"The formula is not complete."},
		{ THE_IDENTIFIER_NOT_DEFINED,"The definition is missing for identifier"},
		{ IDENTIFIER_NOT_DEFINED,"Identifier is not defined."},
		{ MISSING_LEFTPARANTHESIS,"Left paranthesis is missing."},
		{ MISSING_LEFT_BRACKET,"Left bracket is missing."},
		{ MISSING_FORMULA,"No formula has been set."},
		{ UNEXPECTED_TOKEN,"An unexpected token was found in formula."},
		{ THE_UNEXPECTED_TOKEN,"The formula has an unexpected token :"},
		{ CAN_NOT_READ_FORMULA,"Can not read formula."},
		// error messages from the Co__Node classes
		{ INCORRECT_USE_OF_OPERATOR_AND,"Incorrect use of the operator &."},
		{ INCORRECT_USE_OF_OPERATOR_DIVIDE,"Incorrect use of the operator /."},
		{ INCORRECT_USE_OF_OPERATOR_EQUAL,"Incorrect use of the operator =."},
		{ INCORRECT_USE_OF_OPERATOR_GREATER_EQUAL,"Incorrect use of the operator >=."},
		{ INCORRECT_USE_OF_OPERATOR_GREATER,"Incorrect use of the operator >."},
		{ INCORRECT_USE_OF_OPERATOR_LESS_EQUAL,"Incorrect use of the operator <=."},
		{ INCORRECT_USE_OF_OPERATOR_LESS,"Incorrect use of the operator <."},
		{ INCORRECT_USE_OF_OPERATOR_MINUS,"Incorrect use of the operator -."},
		{ INCORRECT_USE_OF_OPERATOR_PLUS,"Incorrect use of the operator +."},
		{ INCORRECT_USE_OF_OPERATOR_MULTIPLY,"Incorrect use of the operator *."},
		{ INCORRECT_USE_OF_OPERATOR_NOT,"Incorrect use of the operator !."},
		{ INCORRECT_USE_OF_OPERATOR_NEGATION,"Incorrect use of the operator -."},
		{ INCORRECT_USE_OF_OPERATOR_NOT_EQUAL,"Incorrect use of the operator !=."},
		{ INCORRECT_USE_OF_NUMBER,"Incorrect use of number."},
		{ INCORRECT_USE_OF_OPERATOR_OR,"Incorrect use of the operator |."},
		{ INCORRECT_USE_OF_VARIABLE,"Incorrect use of variable."},
		{ INCORRECT_USE_OF_UNIT,"Incorrect use of unit."},
		{ DIVISION_BY_0,"Division by 0 is not allowed."},
		{ INCORRECT_CONDITION_EXPRESSION,"The condition expression must have a boolean expression."},
		{ THE_CONDITION_EXPRESSION_MISSING,"The condition expression misses the token"},
		{ INCORRECT_USE_OF_BOOLEAN,"Incorrect use of boolean."},
		{ INCORRECT_USE_OF_STRING,"Incorrect use of string."},
		{ LOOPING_SEQUENCE_FOUND_IN_NODE,"The formula can not be evaluated due to looping in variable"},
		{ INTERVAL_MISSING_FOR_VARIABLE,"No interval covering todays date is definied for variable"},
		{ VALUE_MISSING_FOR_VARIABLE,"No value is set for variable"},
		{ THE_MISSING_VARIABLE,"No variable found with the name"},
		{ ERROR_NODE,"Incorrect formula."},
		// error messages from com.bluebrim.formula.impl.server.CoAbstractVariablesHolder
		{ THE_VARABLE_NAME_NOT_UNIQUE,"Not a unique name. Can not add variable"},
		{ THE_VARABLE_NAME_NOT_DEFINED,"The variable name is not definied :"},
		{ VARABLE_NAME_EMPTY,"A variable name must be given."},
		{ VARABLE_TYPE_INCORRECT,"The type of the variable is incorrect."},
	};	
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)getBundle(CoFormulaErrorResources.class.getName());
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
public static void resetBundle ( ) {
	rb = (CoOldResourceBundle)getBundle(CoFormulaErrorResources.class.getName());
	rb.resetLookup();
}
}
