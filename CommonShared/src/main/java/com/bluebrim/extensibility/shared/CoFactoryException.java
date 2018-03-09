package com.bluebrim.extensibility.shared;

/**
 * Super exception for factory related exceptional events.
 * 
 * PENDING: Should perhaps not extended RuntimeException ...
 * 
 * @author Markus Persson 2002-03-11
 */
public class CoFactoryException extends RuntimeException {

	public CoFactoryException() {
		super();
	}

	public CoFactoryException(String message) {
		super(message);
	}

	/**
	 * NOTE: This should NOT be used to wrap exceptions thrown
	 * by constructors in the (factory) subcontractor implementations.
	 * Use CoConstructorFailedFactoryException for that.
	 * @see CoConstructorFailedFactoryException
	 */
    public CoFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

	/**
	 * NOTE: This should NOT be used to wrap exceptions thrown
	 * by constructors in the (factory) subcontractor implementations.
	 * Use CoConstructorFailedFactoryException for that.
	 * @see CoConstructorFailedFactoryException
	 */
    public CoFactoryException(Throwable cause) {
        super(cause);
    }
}
