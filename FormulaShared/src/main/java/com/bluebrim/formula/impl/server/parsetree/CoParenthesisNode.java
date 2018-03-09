package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

/**
 * Creation date: (2000-03-27 15:05:23)
 * @author: Monika Czerska
 */
public class CoParenthesisNode extends CoUnaryNode {
public CoParenthesisNode(CoFormulaNode node) {
	super(node);
}
public Object evaluate(CoVariableBinderIF variableBinder) throws CoFormulaEvaluationException {
	return m_node.evaluate(variableBinder);
}
public int getEvaluationType() {
	return m_node.getEvaluationType();
}
public String getIncorrectTypeErrorMessage() {
	return m_node.getIncorrectTypeErrorMessage();
}
public void putFormulaTextIn(CoFormulaText doc) {
	doc.addString(CoFormulaParser.LEFT_PARANTHESIS);
	m_node.putFormulaTextIn(doc);
	doc.addString(CoFormulaParser.RIGHT_PARANTHESIS);
}
/*
 * Supress specification of parenthesis enclosed expressions
 */
public void putFormulaTextWithResolvedVariablesIn (CoVariableBinderIF variableBinder, CoFormulaText doc) throws CoFormulaEvaluationException {
	m_node.putFormulaTextWithResolvedVariablesIn(variableBinder,doc);
}
}
