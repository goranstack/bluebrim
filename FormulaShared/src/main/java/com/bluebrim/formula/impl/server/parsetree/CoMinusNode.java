package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

/*
 * A MINUS (-) node
 */
 
public class CoMinusNode extends CoBinaryNode {
public CoMinusNode(CoFormulaNode leftNode, CoFormulaNode rightNode) {
	super(leftNode, rightNode);
}
public Object evaluate (CoVariableBinderIF variableBinder) throws CoFormulaEvaluationException {
	Object result = m_leftNode.evaluate(variableBinder);
	if (result == null) return null;
	
	switch (m_leftNode.getEvaluationType()) {
		case BOOLEAN_TYPE:
			setEvaluationType(BOOLEAN_TYPE);
			break;
			
		case NUMBER_TYPE:
			setEvaluationType(NUMBER_TYPE);

			Object resultRight = m_rightNode.evaluate(variableBinder);
			if (resultRight == null) return null;
			if (m_rightNode.getEvaluationType() != NUMBER_TYPE)  break;
			
			return new Double(((Number) result).doubleValue() -
											  ((Number) resultRight).doubleValue());
			
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
	return CoFormulaErrorResources.INCORRECT_USE_OF_OPERATOR_MINUS;
}
protected String getOperator() {
	return " - ";
}
}
