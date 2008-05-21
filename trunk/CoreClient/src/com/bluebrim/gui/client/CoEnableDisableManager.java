package com.bluebrim.gui.client;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;

public abstract class CoEnableDisableManager
{
	protected List m_valueables = new ArrayList();
	protected List m_components = new ArrayList();
	protected List m_actions = new ArrayList();
public void add( Component c )
{
	m_components.add( c );
}
public void add( Action a )
{
	m_actions.add( a );
}
public void add( CoValueable v )
{
	m_valueables.add( v );
}
protected abstract boolean isEnabled();
public void remove( Component c )
{
	m_components.remove( c );
}
public void remove( Action a )
{
	m_actions.remove( a );
}
public void remove( CoValueable v )
{
	m_valueables.remove( v );
}
public void update()
{
	boolean b = isEnabled();

	Iterator e = m_valueables.iterator();
	while
		( e.hasNext() )
	{
		( (CoValueable) e.next() ).setEnabled( b );
	}
	
	e = m_components.iterator();
	while
		( e.hasNext() )
	{
		( (Component) e.next() ).setEnabled( b );
	}
	
	e = m_actions.iterator();
	while
		( e.hasNext() )
	{
		( (Action) e.next() ).setEnabled( b );
	}
}
}
