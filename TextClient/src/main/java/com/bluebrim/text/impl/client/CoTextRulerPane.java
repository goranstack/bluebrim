package com.bluebrim.text.impl.client;

import java.awt.*;
import java.awt.geom.*;
import java.util.List;

import javax.swing.text.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.impl.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2000-09-12 08:54:23)
 * @author: Dennis
 */
 
public class CoTextRulerPane extends CoTabRulerPane implements CoAttributeListenerIF
{
	protected CoAbstractTextEditor m_editor;

	private double m_firstLineIndent = Double.NaN;
	private double m_leftIndent = Double.NaN;
	private double m_rightIndent = Double.NaN;

	private GeneralPath m_arrowShape = new GeneralPath();

	private class IndentHandle extends CoTabRulerPane.Handle
	{
		public void paint( Graphics2D g ) { g.fill( m_arrowShape ); }
	};


	
	private static MutableAttributeSet m_attributeSet = new com.bluebrim.text.shared.CoSimpleAttributeSet();
	
	private Handle m_leftIndentHandle =
		new IndentHandle()
		{
			public double getX() { return ( getX0() + ( isUndefined() ? 0 : m_leftIndent ) ) * getScale(); }
			public double getY() { return getHeight() - getHandleHeight(); }
			public void move( double dx, double dy )
			{
				Float size = new Float( getMode().snap( Math.max( 0, isUndefined() ? 0 : m_leftIndent + dx ) ) );
				m_attributeSet.removeAttributes( m_attributeSet );
				CoStyleConstants.setLeftIndent( m_attributeSet, size );

				int start = m_editor.getSelectionStart();
				int length = m_editor.getSelectionEnd() - start;
				m_editor.getCoStyledDocument().setParagraphAttributes( start, length, m_attributeSet, false );
			}
			public void delete()
			{
				m_attributeSet.removeAttributes( m_attributeSet );
				CoStyleConstants.setLeftIndent( m_attributeSet, new Float( 0 ) );
				m_editor.unsetParagraphAttributes( m_attributeSet );
			}
		};
		
	private Handle m_rightIndentHandle =
		new IndentHandle()
		{
			public double getX() { return ( getX1() - ( isUndefined() ? 0 : m_rightIndent ) ) * getScale(); }
			public double getY() { return getHeight() - getHandleHeight(); }
			public void move( double dx, double dy )
			{
				Float size = new Float( getMode().snap( Math.max( 0, isUndefined() ? 0 : m_rightIndent - dx ) ) );
				m_attributeSet.removeAttributes( m_attributeSet );
				CoStyleConstants.setRightIndent( m_attributeSet, size );

				int start = m_editor.getSelectionStart();
				int length = m_editor.getSelectionEnd() - start;
				m_editor.getCoStyledDocument().setParagraphAttributes( start, length, m_attributeSet, false );
			}
			public void delete()
			{
				m_attributeSet.removeAttributes( m_attributeSet );
				CoStyleConstants.setRightIndent( m_attributeSet, new Float( 0 ) );
				m_editor.unsetParagraphAttributes( m_attributeSet );
			}
		};
		
