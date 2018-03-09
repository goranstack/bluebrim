package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

/**
 */

public class CoHorizontalToolbarDockingBay extends CoToolbarDockingBay
{
	private class SubDockingBay extends CoToolbarDockingBay.SubDockingBay
	{
		public SubDockingBay()
		{
			super();
			setLayout( new CoRowLayout() );//BoxLayout( this, BoxLayout.X_AXIS ) );
		}

		protected void addImpl( Component comp, Object constraints, int index )
		{
			super.addImpl( comp, constraints, index );
			( (CoToolbar) comp ).setHorizontal();
		}

	}; 
public CoHorizontalToolbarDockingBay()
{
	super();
	setLayout( new CoColumnLayout() );//BoxLayout( this, BoxLayout.Y_AXIS ) );
}
protected Dimension adjustPreferredSize(Dimension d)
{
	if
		( d.height == 0 )
	{
		d = new Dimension(d.width, m_minimumSize);
	}
	return d;
}
protected CoToolbarDockingBay.SubDockingBay createSubDockingBay()
{
	return new SubDockingBay();
}
protected void dock( CoToolbar tb, CoToolbar tb2, Point p )
{
	Rectangle b = tb2.getBounds();
	if
		( p.x < b.x + tb2.m_handleSize )
	{
		dockBefore( tb, tb2 );
	} else if
		( p.x > b.width + b.x - tb2.m_handleSize )
	{
		dockAfter( tb, tb2 );
	} else if
		( p.y < b.y + b.height / 3 )
	{
		dockAbove( tb, tb2 );
	} else if
		( p.y > b.y + 2 * b.height / 3 )
	{
		dockBelow( tb, tb2 );
	} else if
		( p.x < b.x + b.width / 2 )
	{
		dockBefore( tb, tb2 );
	} else {
		dockAfter( tb, tb2 );
	}
}
private void dockAbove( CoToolbar tb, CoToolbar tb2 )
{
	int i = getIndexOf( tb2.getParent() );
	add( tb, i );
}
private void dockAfter( CoToolbar tb, CoToolbar tb2 )
{
	int i = getIndexOf( tb2 ) + 1;
	if
		( i == tb2.getParent().getComponentCount() )
	{
		tb2.getParent().add( tb );
	} else {
		tb2.getParent().add( tb, i );
	}
}
private void dockBefore( CoToolbar tb, CoToolbar tb2 )
{
	int i = getIndexOf( tb2 );
	tb2.getParent().add( tb, i );
}
private void dockBelow( CoToolbar tb, CoToolbar tb2 )
{
	int i = getIndexOf( tb2.getParent() ) + 1;
	if
		( i == getComponentCount() )
	{
		add( tb );
	} else {
		add( tb, i );
	}
}
protected void dockBy( CoToolbar tb, CoToolbar tb2 )
{
	dockBelow( tb, tb2 );
}
protected boolean isAdjacent( Point p, CoToolbarDockingBay.SubDockingBay sdb )
{
	return ( p.y >= sdb.getY() );
}
}
