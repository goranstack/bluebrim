package com.bluebrim.base.shared.debug;

/**
 * Implementation of CoDiffReport that does nothing
 * <p>
 * Creation date: (2001-03-06 10:44:40)
 * @author: Dennis
 */

public class CoNullDiffReport implements CoDiffReport
{
/**
 * CoNullDiffReport constructor comment.
 */
public CoNullDiffReport() {
	super();
}
/**
 * comparing method comment.
 */
public void comparing(String prefix, String attributeName, CoDiffable diffable1, CoDiffable diffable2) {}
/**
 * differentArrayLengths method comment.
 */
public void differentArrayLengths(String prefix, String attributeName, int length1, int length2) {}
/**
 * differentCollectionLengths method comment.
 */
public void differentCollectionLengths(String prefix, String attributeName, int length1, int length2) {}
/**
 * diffFailed method comment.
 */
public void diffFailed(java.lang.Exception ex) {}
/**
 * notEqual method comment.
 */
public void notEqual(String prefix, String attributeName, int index, Object value1, Object value2) {}
/**
 * notIdentical method comment.
 */
public void notIdentical(String prefix, String attributeName, int index, Object value1, Object value2) {}
/**
 * typeClash method comment.
 */
public void typeClash(String prefix, String attributeName, int index, Object value1, Object value2) {}
/**
 * typeClash method comment.
 */
public void typeClash(String prefix, CoDiffable diffable1, CoDiffable diffable2) {}
}