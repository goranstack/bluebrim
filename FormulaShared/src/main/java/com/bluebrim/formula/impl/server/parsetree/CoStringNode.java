package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

/*
 * A STRING node
 */
 
public class CoStringNode extends CoValueNode {
public CoStringNode(String n) {
	super(n);
	
	setEvaluationType(STRING_TYPE);
}
public Object evaluate (CoVariableBinderIF variableBinder)  throws CoFormulaEvaluationException {
	return new String((String)m_value);
}
public String getIncorrectTypeErrorMessage () {
	return CoFormulaErrorResources.INCORRECT_USE_OF_STRING;
}
public String toString () {
	return "\"" + m_value.toString() + "\"";
}
}
