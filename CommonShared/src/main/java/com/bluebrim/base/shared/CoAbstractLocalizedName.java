package com.bluebrim.base.shared;

/**
 * Abstract class for classes that can deliver a localized name
 * through the use of a key.
 *
 * @author Markus Persson 1999-05-19
 */
public abstract class CoAbstractLocalizedName implements CoNameHandler {
	private String m_nameKey;

	public CoAbstractLocalizedName(String nameKey) {
		m_nameKey = nameKey;
	}

	public String getKey() {
		return m_nameKey;
	}
	public abstract String getName();

	public boolean isRenameable() {
		return false;
	}

	public void setName(String name) {
		// throw new UnsupportedOperationException();
	}
}