package com.bluebrim.text.impl.client.actions;

/**
 * Subclass of CoCircularStateCharacterAction with a predefined value set of [ Boolean.TRUE, Boolean.FALSE, null ]
 * 
 * @author: Dennis Malmström
 */

public class CoTriStateBooleanCharacterAction extends CoCircularStateCharacterAction
{
public CoTriStateBooleanCharacterAction( Object attribute ) {
	this( attribute, "Co-3state-boolean-" + attribute );
}
public CoTriStateBooleanCharacterAction( Object attribute, String name ) {
	super( attribute, name, new Boolean[] { Boolean.TRUE,  Boolean.FALSE,  null } );
}
}
