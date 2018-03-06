package com.bluebrim.layout.impl.client.tools;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Tool for rotating page items
 * 
 * @author: Dennis Malmström
 */

public class CoRubberbandRotationTool extends CoRubberbandTool
{
	private CoShapePageItemView m_view;
	
	private CoLine m_lineShape = new CoLine(); // rotation handle shape
	
	private Shape m_draggedShape; // rotated shape
	private double m_r0; // original tool rotation angle
	
	private double m_R0; // original page item rotation angle
	private double m_R; // dragged page item rotation angle

	// rotation point
	private double m_rx;
	private double m_ry;
public CoRubberbandRotationTool( CoLayoutEditor pageItemEditor, CoRotationTool rt, CoShapePageItemView view, MouseEvent ev )
{
	super( rt, pageItemEditor );

	Point2D pos = getLocation( ev );

	m_view = view;

	Point2D p0 = new Point2D.Double( 0, 0 );
	Point2D p1 = new Point2D.Double( m_view.getWidth(), m_view.getHeight() );
	m_view.transformToGlobal( p0 );
	m_view.transformToGlobal( p1 );

	// rotation point = center of shapes bounds
	m_rx = ( p0.getX() + p1.getX() ) / 2;
	m_ry = ( p0.getY() + p1.getY() ) / 2;
	
	m_lineShape.setGeometry( m_rx, m_ry, pos.getX(), pos.getY() );
	m_r0 = Double.NaN;
	m_R0 = m_view.getTransform().getRotation();
	
	m_draggedShape = m_view.getAffineTransform().createTransformedShape( m_view.getCoShape().getShape() );

	setDraggedShape( m_lineShape );



}
private double calculateAngle()
{
	double dx = m_lineShape.getX1() - m_lineShape.getX2();
	double dy = m_lineShape.getY1() - m_lineShape.getY2();
	double r = - Math.atan( dx / dy );
	if ( dy < 0 ) r = Math.PI + r;
	if ( r < 0 ) r = 2 * Math.PI + r;
	return r;
}
protected CoTool doIt( MouseEvent ev, CoShapeIF area )
{
	CoPageItemCommands.SET_ROTATION.prepare( m_view, m_R );
	m_editor.getCommandExecutor().doit( CoPageItemCommands.SET_ROTATION, m_view.getPageItem() );
	
	return m_previousTool;
}
protected void updateDraggedShapeGeometry( Point2D pos )
{
	m_lineShape.setX2( pos.getX() );
	m_lineShape.setY2( pos.getY() );

	if
		( Double.isNaN( m_r0 ) )
	{
		m_r0 = calculateAngle();
	}

	
	double dr = calculateAngle() - m_r0;
	m_R = m_R0 + dr;

	if
		( m_editor.getSnapToGrid() )
	{
		double d = Math.round( m_R * 180.0 / Math.PI );
		m_R = d * Math.PI / 180.0;
	}

	while
		( m_R < 0 )
	{
		m_R += 2 * Math.PI;
	}
	while
		( m_R >= 2 * Math.PI )
	{
		m_R -= 2 * Math.PI;
	}
}
protected void xorDraw()
{
	super.xorDraw();
	
	java.awt.Graphics2D g = getXORGraphics();

	g.rotate( m_R - m_R0, m_rx, m_ry );
	g.draw( m_draggedShape );
	g.rotate( - ( m_R - m_R0 ), m_rx, m_ry );
	g.drawString( "" + Math.round( m_R * 180 / Math.PI ), 10 + (float) m_lineShape.getX2(), (float) m_lineShape.getY2() );
}
}