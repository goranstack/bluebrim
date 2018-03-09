package com.bluebrim.layout.impl.client.editor;

import java.awt.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;

/**
 * An icon displaying a page item view
 *
 * author: Dennis
 */

public class CoPageItemViewIcon implements Icon
{
	private int m_width;
	private int m_height;
	private CoPageItemView m_view;
public CoPageItemViewIcon( CoPageItemView view, int w, int h )
{
	m_width = w;
	m_height = h;
	m_view = view;
}
public int getIconHeight()
{
	return m_height;
}
public int getIconWidth()
{
	return m_width;
}
/**
 * paintIcon method comment.
 */
public void paintIcon( Component c, Graphics g, int x, int y )
{
	Graphics2D G = (Graphics2D) g.create();
	
	double X = ( (CoShapePageItemIF) m_view.getPageItem() ).getX()-1;
	double Y = ( (CoShapePageItemIF) m_view.getPageItem() ).getY()-1;

	double W = ( (CoShapePageItemIF) m_view.getPageItem() ).getCoShape().getWidth() + 2;
	double H = ( (CoShapePageItemIF) m_view.getPageItem() ).getCoShape().getHeight() + 2;
	double s = Math.min( m_width / W, m_height / H );
	
	G.translate( x, y );
	G.scale( s, s );
	G.translate( -X, -Y );

	G.setColor( Color.white );
	G.fill( new java.awt.geom.Rectangle2D.Double( X, Y, W, H ) );

	m_paintable.setDelegate( G );
	m_view.paint( m_paintable );
	m_paintable.releaseDelegate();
	
	G.translate( X, Y );
	G.scale( 1 / s, 1 / s );
	G.translate( -x, -y );
	
	G.dispose();

}

	private static CoScreenPaintable m_paintable =
		new CoScreenPaintable()
		{
			boolean doFill = true;
			{
				super.setColor( Color.black );
				super.setStroke( null );
			}
			public void setColor( Color color )
			{
				if
					( color == Color.white )
				{
					super.setColor( color );
				} else {
					super.setColor( Color.black );
				}
			}
			public void setPaint( Paint p )
			{
				setColor( Color.black );
			}
			public void setStroke( Stroke s ) {}
			public void fill( Shape s )
			{
				if
					( doFill )
				{
					super.fill( s );
				} else {
					super.draw( s );
				}
				doFill = false;
			}
		};
}