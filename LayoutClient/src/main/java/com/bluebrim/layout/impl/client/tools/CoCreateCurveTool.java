package com.bluebrim.layout.impl.client.tools;

import java.awt.event.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;

/**
 * Creation tool for page items that have a curve as shape.
 *
 * @author: Dennis Malmström
 */

public class CoCreateCurveTool extends CoAbstractCreateTool
{
	private CoCubicBezierCurveShape m_curveShape; // curve being built
	
	private CoReshapeHandleIF m_handle; // handle of point being dragged

	// position of previous point
	private double m_x = Double.NaN;
	private double m_y = Double.NaN;

	private boolean m_isDragging = true;

	
	private static double m_minSampleDistance = 10; // when freehanding, don't sample points closer than this

	
	// creation rubberband tool
	private class CreateCurveRubberbandTool extends CoRubberbandTool
	{
		public CreateCurveRubberbandTool( MouseEvent e )
		{
			super( null, CoCreateCurveTool.this.m_shapePos, CoCreateCurveTool.this.m_editor );
		}
		
		public void deactivate( Point2D pos )
		{
			super.deactivate( pos );

			CoCreateCurveTool.this.finish();
		}

		protected CoTool doIt( MouseEvent releaseEvent, CoShapeIF area )
		{
			xorDraw();
			CoCreateCurveTool.this.addPoint();
			xorDraw();

			return this;
		}
		
		public String getName() {
			return CoCreateCurveTool.this.getName();
		}
	
		public CoTool mouseMoved( MouseEvent e )
		{
			m_isDragging = false;
			CoTool t = mouseDragged( e );
			m_isDragging = true;
			return t;
		}

		public CoTool mouseDragged( MouseEvent e )
		{
			if
				( m_isDragging )
			{
				// dragging = freehanding
				xorDraw();
				CoCreateCurveTool.this.samplePoint();
				xorDraw();
			}
			return super.mouseDragged( e );
		}

		public CoTool mouseClicked( MouseEvent e )
		{
			// exit on double click
			if
				( e.getClickCount() > 1 )
			{
				int i = e.getClickCount() - 1;
				while
					( i-- > 0 )
				{
					m_curveShape.removePoint( m_curveShape.getPointCount() - 1 );
				}
				return getNextTool( e.getModifiers() );
			} else {
				CoTool t = super.mouseClicked( e );
				return t;
			}
		}

