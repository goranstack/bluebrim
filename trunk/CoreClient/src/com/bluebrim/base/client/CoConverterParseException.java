package com.bluebrim.base.client;

/**
 	Signalerar att ett fel har uppstått i en CoConverter vid parsning 
 	av en inmatad sträng i ett textfält.
 */
public class CoConverterParseException extends Exception {
/**
 * CoConverterParseException constructor comment.
 */
public CoConverterParseException() {
	super();
}
/**
 * CoConverterParseException constructor comment.
 * @param s java.lang.String
 */
public CoConverterParseException(String s) {
	super(s);
}
}
