package com.bluebrim.layout.impl.client.tools;

import java.awt.event.*;
import java.awt.geom.*;

import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Tool for translating an image within a its view
 * 
 * @author: Dennis Malmström
 */

public class CoMoveBoundedContentTool extends CoAbstractTool
{
	protected Point2D m_mousePos;

	protected CoPageItemBoundedContentView m_view;
public CoMoveBoundedContentTool( CoTool previousTool, CoLayoutEditor pageItemEditor, CoPageItemBoundedContentView view )
{
	super( previousTool, pageItemEditor );

	m_view = view;
}
public void activate( Point2D pos )
{
	super.activate( pos );

	m_mousePos = pos;
}
public CoTool mouseDragged(MouseEvent e)
{
	// move image in view
	
	Point2D p = getLocation( e );
	double dx = p.getX() - m_mousePos.getX();
	double dy = p.getY() - m_mousePos.getY();
	m_mousePos = p;

	p = new Point2D.Double( dx, dy );

	m_viewPanel.untransform( p );
	m_view.translateContent( p.getX(), p.getY() );

	m_viewPanel.repaint();
	
	return this;
}
public CoTool mouseReleased( MouseEvent e )
{
	CoPageItemCommands.SET_BOUNDED_CONTENT_POSITION.prepare( m_view.getOwner(), m_view.getX(), m_view.getY() );
	m_editor.getCommandExecutor().doit( CoPageItemCommands.SET_BOUNDED_CONTENT_POSITION, null );

	return m_previousTool;
}
}