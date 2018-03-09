package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

 
public class CoEmptyNode extends CoLeafNode {
public CoEmptyNode() {
	super();
	setEvaluationType(STRING_TYPE);
}
public Object evaluate (CoVariableBinderIF variableBinder)  throws CoFormulaEvaluationException {
	return "";
}
public String getIncorrectTypeErrorMessage () {
	return CoFormulaErrorResources.INCORRECT_USE_OF_STRING;
}
public String toString () {
	return "";
}
}
