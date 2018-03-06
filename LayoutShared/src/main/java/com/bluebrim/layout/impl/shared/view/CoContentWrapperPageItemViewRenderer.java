package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.base.shared.*;

/**
 * Renderer for CoContentWrapperPageItemView
 * 
 * @author: Dennis Malmström
 */

public class CoContentWrapperPageItemViewRenderer extends CoShapePageItemViewRenderer
{
protected void paintAfterContentView( CoPaintable g, CoContentWrapperPageItemView v, Rectangle bounds)
{
	if
		( DO_PAINT_GEOMETRY || DO_PAINT_PI || DO_PAINT_PIV )
	{
		g.setColor( Color.black );
		String str = ( DO_PAINT_GEOMETRY ? "( " + v.m_X + ", " + v.m_Y + " x " + v.m_W + ", " + v.m_H + " ) " : "" ) +
		             ( DO_PAINT_PI ? v.m_pageItem.toString() : "" ) +
		             ( DO_PAINT_PIV ? v.toString() : "" );
		g.drawDecorationString( str, 5.0f, 15.0f, null );
	}
}


protected void paintBeforeContentView( CoPaintable g, CoContentWrapperPageItemView v, Rectangle bounds)
{
}


public void paintContents( CoPaintable g, CoShapePageItemView v, Rectangle bounds )
{
	CoContentWrapperPageItemView view = (CoContentWrapperPageItemView) v;
	
	paintBeforeContentView( g, view, bounds );
	paintContentView( g, view, bounds );
	paintAfterContentView( g, view, bounds );
}


public void paintContentView( CoPaintable g, CoContentWrapperPageItemView v, Rectangle bounds)
{
	if 		// Optimize away clipping if unnecessary /Magnus Ihse (magnus.ihse@appeal.se) (2001-07-03 14:03:44)
		( ! ( v.m_contentView instanceof CoPageItemNoContentView ) )
	{
		Shape prevClip = g.pushClip();

		if
			( v.m_clipShape == null )
		{
			v.m_clipShape = v.m_clipping.getShape();
		}
v.m_clipping.getShape(); //???
		g.clip( v.m_clipShape );

		v.m_contentView.paint( g, bounds );

		g.popClip( prevClip );
	} else 
	{
		g.addComment("NO CONTENT - skipping");
	}
	
}
}