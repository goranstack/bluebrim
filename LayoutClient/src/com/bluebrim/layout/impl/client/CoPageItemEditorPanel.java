package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.view.*;
import com.bluebrim.swing.client.*;

/**
 * A CoPanel that implements the workspace of a layout editor.
 * It is the link between swing and page item views.
 * 
 * Auto scrolling:
 *  See CoAutoscroller.
 * 
 * User scale:
 *  The zoom scale selected by the user.
 * 
 * Screen scale:
 *  user scale + compensation for the difference between points (1/72 inch) and screen pixels
 * 
 * @author: Dennis Malmström
 */

public class CoPageItemEditorPanel extends CoPanel implements Scrollable
{
	// pixel / point ratio for screen
	private static double m_screenResolution = Toolkit.getDefaultToolkit().getScreenResolution() / 72.0;

	// screen -> user coordinate transform
	private AffineTransform	m_userToDeviceTransform = new AffineTransform();
	private AffineTransform	m_userToDeviceInverseTransform;
	
	private CoRootView m_rootView;
	private CoViewSelectionManager m_selectionManager = new CoViewSelectionManager();
	
	private Dimension m_preferredSize;
	private Dimension m_modelSize; // scaled model size in screen coordinates

	// cached	graphics for XOR-painting
	private Graphics2D m_xorG;
	private static final Stroke m_onePixelStroke = new BasicStroke( 0 );



	private CoAutoscroller m_autoscroller;
	// see addNotify
	private Component m_monitoredParent;
	private ComponentListener m_parentListener =
		new ComponentListener()
		{
			public void componentHidden( ComponentEvent e ) { m_xorG = null; }
			public void componentMoved( ComponentEvent e ) { m_xorG = null; }
			public void componentResized( ComponentEvent e ) { m_xorG = null; }
			public void componentShown( ComponentEvent e ) { m_xorG = null; }
		};

	private boolean m_supressPainting;


	
	
/*
	// repaint memory: the region that has been sent to super.repaint but hasn't been painted
	private int m_awtX0 = -1;
	private int m_awtX1 = -1;
	private int m_awtY0 = -1;
	private int m_awtY1 = -1;
	*/

	LinkedList m_pendingRepaints = new LinkedList();
	public static boolean TRACE_PAINTING = false;
	public static boolean USE_REPAINT_AREA_SEPARATION = false;

public CoPageItemEditorPanel()
{
	super( (LayoutManager) null );

	setOpaque( true );

	m_userToDeviceTransform.setToScale( m_screenResolution * 1.0, m_screenResolution * 1.0 );


	
  addMouseListener( new MouseAdapter()
	  {
		  public void mouseClicked( MouseEvent e )
  		{
				// secret dialog for testing purposes
				if ( e.isControlDown() && e.isAltDown() && e.isShiftDown() ) CoViewTestUI.open();
  		}
		} );

  setFont( new Font( "monospaced", Font.BOLD, 12 ) );


  if
  	( ! ( RepaintManager.currentManager( this ) instanceof CoRepaintManager ) )
  {
		RepaintManager.setCurrentManager( new CoRepaintManager() );
  }

}
public void addAutoScrollListener( CoAutoScrollListener l )
{
	listenerList.add( CoAutoScrollListener.class, l );
}
/**
 * If the parent of this panel is a JViewport then its size changes must be monitored
 * so the cached xor-painting graphics can be invalidated when the effective clipping
 * area (= parent area in case of viewport) changes.
*/

public void addNotify()
{
	super.addNotify();

	if
		( m_monitoredParent != null )
	{
		m_monitoredParent.removeComponentListener( m_parentListener );
	}

	m_monitoredParent = getParent();

	if
		( ! ( m_monitoredParent instanceof JViewport ) )
	{
		m_monitoredParent = null;
	}
	
	if
		( m_monitoredParent != null )
	{
		m_monitoredParent.addComponentListener( m_parentListener );
	}
}
public void applyTransform( Graphics2D g )
{
	g.transform( m_userToDeviceTransform );
}
private void doPaintComponent( Graphics g, String caller )
{
	if ( TRACE_PAINTING ) System.out.println( "PAINT " + caller + " " + g.getClip() );

	{
		g.setColor( getBackground() );
		Rectangle r = g.getClipBounds();
		g.fillRect( r.x, r.y, r.width, r.height );
	}

	if ( m_rootView == null ) return;

	// prepare g
	Graphics2D g2d = (Graphics2D) g;

	Rectangle bounds = g2d.getClipBounds();

	AffineTransform t = g2d.getTransform();
	applyTransform( g2d );

	// wrap g
	CoScreenPaintable p = CoScreenPaintable.wrap( g2d );

	// paint view tree
	m_rootView.paint( p, bounds );

	p.releaseDelegate();
	
	g2d.setTransform( t );
}
private void fireAutoScrollEvent( CoAutoScrollEvent ev )
{
	Object listeners[] = listenerList.getListenerList();
	
	Class listenerClass = CoAutoScrollListener.class;
	
	for
		( int k = listeners.length - 2; k >= 0; k -= 2 )
	{
		if
			( listeners[ k ] == listenerClass )
		{
			( (CoAutoScrollListener) listeners[ k + 1 ] ).autoScroll( ev );
		}
	}
}
private void flushPendingRepaints()
{
	// perform pending repaints
	int i = m_pendingRepaints.size();
	while
		( i > 0 )
	{
		Rectangle r = (Rectangle) m_pendingRepaints.removeFirst();
		i--;
		repaint( r.x, r.y, r.width, r.height );
	}
}
public CoAutoscroller getAutoscroller( int range )
{
	if
		( m_autoscroller == null )
	{
		m_autoscroller = new CoAutoscroller( this,
			                                   ( (JScrollPane) getParent().getParent() ).getHorizontalScrollBar(),
				                                 ( (JScrollPane) getParent().getParent() ).getVerticalScrollBar(),
				                                 range );

		addAutoScrollListener( m_autoscroller );
	}

	return m_autoscroller;
}
public Dimension getModelSize() 
{
	return m_modelSize;
}
/**
 * getPreferredScrollableViewportSize method comment.
 */
public java.awt.Dimension getPreferredScrollableViewportSize() {
	return null;
}
public Dimension getPreferredSize() 
{
	return ( m_preferredSize != null ) ? m_preferredSize : super.getPreferredSize();
}
public CoRootView getRootView()
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( m_rootView != null, getClass() + ".getRootView()\nNo root view available." );
	return m_rootView;
}
public double getScreenScale() 
{
	return m_userToDeviceTransform.getScaleX();
}
/**
 * getScrollableBlockIncrement method comment.
 */
