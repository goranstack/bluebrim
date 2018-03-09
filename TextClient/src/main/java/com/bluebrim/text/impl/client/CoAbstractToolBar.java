package com.bluebrim.text.impl.client;

import java.util.*;

import javax.swing.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.resource.shared.*;
import com.bluebrim.text.impl.client.actions.*;


public abstract class CoAbstractToolBar extends CoToolbar implements CoAttributeListenerIF
{
	protected CoAbstractTextEditor m_editor;
	protected Map m_actions;



public CoAbstractToolBar(Action[] actions)
{
	super();

	m_actions = CoActionUtilities.actionsToHashtable(actions);
}
protected Action getAction( String cmd )
{
	return (Action) m_actions.get (cmd );
}
protected static Icon getIcon( Class c, String image )
{
	return CoResourceLoader.loadIcon( c, CoTextStringResources.getName( image ) );
}
protected String getResourceString( String key )
{
	return CoTextStringResources.getName( key );
}
public void setEditor( CoAbstractTextEditor editor )
{
	if ( m_editor == editor ) return;
	
	if
		( m_editor != null )
	{
	  m_editor.removeAttributeListener( this );
	}
	
	m_editor = editor;
	
	if
		( m_editor != null )
	{
	  m_editor.addAttributeListener( this );
	}
}
}
