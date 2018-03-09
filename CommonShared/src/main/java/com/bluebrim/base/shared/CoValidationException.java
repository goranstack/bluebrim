package com.bluebrim.base.shared;

/**
 * Superclass for all exceptions thrown in a "set" method
 * in a server object. By always using this class a superclass
 * it's possible to catch and handle a validation failure.
 * Creation date: (2000-05-18 11:47:13)
 * @author: Lasse
 */
public class CoValidationException extends Exception {
	private Object m_correctedValue;
public CoValidationException() {
	super();
}
public CoValidationException(String s) {
	super(s);
}
public CoValidationException(String s, Object correctedValue) {
	super(s);
	m_correctedValue = correctedValue;
}
public Object getCorrectedValue() {
	return m_correctedValue;
}
}
