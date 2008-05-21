package com.bluebrim.xml.impl.shared;

/**
 *	A runtime exception generated by an xml visitor when an
 *  exception is thrown during a read. It is subclassed from
 *  RuntimeException to avoid having to have throws clauses
 *  in every xmlVisit() method everywhere
 *  
 */
public class CoXmlRuntimeException extends RuntimeException {
/**
 * com.bluebrim.xml.shared.CoXmlGenerationException constructor comment.
 */
public CoXmlRuntimeException() {
	super();
}
/**
 * com.bluebrim.xml.shared.CoXmlGenerationException constructor comment.
 * @param s java.lang.String
 */
public CoXmlRuntimeException(String s) {
	super(s);
}
}