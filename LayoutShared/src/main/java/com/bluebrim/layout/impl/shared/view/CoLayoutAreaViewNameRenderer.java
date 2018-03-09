package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.base.shared.*;

/**
 * Renderer for CoLayoutAreaView that also paints the page item name
 * 
 * @author: Dennis Malmström
 */

public class CoLayoutAreaViewNameRenderer extends CoCompositePageItemViewRenderer
{


protected void paintAfterChildren( CoPaintable g, CoCompositePageItemView v, Rectangle bounds)
{
	super.paintAfterChildren( g, v, bounds );

	g.setColor( Color.black );
	g.drawDecorationString( v.getName(), 0.0f, (float) ( -2 / v.getScreenScale() ), 14 );
}
}