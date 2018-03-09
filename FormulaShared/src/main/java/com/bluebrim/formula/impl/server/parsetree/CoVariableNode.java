package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

/*
 * A VARAIBLE ( spaltmillimeter, .. ) node
 */
 
public class CoVariableNode extends CoLeafNode {
	private CoAbstractVariableIF 	m_variable 	= null;
public CoVariableNode(CoAbstractVariableIF variable) {
	super();
	m_variable = variable;
}
public Object evaluate(CoVariableBinderIF variableBinder) throws CoFormulaEvaluationException {
	Object value = m_variable.bindValue(variableBinder);
	if (value == null) {
		throw new CoVariableBindingException(CoFormulaErrorResources.getName(CoFormulaErrorResources.VALUE_MISSING_FOR_VARIABLE) + " " + toString());
	}
	if (value instanceof String) {
		setEvaluationType(STRING_TYPE);
		return value;
	} else
		if (value instanceof Number) {
			setEvaluationType(NUMBER_TYPE);
			return value;
		} else
			if (value instanceof Boolean) {
				setEvaluationType(BOOLEAN_TYPE);
				return value;
			} else
				setEvaluationType(NO_TYPE);
	throwErrorMessage();
	return null;
}
public String getIncorrectTypeErrorMessage () {
	return CoFormulaErrorResources.INCORRECT_USE_OF_VARIABLE;
}
public void putFormulaTextIn (CoFormulaText doc) {
	doc.insertFormulaVariable(doc.getLength(), 0, toString());
}
public void putFormulaTextWithResolvedVariablesIn (CoVariableBinderIF variableBinder, CoFormulaText doc) throws CoVariableBindingException {
	Object value = m_variable.bindValue(variableBinder);
	if (value != null)
		doc.addString(value.toString());
}
public String toString () {
	return m_variable.getName();
}
}