public int getScrollableBlockIncrement(java.awt.Rectangle visibleRect, int orientation, int direction) {
	if (orientation == SwingConstants.HORIZONTAL) {
		return (int) visibleRect.getWidth();
	} else
		return (int) visibleRect.getHeight();
}
/**
 * getScrollableTracksViewportHeight method comment.
 */
public boolean getScrollableTracksViewportHeight() {
	return false;
}
/**
 * getScrollableTracksViewportWidth method comment.
 */
public boolean getScrollableTracksViewportWidth() {
	return false;
}
/**
 * getScrollableUnitIncrement method comment.
 */
public int getScrollableUnitIncrement(java.awt.Rectangle visibleRect, int orientation, int direction) {
	if (orientation == SwingConstants.HORIZONTAL) {
		return (int) (visibleRect.getWidth() / 10);
	} else
		return (int) (visibleRect.getHeight() / 10);
}
public CoViewSelectionManager getSelectionManager()
{
	return m_selectionManager;
}
public double getUserScale() 
{
	return m_userToDeviceTransform.getScaleX() / m_screenResolution;
}
public Graphics2D getXORGraphics()
{
	if
		( m_xorG == null )
	{
		// create and prepare xor-graphics
		m_xorG = (Graphics2D) getGraphics();
		m_xorG.setXORMode( Color.gray );

		// clip
		if
			( getParent() instanceof JViewport )
		{
			Component c = getParent();
			m_xorG.clipRect( c.getX() - getX(), c.getY() - getY(), c.getWidth(), c.getHeight() );
		} else {
			m_xorG.clipRect( getX(), getY(), getWidth(), getHeight() );
		}
		
		// scale
		m_xorG.scale( m_userToDeviceTransform.getScaleX(), m_userToDeviceTransform.getScaleY() );

		// stroke (1 pixel)
		m_xorG.setStroke( m_onePixelStroke );
	}

	return m_xorG;
}
public boolean hasRootView()
{
	return m_rootView != null;
}
public void paintComponent( Graphics g )
{
	if
		( m_supressPainting )
	{
//System.out.println( "SUPRESSED PAINT " + g.getClip() );
		m_pendingRepaints.addLast( new Rectangle( g.getClipBounds() ) );
		return;
	}

	// clear repaint memory
//	m_awtX0 = m_awtY0 = m_awtX1 = m_awtY1 = -1;
	
//	super.paintComponent( g );

	doPaintComponent( g, "old" );
//	flushPendingRepaints();
}
protected void processAutoScrollEvent( CoAutoScrollEvent e )
{
	fireAutoScrollEvent( e );
}
// intercept auto scroll events

