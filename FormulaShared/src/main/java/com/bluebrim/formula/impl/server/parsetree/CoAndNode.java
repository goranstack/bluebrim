package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

/*
 * An AND (&) node
 */
 
public class CoAndNode extends CoBinaryNode {
public CoAndNode(CoFormulaNode leftNode, CoFormulaNode rightNode) {
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
			if (m_rightNode.getEvaluationType() != BOOLEAN_TYPE) break;
			
			return new Boolean(((Boolean) result).booleanValue() && 
												((Boolean) resultRight).booleanValue());
			
		case NUMBER_TYPE:
			setEvaluationType(NUMBER_TYPE);
			break;
			
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
	return CoFormulaErrorResources.INCORRECT_USE_OF_OPERATOR_AND;
}
protected String getOperator() {
	return " " + CoFormulaScanner.RESERVED_WORD_AND + " ";
}
}
