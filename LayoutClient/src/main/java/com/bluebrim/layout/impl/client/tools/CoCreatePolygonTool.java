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
 * Creation tool for page items that have a polygon as shape.
 *
 * @author: Dennis Malmström
 */

public class CoCreatePolygonTool extends CoAbstractCreateTool
{
	private CoCurveShape m_curveShape; // polygon being built

	// auxillary shapes (closing line, line being sketched)
	private CoLine m_lineShape;
	private CoLine m_lineShape2;


	// creation rubberband tool
	private class CreateCurveRubberbandTool extends CoRubberbandTool
	{
		public CreateCurveRubberbandTool( MouseEvent e )
		{
			super( null, CoCreatePolygonTool.this.m_shapePos, CoCreatePolygonTool.this.m_editor );
		}
		
		public void deactivate( Point2D pos )
		{
			super.deactivate( pos );

			CoCreatePolygonTool.this.finish();
		}

		protected CoTool doIt( MouseEvent releaseEvent, CoShapeIF area )
		{
			area.normalize();
			
			if
				(
					( m_lineShape.getX2() != m_lineShape.getX1() )
				||
					( m_lineShape.getY2() != m_lineShape.getY1() )
				)
			{
				CoCreatePolygonTool.this.addPoint();
				m_lineShape.setX1( m_lineShape.getX2() );
				m_lineShape.setY1( m_lineShape.getY2() );
			}
			return this;
		}

		public String getName() {
			return CoCreatePolygonTool.this.getName();
		}
	
		public CoTool mouseMoved( MouseEvent e )
		{
			return mouseDragged( e );
		}
		
		public CoTool mouseDragged( MouseEvent e )
		{
			if ( m_lineShape2 != null ) getXORGraphics().draw( m_lineShape2.getShape() );
			CoTool t = super.mouseDragged( e );
			if ( m_lineShape2 != null ) getXORGraphics().draw( m_lineShape2.getShape() );
			return t;
		}

		// exit on double click
		public CoTool mouseClicked( MouseEvent e )
		{
			if
				( e.getClickCount() > 1 )
			{
				return getNextTool( e.getModifiers() );
			} else {
				return super.mouseClicked( e );
			}
		}

		// exit when escape pressed
		public CoTool keyPressed( KeyEvent e )
		{
			if
				( e.getKeyCode() == e.VK_ESCAPE )
			{
				return getNextTool( e.getModifiers() );
			} else {
				return super.keyPressed( e );
			}
		}

		
		protected void updateDraggedShapeGeometry( Point2D pos )
		{
			m_lineShape.setX2( pos.getX() );
			m_lineShape.setY2( pos.getY() );
			if
				( m_lineShape2 != null )
			{
				m_lineShape2.setX2( pos.getX() );
				m_lineShape2.setY2( pos.getY() );
			}
		}
		
		
		public void preparePosition( Point2D p )
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
	};


private void addPoint()
{
	Point2D p = new Point2D.Double( m_lineShape.getX2(), m_lineShape.getY2() );
	m_targetTracker.getTargetView().transformFromGlobal( p );
	m_curveShape.addPoint( p.getX(), p.getY() );
}
// create and add new page item

private void finish()
{
	m_viewPanel.getSelectionManager().unselectAllViews();

	m_viewPanel.setCursor( m_originalCursor );

	CoCompositePageItemView newParentView = m_targetTracker.getTargetView();

	CoShapePageItemIF newPi = (CoShapePageItemIF) m_prototype.clonePageItem();
	newPi.setCoShape( m_curveShape );
	
	m_reparentCommand.prepare( "CREATE POLYGON", newPi, null, null, newParentView.getCompositePageItem(), null );
	m_editor.getCommandExecutor().doit( m_reparentCommand, null );

	m_targetTracker.setTargetView( null );
}
public CoTool mouseDragged( MouseEvent e )
{
	return mouseMoved( e );
}
// start rubberbanding

public CoTool mouseReleased( MouseEvent e )
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

	m_curveShape = new CoPolygonShape();
	
	m_curveShape.setClosed( ( (CoImmutableCurveShapeIF) m_prototype.getPageItem().getCoShape() ).isClosed() );
	
	v.transformFromGlobal( m_mousePos );
	m_curveShape.addPoint( m_mousePos.getX(), m_mousePos.getY() );
	v.transformToGlobal( m_mousePos );
	
	m_lineShape = new CoLine( m_mousePos.getX(), m_mousePos.getY(), m_mousePos.getX(), m_mousePos.getY() );
	if ( m_curveShape.isClosed() ) m_lineShape2 = new CoLine( m_mousePos.getX(), m_mousePos.getY(), m_mousePos.getX(), m_mousePos.getY() );
	t.setDraggedShape( m_lineShape );

	m_targetTracker.setTargetView( v );
	m_viewPanel.setCursor( m_viewPanel.getRootView().getRubberbandCreateCursor() );

	m_shapePos = null;

	return t;
}

public CoCreatePolygonTool( CoPageItemPrototypeIF prototype, CoLayoutEditor pageItemEditor )
{
	super( prototype, null, pageItemEditor );
}
}