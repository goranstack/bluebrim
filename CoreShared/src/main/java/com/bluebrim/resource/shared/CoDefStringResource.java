package com.bluebrim.resource.shared;

/**
 * This must be subclassed to construct string resources.
 * 
 * @author Markus Persson 2002-06-04
 */
public abstract class CoDefStringResource implements CoStringResource {
	private int m_ordinal;
	private transient String m_text;
	
	protected CoDefStringResource(int ordinal) {
		m_ordinal = ordinal;
	}
	
	public String text() {
		if (m_text == null) {
			m_text = obtainText();
		}
		return m_text;
	}
	
	protected abstract String obtainText();
}
