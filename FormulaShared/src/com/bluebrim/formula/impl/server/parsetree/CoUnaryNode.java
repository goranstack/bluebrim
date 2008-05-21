package com.bluebrim.formula.impl.server.parsetree;


/*
 * An abstract class
 */
 
public abstract class CoUnaryNode extends CoFormulaNode {
	protected CoFormulaNode m_node;
public CoFormulaNode getNode()
{
	return m_node;
}


public CoUnaryNode(CoFormulaNode node) {
	super();
	
	m_node = node;
}
}