package com.bluebrim.transact.shared;

/**
 * com.bluebrim.gemstone.shared.CoRef concretization using local mapping.
 *
 * This is primarily intended for GemStone simulation.
 *
 * @see CoLocalRefManager
 * @author Markus Persson 2001-01-15
 */
public class CoLocalRef extends com.bluebrim.transact.shared.CoRef {
	// Package private. Only to be used from CoRefManager subclasses.
	int m_id;
	private int m_hashCode;
/**
 * Package private. Only to be used from CoRefManager subclasses.
 */
CoLocalRef(int id, Object obj) {
	m_id = id;
	m_hashCode = obj.hashCode();
}
public final boolean equals(Object obj) {
	return (obj instanceof CoLocalRef) && equals((CoLocalRef) obj);
}
public final boolean equals(CoLocalRef other) {
	return (other.m_id == m_id) && (other.m_hashCode == m_hashCode);
}
/**
 * Resolve reference.
 *
 * NOTE: While it may seem that all subclasses to com.bluebrim.gemstone.shared.CoRef
 * implements this method exactly the same, separate
 * implementations are required because we rely on method
 * overloading which is determined at complie time, NOT
 * dynamically.
 *
 * @author Markus Persson 2001-01-15
 */
public Object get() {
	return MANAGER.resolve(this);
}
public int hashCode() {
	return m_hashCode;
}
}
