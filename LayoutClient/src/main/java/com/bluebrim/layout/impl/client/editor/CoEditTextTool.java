package com.bluebrim.layout.impl.client.editor;

import java.awt.event.*;
import java.awt.geom.*;

import com.bluebrim.layout.impl.client.tools.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Tool that is active when text editor is active
 * 
 * @author: Dennis Malmström
 */

class CoEditTextTool extends CoAbstractTool
{
	CoPageItemAbstractTextContentView m_textView = null;
	MouseEvent m_initialEvent;
public CoEditTextTool( CoContentTool previousTool, CoLayoutEditor pageItemEditor )
{
	super( previousTool, pageItemEditor );
}
public void activate( Point2D pos )
{
	super.activate( pos );

	if
		( m_textView == null )
	{
		// start text editor
		m_textView = (CoPageItemAbstractTextContentView) ( (CoContentTool) m_previousTool ).getContentWrapperView().getContentView();
	}
	
	if
		( ! m_editor.startTextEditing( m_textView, m_initialEvent ) )
	{
		m_textView = null;
	}
}
public void deactivate( Point2D pos )
{
	super.deactivate( pos );

	// stop text editor
	if ( m_textView != null ) m_editor.stopTextEditing( m_textView );
}
public CoTool mouseMoved( MouseEvent e )
{
	if ( m_previousTool != null ) m_previousTool.mouseMoved( e ); // delegate to previous tool (CoContentTool)
	return this;
}
public CoTool mousePressed( MouseEvent e )
{
	 // if outside text view, delegate to previous tool (CoContentTool)
	if
		( m_previousTool != null )
	{
		if ( ( (CoContentTool) m_previousTool ).getContentWrapperView() == null ) return m_previousTool.mousePressed( e );
		if ( ( (CoContentTool) m_previousTool ).getContentWrapperView().getContentView() != m_textView ) return m_previousTool.mousePressed( e );
	}

	return this;
}
public void setInitialEvent( MouseEvent initialEvent )
{
	m_initialEvent = initialEvent;
}
public void setView( CoPageItemAbstractTextContentView v )
{
	m_textView = v;
}
}
