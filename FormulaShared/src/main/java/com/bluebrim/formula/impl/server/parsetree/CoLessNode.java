package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

/*
 * A LESS (<) node
 */
 
public class CoLessNode extends CoBinaryNode {
public CoLessNode(CoFormulaNode leftNode, CoFormulaNode rightNode) {
	super(leftNode, rightNode);
}
public Object evaluate (CoVariableBinderIF variableBinder) throws CoFormulaEvaluationException {
	Object result = m_leftNode.evaluate(variableBinder);
	
	switch (m_leftNode.getEvaluationType()) {
		case BOOLEAN_TYPE:
			setEvaluationType(BOOLEAN_TYPE);
			// return false if left is true and right is false
			// return true in all other cases
			Object resultRight = m_rightNode.evaluate(variableBinder);
			if (resultRight == null) return null;
			if (m_rightNode.getEvaluationType() != BOOLEAN_TYPE)  break;
			
			return new Boolean(((Boolean) result).booleanValue() != true &&
												((Boolean) resultRight).booleanValue() != false);
			
		case NUMBER_TYPE:
			// return true if left number < right number
			setEvaluationType(BOOLEAN_TYPE);

			resultRight = m_rightNode.evaluate(variableBinder);
			if (resultRight == null) return null;
			if (m_rightNode.getEvaluationType() != NUMBER_TYPE)  break;
			
			return new Boolean(((Number) result).doubleValue() <
												((Number) resultRight).doubleValue());
			
		case STRING_TYPE:
			// return true if right string begins with the same characters as left string, they may not be equal
			setEvaluationType(BOOLEAN_TYPE);

			resultRight = m_rightNode.evaluate(variableBinder);
			if (resultRight == null) return null;
			if (m_rightNode.getEvaluationType() != STRING_TYPE)  break;
			
			return new Boolean(((String) resultRight).startsWith((String) result) && 
												!((String) resultRight).equals((String) result));
			
		default:
			setEvaluationType(NO_TYPE);
			break;
	}
	
	throwErrorMessage();
	return null;
}
public String getIncorrectTypeErrorMessage () {
	return CoFormulaErrorResources.INCORRECT_USE_OF_OPERATOR_LESS;
}
protected String getOperator() {
	return " < ";
}
}
