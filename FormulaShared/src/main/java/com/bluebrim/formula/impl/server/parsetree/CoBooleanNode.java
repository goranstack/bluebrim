package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

/*
 * A BOOLEAN ( true/false ) node
 */
 
public class CoBooleanNode extends CoValueNode {
public CoBooleanNode(Boolean n) {
	super(n);
	
	setEvaluationType(BOOLEAN_TYPE);
}
public Object evaluate (CoVariableBinderIF variableBinder)  throws CoFormulaEvaluationException {
	return new Boolean(((Boolean)m_value).booleanValue());
}
public String getIncorrectTypeErrorMessage () {
	return CoFormulaErrorResources.INCORRECT_USE_OF_BOOLEAN;
}
public String toString () {
	if (((Boolean)m_value).booleanValue())
		return " " +  CoFormulaScanner.RESERVED_WORD_TRUE;
	else
		return " " +  CoFormulaScanner.RESERVED_WORD_FALSE;
}
}
