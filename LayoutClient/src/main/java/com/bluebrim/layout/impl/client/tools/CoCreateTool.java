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
 * Implementation of tools that create page items by dragging a rectangle.
 * This includes all shapes except polygons and curves.
 * 
 * @author: Dennis Malmström
 */

public class CoCreateTool extends CoAbstractCreateTool
{
	// dragged shape bounds
	private double m_W;
	private double m_H;
	private double m_X;
	private double m_Y;

	private CoShapeIF m_shape;
	
	
	// creation rubberband tool
	private class CreateRubberbandTool extends CoRubberbandTool
	{
		public CreateRubberbandTool( MouseEvent e )
		{
			super( null, CoCreateTool.this.m_shapePos, CoCreateTool.this.m_editor );
		}

		protected void xorDraw()
		{
			CoCreateTool.this.xorDraw( m_draggedShape );
		}

		protected CoTool doIt( MouseEvent ev, CoShapeIF area )
		{
			return CoCreateTool.this.doit( ev, area );
		}

		public String getName() {
			return CoCreateTool.this.getName();
		}
	
		public void preparePosition( Point2D p )
		{
			CoCompositePageItemView targetview = m_targetTracker.getTargetView();
			if
				( m_editor.getSnapToGrid() )
			{
				// snap to column grid
				int edgeMask = CoGeometryConstants.RIGHT_EDGE_MASK | CoGeometryConstants.BOTTOM_EDGE_MASK;

				if
					( m_draggedShape.getWidth() < 0 )
				{
					edgeMask = edgeMask ^ CoGeometryConstants.VERTICAL_EDGE_MASK;
				}
				if
					( m_draggedShape.getHeight() < 0 )
				{
					edgeMask = edgeMask ^ CoGeometryConstants.HORIZONTAL_EDGE_MASK;
				}

				Point2D d = new Point2D.Double( Double.MAX_VALUE, Double.MAX_VALUE );
				
				Point2D P = (Point2D) p.clone();
				P = m_viewPanel.getRootView().snap( P.getX(), P.getY(), getSnapRange(), edgeMask, CoGeometryConstants.ALL_DIRECTIONS_MASK, true, d );

				// transform m_mousePos to local Coordinates
				targetview.transformFromGlobal( P );
				P = targetview.snap( P.getX(), P.getY(), getSnapRange(), edgeMask, CoGeometryConstants.ALL_DIRECTIONS_MASK, true, d );

				p.setLocation( P );
		
				// transforme m_mousePos to global koordinates
				targetview.transformToGlobal( p );
			}

		}
	};

public CoCreateTool( CoPageItemPrototypeIF prototype, CoLayoutEditor pageItemEditor )
{
	super( prototype, null, pageItemEditor );
}
private void calculateRotatedBounds( CoImmutableShapeIF area )
{
	Point2D p0 = new Point2D.Double( area.getX(), area.getY() );
	Point2D p1 = new Point2D.Double( area.getX() + area.getWidth(), area.getY() + area.getHeight() );
	m_targetTracker.getTargetView().transformFromGlobal( p0 );
	m_targetTracker.getTargetView().transformFromGlobal( p1 );
	
	double w = p1.getX() - p0.getX();
	double h = p1.getY() - p0.getY();
	
	Point2D r = new Point2D.Double( w, h );
	AffineTransform.getRotateInstance( ( ( w * h < 0 ) ? 1 : -1 ) * getView().getTransform().getRotation() ).transform( r, r );
	
	m_W = r.getX();
	m_H = r.getY();
	m_X = ( p0.getX() + p1.getX() ) / 2.0 - m_W / 2.0;
	m_Y = ( p0.getY() + p1.getY() ) / 2.0 - m_H / 2.0;
}
private CoTool doit( MouseEvent ev, CoShapeIF area )
{
	m_viewPanel.getSelectionManager().unselectAllViews();

	m_viewPanel.setCursor( m_originalCursor );

	// create new page item
	CoShapePageItemIF child = (CoShapePageItemIF) m_prototype.clonePageItem();
	{
		// set dimensions
		if
			( ( m_W != 0 ) || ( m_H != 0 ) )
		{
			child.getMutableCoShape().setSize( m_W, m_H );
		}

		// set location
		if
			( child.getLocationSpec().getFactoryKey().equals( CoNoLocationIF.NO_LOCATION ) )
		{
			child.setPosition( m_X, m_Y );
		}

		child.getMutableCoShape().normalize();
	}



				
	calculateRotatedBounds( area );

	// add page item
	CoCompositePageItemIF newParent = m_targetTracker.getTargetView().getCompositePageItem();
	m_reparentCommand.prepare( "CREATE PAGE ITEM", child, null, null, newParent, null );
	m_editor.getCommandExecutor().doit( m_reparentCommand, null );

	m_targetTracker.setTargetView( null );

	return getNextTool( ev.getModifiers() );
}
public CoTool mousePressed( MouseEvent e )
{
	CoCompositePageItemView v = (CoCompositePageItemView) m_viewPanel.getRootView().findTopMostViewContaining( CoAbstractTool.getLocation( e ), null, true, false, -1 );
	if
		( ! v.validateAdd( getView() ) )
	{
		return this; // parent not valid, ignore event
	}
		
	m_originalCursor = m_viewPanel.getCursor();

	m_mousePos = getLocation( e );
	m_viewPanel.untransform( m_mousePos );
	snap( m_mousePos );
	drawExtraCursor();

	// prepare and activate rubberbanding tool
	CoRubberbandTool t = new CreateRubberbandTool( e );

	m_shape = getView().getCoShape().deepClone();
	t.setDraggedShape( m_shape.deepClone() );

	m_targetTracker.setTargetView( v );

	m_viewPanel.setCursor( m_viewPanel.getRootView().getRubberbandCreateCursor() );

	m_shapePos = null;
	
	return t;
}
protected void xorDraw( CoImmutableShapeIF area )
{
	java.awt.Graphics2D g = getXORGraphics();

	AffineTransform t = g.getTransform();

	calculateRotatedBounds( area );

	g.transform( m_targetTracker.getTargetView().getAffineTransform() );
	g.rotate( getView().getTransform().getRotation(), m_X + m_W / 2, m_Y + m_H / 2 );

	m_shape.setTranslation( m_X, m_Y );
	m_shape.setSize( m_W, m_H );
	
	g.draw( m_shape.getShape() );
	
	g.setTransform( t );
}
}