package com.bluebrim.formula.impl.server.parsetree;

/**
 * Abstract superclass to nodes that has a bounded value. 
 * Used as return type at variable binding methods.
 * Creation date: (1999-12-15 01:57:55)
 * @author: Göran Stäck
 */
public abstract class CoValueNode extends CoLeafNode {
	protected Object m_value;
public CoValueNode(Object value) {
	super();
	m_value = value;
}
public String toString () {
	return m_value.toString();
}
}
