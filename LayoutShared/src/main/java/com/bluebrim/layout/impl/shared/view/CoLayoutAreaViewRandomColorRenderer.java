package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.base.shared.*;

/**
 * Renderer for CoLayoutAreaView that picks a fill color at random
 * 
 * @author: Dennis Malmström
 */

public class CoLayoutAreaViewRandomColorRenderer extends CoCompositePageItemViewRenderer
{
	static Color[] m_colors =
	{
		Color.black,
		Color.green,
		Color.yellow,
		Color.red,
		Color.blue,
		Color.cyan,
		Color.magenta,
	};





// fill view

protected void fill( CoPaintable g, CoShapePageItemView v, Rectangle bounds)
{
	
//	g.setPaint( m_colors[ (int) ( Math.random() * m_colors.length ) ] );
	g.setPaint( new Color( (float) Math.random(), (float) Math.random(), (float) Math.random() ) );
	g.fill( v.m_effectiveShape.getShape() );
}
}