package com.bluebrim.text.impl.client.actions;


/**
 * Abstract superclass of all text editor actions that set text attribute integer values.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoIntegerAction extends CoNumberAction
{
public CoIntegerAction(Object attribute, String actionName)
{
	super(attribute, actionName);
}
}
