package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;
/**
 * Expressions enclosed in bracket is specified as one value when calling
 * putFormulaTextWithResolvedVariablesIn.
 * Creation date: (2000-03-22 15:18:23)
 * @author: Göran Stäck
 */
public class CoBracketNode extends CoUnaryNode {
public CoBracketNode(CoFormulaNode node) {
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
	doc.addString(CoFormulaParser.LEFT_BRACKET);
	m_node.putFormulaTextIn(doc);
	doc.addString(CoFormulaParser.RIGHT_BRACKET);
}
/*
 * Supress specification of bracket enclosed expressions
 */
public void putFormulaTextWithResolvedVariablesIn (CoVariableBinderIF variableBinder, CoFormulaText doc) throws CoFormulaEvaluationException {
	Object value = m_node.evaluate(variableBinder);
	if (m_node.getEvaluationType() == NUMBER_TYPE)
		new CoNumberNode((Double)value).putFormulaTextWithResolvedVariablesIn(variableBinder,doc);
	else
		doc.addString(value.toString());
}
}
