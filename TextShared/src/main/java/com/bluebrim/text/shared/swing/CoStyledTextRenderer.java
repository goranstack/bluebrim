package com.bluebrim.text.shared.swing;

import java.awt.*;

import javax.swing.text.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Class that renders a CoStyledDocumentIF but offers no editing or selection capability.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoStyledTextRenderer
{
	protected CoColumnGeometryIF m_columnGeometry;
	protected CoBaseLineGeometryIF m_baseLineGeometry;
	protected CoTextGeometryIF m_textGeometry;
	protected String m_dummyText;
	
	protected Document m_doc;
	protected Color m_backgroundColor;


	
	private MySectionView m_sectionView;


	
	private final class MySectionView extends CoSectionView
	{
		public MySectionView( Element elem )
		{
			super( elem );
		}

		public ViewFactory getViewFactory()
		{
			return CoStyledTextViewFactory.getInstance();
		}

		public float getScale()
		{
			return 1;
		}

		protected Color getBackgroundColor()
		{
			return m_backgroundColor;
		}

		protected void set( Element elem )
		{
			super.set( elem );
			loadChildren( getViewFactory() );
		}

		public Container getContainer()
		{
			return CoStyledTextRenderer.this.getContainer();
		}
		
		public boolean showParagraphTags()
		{
			return CoStyledTextRenderer.this.m_showParagraphTags;
		}
	};
	
public CoStyledTextRenderer()
{
	this( null, null, null );
}
private CoStyledTextRenderer( Document doc, CoColumnGeometryIF c, CoBaseLineGeometryIF bl, CoTextGeometryIF tg )
{
	setGeometry( c, bl, tg );
	setDocument( doc );
}
private CoStyledTextRenderer( CoColumnGeometryIF c, CoBaseLineGeometryIF bl, CoTextGeometryIF tg )
{
	this( null, c, bl, tg );
}
public final CoBaseLineGeometryIF getBaseLineGeometry()
{
	return m_baseLineGeometry;
}
/**
 * @return kolumndefinitionen i form av en sekvens av rektanglar [pixels] (om en sådan specification är given annars null)
 */
public final CoColumnGeometryIF getColumnGeometry()
{
	 return m_columnGeometry;
}
public float getHeight()
{
	return ( m_sectionView == null ) ? 0 : m_sectionView.getPreferredHeight();
}

public void refresh()
{
	if
		( m_sectionView != null )
	{
		m_sectionView.removeAll();
		m_sectionView.reset();
		m_sectionView.set( m_doc.getRootElements()[ 0 ] );
	} else {
		m_sectionView = new MySectionView( m_doc.getRootElements()[ 0 ] );
	}
	
	m_sectionView.setColumnGeometry( m_columnGeometry );
	m_sectionView.setBaselineGeometry( m_baseLineGeometry );
	m_sectionView.setTextGeometry( m_textGeometry );
	m_sectionView.setDummyText( m_dummyText );
}
public void setDocument( Document doc )
{
	m_doc = doc;

	if
		( m_doc != null )
	{
		refresh();
	} else {
		if ( m_sectionView != null ) m_sectionView.removeAll();
		m_sectionView = null;
	}
}
public void setDummyText( String t )
{
	m_dummyText = t;
	if ( m_sectionView != null ) m_sectionView.setDummyText( m_dummyText );
}
  /**
   * Ange kolumngeometrin i form av en sekvens av rektanglar (@see java.awt.Rectangle).
   * Alla värden antas vara givna i pixels.
   * Värdet null är giltigt och betyder att denna typ av kolumngeometridefinition ej ska användas.
   * @param  columns   Rektangelsekvensen.
   */
public void setGeometry( CoColumnGeometryIF columns, CoBaseLineGeometryIF bl, CoTextGeometryIF tg )
{
	m_columnGeometry = columns;
	m_baseLineGeometry = bl;
	m_textGeometry = tg;

	if
		( m_sectionView != null )
	{
		m_sectionView.setColumnGeometry( m_columnGeometry );
		m_sectionView.setBaselineGeometry( m_baseLineGeometry );
		m_sectionView.setTextGeometry( m_textGeometry );
//		m_sectionView.setDummyText( m_dummyText );
		m_sectionView.setSize( -1, -1 );
	}
}

	private boolean m_showParagraphTags;

protected abstract Container getContainer();

public Document getDocument()
{
	return m_doc;
}

public void paint( CoPaintable g, Shape s )
{
	if ( m_doc == null ) return;
	
	if
		( m_sectionView != null )
	{
		m_backgroundColor = g.getColor();
		m_sectionView.paint( g, s );
	}		
}

public void setShowParagraphTags( boolean b )
{
	m_showParagraphTags = b;
}
}