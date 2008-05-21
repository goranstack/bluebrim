package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Point;

import javax.swing.SizeRequirements;

/*
	A layout manager that places components left justified in a single column.
	The configuration possibilities are:
	Sticky - If set all components are stretched to the width of the widest one (default = false).
	Spacing - Spacing between the components (default = 0).
	Fill - If the constant FILL is passed as layout constraint ( in the call to Container.add( Component, Object ) )
	       then that particular component will be stretched verticaly to fill the container (default = none).
	       If the constant FILL is passed as layout constraint for several components in the same container
	       the last call will overwrite the state set by the previous ones.
*/

public class CoColumnLayout implements LayoutManager2
{
	public static final String FILL = "FILL";
	
	private static final boolean DEBUG = false;

	private int m_spacing;
	private boolean m_sticky = false;
	private Component m_fillComponent;

	private transient SizeRequirements[] m_h;
	private transient SizeRequirements m_W;
	private transient SizeRequirements m_H;

public CoColumnLayout()
{
	this(0);
}
public CoColumnLayout(int spacing)
{
	this(spacing, false);
}
public CoColumnLayout(int spacing, boolean sticky )
{
	m_spacing = spacing;
	m_sticky = sticky;
}
public CoColumnLayout( boolean sticky )
{
	this( 0, sticky );
}
public void addLayoutComponent(Component comp, Object constraints)
{
	if
		( constraints == FILL )
	{
		m_fillComponent = comp;
	}
}
public void addLayoutComponent(String name, Component comp)
{
	if
		( name == FILL )
	{
		m_fillComponent = comp;
	}
}
void checkRequests( Container target )
{
	if
		( m_H == null )
	{
		int I = target.getComponentCount();

		m_W = new SizeRequirements();
		m_H = new SizeRequirements();
		m_h = new SizeRequirements[ I ];
		for
			( int i = 0; i < I; i++ )
		{
			m_h[ i ] = new SizeRequirements();
		}
		
		for
			( int i = 0; i < I; i++ )
		{
			Component c = target.getComponent( i );
			
			Dimension min = c.getMinimumSize();
			Dimension typ = c.getPreferredSize();
			Dimension max = c.getMaximumSize();
			
			m_h[ i ].minimum =   (int) min.height;
			m_h[ i ].preferred = (int) typ.height;
			m_h[ i ].maximum =   (int) max.height;

			m_W.minimum =   (int) Math.max( (long) m_W.minimum,   min.width );
			m_W.preferred = (int) Math.max( (long) m_W.preferred, typ.width );
			m_W.maximum =   (int) Math.max( (long) m_W.maximum,   max.width );

			m_H.minimum += m_h[ i ].minimum + ( ( i == 0 ) ? 0 : m_spacing );
			m_H.preferred += m_h[ i ].preferred + ( ( i == 0 ) ? 0 : m_spacing );
			m_H.maximum += m_h[ i ].maximum + ( ( i == 0 ) ? 0 : m_spacing );

			if ( m_H.minimum < 0 ) m_H.minimum = Integer.MAX_VALUE;
			if ( m_H.preferred < 0 ) m_H.preferred = Integer.MAX_VALUE;
			if ( m_H.maximum < 0 ) m_H.maximum = Integer.MAX_VALUE;
		}
	}
}
public float getLayoutAlignmentX(Container target)
{
	checkRequests(target);
	return m_W.alignment;
}
public float getLayoutAlignmentY(Container target)
{
	checkRequests(target);
	return m_H.alignment;
}
public void invalidateLayout(Container target)
{
	m_H = null;
	m_h = null;
	m_W = null;
}
public void layoutContainer( Container target )
{
	checkRequests( target );

	int I = target.getComponentCount();

	  // determine the child placements
	Dimension alloc = target.getSize();

	Insets in = target.getInsets();

	long W = ( m_fillComponent != null ) ? Math.max( (long) m_W.preferred, (long) target.getBounds().getWidth() ) : (long) m_W.preferred;
	long Y = 0;
	int n = -1;
	for
		( int i = 0; i < I; i++ )
	{
		Component c = target.getComponent( i );
		if ( c == m_fillComponent ) n = i;

		int x = (int) Math.min( (long) in.left, Short.MAX_VALUE );
		int y = (int) Math.min( Y + (long) in.top, Short.MAX_VALUE );
		int w = m_sticky ? (int) Math.min( W, Short.MAX_VALUE ) : c.getPreferredSize().width;
		int h = (int) Math.min( (long) m_h[ i ].preferred, Short.MAX_VALUE );
		Y += h + m_spacing;

		if
			( DEBUG )
		{
			System.err.print( x );
			System.err.print( " " );
			System.err.print( y );
			System.err.print( " " );
			System.err.print( w );
			System.err.print( " " );
			System.err.print( h );
			System.err.print( " ::: " );
			System.err.println( c );
		}

		c.setBounds( x, y, w, h );
	}

	Y -= m_spacing;
	long H = (long) target.getBounds().getHeight();
	if
		( ( m_fillComponent != null ) && ( H > Y ) )
	{
		Dimension d = m_fillComponent.getSize();
		int dH = (int) ( H - Y );
		d.height += dH;
		m_fillComponent.setSize( d );
		for
			( int i = n + 1; i < I; i++ )
		{
			Component c = target.getComponent( i );
			Point p = c.getLocation();
			p.y += dH;
			c.setLocation( p );
		}
	}
}
public Dimension maximumLayoutSize( Container target )
{
	checkRequests( target );
	Dimension size = new Dimension( m_W.maximum, m_H.maximum );
	Insets insets = target.getInsets();
	size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Short.MAX_VALUE );
	size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Short.MAX_VALUE );
	return size;
}
public Dimension minimumLayoutSize( Container target )
{      
	checkRequests( target );
	Dimension size = new Dimension( m_W.minimum, m_H.minimum );
	Insets insets = target.getInsets();
	size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Short.MAX_VALUE );
	size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Short.MAX_VALUE );
	return size;
}
public Dimension preferredLayoutSize( Container target )
{      
	checkRequests( target );
	Dimension size = new Dimension( m_W.preferred, m_H.preferred );
	Insets insets = target.getInsets();
	size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Short.MAX_VALUE );
	size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Short.MAX_VALUE );
	return size;
}
public void removeLayoutComponent(Component comp)
{
	if ( m_fillComponent == comp ) m_fillComponent = null;
}
}
