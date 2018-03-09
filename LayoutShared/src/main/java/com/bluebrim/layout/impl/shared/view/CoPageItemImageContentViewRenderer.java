package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.shared.swing.*;

/**
 * Renderer for CoPageItemImageContentView
 * 
 * @author: Dennis Malmström
 */

public class CoPageItemImageContentViewRenderer extends CoPageItemBoundedContentViewRenderer
{



protected void doPaintContent( CoPaintable g, CoPageItemBoundedContentView v, Rectangle bounds )
{
	g.addComment( "IMAGE CONTENT" );

	CoPageItemImageContentView view = (CoPageItemImageContentView) v;
	

	if
		( g.getRenderingHint( PAINT_IMAGE_PLACEHOLDERS ) == PAINT_IMAGE_PLACEHOLDERS_ON )
	{
		g.setColor( Color.lightGray );

		double w = view.getOwner().getWidth() / view.getScaleX();//view.m_imageView.getWidth();
		double h = view.getOwner().getHeight() / view.getScaleY();//view.m_imageView.getHeight();
		double x = - view.getX() / view.getScaleX();
		double y = - view.getY() / view.getScaleY();

		m_rect.setRect( x, y, w, h );
		g.fill( m_rect );

		g.setStroke( m_emptyStroke );
		
		g.setColor( Color.black );
		
		m_line.setLine( x, y, x + w, y + h );
		g.draw( m_line );
		
		m_line.setLine( x + w, y, x, y + h );
		g.draw( m_line );

	} else {

		if
			( view.m_color != null )
		{
			g.setColor( view.m_color );
			m_rect.setRect( 0, 0, view.m_imageView.getWidth(), view.m_imageView.getHeight() );
			g.fill( m_rect );
		}
		view.m_imageView.paint( g );
	}
}

protected void paintNoContent( CoPaintable g, CoPageItemBoundedContentView v, Rectangle bounds )
{
	super.paintNoContent( g, v, bounds );

	CoPageItemImageContentView view = (CoPageItemImageContentView) v;

	// paint order tag
	if
		( view.m_imageTag >= 0 )
	{
		g.setColor( CoSectionView.DUMMY_TEXT_COLOR );
		g.drawDecorationString( "[" + view.m_imageTag + "]", 5, (float) ( view.m_owner.m_shape.getHeight() / 2 ), CoSectionView.DUMMY_TEXT_FONT );
	}

}
}