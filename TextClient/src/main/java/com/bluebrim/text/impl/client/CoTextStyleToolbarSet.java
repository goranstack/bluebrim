package com.bluebrim.text.impl.client;

import javax.swing.*;

import com.bluebrim.gui.client.*;

/**
 * 
 */

public class CoTextStyleToolbarSet
{
	public CoTagToolBar m_tagToolBar;
	public CoFontToolBar m_fontToolBar;
	public CoStyleToolBar m_styleToolBar;

public CoTextStyleToolbarSet( Action[] actions )
{
	this( actions, null );
}
public CoTextStyleToolbarSet( Action[] actions, CoAbstractTextEditor editor )
{
	m_tagToolBar = new CoTagToolBar( actions, editor );
	m_fontToolBar = new CoFontToolBar( actions, editor );
	m_styleToolBar = new CoStyleToolBar( actions, editor );
}
public CoTextStyleToolbarSet( CoUserInterfaceBuilder b, Action[] actions )
{
	this( actions );
	init( b );
}
public CoTextStyleToolbarSet( CoUserInterfaceBuilder b, Action[] actions, CoAbstractTextEditor editor )
{
	this( actions, editor );
	init( b );
}
private final void init( CoUserInterfaceBuilder b )
{
	b.prepareToolbar( m_tagToolBar );
	b.prepareToolbar( m_fontToolBar );
	b.prepareToolbar( m_styleToolBar );
}
public void setEditor( CoAbstractTextEditor editor )
{
	m_tagToolBar.setEditor( editor );
	m_fontToolBar.setEditor( editor );
	m_styleToolBar.setEditor( editor );
}
}
