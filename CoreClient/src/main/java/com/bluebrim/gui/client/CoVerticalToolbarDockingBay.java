package com.bluebrim.gui.client;

import java.awt.*;

/**
 * 
 */

public class CoVerticalToolbarDockingBay extends CoToolbarDockingBay
{
	private class SubDockingBay extends CoToolbarDockingBay.SubDockingBay
	{
		public SubDockingBay()
		{
			super();
			setLayout( new CoColumnLayout() );//BoxLayout( this, BoxLayout.Y_AXIS ) );
		}

		protected void addImpl( Component comp, Object constraints, int index )
		{
			super.addImpl( comp, constraints, index );
			( (CoToolbar) comp ).setVertical(); 
		}
	};
public CoVerticalToolbarDockingBay()
{
	super();
	setLayout( new CoRowLayout() );//BoxLayout( this, BoxLayout.X_AXIS ) );
}
protected Dimension adjustPreferredSize( Dimension d )
{
	if
		( d.width == 0 )
	{
		d = new Dimension( m_minimumSize, d.height );
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
		( p.y < b.y + tb2.m_handleSize )
	{
		dockBefore( tb, tb2 );
	} else if
		( p.y > b.height + b.y - tb2.m_handleSize )
	{
		dockAfter( tb, tb2 );
	} else if
		( p.x < b.x + b.width / 3 )
	{
		dockLeft( tb, tb2 );
	} else if
		( p.x > b.x + 2 * b.width / 3 )
	{
		dockRight( tb, tb2 );
	} else if
		( p.y < b.y + b.height / 2 )
	{
		dockBefore( tb, tb2 );
	} else {
		dockAfter( tb, tb2 );
	}
}
private void dockAfter( CoToolbar tb, CoToolbar tb2 )
{
	int i = getIndexOf( tb2 ) + 1;
	if
		( i == getParent().getComponentCount() )
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
protected void dockBy( CoToolbar tb, CoToolbar tb2 )
{
	dockRight( tb, tb2 );
}
private void dockLeft( CoToolbar tb, CoToolbar tb2 )
{
	int i = getIndexOf( tb2.getParent() );
	add( tb, i );
}
private void dockRight( CoToolbar tb, CoToolbar tb2 )
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
protected boolean isAdjacent( Point p, CoToolbarDockingBay.SubDockingBay sdb )
{
	return ( p.x >= sdb.getX() );
}
}
