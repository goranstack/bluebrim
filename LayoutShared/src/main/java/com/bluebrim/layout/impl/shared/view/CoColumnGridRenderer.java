package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Renderer for column grids.
 * 
 * @author: Dennis Malmström
 */

public class CoColumnGridRenderer extends CoGridRenderer
{
	// rendering cache
	protected int m_cachedDetailMask = -1;

public void setColumnGrid( CoImmutableColumnGridIF columnGrid )
{
	setGrid( columnGrid );
}
public void setGrid( CoImmutableSnapGridIF grid )
{
	super.setGrid( grid );

	m_cachedDetailMask = -1;
}

	public static int COLUMN_PAINTING_THRESHOLD = 2;
	public static int GAP_PAINTING_THRESHOLD = 1;
	public static int MARGIN_PAINTING_THRESHOLD = 1;

public void paint( com.bluebrim.base.shared.CoPaintable g )
{
	double scale = g.getScale();//com.bluebrim.base.shared.CoBaseUtilities.getXScale( g.getTransform() );

	CoImmutableColumnGridIF columnGrid = (CoImmutableColumnGridIF) m_grid;
	
	if
		( columnGrid != null )
	{
		if
			(
				( scale != m_cachedScale )
			||
				( ! m_isCacheValid )
			)
		{
			m_cachedScale = scale;

			int detailMask = 0;
			if
				( m_cachedScale >= 0.5 )
			{
				// don't reduce level of detail when scale >= 50%
				detailMask = CoColumnGridIF.MARGINS | CoColumnGridIF.COLUMN_SPACINGS | CoColumnGridIF.COLUMNS;
			} else {
				// calc level of detail
				double D = Double.MAX_VALUE;
				double d = columnGrid.getLeftMarginPosition();
				if ( d > 0 ) D = Math.min( d, D );
				d = columnGrid.getTopMarginPosition();
				if ( d > 0 ) D = Math.min( d, D );
				d = columnGrid.getWidth() - columnGrid.getRightMarginPosition();
				if ( d > 0 ) D = Math.min( d, D );
				d = columnGrid.getHeight() - columnGrid.getBottomMarginPosition();
				if ( d > 0 ) D = Math.min( d, D );

				if
					( D * m_cachedScale > MARGIN_PAINTING_THRESHOLD )
				{
					detailMask |= CoColumnGridIF.MARGINS; // margins too small -> don't paint them
				}
				if
					( columnGrid.getColumnSpacing() * m_cachedScale > GAP_PAINTING_THRESHOLD )
				{
					detailMask |= CoColumnGridIF.COLUMN_SPACINGS; // column spacing too small -> don't paint them
				}
				if
					( columnGrid.getColumnWidth() * m_cachedScale > COLUMN_PAINTING_THRESHOLD )
				{
					detailMask |= CoColumnGridIF.COLUMNS; // columns too small -> don't paint them
				}
			}
			
			if
				( detailMask != m_cachedDetailMask )
			{
				m_cachedDetailMask = detailMask;
				m_cachedShape = columnGrid.getShape( m_cachedDetailMask );
			}

			m_cachedStroke = new BasicStroke( (float) Math.abs( 1 / m_cachedScale ) );
			m_isCacheValid = true;
		}
		

		if
			( m_cachedShape != null )
		{
			g.setStroke( m_cachedStroke );
			g.draw( m_cachedShape );
		}
	}
}
}