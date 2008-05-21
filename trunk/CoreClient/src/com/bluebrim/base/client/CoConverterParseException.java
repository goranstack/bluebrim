package com.bluebrim.base.client;

/**
 	Signalerar att ett fel har uppst�tt i en CoConverter vid parsning 
 	av en inmatad str�ng i ett textf�lt.
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
