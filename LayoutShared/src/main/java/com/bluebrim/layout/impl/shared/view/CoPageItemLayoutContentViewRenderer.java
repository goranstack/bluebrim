package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.shared.swing.*;

/**
 * Renderer for CoPageItemImageContentView
 * 
 * @author: Dennis Malmström
 */

public class CoPageItemLayoutContentViewRenderer extends CoPageItemBoundedContentViewRenderer
{



protected void doPaintContent( CoPaintable g, CoPageItemBoundedContentView v, Rectangle bounds )
{
	g.addComment( "LAYOUT CONTENT" );

	CoPageItemLayoutContentView view = (CoPageItemLayoutContentView) v;

	if
		( view.m_isRecursionTerminator )
	{
		// ???
	} else if
		( view.m_contentView != null )
	{
		double dx = - view.m_contentView.getX();
		double dy = - view.m_contentView.getY();
		
		g.translate( dx, dy );

		if
			( bounds == null )
		{
			view.m_contentView.paint( g, bounds );
		} else {
			int x = bounds.x;
			int y = bounds.y;
			bounds.x = 0;
			bounds.y = 0;

			view.m_contentView.paint( g, bounds );

			bounds.x = x;
			bounds.y = y;
		}
		
		g.untranslate( dx, dy );
	} else {
	}
}

protected void paintNoContent( CoPaintable g, CoPageItemBoundedContentView v, Rectangle bounds )
{
	super.paintNoContent( g, v, bounds );

	CoPageItemLayoutContentView view = (CoPageItemLayoutContentView) v;

	// paint order tag
	if
		( view.m_layoutTag >= 0 )
	{
		g.setColor( CoSectionView.DUMMY_TEXT_COLOR );
		g.drawDecorationString( "[" + view.m_layoutTag + "]", 5, (float) ( view.m_owner.m_shape.getHeight() / 2 ), CoSectionView.DUMMY_TEXT_FONT );
	}

}
}