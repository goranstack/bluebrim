package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

/*
 * An abstract class
 */
 
public abstract class CoBinaryNode extends CoFormulaNode {
	protected CoFormulaNode m_leftNode;
	protected CoFormulaNode m_rightNode;
public CoBinaryNode(CoFormulaNode leftNode,  CoFormulaNode rightNode) {
	super();
	
	m_leftNode = leftNode;
	m_rightNode = rightNode;
}
protected abstract String getOperator();
public void putFormulaTextIn (CoFormulaText doc) {
	m_leftNode.putFormulaTextIn(doc);
	doc.addString(getOperator());
	m_rightNode.putFormulaTextIn(doc);
}
public void putFormulaTextWithResolvedVariablesIn (CoVariableBinderIF variableBinder, CoFormulaText doc) throws CoFormulaEvaluationException {
	m_leftNode.putFormulaTextWithResolvedVariablesIn(variableBinder,doc);
	doc.addString(getOperator());
	m_rightNode.putFormulaTextWithResolvedVariablesIn(variableBinder,doc);
}
public String toString () {
	return m_leftNode.toString() + getOperator() + m_rightNode.toString();
}
}
