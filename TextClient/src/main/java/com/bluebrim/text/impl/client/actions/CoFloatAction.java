package com.bluebrim.text.impl.client.actions;


/**
 * Abstract superclass of all text editor actions that set text attribute float values.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoFloatAction extends CoNumberAction
{
public CoFloatAction( Object attribute, String actionName )
{
	super( attribute, actionName );
}
}
