package com.bluebrim.text.shared.swing;

import java.util.*;

import javax.swing.text.*;

import com.bluebrim.text.shared.*;

/**
 * Class that measures text in the form of CoImmutableStyledDocumentIFs.
 * 
 * @author: Dennis Malmström
 */

public class CoStyledTextMeasurer
{
	private static MySectionView m_view;


	
	private static class MySectionView extends CoSectionView
	{
		public MySectionView( Element e )
		{
			super( e );
		}
			
		public ViewFactory getViewFactory()
		{
			return CoStyledTextViewFactory.getInstance();
		}

		public float getPreferredSpan( int axis )
		{
			float s = 0;
		  int I = getViewCount();
		  for
		  	( int i = 0; i < I; i++ )
		  {
			  s += getView( i ).getPreferredSpan( axis );
		  }
			return s;
		}

		public java.awt.Container getContainer()
		{
			return null;
		}
	};

private static CoSectionView createView( CoImmutableStyledDocumentIF doc, CoColumnGeometryIF columns, CoBaseLineGeometryIF bl, CoTextGeometryIF tg )
{
	if
		( m_view == null )
	{
		m_view = new MySectionView( null );
	}

	m_view.removeAll();
	m_view.reset();
	m_view.set( doc.getRootElements()[ 0 ] );

	m_view.prepareForMeasurement( m_view.getViewFactory() );

	m_view.setColumnGeometry( columns );
	m_view.setBaselineGeometry( bl );
	m_view.setTextGeometry( tg );
	

	return m_view;
}
public static boolean doesOverflow( CoImmutableStyledDocumentIF doc, float width, CoBaseLineGeometryIF bl, CoTextGeometryIF tg )
{
	final CoColumnGeometryIF.CoColumnIF column = new CoRectangularColumn( 0, 0, width, Float.MAX_VALUE );

	CoSectionView v = createView( doc,
		                            new CoAbstractColumnGeometry()
																{
																	public int getColumnCount() { return 1; }
																	public CoColumnGeometryIF.CoColumnIF getColumn( int index ) { return column; }
																	public boolean isRectangular() { return column.isRectangular(); }
																},
																bl,
																tg
	);

	v.setSize( Float.MAX_VALUE, Float.MAX_VALUE );

List l = new ArrayList();
v.collectRowHeights( l );
System.err.println( "############ " + l.size() );
Iterator i = l.iterator();
while
	( i.hasNext() )
{	
	System.err.println( i.next() );
}

	return v.doesOverflow();
}
public static boolean doesOverflow( CoImmutableStyledDocumentIF doc, CoColumnGeometryIF columns, CoBaseLineGeometryIF bl, CoTextGeometryIF tg )
{
	CoSectionView v = createView( doc, columns, bl, tg );

	v.setSize( Float.MAX_VALUE, Float.MAX_VALUE );

	return v.doesOverflow();
}
public static float getHeight( CoImmutableStyledDocumentIF doc, float width, CoBaseLineGeometryIF bl, CoTextGeometryIF tg )
{
	final CoColumnGeometryIF.CoColumnIF column = new CoRectangularColumn( 0, 0, width, Float.MAX_VALUE );

	CoSectionView v = createView( doc,
		                            new CoAbstractColumnGeometry()
																{
																	public int getColumnCount() { return 1; }
																	public CoColumnGeometryIF.CoColumnIF getColumn( int index ) { return column; }
																	public boolean isRectangular() { return column.isRectangular(); }
																},
																bl,
																tg
	);

	v.setSize( Float.MAX_VALUE, Float.MAX_VALUE );

	return v.getPreferredSpan( View.Y_AXIS );
}
public static float getHeight( CoImmutableStyledDocumentIF doc, CoColumnGeometryIF columns, CoBaseLineGeometryIF bl, CoTextGeometryIF tg )
{
	CoSectionView v = createView( doc, columns, bl, tg );

	v.setSize( Float.MAX_VALUE, Float.MAX_VALUE );

	return v.getPreferredSpan( View.Y_AXIS );
}
public static float getWidth( CoImmutableStyledDocumentIF doc, CoBaseLineGeometryIF bl, CoTextGeometryIF tg )
{
	final CoColumnGeometryIF.CoColumnIF column = new CoRectangularColumn( 0, 0, Float.MAX_VALUE, Float.MAX_VALUE );

	CoSectionView v = createView( doc,
		   	                        new CoAbstractColumnGeometry()
																{
																	public int getColumnCount() { return 1; }
																	public CoColumnGeometryIF.CoColumnIF getColumn( int index ) { return column; }
																	public boolean isRectangular() { return column.isRectangular(); }
																},
																bl,
																tg
	);

	v.setSize( Float.MAX_VALUE, Float.MAX_VALUE );

	return v.getPreferredSpan( View.X_AXIS );
}
public static int getVisiblePortion( CoImmutableStyledDocumentIF doc, CoColumnGeometryIF columns, CoBaseLineGeometryIF bl, CoTextGeometryIF tg )
{
	CoSectionView v = createView( doc, columns, bl, tg );

	v.setSize( Float.MAX_VALUE, Float.MAX_VALUE );

	if
		( v.doesOverflow() )
	{
		return v.getOverflowPosition();
	} else {
		return v.getEndOffset();
	}
}

public static List getRowHeights( CoImmutableStyledDocumentIF doc, float width, CoBaseLineGeometryIF bl, CoTextGeometryIF tg )
{
	final CoColumnGeometryIF.CoColumnIF column = new CoRectangularColumn( 0, 0, width, Float.MAX_VALUE );

	CoSectionView v = createView( doc,
		                            new CoAbstractColumnGeometry()
																{
																	public int getColumnCount() { return 1; }
																	public CoColumnGeometryIF.CoColumnIF getColumn( int index ) { return column; }
																	public boolean isRectangular() { return column.isRectangular(); }
																},
																bl,
																tg
	);

	v.setSize( Float.MAX_VALUE, Float.MAX_VALUE );

	List l = new ArrayList();
	v.collectRowHeights( l );

	return l;
}
}