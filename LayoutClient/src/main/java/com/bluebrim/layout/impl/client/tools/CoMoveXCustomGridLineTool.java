package com.bluebrim.layout.impl.client.tools;

import java.awt.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Tool for translating horizontal custom gridlines
 * 
 * @author: Dennis Malmström
 */

public class CoMoveXCustomGridLineTool extends CoMoveCustomGridLineTool
{

protected Line2D createLine( double pos, double span )
{
	return new Line2D.Double( pos, 0, pos, span );
}
protected void doit()
{
	final double pos = m_line.getX1();

	if
		( pos != m_pos0 )
	{
		CoPageItemCommands.MOVE_CUSTOM_GRID_LINE.prepare( m_view.getMutableCustomGrid(), m_pos0, pos, Double.NaN, Double.NaN );
		m_editor.getCommandExecutor().doit( CoPageItemCommands.MOVE_CUSTOM_GRID_LINE, null );
	}

}
protected double getPos(java.awt.geom.Point2D p)
{
	return p.getX();
}
protected void updateLine( double pos )
{
	m_line.setLine( pos, 0, pos, m_line.getY2() );
}
protected void xorDrawText( Graphics2D g )
{
	double pos = m_line.getX1();
	if ( pos <= 0 ) return;
	if ( pos >= m_view.getWidth() ) return;
	
	float x = 10 + (float) m_pos.getX();
	float y = (float) m_pos.getY() - g.getFont().getSize();
	
	g.translate( x, y );

//	float x = (float) m_line.getX1();
	double s = 1 / com.bluebrim.base.shared.CoBaseUtilities.getXScale( g.getTransform() );
	g.scale ( s, s );

//	g.drawString( Double.toString( m_line.getX1() ), 0, 0 );//, x, g.getFont().getSize() );
	g.drawString( CoLengthUnitSet.format( pos, CoLengthUnit.LENGTH_UNITS ), 0, 0 );

	g.translate( -x, -y );

}

public CoMoveXCustomGridLineTool( CoTool previousTool, CoLayoutEditor pageItemEditor, CoShapePageItemView v, double pos, double span )
{
	super( previousTool, pageItemEditor, v, pos, span );
}
}