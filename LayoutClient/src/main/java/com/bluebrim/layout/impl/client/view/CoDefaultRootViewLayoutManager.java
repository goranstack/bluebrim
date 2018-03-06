package com.bluebrim.layout.impl.client.view;

import java.awt.geom.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-10-04 09:50:11)
 * @author: Dennis
 */
 
public class CoDefaultRootViewLayoutManager extends CoRootViewLayoutManager
{
protected Dimension2D doLayout( CoRootView target )
{
	double x = 0;
	double y = 0;

	if
		( m_insets != null )
	{
		y = m_insets.top;
	}
	
	double w = 500; // min size
	double h = 500;

	List layoutEditorModels = target.getModels();
	
	if
		( layoutEditorModels.size() > 0 )
	{
		w = - m_spacing;
		h = Double.MIN_VALUE;
		Iterator e = layoutEditorModels.iterator();
		while
			( e.hasNext() )
		{
			CoLayoutEditorModel model = (CoLayoutEditorModel) e.next();
			int I = model.getLayerCount();
			double W = 0;
			double H = 0;
			for
				( int i = 0; i < I; i++ )
			{
				CoShapePageItemIF pi = model.getLayer( i ).m_pageItem;
				CoImmutableShapeIF s = pi.getCoShape();
				W = Math.max( W, s.getWidth() );
				H = Math.max( H, s.getHeight() );
			}
			w = w + W + m_spacing;
			h = Math.max( h, H );
		}
	}

	x = w / 2;			
	Iterator e = layoutEditorModels.iterator();
	while
		( e.hasNext() )
	{
		CoLayoutEditorModel model = (CoLayoutEditorModel) e.next();
		int I = model.getLayerCount();
		double W = 0;
		for
			( int i = 0; i < I; i++ )
		{
			CoShapePageItemView wr = model.getLayer( i ).m_view;

			( (CoFixedPositionAdapter) wr.getAdapter() ).setChildPosition( x, y );
			W = Math.max( W, wr.getWidth() );
		}
		x += W + m_spacing;
		
	}

	m_insets.left = (int) ( w / 2 );
	m_insets.right = (int) ( w / 2 );
	
	return new CoDimension2D( w, h );
}
}
