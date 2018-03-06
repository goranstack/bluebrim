package com.bluebrim.text.impl.client.actions;


/**
 * Abstract superclass of all text editor actions that set text attribute CoEnumValue values.
 * The values set are selected from a circular list of values.
 * Each time this action is executed, the value is changed to the "next" value according to the order of the value set.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoCircularStateAction extends CoAttributeAction
{
	protected Object[] m_states;
public CoCircularStateAction(Object attribute, String name, Object[] states)
{
	super(attribute, name);
	m_states = states;
}
}
