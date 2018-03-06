package com.bluebrim.gui.client;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.BevelBorder;

//
 
public class CoToolbar extends JPanel
{
	private JWindow m_window;
	private boolean m_isDocked = true;
	private Window m_owner;
	private Insets m_insets = m_verticalInsets;
	private boolean m_floatable;

	private LayoutManager2 m_horizontalLayout;
	private LayoutManager2 m_verticalLayout;
	
	static final int m_handleSize = 6;
	private static final Insets m_horizontalInsets = new Insets( 1, 1 + m_handleSize, 1, 1 );
	private static final Insets m_verticalInsets = new Insets( 1 + m_handleSize, 1, 1, 1 );
	
	private Window m_dragWindow;
	private Point m_p0;
	private Point m_localP0;

	protected CoToolbarDockingCriteriaIF m_dockingCriteria = CoToolbarDockingCriteria.ANYWHERE;
public CoToolbar()
{
	this( null );
}
public CoToolbar( CoToolbarDockingCriteriaIF dc )
{
	super();

	m_horizontalLayout = createHorizontalLayout();
	m_verticalLayout = createVerticalLayout();

	setAlignmentY( Component.TOP_ALIGNMENT );
	setAlignmentX( Component.LEFT_ALIGNMENT );

	setHorizontal();

	setFloatable( true );

	setDockingCriteria( dc );
}
protected void addImpl( Component comp, Object constraints, int index )
{
	super.addImpl( comp, constraints, index );
	if
		( comp instanceof JComponent )
	{
		( (JComponent) comp ).setAlignmentY( Component.CENTER_ALIGNMENT );
		( (JComponent) comp ).setAlignmentX( Component.CENTER_ALIGNMENT );
	}

}
protected LayoutManager2 createHorizontalLayout()
{
	return new CoRowLayout();//BoxLayout( this, BoxLayout.X_AXIS );
}
protected LayoutManager2 createVerticalLayout()
{
	return new CoColumnLayout();//BoxLayout( this, BoxLayout.Y_AXIS );
}
private void doDrag( MouseEvent e )
{
	m_dragWindow.setLocation( m_p0.x - m_localP0.x + e.getX(), m_p0.y - m_localP0.y + e.getY() );
}
private void endDrag( MouseEvent e )
{
	Point p = m_dragWindow.getLocationOnScreen();
	p.x += m_localP0.x;
	p.y += m_localP0.y;

	Component c = null;
	
	if
		( m_owner instanceof JFrame )
	{	
		c = ( (JFrame) m_owner ).getContentPane();
	} else if
		( m_owner instanceof JDialog )
	{	
		c = ( (JDialog) m_owner ).getContentPane();
	} else {
		c = m_owner.getComponent( 0 );
	}
	
	Component C = c;
	
	Point dp = c.getLocationOnScreen();
	p.x -= dp.x;
	p.y -= dp.y;
	
	Component prev = null;
	while
		( ( c != null ) && ( c != prev ) && ( c instanceof Container ) )
	{
		if
			( c instanceof CoToolbarDockingBay )
		{
			CoToolbarDockingBay tbdb = (CoToolbarDockingBay) c;

			if
				( ! m_dockingCriteria.isDockable( this, tbdb ) )
			{
				break;
			}


			m_isDocked = true;
			p = m_dragWindow.getLocationOnScreen();
			p.x += m_localP0.x;
			p.y += m_localP0.y;
			tbdb.dock( this, p );
			m_dragWindow.setVisible( false );
			m_owner.validate();
			return;
		}

		if
			( c != C )
		{
			dp = c.getLocation();
			p.x -= dp.x;
			p.y -= dp.y;
		}

		prev = c;
		c = ( (Container) c ).getComponentAt( p );
	}

	// drag window was not dropped on a docking bay
	
	m_dragWindow.setVisible( false );

	if
		( m_isDocked )
	{
		if
			( m_window == null )
		{
			m_window = new JWindow( m_owner );
			( (JComponent) m_window.getContentPane() ).setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
		}


		m_window.getContentPane().add( this );
		m_window.pack();
		m_owner.validate();
		m_isDocked = false;
	}

	p = m_dragWindow.getLocation();
	int d = ( (JComponent) m_window.getContentPane() ).getBorder().getBorderInsets( m_window.getContentPane() ).left;
	p.x -= d;
	p.y -= d;
	m_window.setLocation( p );
	m_window.show();
}
public CoToolbarDockingCriteriaIF getDockingCriteria()
{
	return m_dockingCriteria;
}
public boolean getFloatable()
{
	return m_floatable;
}
public Insets getInsets()
{
	return m_insets;
}
public Dimension getMaximumSize()
{
	return getPreferredSize();
}
public Dimension getMinimumSize()
{
	return getPreferredSize();
}
public Dimension getPreferredSize()
{
	if
		( isVisible() )
	{
		return super.getPreferredSize();
	} else {
		return new Dimension( 0, 0 );
	}
}
public boolean isVisible()
{
	if
		( m_isDocked )
	{
		return super.isVisible();
	} else {
		return m_window.isVisible();
	}
}
public void paintBorder( Graphics g )
{
	if
		( m_insets == m_horizontalInsets )
	{
		paintHorizontalBorder( g );
	} else if
		( m_insets == m_verticalInsets )
	{
		paintVerticalBorder( g );
	}

	super.paintBorder( g );
}
private void paintHorizontalBorder( Graphics g )
{
	if ( ! m_floatable ) return;
	
	int x0 = m_insets.top;
	int y0 = m_insets.top;
	int X = m_insets.left - 1;
	int Y = getHeight() - m_insets.top - m_insets.bottom;

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
private void paintVerticalBorder( Graphics g )
{
	if ( ! m_floatable ) return;
	
	int y0 = m_insets.left;
	int x0 = m_insets.left;
	int Y = m_insets.top - 1;
	int X = getWidth() - m_insets.left - m_insets.right;

	boolean skipFirst = false;
	g.setColor( Color.white );
	for
		( int y = y0; y < Y - 1; y += 2 )
	{
		boolean skip = skipFirst;
		skipFirst = ! skipFirst;
		for
			( int x = x0; x < X - 1; x += 2 )
		{
			if ( ! skip ) g.drawLine( x, y, x, y );
			skip = ! skip;
		}
	}

	skipFirst = false;
	g.setColor( Color.black );
	for
		( int y = y0 + 1; y < Y; y += 2 )
	{
		boolean skip = skipFirst;
		skipFirst = ! skipFirst;
		for
			( int x = x0 + 1; x < X; x += 2 )
		{
			if ( ! skip ) g.drawLine( x, y, x, y );
			skip = ! skip;
		}
	}
}
protected void processMouseEvent( MouseEvent e )
{
	int id = e.getID();
	switch
		( id )
	{
		case MouseEvent.MOUSE_PRESSED :
			startDrag( e );
			break;
		
		case MouseEvent.MOUSE_RELEASED :
			endDrag( e );
			break;
		
		case MouseEvent.MOUSE_CLICKED :
		case MouseEvent.MOUSE_EXITED :
		case MouseEvent.MOUSE_ENTERED :
			break;
	}
}
protected void processMouseMotionEvent( MouseEvent e )
{
	if ( e.getID() == MouseEvent.MOUSE_DRAGGED ) doDrag( e );
}
public void setDockingCriteria( CoToolbarDockingCriteriaIF dc )
{
	if ( dc == null ) dc = CoToolbarDockingCriteria.ANYWHERE;
	m_dockingCriteria = dc;
}
public void setFloatable( boolean floatable )
{
	m_floatable = floatable;

	if
		( m_floatable )
	{
		enableEvents( AWTEvent.MOUSE_EVENT_MASK );
		enableEvents( AWTEvent.MOUSE_MOTION_EVENT_MASK );
	} else {
		disableEvents( AWTEvent.MOUSE_EVENT_MASK );
		disableEvents( AWTEvent.MOUSE_MOTION_EVENT_MASK );
	}

}
public void setHorizontal()
{
	m_insets = m_horizontalInsets;
	setLayout( m_horizontalLayout );
}
public void setVertical()
{
	m_insets = m_verticalInsets;
	setLayout( m_verticalLayout );
}
public void setVisible( boolean v )
{
	if
		( m_isDocked )
	{
		super.setVisible( v );
		getParent().getParent().getParent().validate();
	} else {
		m_window.setVisible( v );
	}
}
private void startDrag( MouseEvent e )
{
	if
		( m_owner == null )
	{
		m_owner = (Window) getTopLevelAncestor();
		m_dragWindow = new Window( m_owner );
	}
	
	m_p0 = new Point( getLocationOnScreen() );
	m_localP0 = e.getPoint();
	
	m_dragWindow.setLocation( m_p0 );
	m_dragWindow.setSize( getSize() );

	if ( m_window != null ) m_window.setVisible( false );

	m_dragWindow.show();
}
}
