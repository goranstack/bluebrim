package com.bluebrim.extensibility.shared;

/**
 * A wrapping exception indicating that the actual constructor failed.
 * 
 * PENDING: There was some thoughts and initial code on how to make this
 * class serializable for exceptions that contained information that
 * wasn't serializable. However, those exception classes should take
 * care of that themselves. (Or they would violate the Serializable
 * contract.) However, there still is the possibility where the causing
 * exception doesn't (and shouldn't) exist in the VM of the recieving end.
 * To support extensions to handle that case, use the static create method
 * instead.
 * 
 * @author Markus Persson 2002-03-12
 */
public class CoConstructorFailedFactoryException extends CoFactoryException {
	public static CoConstructorFailedFactoryException create(String message, Throwable cause) {
		return new CoConstructorFailedFactoryException(message, cause);
	}

	public static CoConstructorFailedFactoryException create(Throwable cause) {
		return new CoConstructorFailedFactoryException(cause);
	}
	
	private CoConstructorFailedFactoryException(String message, Throwable cause) {
		super(message, cause);
	}

	private CoConstructorFailedFactoryException(Throwable cause) {
		super(cause);
	}
}
