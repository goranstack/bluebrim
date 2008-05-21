package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

/*
 * A DIVIDE (/) node
 */
 
public class CoDivideNode extends CoBinaryNode {
public CoDivideNode(CoFormulaNode leftNode, CoFormulaNode rightNode) {
	super(leftNode, rightNode);
}
public Object evaluate (CoVariableBinderIF variableBinder) throws CoFormulaEvaluationException {	
	Object result = m_rightNode.evaluate(variableBinder);
	if (result == null) return null;
	
	switch (m_rightNode.getEvaluationType()) {
		case BOOLEAN_TYPE:
			setEvaluationType(BOOLEAN_TYPE);
			break;
			
		case NUMBER_TYPE:
			setEvaluationType(NUMBER_TYPE);

			double d = ((Number) result).doubleValue();
			if (d == 0) {
				throw new CoFormulaEvaluationException(CoFormulaErrorResources.getName(CoFormulaErrorResources.DIVISION_BY_0));
			} else {
				Object resultLeft = m_leftNode.evaluate(variableBinder);
				if (resultLeft == null) return null;
				if (m_leftNode.getEvaluationType() != NUMBER_TYPE) break;
				
				return new Double(((Number) resultLeft).doubleValue() / d);
			}
			
		case STRING_TYPE:
			setEvaluationType(STRING_TYPE);
			break;
			
		default:
			setEvaluationType(NO_TYPE);
			break;
	}
	
	throwErrorMessage();
	return null;
}
public String getIncorrectTypeErrorMessage () {
	return CoFormulaErrorResources.INCORRECT_USE_OF_OPERATOR_DIVIDE;
}
protected String getOperator() {
	return " / ";
}
}
