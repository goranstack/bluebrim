package com.bluebrim.text.impl.client.actions;


/**
 * Abstract superclass of all text editor actions that set the foreground color attribute.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoColorAction extends CoAttributeAction
{

public CoColorAction( Object attribute, String name )
{
	super( attribute, name );
}
}