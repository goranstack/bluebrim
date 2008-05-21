package com.bluebrim.base.shared.debug;


/**
 * Handles diff reports from <code>CoDiff</code>.
 * <p>
 * Creation date: (2001-03-06 10:44:40)
 *
 * @see CoDiff CoDiff
 *
 * @author Dennis
 */

public interface CoDiffReport
{

/**
@param prefix is a <code>String</code> containing only whitespace.
It is used for indentation.
*/
void comparing( String prefix, String attributeName, CoDiffable diffable1, CoDiffable diffable2 );

/**
@param prefix is a <code>String</code> containing only whitespace.
It is used for indentation.
*/
void differentArrayLengths( String prefix, String attributeName, int length1, int length2 );

/**
@param prefix is a <code>String</code> containing only whitespace.
It is used for indentation.
*/
void differentCollectionLengths( String prefix, String attributeName, int length1, int length2 );

void diffFailed( Exception ex );

/**
@param prefix is a <code>String</code> containing only whitespace.
It is used for indentation.

@param index says what part of the values that differ.
A value of -1 indicates that the index has no meaning for this attribute.
*/
void notEqual( String prefix, String attributeName, int index, Object value1, Object value2 );


/**
@param prefix is a <code>String</code> containing only whitespace.
It is used for indentation.

@param index says what part of the values that differ.
A value of -1 indicates that the index has no meaning for this attribute.
*/
void notIdentical( String prefix, String attributeName, int index, Object value1, Object value2 );

/**
@param prefix is a <code>String</code> containing only whitespace.
It is used for indentation.

@param index says what part of the values that differ.
A value of -1 indicates that the index has no meaning for this attribute.
*/
void typeClash( String prefix, String attributeName, int index, Object value1, Object value2 );

/**
This method does not take a parameter with an attribute name because
it will be called right after a call to the <code>comparing()</code> method.

@param prefix is a <code>String</code> containing only whitespace.
It is used for indentation.
*/
void typeClash( String prefix, CoDiffable diffable1, CoDiffable diffable2 );
}