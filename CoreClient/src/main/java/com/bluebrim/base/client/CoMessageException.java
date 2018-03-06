package com.bluebrim.base.client;

/**
 	Exceptionklass som skickas av CoMessage och bakar
 	i hop de tre exceptiontyper som dess metoder fångar.
 */
public class CoMessageException extends Exception {
/**
 * CoMessageException constructor comment.
 */
public CoMessageException() {
	super();
}
/**
 * CoMessageException constructor comment.
 * @param s java.lang.String
 */
public CoMessageException(String s) {
	super(s);
}
}
