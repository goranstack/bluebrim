package com.bluebrim.layout.impl.client.tools;

import java.awt.event.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.client.editor.*;

/**
 * Abstract subclass for rubberbanding tools
 * 
 * @author: Dennis Malmström
 */

public abstract class CoRubberbandTool extends CoAbstractTool
{
	Point2D m_origin;
	
	CoShapeIF m_draggedShape;
	boolean m_wasDragged;

public CoRubberbandTool( CoTool previousTool, Point2D origin, CoLayoutEditor pageItemEditor )
{
	super( previousTool, pageItemEditor );

	m_origin = origin;
}
public CoRubberbandTool( CoTool previousTool, CoLayoutEditor pageItemEditor )
{
	this( previousTool, null, pageItemEditor );
}
public void activate( Point2D pos )
{
	super.activate( pos );

	m_viewPanel.untransform( pos );

	if ( m_origin == null ) m_origin = pos;

	updateDraggedShapeGeometry( pos );

	xorDraw();
}
// hook called when mouse i released

protected abstract CoTool doIt( MouseEvent releaseEvent, CoShapeIF area );
public CoTool mouseDragged( MouseEvent e )
{
	xorDraw();

	Point2D p = getLocation( e );
	
	m_viewPanel.untransform( p );
	
	preparePosition( p );

	updateDraggedShapeGeometry( p );
	
	autoScroll( e );

	xorDraw();

	m_wasDragged = true;

	return this;
}
public CoTool mouseReleased( MouseEvent ev )
{
	if
		( ! m_wasDragged )
	{
		m_draggedShape.setSize( 0, 0 );
	}
	
	return doIt( ev, m_draggedShape );
}
// can be called by subclasses to manipulate the position (snapping, ...)

public void preparePosition( Point2D p )
{
}
void setDraggedShape( CoShapeIF shape )
{
	m_draggedShape = shape;
}
// hook for updating the dragged shape, called when mouse is dragged

protected void updateDraggedShapeGeometry( Point2D pos )
{
	double x = m_origin.getX();
	double y = m_origin.getY();
	double w = pos.getX() - x;
	double h = pos.getY() - y;

	if
		( m_draggedShape == null )
	{
		m_draggedShape = new CoRectangleShape(); // default shape is a rectangle
	}

	m_draggedShape.setTranslation( x, y );
	m_draggedShape.setSize( w, h );
}
protected void xorDraw()
{
	java.awt.Graphics2D g = getXORGraphics();

	g.draw( m_draggedShape.getShape() );
}
}
