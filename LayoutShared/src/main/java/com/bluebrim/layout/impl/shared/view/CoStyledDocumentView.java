package com.bluebrim.layout.impl.shared.view;

////import se.corren.calvin.editorial.interfaces.*;
import java.awt.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.text.shared.swing.*;

/**
 * A view for formatted text in the form of a CoImmutableStyledDocumentIF.
 * WARNING: Allthough this class implements (indirectly) Serializable it does not implement serialization behavior properly (m_document is lost).
 * 
 * @author: Dennis Malmström
 */

public class CoStyledDocumentView extends CoView
{
	protected transient CoImmutableStyledDocumentIF m_document; // model of the view

	// geoemtry used to render the text
	protected CoColumnGeometryIF m_columnGeometry;
	protected CoBaseLineGeometryIF m_baseLineGeometry;
	protected CoTextGeometryIF m_textGeometry;
	protected String m_dummyText;

	// cached renderer
	protected transient CoStyledTextRenderer m_renderer;
	protected transient boolean m_isRendererValid = false;

	// view geoemtry
	protected double m_width;
	protected double m_height;

	// shared rendering stuff
	protected static Rectangle m_bounds = new Rectangle();

public CoStyledDocumentView()
{
}
public CoStyledDocumentView( CoImmutableStyledDocumentIF doc, double width, double height, CoBaseLineGeometryIF blg, CoTextGeometryIF tg )
{
	this( doc, width, height, null, blg, tg );
}
public CoStyledDocumentView( CoImmutableStyledDocumentIF doc,
	                           double width,
	                           double height,
	                           CoColumnGeometryIF columnGeometry,
	                           CoBaseLineGeometryIF blg,
	                           CoTextGeometryIF tg )
{
	set( doc, width, height, columnGeometry, blg, tg );
}
public CoStyledDocumentView( CoImmutableStyledDocumentIF doc, double width, CoBaseLineGeometryIF blg, CoTextGeometryIF tg )
{
	this( doc, width, 1 + CoStyledTextMeasurer.getHeight( doc, (float) width, blg, tg ), blg, tg );
}
public CoStyledDocumentView( CoImmutableStyledDocumentIF doc, CoColumnGeometryIF columnGeometry, CoBaseLineGeometryIF blg, CoTextGeometryIF tg )
{
	double w = 0;
	double h = 0;

	int I = columnGeometry.getColumnCount();
	for
		( int i = 0; i < I; i++ )
	{
		Rectangle2D r = columnGeometry.getColumn( i ).getBounds();
		w = Math.max( w, r.getX() + r.getWidth() );
		h = Math.max( h, r.getY() + r.getHeight() );
	}
	set( doc, w, h, columnGeometry, blg, tg );
}
public void dispose()
{
	if ( m_renderer != null ) m_renderer.setDocument( null );
}
private void doSetDocument( CoImmutableStyledDocumentIF doc )
{
	m_document = doc;
}
private void doSetDummyText( String t )
{
	m_dummyText = t;
}
private void doSetGeometry( CoColumnGeometryIF columnGeometry, CoBaseLineGeometryIF blg, CoTextGeometryIF tg )
{
	if
		( columnGeometry == null )
	{
		columnGeometry =
			new CoAbstractColumnGeometry()
			{
				private transient CoColumnGeometryIF.CoColumnIF m_column;
				
				public int getColumnCount()
				{
					return 1;
				}
				
				public CoColumnGeometryIF.CoColumnIF getColumn( int i )
				{
					if
						( m_column == null )
					{
						m_column = new CoRectangularColumn( new CoRectangle2DFloat( 0, 0, (int) m_width, (int) m_height ) );
					}
					
					return m_column;
				}
				
				public boolean isRectangular()
				{
					return m_column.isRectangular();
				}
			};
	}

	m_columnGeometry = columnGeometry;
	m_baseLineGeometry = blg;
	m_textGeometry = tg;
}
private void doSetSize( double width, double height )
{
	m_width = width;
	m_height = height;
}
public CoBaseLineGeometryIF getBaseLineGeometry()
{
	return m_baseLineGeometry;
}
public CoColumnGeometryIF getColumnGeometry()
{
	return m_columnGeometry;
}

public double getHeight()
{
	return m_height;
}
public double getWidth()
{
	return m_width;
}
private void invalidate()
{
	m_isRendererValid = false;
}
public void modelChanged()
{
	invalidate();
}

public void set( double width,
	               double height,
	               CoColumnGeometryIF columnGeometry,
	               CoBaseLineGeometryIF blg,
	               CoTextGeometryIF tg )
{
	doSetGeometry( columnGeometry, blg, tg );
	doSetSize( width, height );
	
	invalidate();
}
public void set( CoImmutableStyledDocumentIF doc,
	               double width,
	               double height,
	               CoColumnGeometryIF columnGeometry,
	               CoBaseLineGeometryIF blg,
	               CoTextGeometryIF tg )
{
	doSetGeometry( columnGeometry, blg, tg );
	doSetSize( width, height );
	doSetDocument( doc );

	invalidate();
}
public void setDocument( CoImmutableStyledDocumentIF doc )
{
	doSetDocument( doc );

	if ( m_document == null ) return;
	
	invalidate();
}
public void setDummyText( String t )
{
	doSetDummyText( t );
	
	invalidate();
}
public void setGeometry( CoColumnGeometryIF columnGeometry, CoBaseLineGeometryIF blg, CoTextGeometryIF tg )
{
	doSetGeometry( columnGeometry, blg, tg );
	
	invalidate();
}
public void setSize( double width, double height )
{
	doSetSize( width, height );
	
	invalidate();
}

	private ContainerHolder m_containerHolder;
	private final static long serialVersionUID = 1324691923184156764L;

	public interface ContainerHolder
	{
		Container getContainer();
	}

public Container getContainer()
{
	return ( m_containerHolder == null ) ? null : m_containerHolder.getContainer();
}

public void paint( CoPaintable g )
{
	if
		( ! m_isRendererValid )
	{
		if
			( m_renderer == null )
		{
			m_renderer =
				new CoStyledTextRenderer()
				{
					public Container getContainer()
					{
						return CoStyledDocumentView.this.getContainer();
					}
				};
		}
		
		m_renderer.setGeometry( m_columnGeometry, m_baseLineGeometry, m_textGeometry );
		m_renderer.setDummyText( m_dummyText );
		m_renderer.setDocument( m_document );

		m_isRendererValid = true;
	}

	m_bounds.setBounds( 0, 0, (int) m_width, (int) m_height );
	m_renderer.paint( g, m_bounds );
}

public void setContainerHolder( ContainerHolder ch )
{
	m_containerHolder = ch;
}
}