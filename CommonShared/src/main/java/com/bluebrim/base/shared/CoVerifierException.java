package com.bluebrim.base.shared;

public class CoVerifierException extends RuntimeException {
	public static boolean WARNING = true;
	private boolean m_isWarning;

public CoVerifierException() {
	this(null);
}


public CoVerifierException(String message) {
	this(message, false);
}


public CoVerifierException(String message, boolean isWarning) {
	super(message);
	m_isWarning = isWarning;
}


public boolean isWarning() {
	return m_isWarning;
}
}