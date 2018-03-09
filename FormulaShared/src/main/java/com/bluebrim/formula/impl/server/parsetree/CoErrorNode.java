package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

/*
 * A STRING node
 */
 
public class CoErrorNode extends CoStringNode {
public CoErrorNode(String n) {
	super(n);
}
public Object evaluate (CoVariableBinderIF variableBinder)  throws CoFormulaEvaluationException {
	throwErrorMessage();
	return null;
}
public String getIncorrectTypeErrorMessage () {
	return CoFormulaErrorResources.ERROR_NODE;
}
}
