package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

/**
 * A renderer that only fills with a fixed color.
 * Used for painting overviews of page items.
 * 
 * @author: Dennis Malmström
 */

public class CoStopAtFirstProjectorPageItemViewRenderer extends CoShapePageItemViewRenderer
{


	public static Color FILL_COLOR = Color.green;

protected void paintContents( com.bluebrim.base.shared.CoPaintable g, CoShapePageItemView v, java.awt.Rectangle bounds )
{
	g.setPaint( FILL_COLOR );
	g.fill( v.m_effectiveShape.getShape() );
}
}