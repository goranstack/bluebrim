package com.bluebrim.text.impl.client.actions;


/**
 * Abstract superclass of all text editor actions that set text attribute CoEnumValue values.
 * 
 * @author: Dennis Malmström
 */

 public abstract class CoEnumAction extends CoAttributeAction
{
public CoEnumAction(Object attribute, String actionName)
{
	super(attribute, actionName);
}
}
