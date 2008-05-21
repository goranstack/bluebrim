package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.base.shared.*;

/**
 * Renderer for CoLayoutAreaView
 * 
 * @author: Dennis Malmström
 */

public class CoLayoutAreaViewRenderer extends CoCompositePageItemViewRenderer
{


public void paintBeforeChildren(CoPaintable g, CoCompositePageItemView piv, Rectangle bounds)
{
	CoLayoutAreaView v = (CoLayoutAreaView) piv;
	
	// paint empty and projecting layout areas red, why ???
	if
		( v.hasWorkPiece() && ! v.hasChildren() )
	{
		g.setColor( Color.red );
		g.fill( v.m_effectiveShape.getShape() );
	} else {
		super.paintBeforeChildren( g, v, bounds );
	}
}
}