package com.bluebrim.text.impl.shared;

import java.awt.geom.*;

import com.bluebrim.text.shared.*;

/**
 * Tab stop leader renderer for tab stops with line leader.
 * 
 * @author: Dennis Malmström
 */

public class CoTabLineLeaderRenderer extends CoAbstractTabLeaderRenderer
{
	private Rectangle2D m_line = new Rectangle2D.Double();
	private double m_thickness;
public CoTabLineLeaderRenderer( double thickness )
{
	m_thickness = thickness;
}


public void paint( com.bluebrim.base.shared.CoPaintable g, CoGlyphVector gv, float y, float x0, float x1, float tracking )
{
	
	double w = m_thickness * gv.getFont().getLineMetrics().getUnderlineThickness();
	y += gv.getFont().getLineMetrics().getUnderlineOffset();
	
	m_line.setRect( x0, y - w / 2, x1 - x0, w );

	g.fill( m_line );
}
}