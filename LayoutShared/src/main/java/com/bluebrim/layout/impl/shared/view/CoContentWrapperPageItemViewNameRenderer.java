package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.base.shared.*;

/**
 * Renderer for CoContentWrapperPageItemView that also paints the page item name
 * 
 * @author: Dennis Malmström
 */

public class CoContentWrapperPageItemViewNameRenderer extends CoContentWrapperPageItemViewRenderer
{
protected void paintAfterContentView( CoPaintable g, CoContentWrapperPageItemView v, Rectangle bounds)
{
	super.paintAfterContentView( g, v, bounds );
	
	g.setColor( Color.black );
	g.drawDecorationString( v.getName(), 0.0f, (float) ( -2 / v.getScreenScale() ), 14 );
}
}