	private Handle m_firstLineIndentHandle =
		new IndentHandle()
		{
			public double getX() { return ( getX0() + ( isUndefined() ? 0 : getEffectiveFirstLineIndent() ) ) * getScale(); }
			public double getY() { return getHeight() - getHandleHeight() * 2; }
			public void move( double dx, double dy )
			{
				Float size = new Float( getMode().snap( Math.max( 0, isUndefined() ? 0 : m_firstLineIndent + dx ) ) );
				m_attributeSet.removeAttributes( m_attributeSet );
				CoStyleConstants.setFirstLineIndent( m_attributeSet, size );

				int start = m_editor.getSelectionStart();
				int length = m_editor.getSelectionEnd() - start;
				m_editor.getCoStyledDocument().setParagraphAttributes( start, length, m_attributeSet, false );
			}
			public void delete()
			{
				m_attributeSet.removeAttributes( m_attributeSet );
				CoStyleConstants.setFirstLineIndent( m_attributeSet, new Float( 0 ) );
				m_editor.unsetParagraphAttributes( m_attributeSet );
			}
		};

public CoTextRulerPane( CoTabSetPanel.TabSetEditor editor )
{
	super( editor );
}
public void attributesChanged(CoAttributeEvent e)
{
	if
		( e.didEditableChange() )
	{
		setEnabled( m_editor.isEditable() );
		return;
	}
	if
		( e.didParagraphChange() )
	{
		updateRange( e.getP0() );

		update( e.getParagraphAttributes() );
		repaint();
	}
}
protected void createHandles( List handles )
{
	super.createHandles( handles );
	
	handles.add( m_firstLineIndentHandle );
	m_firstLineIndentHandle.setUndefined( Double.isNaN( m_firstLineIndent ) );

	handles.add( m_leftIndentHandle );
	m_leftIndentHandle.setUndefined( Double.isNaN( m_leftIndent ) );

	handles.add( m_rightIndentHandle );
	m_rightIndentHandle.setUndefined( Double.isNaN( m_rightIndent ) );
}
private double getEffectiveFirstLineIndent()
{
	if ( Double.isNaN( m_firstLineIndent ) ) return Double.NaN;

	return ( Double.isNaN( m_leftIndent ) ? 0 : m_leftIndent ) + m_firstLineIndent;
}
public void setEditor( CoAbstractTextEditor e )
{
	if ( m_editor == e ) return;
	
	if
		( m_editor != null )
	{	
		m_editor.removeAttributeListener( this );
	}
		
	m_editor = e;
	
	if
		( m_editor != null )
	{	
		m_editor.addAttributeListener( this );
		updateRange( 0 );
		update( m_editor.getParagraphAttributes() );
	}
}
private void update( AttributeSet a )
{
	Float f = CoStyleConstants.getFirstLineIndent( a );
	if
		( f == null )
	{
		m_firstLineIndent = Double.NaN;
	} else if
		( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
	{
		m_firstLineIndent = Double.NaN;
	} else {
		m_firstLineIndent = f.doubleValue();
	}

	f = CoStyleConstants.getLeftIndent( a );
	if
		( f == null )
	{
		m_leftIndent = Double.NaN;
	} else if
		( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
	{
		m_leftIndent = Double.NaN;
	} else {
		m_leftIndent = f.doubleValue();
	}

	f = CoStyleConstants.getRightIndent( a );
	if
		( f == null )
	{
		m_rightIndent = Double.NaN;
	} else if
		( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
	{
		m_rightIndent = Double.NaN;
	} else {
		m_rightIndent = f.doubleValue();
	}

	
	CoTabSetIF s = CoStyleConstants.getTabSet( a );
	setTabSet( s );
	
	if
		( s == CoStyleConstants.AS_IS_TAB_SET_VALUE )
	{
		setTabSet( null );
	} else if
		( s != null )
	{
	} else {
		f = CoStyleConstants.getRegularTabStopInterval( a );
		if
			( f == null )
		{
			setRegularTabStopInterval( Double.NaN );
		} else if
			( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
		{
			setRegularTabStopInterval( Double.NaN );
		} else {
			setRegularTabStopInterval( f.doubleValue() );
		}
	}



	
	invalidateHandles();
}
private void updateRange( int pos )
{
	if
		( m_editor instanceof CoStyledTextEditor )
	{
		CoStyledTextEditor ed = (CoStyledTextEditor) m_editor;

		com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF c = ed.getColumn( pos );

		if
			( c != null )
		{
			CoRectangle2DFloat b = c.getBounds();
			setRange( b.getX(), getX0() + b.getWidth() );
		}
	}
}
protected void updateShapes()
{
	super.updateShapes();
	
	float w = (float) ( m_handleWidth / getScale() ) ;
	float h = - (float) ( m_handleHeight / getScale() );
	
	m_arrowShape.reset();
	m_arrowShape.moveTo( 0, 0 );
	m_arrowShape.lineTo( -w, h );
	m_arrowShape.lineTo( w, h );
	m_arrowShape.lineTo( 0, 0 );
}
}