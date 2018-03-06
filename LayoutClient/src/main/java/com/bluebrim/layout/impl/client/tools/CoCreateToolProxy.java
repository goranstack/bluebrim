package com.bluebrim.layout.impl.client.tools;

import java.awt.event.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.shared.*;

/**
 * 
 * @author: Dennis
 */
 
public class CoCreateToolProxy extends CoAbstractCreateTool
{
	private final int UNKNOWN = 0;
	private final int POLYGON = 1;
	private final int CURVE = 2;
	private final int OTHER = 3;
	
	private int m_shapeType = UNKNOWN;
	
	private CoAbstractCreateTool m_delegate;
		
public CoCreateToolProxy( CoPageItemPrototypeIF prototype, CoLayoutEditor pageItemEditor )
{
	super( prototype, null, pageItemEditor );
}
public void activate( Point2D pos )
{
	updateDelegate();
	getDelegate().activate( pos );
}
public void deactivate( Point2D pos )
{
	getDelegate().deactivate( pos );
}
protected CoAbstractCreateTool getDelegate()
{
	if ( m_delegate == null ) updateDelegate();
	return m_delegate;
}
public CoTool keyPressed( KeyEvent e )
{
	return getDelegate().keyPressed( e );
}
public CoTool keyReleased( KeyEvent e )
{
	return getDelegate().keyReleased( e );
}
public CoTool keyTyped( KeyEvent e )
{
	return getDelegate().keyTyped( e );
}
public CoTool mouseClicked( MouseEvent e )
{
	return getDelegate().mouseClicked( e );
}
public CoTool mouseDragged(java.awt.event.MouseEvent e)
{
	return getDelegate().mouseDragged( e );
}
public CoTool mouseEntered(java.awt.event.MouseEvent e)
{
	return getDelegate().mouseEntered( e );
}
public CoTool mouseExited(java.awt.event.MouseEvent e)
{
	return getDelegate().mouseExited( e );
}
public CoTool mouseMoved( MouseEvent e )
{
	return getDelegate().mouseMoved( e );
}
public CoTool mousePressed(java.awt.event.MouseEvent e)
{
	return getDelegate().mousePressed( e );
}
public CoTool mouseReleased(java.awt.event.MouseEvent e)
{
	return getDelegate().mouseReleased( e );
}
private void updateDelegate()
{
	CoImmutableShapeIF shape = m_prototype.getPageItem().getCoShape();

	if
		( shape instanceof CoPolygonShapeIF )
	{
		if
			( m_shapeType != POLYGON )
		{
			m_shapeType = POLYGON;
			m_delegate = new CoCreatePolygonTool( m_prototype, m_editor );
		}
	} else if
		( shape instanceof CoCubicBezierCurveShapeIF )
	{
		if
			( m_shapeType != CURVE )
		{
			m_shapeType = CURVE;
			m_delegate = new CoCreateCurveTool( m_prototype, m_editor );
		}
	} else
	{
		if
			( m_shapeType != OTHER )
		{
			m_shapeType = OTHER;
			m_delegate = new CoCreateTool( m_prototype, m_editor );
		}
	}
}
}