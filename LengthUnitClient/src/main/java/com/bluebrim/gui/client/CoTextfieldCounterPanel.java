package com.bluebrim.gui.client;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.*;

import javax.swing.*;


import com.bluebrim.base.shared.*;
import com.bluebrim.swing.client.*;

//

public class CoTextfieldCounterPanel extends CoPanel
{
	protected CoTextField m_textfield;
	protected CoArrowButton m_upButton;
	protected CoArrowButton m_downButton;

	protected float m_min;
	protected float m_max;
	protected float m_delta;
	protected float m_default;

	protected CoConvertibleUnitSet m_unitSet;



	
	private static final int m_buttonWidth = 13;
	
	private class MyLayout implements LayoutManager2
	{
		private boolean m_valid = false;
		
		public Dimension preferredLayoutSize( Container target )
		{
			Dimension d = m_textfield.getPreferredSize();
			Insets insets = target.getInsets();
			d.width += insets.left + insets.right;
			d.height += insets.top + insets.bottom;
			if ( d.width != Integer.MAX_VALUE ) d.width += m_buttonWidth;
			return d;
		}
		
		public Dimension maximumLayoutSize( Container target )
		{
			Dimension d = m_textfield.getMaximumSize();
			Insets insets = target.getInsets();
			d.width += insets.left + insets.right;
			d.height += insets.top + insets.bottom;
			if ( d.width != Integer.MAX_VALUE )	d.width += m_buttonWidth;
			return d;
		}
		
		public Dimension minimumLayoutSize( Container target )
		{
			Dimension d = m_textfield.getMinimumSize();
			Insets insets = target.getInsets();
			d.width += insets.left + insets.right;
			d.height += insets.top + insets.bottom;
			if ( d.width != Integer.MAX_VALUE ) d.width += m_buttonWidth;
			return d;
		}
		
		public float getLayoutAlignmentY( Container target ) { return 0.5f; }
		public float getLayoutAlignmentX( Container target ) { return 0.5f; }
		public void removeLayoutComponent( Component c ) {}
		public void addLayoutComponent( Component c, Object o ) {}
		public void addLayoutComponent( String s, Component c ) {}
		public void invalidateLayout( Container target ) { m_valid = false; }
		public void layoutContainer( Container target )
		{
			if ( m_valid ) return;
			Dimension d = target.getSize();
			Insets insets = target.getInsets();
			int w = ( d.width - insets.left - insets.right );
			int h = ( d.height - insets.top - insets.bottom );
			int x = w - m_buttonWidth;
			int H = h / 2;
			m_textfield.setBounds( insets.left + 0, insets.top + 0, x, h );
			m_upButton.setBounds( insets.left + x, insets.top + 0, m_buttonWidth, H );
			m_downButton.setBounds( insets.left + x, insets.top + H, m_buttonWidth, H );
			m_valid = true;
		}
	};

/**
 * Arne constructor comment.
 */
public CoTextfieldCounterPanel( CoTextField textfield )
{
	this( textfield, null );
}
/**
 * Arne constructor comment.
 */
public CoTextfieldCounterPanel( CoTextField textfield, CoConvertibleUnitSet us )
{
	this( textfield, us, 1.0f );
}
/**
 * Arne constructor comment.
 */
public CoTextfieldCounterPanel( CoTextField textfield, CoConvertibleUnitSet us, float delta )
{
	this( textfield, us, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, delta );
}
/**
 * Arne constructor comment.
 */
public CoTextfieldCounterPanel( CoTextField textfield, CoConvertibleUnitSet us, float min, float max )
{
	this( textfield, us, min, max, 1.0f );
}
/**
 * Arne constructor comment.
 */
public CoTextfieldCounterPanel( CoTextField textfield, CoConvertibleUnitSet us, float min, float max, float delta )
{
	this( textfield, us, min, max, delta, 0f );
}
/**
 * Arne constructor comment.
 */
public CoTextfieldCounterPanel( CoTextField textfield, CoConvertibleUnitSet us, float min, float max, float delta, float _default )
{
	super();

	setOpaque( false );
	
	m_unitSet = us;
	m_min = min;
	m_max = max;
	m_delta = delta;
	m_default = _default;
	
	setLayout( new MyLayout() );
	
	m_textfield = textfield;

	m_upButton = new CoArrowButton( SwingConstants.NORTH );
	m_upButton.setRequestFocusEnabled( false );

	m_downButton = new CoArrowButton( SwingConstants.SOUTH );
	m_downButton.setRequestFocusEnabled( false );

	add( m_textfield );
	add( m_upButton );
	add( m_downButton );

	m_upButton.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e ) { update( m_delta ); }
		} );

	m_downButton.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e ) { update( - m_delta ); }
		} );

	m_textfield.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e ) { check(); }
		} );

	
	m_textfield.addPropertyChangeListener( new PropertyChangeListener()
		{
			public void propertyChange( PropertyChangeEvent e )
			{
				if ( ! e.getPropertyName().equals( "enabled" ) ) return;
				if
					( ( (Boolean) e.getNewValue() ).booleanValue() )
				{
					check();
				} else {
					m_upButton.setEnabled( false );
					m_downButton.setEnabled( false );
				}
			}
		} );

}
protected void check()
{
	try
	{
		double f = CoLengthUnitSet.parse( m_textfield.getText(), m_unitSet );
		m_upButton.setEnabled( f < m_max );
		m_downButton.setEnabled( f > m_min );
	}
	catch ( ParseException ex )
	{
		m_upButton.setEnabled( true );
		m_downButton.setEnabled( true );
	}
}
public float getDefault()
{
	return m_default;
}
public float getDelta()
{
	return m_delta;
}
public float getMaximum()
{
	return m_max;
}
public float getMinimum()
{
	return m_min;
}
public float setDefault( float d )
{
	return m_default = d;
}
public float setDelta( float delta )
{
	return m_delta = delta;
}
public float setMaximum( float max )
{
	return m_max = max;
}
public float setMinimum( float min )
{
	return m_min = min;
}
protected void update( float delta )
{
	double f;
	try
	{
		f = CoLengthUnitSet.parse( m_textfield.getText() );
		f += delta;
	}
	catch ( ParseException e )
	{
		f = Math.min( Math.max( m_min, m_default ), m_max );
	}
	if ( m_unitSet != null ) f = m_unitSet.from( f );
	m_textfield.setText( CoLengthUnitSet.format( f ) );
	m_textfield.postActionEvent();
}
}