protected void processEvent( AWTEvent e )
{
	if
		( e instanceof CoAutoScrollEvent )
	{
		processAutoScrollEvent( (CoAutoScrollEvent) e );
		return;
	}
	
	super.processEvent( e );
}
public void removeAutoScrollListener( CoAutoScrollListener l )
{
	listenerList.remove( CoAutoScrollListener.class, l );
}
public void repaint()
{
	super.repaint();
}
public void repaint( int x, int y, int w, int h )
{
	/*
	if
		( USE_REPAINT_AREA_SEPARATION )
	{
		boolean sendNow = ( m_awtX0 == -1 ); // repaint memory is empty -> forward this call

		if
			( ! sendNow )
		{
			// if this repaint region intersects the repaint memory then forward this call
			noIntersect:
			{
				if ( x >= m_awtX1 ) break noIntersect;
				if ( y >= m_awtY1 ) break noIntersect;
				if ( x + w <= m_awtX0 ) break noIntersect;
				if ( y + h <= m_awtY0 ) break noIntersect;
				sendNow = true;
			}
		}

		if
			( sendNow )
		{
			// update repaint memory and forward the call
			m_awtX0 = ( m_awtX0 == -1 ) ? x : Math.min( m_awtX0, x );
			m_awtY0 = ( m_awtY0 == -1 ) ? y : Math.min( m_awtY0, y );
			m_awtX1 = ( m_awtX1 == -1 ) ? x + w : Math.max( m_awtX1, x + w );
			m_awtY1 = ( m_awtY1 == -1 ) ? y + h : Math.max( m_awtY1, y + h );
			super.repaint( x, y, w, h );
		} else {
			// save this repaint request for later execution
			m_pendingRepaints.addLast( new Rectangle( x, y, w, h ) );
		}
	} else {
		super.repaint( x, y, w, h );
	}
	*/

	super.repaint( x, y, w, h );
}
/**
 * Invalidate xor-painting graphics when the clipping area changes.
 *
 * Note: if this panel resides in a JViewport then the effective clipping area is
 * the area of the viewport (see addNotify).
*/

public void reshape( int x, int y, int w, int h ) 
{
	super.reshape( x, y, w, h );

	m_xorG = null;
}
protected void screenScaleChanged() 
{
}
public void setRootView( CoRootView rootView )
{
	if ( rootView == m_rootView ) return;

	if
		( m_rootView != null )
	{
		m_rootView.dettachContainer();
		CoRootView.removePageItemListeners( m_rootView );
		m_rootView.dispose();
	}
	
	m_rootView = rootView;
	
	if
		( m_rootView != null )
	{
		m_selectionManager.unselectAllViews();
		m_rootView.attachContainer( this );
	}

	updateSize();
}
public void setScreenScale( double scale ) 
{
	m_userToDeviceTransform.setToScale( scale, scale );
	m_userToDeviceInverseTransform = null;
	
	updateSize();

	m_xorG = null;
	
	if ( m_rootView != null ) m_rootView.setParent( m_rootView.getParent() ); // trigger update of absolute geometry cache


	invalidate();
	if ( getParent() != null ) getParent().getParent().validate();
	repaint();

//	revalidate();

//	m_userScaleValueModel.valueHasChanged();
	screenScaleChanged();
}
public void setSupressPainting(boolean supressPainting)
{
	if
		( ( supressPainting == false ) && ( m_supressPainting == true ) ) 
	{
		m_supressPainting = supressPainting;
		flushPendingRepaints();
//		repaint();
	} else {
		m_supressPainting = supressPainting;
	}
	
}
public void setUserScale( double scale ) 
{
	setScreenScale( scale * m_screenResolution );
}
public void transform( Point2D p )
{
	m_userToDeviceTransform.transform( p, p );
}
public void untransform( Point2D p )
{
	try
	{
		if
			( m_userToDeviceInverseTransform == null )
		{
			m_userToDeviceInverseTransform = m_userToDeviceTransform.createInverse();
		}
		
		m_userToDeviceInverseTransform.transform( p, p );
	}
	catch ( NoninvertibleTransformException e )
	{
	}
}
public void updateSize() 
{
	Insets i = getInsets();

	if
		( m_rootView != null )
	{
		double f = m_userToDeviceTransform.getScaleX();

		double w = m_rootView.getPreferredSize().getWidth();
		double h = m_rootView.getPreferredSize().getHeight();

		m_preferredSize = new Dimension( (int) ( 0.5 + w * f + i.left + i.right ),
			                               (int) ( 0.5 + h * f + i.top + i.bottom ) );
		
		w = m_rootView.getModelSize().getWidth();
		h = m_rootView.getModelSize().getHeight();

		m_modelSize = new Dimension( (int) ( 0.5 + w * f ),
			                           (int) ( 0.5 + h * f ) );
	} else {
		m_preferredSize = null;
		m_modelSize = new Dimension( 10, 10 );
	}
}
}
