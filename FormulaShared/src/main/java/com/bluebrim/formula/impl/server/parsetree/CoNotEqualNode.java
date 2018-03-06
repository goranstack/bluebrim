package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

/*
 * A NOT EQUAL (!=) node
 */
 
public class CoNotEqualNode extends CoBinaryNode {
public CoNotEqualNode(CoFormulaNode leftNode, CoFormulaNode rightNode) {
	super(leftNode, rightNode);
}
public Object evaluate (CoVariableBinderIF variableBinder) throws CoFormulaEvaluationException {
	Object result = m_leftNode.evaluate(variableBinder);
	if (result == null) return null;
	
	switch (m_leftNode.getEvaluationType()) {
		case BOOLEAN_TYPE:
			setEvaluationType(BOOLEAN_TYPE);
			
			Object resultRight = m_rightNode.evaluate(variableBinder);
			if (resultRight == null) return null;
			if (m_rightNode.getEvaluationType() != BOOLEAN_TYPE)  break;
			
			return new Boolean(((Boolean) result).booleanValue() != 
												((Boolean) resultRight).booleanValue());
			
		case NUMBER_TYPE:
			setEvaluationType(BOOLEAN_TYPE);

			resultRight = m_rightNode.evaluate(variableBinder);
			if (resultRight == null) return null;
			if (m_rightNode.getEvaluationType() != NUMBER_TYPE)  break;
			
			return new Boolean(((Number) result).doubleValue() !=
												((Number) resultRight).doubleValue());
			
		case STRING_TYPE:
			setEvaluationType(BOOLEAN_TYPE);

			resultRight = m_rightNode.evaluate(variableBinder);
			if (resultRight == null) return null;
			if (m_rightNode.getEvaluationType() != STRING_TYPE)  break;
			
			return new Boolean( ! ((String) result).equals((String) resultRight));
			
		default:
			setEvaluationType(NO_TYPE);
			break;
	}
	
	throwErrorMessage();
	return null;
}
public String getIncorrectTypeErrorMessage () {
	return CoFormulaErrorResources.INCORRECT_USE_OF_OPERATOR_NOT_EQUAL;
}
protected String getOperator() {
	return " != ";
}
}
