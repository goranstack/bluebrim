package com.bluebrim.postscript.impl.shared;
/**
 * Postscript Language Level exception. This exception signifies that a postscript operation was
 * tried that is not available with the current postscript language level, e.g. trying to construct something
 * that requires Level 3 on a Level 2 output.
 *
 * <p><b>Creation date:</b> 2001-08-13
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoPostscriptLanguageLevelException extends CoPostscriptException {
/**
 * CoPostscriptLanguageLevelException constructor comment.
 */
public CoPostscriptLanguageLevelException() {
	super();
}


/**
 * CoPostscriptLanguageLevelException constructor comment.
 * @param e java.lang.Exception
 */
public CoPostscriptLanguageLevelException(Exception e) {
	super(e);
}


/**
 * CoPostscriptLanguageLevelException constructor comment.
 * @param s java.lang.String
 */
public CoPostscriptLanguageLevelException(String s) {
	super(s);
}


/**
 * CoPostscriptLanguageLevelException constructor comment.
 * @param s java.lang.String
 * @param e java.lang.Exception
 */
public CoPostscriptLanguageLevelException(String s, Exception e) {
	super(s, e);
}
}