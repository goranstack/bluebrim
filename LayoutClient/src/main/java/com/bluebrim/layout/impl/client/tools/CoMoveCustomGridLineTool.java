package com.bluebrim.layout.impl.client.tools;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Tool for translating custom gridlines
 * 
 * @author: Dennis Malmström
 */

public abstract class CoMoveCustomGridLineTool extends CoAbstractTool
{
	protected double m_pos0;
	protected Line2D m_line;
	protected Point2D m_pos;

public void activate( Point2D pos )
{
	super.activate( pos );

	m_pos = pos;
	m_viewPanel.untransform( m_pos );
	m_view.transformFromGlobal( m_pos );

	// xor-draw rubberband shape
	xorDraw();
}
protected abstract Line2D createLine( double pos, double span );
protected abstract void doit();
protected abstract double getPos( Point2D p );
public CoTool mouseDragged(MouseEvent e)
{
	// xor-draw rubberband shape
	xorDraw();

	// mouse position (unzoomed)
	m_pos = getLocation( e );
	m_viewPanel.untransform( m_pos );
	m_view.transformFromGlobal( m_pos );

	autoScroll( e );

	// apply movement		
	double p = getPos( m_pos );
	updateLine( p );
	
	// xor-draw rubberband shape
	xorDraw();
	
	return this;
}
public CoTool mouseReleased( MouseEvent e )
{
	doit();

	m_viewPanel.repaint();
	
	return m_previousTool;
}
protected abstract void updateLine( double pos );
private void xorDraw()
{
	Graphics2D g = getXORGraphics();
	
	AffineTransform t = g.getTransform();
	
	g.transform( m_view.getAffineTransform() );
	
	g.draw( m_line );
	
	xorDrawText( g );

	g.setTransform( t );
}
protected abstract void xorDrawText( Graphics2D g );

	protected CoShapePageItemView m_view;

public CoMoveCustomGridLineTool( CoTool previousTool, CoLayoutEditor pageItemEditor, CoShapePageItemView v, double pos, double span )
{
	super( previousTool, pageItemEditor );

	m_view = v;
	
	m_pos0 = pos;
	m_line = createLine( pos, Math.min( span, 1e5 ) ); //  Proper span (CoPageItemIF.INFINITE_DIMENSION) won't draw properly
}
}