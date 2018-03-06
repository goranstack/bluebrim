package com.bluebrim.base.shared;

/**
 * Execption that could be thrown by classes implementing <code>CoValueable</code> 
 * when <code>setValue</code> fails, for example when a transaction fails to commit.
 * Ought to be a checked exception but this would mean that every code segment 
 * calling <code>setValue</code> would have to be rewritten.
 * Creation date: (2000-01-03 15:06:45)
 * @author: Lasse Svadängs
 */
public class CoSetValueException extends RuntimeException {
	private boolean m_failureHasBeenReported;
/**
 * CoTransactionFailureException constructor comment.
 */
public CoSetValueException() {
	this(false);
}
/**
 * CoTransactionFailureException constructor comment.
 * @param s java.lang.String
 */
public CoSetValueException(String s) {
	this(s, false);
}
/**
 * CoTransactionFailureException constructor comment.
 * @param s java.lang.String
 */
public CoSetValueException(String s,boolean failureWasReported) {
	super(s);
	m_failureHasBeenReported = failureWasReported;
}
/**
 * CoTransactionFailureException constructor comment.
 */
public CoSetValueException(boolean failureWasReported) {
	super();
	m_failureHasBeenReported = failureWasReported;
}
public boolean failureHasBeenReported()
{
	return m_failureHasBeenReported;
}
}
