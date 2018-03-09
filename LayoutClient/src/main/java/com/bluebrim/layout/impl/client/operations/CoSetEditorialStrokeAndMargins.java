package com.bluebrim.layout.impl.client.operations;



import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;

/**
 * Custom layout editor operation that sets a stroke width of 0.5 points and margins of 4 mm.
 *
 * @author: Dennis
 */

public class CoSetEditorialStrokeAndMargins extends CoAbstractPageItemOperation
{
	public static final String NAME = "CoSetEditorialStrokeAndMargins_NAME";
public CoSetEditorialStrokeAndMargins()
{
	super( CoPageItemUIStringResources.getName( NAME ) );
}


	private List m_undoMemory = new ArrayList();

	class UndoMemory
	{
		float m_strokeWidth;
		boolean m_derivedGrid;
		int m_columnCount;
		double m_columnSpacing;
		double m_leftMargin;
		double m_rightMargin;
		double m_topMargin;
		double m_bottomMargin;

		UndoMemory( float strokeWidth )
		{
			m_strokeWidth = strokeWidth;
			m_derivedGrid = true;
		}

		UndoMemory( float strokeWidth, int columnCount, double columnSpacing, double leftMargin, double rightMargin, double topMargin, double bottomMargin )
		{
			m_strokeWidth = strokeWidth;
			m_derivedGrid = false;
			m_columnCount = columnCount;
			m_columnSpacing = columnSpacing;
			m_leftMargin = leftMargin;
			m_rightMargin = rightMargin;
			m_topMargin = topMargin;
			m_bottomMargin = bottomMargin;
		}
	}

public boolean canBeUndone()
{
	return true;
}

public boolean doit( CoLayoutEditor editor, java.util.List operands )
{
	m_undoMemory.clear();

	Iterator i = operands.iterator();
	while
		( i.hasNext() )
	{
		CoShapePageItemView v = (CoShapePageItemView) i.next();
		CoShapePageItemIF pi = v.getShapePageItem();

		if
			( v.getColumnGrid().isDerived() )
		{
			m_undoMemory.add( new UndoMemory( v.getStrokeProperties().getWidth() ) );
		} else {
			CoImmutableColumnGridIF g = v.getColumnGrid();
			m_undoMemory.add( new UndoMemory( v.getStrokeProperties().getWidth(),
				                                g.getColumnCount(),
				                                g.getColumnSpacing(),
				                                g.getLeftMargin(),
				                                g.getRightMargin(),
				                                g.getTopMargin(),
				                                g.getBottomMargin() ) );
		}
	
		pi.getMutableStrokeProperties().setWidth( 0.5f );
		
		pi.setDerivedColumnGrid( false );
		CoColumnGridIF g = pi.getMutableColumnGrid();

		double _4mm = CoLengthUnit.MM.from( 4 );
		g.setTopMargin( _4mm );
		g.setBottomMargin( _4mm );
		g.setLeftMargin( _4mm );
		g.setRightMargin( _4mm );
	}
	
	return true;
}

public boolean redoit( CoLayoutEditor editor, java.util.List operands )
{
	return doit( editor, operands );
}

public boolean undoit( CoLayoutEditor editor, java.util.List operands )
{
	Iterator i = operands.iterator();
	Iterator j = m_undoMemory.iterator();
	while
		( i.hasNext() )
	{
		CoShapePageItemView v = (CoShapePageItemView) i.next();
		CoShapePageItemIF pi = v.getShapePageItem();
		UndoMemory um = (UndoMemory) j.next();

		pi.getMutableStrokeProperties().setWidth( um.m_strokeWidth );
		
		pi.setDerivedColumnGrid( um.m_derivedGrid );

		if
			( ! um.m_derivedGrid )
		{
			pi.getMutableColumnGrid().set( um.m_columnCount, um.m_columnSpacing, um.m_leftMargin, um.m_rightMargin, um.m_topMargin, um.m_bottomMargin );
		}
	}
	
	return true;
}
}