package com.bluebrim.base.shared;

import java.io.*;


public abstract class CoAbstractUnit extends CoSimpleObject implements CoUnit, Serializable {

	private String m_key;

	public CoAbstractUnit(String key) {
		m_key = key;
	}
	public boolean equals(Object other) {
		return (other instanceof CoAbstractUnit) && ((CoAbstractUnit) other).getKey().equals(m_key);
	}
	public String getKey() {
		return m_key;
	}
	public abstract String getName();
	
	public int hashCode() {
		return m_key.hashCode();
	}
}
