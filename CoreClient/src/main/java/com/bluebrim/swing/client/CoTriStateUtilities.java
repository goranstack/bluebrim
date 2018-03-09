package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public final class CoTriStateUtilities
{
private CoTriStateUtilities() {}
public static final void drawBorder( Graphics g, int x, int y, int width, int height, Color dark, Color bright )
{
	g.translate(x, y);

	g.setColor( dark );
	g.drawLine( 0, 0, 0,         height - 1 );
	g.drawLine( 1, 0, width - 1, 0 );

	g.setColor( dark.darker() );
	g.drawLine( 1, 1, 1,         height - 2 );
	g.drawLine( 2, 1, width - 2, 1 );

	g.setColor( bright );
	g.drawLine( 1,         height - 1, width - 1, height - 1 );
	g.drawLine( width - 1, 1,          width - 1, height - 2 );

	g.setColor( bright.brighter() );
	g.drawLine( 2,         height - 2, width - 2, height - 2 );
	g.drawLine( width - 2, 2,          width - 2, height - 3 );

	g.translate( -x, -y );
}
public static final void drawBorder( Graphics g, Rectangle r, Color dark, Color bright )
{
	drawBorder( g, r.x, r.y, r.width, r.height, dark, bright );
}
public static final void drawEtchedBorder( Graphics g, int x, int y, int width, int height, Color dark, Color bright )
{
	g.translate(x, y);

	g.setColor( dark );
	g.drawRect( 0, 0, width - 2, height - 2 );
	
	g.setColor( bright );
	g.drawLine( 1, height - 3, 1, 1 );
	g.drawLine( 1, 1, width - 3, 1 );
	g.drawLine( 0, height - 1, width - 1, height - 1 );
	g.drawLine( width - 1, height - 1, width - 1, 0 );

	g.translate( -x, -y );
}
public static final void drawEtchedBorder( Graphics g, Rectangle r, Color dark, Color bright )
{
	drawEtchedBorder( g, r.x, r.y, r.width, r.height, dark, bright );
}
public static final Boolean nextState( Boolean b )
{
	if      ( b == null )         return Boolean.TRUE;
	else if ( b == Boolean.TRUE ) return Boolean.FALSE;
	else                          return null;
}
}
