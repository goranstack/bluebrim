package com.bluebrim.text.impl.client.actions;


/**
 * Abstract superclass of all text editor actions that set text attribute values.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoAttributeAction extends CoStyledTextAction
{
	protected Object m_attribute; // the attribute
public CoAttributeAction(Object attribute, String actionName)
{
	super(actionName);
	m_attribute = attribute;
}
}