		public CoTool keyPressed( KeyEvent e )
		{
			// exit on escape pressed
			if
				( e.getKeyCode() == e.VK_ESCAPE )
			{
				m_curveShape.removePoint( m_curveShape.getPointCount() - 1 );
				return getNextTool( e.getModifiers() );
			} else {
				return super.keyPressed( e );
			}
		}

		
		protected void updateDraggedShapeGeometry( Point2D pos )
		{
			m_handle.move( pos.getX() - m_handle.getX(), pos.getY() - m_handle.getY() );
			m_curveShape.smooth();
		}
		
		
		public void preparePosition( Point2D p )
		{
			CoCreateCurveTool.this.preparePosition( p );
		}
	};

private void addPoint()
{
	if
		(
			( m_x != m_handle.getX() )
		||
			( m_y != m_handle.getY() )
		)
	{
		m_x = m_handle.getX();
		m_y = m_handle.getY();
		m_curveShape.addPoint( m_x, m_y );
		m_curveShape.smooth();
	
		m_handle = m_curveShape.getLastPointHandle();
	}
}
// create and add new page item

protected void finish()
{
	// remove doubled points
	int I = m_curveShape.getPointCount();
	while
		(
			( I >= 4 )
		&&
			( m_curveShape.getPoint( I - 1 ).equals( m_curveShape.getPoint( I - 1 - 3 ) ) )
		)
	{
		m_curveShape.removePoint( I - 1 );
		I = m_curveShape.getPointCount();
	}

	CoCompositePageItemView v = m_targetTracker.getTargetView();
	Point2D p = new Point2D.Double();
	v.transformFromGlobal( p );
	m_curveShape.translateBy( p.getX(), p.getY() );
	
	m_viewPanel.getSelectionManager().unselectAllViews();

	m_viewPanel.setCursor( m_originalCursor );

	CoCompositePageItemView newParentView = m_targetTracker.getTargetView();

	CoShapePageItemIF newPi = (CoShapePageItemIF) m_prototype.clonePageItem();
	m_curveShape.setClosed( ( (CoImmutableCurveShapeIF) m_prototype.getPageItem().getCoShape() ).isClosed() );
	newPi.setCoShape( m_curveShape );
	
	m_reparentCommand.prepare( "CREATE CURVE", newPi, null, null, newParentView.getCompositePageItem(), null );
	m_editor.getCommandExecutor().doit( m_reparentCommand, null );

	m_targetTracker.setTargetView( null );
}
// start rubberbanding

public CoTool mousePressed( MouseEvent e )
{
	CoCompositePageItemView v = (CoCompositePageItemView) m_viewPanel.getRootView().findTopMostViewContaining( CoAbstractTool.getLocation( e ), null, true, false, -1 );
	if
		( ! v.validateAdd( getView() ) )
	{
		return this;
	}
		
	m_originalCursor = m_viewPanel.getCursor();

	m_mousePos = getLocation( e );
	m_viewPanel.untransform( m_mousePos );
	snap( m_mousePos );
	drawExtraCursor();
	
	CoRubberbandTool t = new CreateCurveRubberbandTool( e );

	m_curveShape = new CoCubicBezierCurveShape();

	Point2D p = getLocation( e );
	m_viewPanel.untransform( p );
	preparePosition( p );
	m_curveShape.addPoint( p.getX(), p.getY() );
	m_curveShape.addPoint( p.getX(), p.getY() );

	m_handle = m_curveShape.getLastPointHandle();
	m_x = m_handle.getX();
	m_y = m_handle.getY();
	t.setDraggedShape( m_curveShape );

	m_targetTracker.setTargetView( v );
	m_viewPanel.setCursor( m_viewPanel.getRootView().getRubberbandCreateCursor() );

	m_shapePos = null;

	return t;
}
private void preparePosition( Point2D p )
{
	CoCompositePageItemView targetview = m_targetTracker.getTargetView();
	if
		( m_editor.getSnapToGrid() )
	{
		// snap to column grid
		Point2D d = new Point2D.Double( Double.MAX_VALUE, Double.MAX_VALUE );
		Point2D P = (Point2D) p.clone();
		P = m_viewPanel.getRootView().snap( P.getX(), P.getY(), getSnapRange(), CoGeometryConstants.ALL_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, true, d );
		
		// transform m_mousePos to local koordinates
		targetview.transformFromGlobal( P );
		P = targetview.snap( P.getX(), P.getY(), getSnapRange(), CoGeometryConstants.ALL_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, true, d );

		p.setLocation( P );

		// transforme m_mousePos to global koordinates
		targetview.transformToGlobal( p );
	}

}
// add point (freehanding)

private void samplePoint()
{
	boolean doSample = false;
	
	if
		( Double.isNaN( m_x ) )
	{
		doSample = true;
	} else {
		double distQuad = Math.pow( m_handle.getX() - m_x, 2 ) + Math.pow( m_handle.getY() - m_y, 2 );
		if
			( distQuad > m_minSampleDistance * m_minSampleDistance )
		{
			doSample = true;
		}
	}

	if
		( doSample )
	{
		m_x = m_handle.getX();
		m_y = m_handle.getY();
		m_curveShape.addPoint( m_x, m_y );
		m_curveShape.smooth();
	
		m_handle = m_curveShape.getLastPointHandle();
	}
}

public CoCreateCurveTool( CoPageItemPrototypeIF prototype, CoLayoutEditor pageItemEditor )
{
	super( prototype, null, pageItemEditor );
	
//	m_closed = ( (CoImmutableCurveShapeIF) prototype.getPrototype().getCoShape() ).isClosed();
}
}