package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

/*
 * A MULTIPLY (*) node
 */
 
public class CoMultiplyNode extends CoBinaryNode {
public CoMultiplyNode(CoFormulaNode leftNode, CoFormulaNode rightNode) {
	super(leftNode, rightNode);
}
public Object evaluate (CoVariableBinderIF variableBinder) throws CoFormulaEvaluationException {
	Object result = m_leftNode.evaluate(variableBinder);
	if (result == null) return null;
	
	switch (m_leftNode.getEvaluationType()) {
		case BOOLEAN_TYPE:
			setEvaluationType(BOOLEAN_TYPE);
			// return true if both left and right nodes are true
			Object resultRight = m_rightNode.evaluate(variableBinder);
			if (resultRight == null) return null;
			if (m_rightNode.getEvaluationType() != BOOLEAN_TYPE)  break;
			
			return new Boolean(((Boolean) result).booleanValue() &&
												((Boolean) resultRight).booleanValue());
			
		case NUMBER_TYPE:
			setEvaluationType(NUMBER_TYPE);

			resultRight = m_rightNode.evaluate(variableBinder);
			if (resultRight == null) return null;
			if (m_rightNode.getEvaluationType() != NUMBER_TYPE)  break;
			
			return new Double(((Number) result).doubleValue() *
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
	return CoFormulaErrorResources.INCORRECT_USE_OF_OPERATOR_MULTIPLY;
}
protected String getOperator() {
	return " * ";
}
/*
 * Supress specification if one of the nodes has a value of 1
 */
public void putFormulaTextWithResolvedVariablesIn (CoVariableBinderIF variableBinder, CoFormulaText doc) throws CoFormulaEvaluationException {
	Object result = m_leftNode.evaluate(variableBinder);
	if (m_leftNode.getEvaluationType() == NUMBER_TYPE && ((Number) result).doubleValue() == 1)
		m_rightNode.putFormulaTextWithResolvedVariablesIn(variableBinder,doc);
	else
	{
		result = m_rightNode.evaluate(variableBinder);
		if (m_rightNode.getEvaluationType() == NUMBER_TYPE && ((Number) result).doubleValue() == 1)
			m_leftNode.putFormulaTextWithResolvedVariablesIn(variableBinder,doc);
		else
			super.putFormulaTextWithResolvedVariablesIn(variableBinder,doc);
	}
}
}
