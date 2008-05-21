package com.bluebrim.text.impl.client.actions;

/**
 * Subclass of CoCircularStateParagraphAction with a predefined value set of [ Boolean.TRUE, Boolean.FALSE, null ]
 * 
 * @author: Dennis Malmström
 */

public class CoTriStateBooleanParagraphAction extends CoCircularStateParagraphAction
{
public CoTriStateBooleanParagraphAction( Object attribute ) {
	this( attribute, "Co-3state-boolean-" + attribute );
}
public CoTriStateBooleanParagraphAction( Object attribute, String name ) {
	super( attribute, name, new Boolean[] { Boolean.TRUE,  Boolean.FALSE,  null } );
}
}
