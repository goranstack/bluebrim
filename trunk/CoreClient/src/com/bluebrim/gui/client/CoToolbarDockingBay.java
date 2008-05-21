package com.bluebrim.gui.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JPanel;

/**
 */

public abstract class CoToolbarDockingBay extends JPanel
{
	protected static final int m_minimumSize = 5;


	protected abstract class SubDockingBay extends JPanel
	{
		public SubDockingBay()
		{
			super();
//			setAlignmentY( Component.TOP_ALIGNMENT );
//			setAlignmentX( Component.LEFT_ALIGNMENT );
		}
		
		public void remove( int i )
		{
			super.remove( i );
			if ( getParent() == null ) return;
			if ( getComponentCount() == 0 ) getParent().remove( this );
		}

		public void remove( Component c )
		{
			super.remove( c );
			if ( getParent() == null ) return;
			if ( getComponentCount() == 0 ) getParent().remove( this );
		} 
	};
public Component add( Component comp )
{
	if
		( ! ( comp instanceof CoToolbar ) )
	{
		throw new IllegalArgumentException( "Child must be MyToolbar" );
	}

	CoToolbar tb = (CoToolbar) comp;
	
	SubDockingBay h = createSubDockingBay();
	
	h.add( tb );
	super.add( h );

	return tb;
}
public Component add( Component comp, int i )
{
	if
		( ! ( comp instanceof CoToolbar ) )
	{
		throw new IllegalArgumentException( "Child must be MyToolbar" );
	}

	CoToolbar tb = (CoToolbar) comp;
	
	SubDockingBay h = createSubDockingBay();
	
	h.add( tb );
	super.add( h, i );

	return tb;
}
public void addToolbar( CoToolbar tb, int subBayIndex )
{
	subBayIndex = Math.min( subBayIndex, getComponentCount() - 1 );
 	SubDockingBay sdb = (SubDockingBay) getComponent( subBayIndex );
	sdb.add( tb );
}
public void addToolbar( CoToolbar tb, int subBayIndex, int position )
{
	subBayIndex = Math.min( subBayIndex, getComponentCount() - 1 );
 	SubDockingBay sdb = (SubDockingBay) getComponent( subBayIndex );
	sdb.add( tb, position );
}
protected abstract Dimension adjustPreferredSize( Dimension d );
protected abstract SubDockingBay createSubDockingBay();
public void dock( CoToolbar tb, Point p )
{
	Point p0 = getLocationOnScreen();

	p.x -= p0.x;
	p.y -= p0.y;

	Component c = getComponentAt( p );

	if
		( c == this )
	{
		// dropped on container, not on any toolbar
		int I = getComponentCount();
		if
			( I == 0 )
		{
			// container is empty -> just add the new toolbar
			add( tb );
		} else {
		  // find adjacent sub-container and append new toolbar to it
			for
				( int i = I - 1; i >= 0; i-- )
			{
			  SubDockingBay sdb = (SubDockingBay) getComponent( i );
				if
					( isAdjacent( p, sdb ) )
				{
					if
						( ( sdb != tb.getParent() ) ||
							( sdb.getComponentCount() > 1 ) )
					{
						sdb.add( tb );
					}
					break;
				}
			}
		}
	} else {
	  // dropped on a sub-container
	  SubDockingBay sdb = (SubDockingBay) c;
	  p0 = c.getLocation();
		p.x -= p0.x;
		p.y -= p0.y;
		c = sdb.getComponentAt( p );
		if
			( c instanceof SubDockingBay )
		{
			dockBy( tb, (CoToolbar) sdb.getComponent( 0 ) );
		} else if
			( tb != c )
		{
			dock( tb, (CoToolbar) c, p );
		}
	}
}
protected abstract void dock( CoToolbar tb, CoToolbar tb2, Point p );
protected abstract void dockBy( CoToolbar tb, CoToolbar tb2 );
	protected int getIndexOf( Component c )
{
	Container C = c.getParent();
	if ( C == null ) return -1;

	for
		( int i = 0; ; i++ )
	{
		if
			( c == C.getComponent( i ) )
		{
			return i;
		}
	}
}
public Dimension getPreferredSize()
{
	Dimension d = super.getPreferredSize();
	d = adjustPreferredSize( d );
	return d;
}
protected abstract boolean isAdjacent( Point p, SubDockingBay sdb );
public void paintBorder( Graphics g )
{
	Dimension d = super.getPreferredSize();
	if
		( ( d.width == 0 ) && ( d.height == 0 ) )
	{
		Insets insets = getInsets();
		
		int x0 = insets.top;
		int y0 = insets.top;
		int X = getWidth() - insets.right;
		int Y = getHeight() - insets.top - insets.bottom;


		boolean skipFirst = false;
		g.setColor( Color.white );
		for
			( int x = x0; x < X - 1; x += 2 )
		{
			boolean skip = skipFirst;
			skipFirst = ! skipFirst;
			for
				( int y = y0; y < Y - 1; y += 2 )
			{
				if ( ! skip ) g.drawLine( x, y, x, y );
				skip = ! skip;
			}
		}

		skipFirst = false;
		g.setColor( Color.black );
		for
			( int x = x0 + 1; x < X; x += 2 )
		{
			boolean skip = skipFirst;
			skipFirst = ! skipFirst;
			for
				( int y = y0 + 1; y < Y; y += 2 )
			{
				if ( ! skip ) g.drawLine( x, y, x, y );
				skip = ! skip;
			}
		}
	}
	super.paintBorder( g );
}
}
