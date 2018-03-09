package com.bluebrim.text.impl.client.actions;


/**
 * Abstract superclass of all text editor actions that set text attribute string values.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoStringAction extends CoAttributeAction
{
public CoStringAction(Object attribute, String actionName)
{
	super(attribute, actionName);
}
}
