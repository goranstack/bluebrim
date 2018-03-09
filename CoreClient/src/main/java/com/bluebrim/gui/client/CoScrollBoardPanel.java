package com.bluebrim.gui.client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

/**
 * Creation date: (2000-09-07 15:53:17)
 * @author: Dennis
 */
 
public class CoScrollBoardPanel extends JComponent
{
	private JScrollPane m_model;

	private AdjustmentListener m_scrollbarListener =
		new AdjustmentListener()
		{
			public void adjustmentValueChanged( AdjustmentEvent ev )
			{
				update();
			}
		};
	private int m_W;
	private int m_H;
	private int m_w;
	private int m_h;
	private int m_x;
	private int m_y;

	protected double m_scale;

	private Color m_viewportColor = Color.black;

	
	public final Mode CONTINUOUS_SCROLL_MODE = new ContinuousScrollMode();
	public final Mode DISCRETE_SCROLL_MODE = new DiscreteScrollMode();
	
	private Mode m_mode;


	
	public class Mode implements MouseListener, MouseMotionListener
	{
		protected boolean m_isActive = false;
		protected int m_x0;
		protected int m_y0;
		protected int m_x1;
		protected int m_y1;

		public void mouseClicked( MouseEvent e ) {}
		public void mouseEntered( MouseEvent e ) {}
		public void mouseExited( MouseEvent e ) {}
		public void mousePressed( MouseEvent e )
		{
			m_x0 = e.getX();
			m_y0 = e.getY();
			m_x1 = m_x0;
			m_y1 = m_y0;
			m_isActive = ( ( e.getModifiers() & e.CTRL_MASK ) != 0 ) && ( hit( m_x0, m_y0 ) == INSIDE );
			if ( ! m_isActive ) xor( m_x0, m_y0, m_x1, m_y1 );
		}
		public void mouseReleased( MouseEvent e )
		{
			xor( m_x0, m_y0, m_x1, m_y1 );
			m_x1 = e.getX();
			m_y1 = e.getY();
			if
				( ( m_x0 == m_x1 ) && ( m_y0 == m_y1 ) )
			{
				moveTo( m_x0, m_y0 );
			} else {
				areaSelected( m_x0, m_y0, m_x1, m_y1 );
			}
		}
		
		public void mouseDragged( MouseEvent e )
		{
			xor( m_x0, m_y0, m_x1, m_y1 );
			m_x1 = e.getX();
			m_y1 = e.getY();
			xor( m_x0, m_y0, m_x1, m_y1 );
		}
		
		public void mouseMoved( MouseEvent e )
		{
			if
				( ( e.getModifiers() & e.CTRL_MASK ) == 0 )
			{
				setCursor( m_cursors[ OUTSIDE ] );
			} else {
				setCursor( m_cursors[ hit( e.getX(), e.getY() ) ] );
			}
		}
		
		protected int hit( int x, int y )
		{
			if ( x < m_x - 5 ) return OUTSIDE;
			if ( x > m_x + m_w + 5 ) return OUTSIDE;
			if ( y < m_y - 5 ) return OUTSIDE;
			if ( y > m_y + m_h + 5 ) return OUTSIDE;
			/*
			if ( x <= m_x ) return ON_LEFT;
			if ( x >= m_x + m_w ) return ON_RIGHT;
			if ( y <= m_y ) return ON_TOP;
			if ( y >= m_y + m_h ) return ON_BOTTOM;
*/
			return INSIDE;
		}

	};


	private class ContinuousScrollMode extends Mode
	{
		private int m_mouseX;
		private int m_mouseY;
		
		public void mousePressed( MouseEvent e )
		{
			super.mousePressed( e );
			if ( ! m_isActive ) return;
			m_mouseX = e.getX();
			m_mouseY = e.getY();
		}
		
		public void mouseDragged( MouseEvent e )
		{
			if
				( m_isActive )
			{
				int dx = (int) ( ( e.getX() - m_mouseX ) / m_scale );
				int dy = (int) ( ( e.getY() - m_mouseY ) / m_scale );

				m_mouseX = e.getX();
				m_mouseY = e.getY();

				BoundedRangeModel brm;
				brm = m_model.getHorizontalScrollBar().getModel();
				brm.setValue( brm.getValue() + dx );
				
				brm = m_model.getVerticalScrollBar().getModel();
				brm.setValue( brm.getValue() + dy );
			} else {
				super.mouseDragged( e );
			}
		}
	};


	private class DiscreteScrollMode extends Mode
	{
		private int m_x;
		private int m_y;
		private int m_mouseX;
		private int m_mouseY;
		private Graphics m_graphics;
		
		public void mousePressed( MouseEvent e )
		{
			super.mousePressed( e );
			if ( ! m_isActive ) return;
			
			m_mouseX = e.getX();
			m_mouseY = e.getY();
			m_x = CoScrollBoardPanel.this.m_x;
			m_y = CoScrollBoardPanel.this.m_y;
			m_graphics = getGraphics();
			m_graphics.setColor( m_viewportColor );
			m_graphics.setXORMode( Color.white );
		}
		
		public void mouseReleased( MouseEvent e )
		{
			if 
				( m_isActive )
			{			
				int dx = (int) ( ( m_x - CoScrollBoardPanel.this.m_x ) / m_scale );
				int dy = (int) ( ( m_y - CoScrollBoardPanel.this.m_y ) / m_scale );

				BoundedRangeModel brm;
				brm = m_model.getHorizontalScrollBar().getModel();
				brm.setValue( brm.getValue() + dx );
				
				brm = m_model.getVerticalScrollBar().getModel();
				brm.setValue( brm.getValue() + dy );
			} else {
				super.mouseReleased( e );
			}
		}
		
		public void mouseDragged( MouseEvent e )
		{
			if 
				( m_isActive )
			{			
				paintViewport( m_graphics, m_x, m_y );
				
				int dx = e.getX() - m_mouseX;
				int dy = e.getY() - m_mouseY;

				m_mouseX = e.getX();
				m_mouseY = e.getY();

				m_x += dx;
				m_y += dy;

				paintViewport( m_graphics, m_x, m_y );
			} else {
				super.mouseDragged( e );
			}
		}
	};
public CoScrollBoardPanel()
{
	this( null );
}
public CoScrollBoardPanel( JScrollPane model )
{
	this( true, model );
}
public Mode getMode()
{
	return m_mode;
}
public JScrollPane getModel()
{
	return m_model;
}
public Dimension getPreferredSize()
{
	Dimension d = super.getPreferredSize();

	if
		( m_model != null )
	{
		double w = d.getWidth();
		double h = d.getHeight();
		
		BoundedRangeModel brm = m_model.getHorizontalScrollBar().getModel();
		double W = ( brm.getMaximum() - brm.getMinimum() );

		brm = m_model.getVerticalScrollBar().getModel();
		double H = ( brm.getMaximum() - brm.getMinimum() );

		if
			( ( w / h ) < ( W / H ) )
		{
			h = w * H / W;
		} else {
			w = h * W / H;
		}

		d.setSize( w, h );
	}
	
	return d;
}
public Color getViewportColor()
{
	return m_viewportColor;
}
protected void paintComponent( Graphics g )
{
	super.paintComponent( g );

	g.setColor( getBackground() );
	g.fillRect( 0, 0, getWidth(), getHeight() );

	if ( m_model == null ) return;

	g.setColor( getForeground() );
	g.fillRect( 0, 0, m_W, m_H );

	paintModelDetails( g );
	paintModelScrollState( g );


}
protected void paintModelDetails( Graphics g )
{
}
protected void paintModelScrollState( Graphics g )
{

	paintViewport( g, m_x, m_y );
}
protected void paintViewport( Graphics g, int x, int y )
{
	g.setColor( m_viewportColor );
	g.drawRect( x, y, m_w - 1, m_h - 1 );
}
public void reshape( int x, int y, int w, int h )
{
	super.reshape( x, y, w, h );
	
	update();
}
public void setMode( Mode m )
{
	if
		( m_mode != null )
	{
		removeMouseListener( m_mode );
		removeMouseMotionListener( m_mode );
	}

	m_mode = m;

	if
		( m_mode != null )
	{
		addMouseListener( m_mode );
		addMouseMotionListener( m_mode );
	}
}
public void setModel( JScrollPane model )
{
	if
		( m_model != null )
	{
		m_model.getHorizontalScrollBar().removeAdjustmentListener( m_scrollbarListener );
		m_model.getVerticalScrollBar().removeAdjustmentListener( m_scrollbarListener );
	}
	
	m_model = model;

	if
		( m_model != null )
	{
		m_model.getHorizontalScrollBar().addAdjustmentListener( m_scrollbarListener );
		m_model.getVerticalScrollBar().addAdjustmentListener( m_scrollbarListener );
	}

	update();
}
public void setViewportColor( Color vpc )
{
	m_viewportColor = vpc;
	repaint();
}
protected void update()
{
	if ( m_model == null ) return;

	double width = getWidth();
	double height = getHeight();
	
	BoundedRangeModel brm = m_model.getHorizontalScrollBar().getModel();
	double W = ( brm.getMaximum() - brm.getMinimum() );
	double w = ( brm.getExtent() );
	double x = ( brm.getValue() );

	brm = m_model.getVerticalScrollBar().getModel();
	double H = ( brm.getMaximum() - brm.getMinimum() );
	double h = ( brm.getExtent() );
	double y = ( brm.getValue() );


	if
		( ( width / height ) < ( W / H ) )
	{
		m_scale = width / W;
	} else {
		m_scale = height / H;
	}

	m_W = (int) ( W * m_scale );
	m_w = (int) ( w * m_scale );
	m_x = (int) ( x * m_scale );

	m_H = (int) ( H * m_scale );
	m_h = (int) ( h * m_scale );
	m_y = (int) ( y * m_scale );

	repaint();
}

	private static final int INSIDE = 1;
	private boolean m_allowZooming;
	private static final Cursor [] m_cursors =
		new Cursor []
		{
			Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ),
			Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR ),
			Cursor.getPredefinedCursor( Cursor.W_RESIZE_CURSOR ),
			Cursor.getPredefinedCursor( Cursor.E_RESIZE_CURSOR ),
			Cursor.getPredefinedCursor( Cursor.N_RESIZE_CURSOR ),
			Cursor.getPredefinedCursor( Cursor.S_RESIZE_CURSOR ),
		};
	private Rectangle m_zoom;
	private static final int ON_BOTTOM = 5;
	private static final int ON_LEFT = 2;
	private static final int ON_RIGHT = 3;
	private static final int ON_TOP = 4;
	private static final int OUTSIDE = 0;

public CoScrollBoardPanel( boolean allowZooming, JScrollPane model )
{
	super();

	setModel( model );
	setMode( DISCRETE_SCROLL_MODE );
	setForeground( Color.white );

	m_allowZooming = allowZooming;
}

private void areaSelected( int x0, int y0, int x1, int y1 )
{
	if ( ! m_allowZooming ) return;

	m_zoom = normalize( x0, y0, x1, y1, m_zoom );

	
	zoom( (double) m_w / (double) m_zoom.width );

	
	int dx = (int) ( ( m_zoom.x - m_x ) / m_scale );
	int dy = (int) ( ( m_zoom.y - m_y ) / m_scale );

	BoundedRangeModel brm;
	brm = m_model.getHorizontalScrollBar().getModel();
	brm.setValue( brm.getValue() + dx );
	
	brm = m_model.getVerticalScrollBar().getModel();
	brm.setValue( brm.getValue() + dy );
}

private void moveTo( int x, int y )
{
	int dx = (int) ( ( x - m_w / 2 - m_x ) / m_scale );
	int dy = (int) ( ( y - m_h / 2 - m_y ) / m_scale );

	BoundedRangeModel brm;
	brm = m_model.getHorizontalScrollBar().getModel();
	brm.setValue( brm.getValue() + dx );
	
	brm = m_model.getVerticalScrollBar().getModel();
	brm.setValue( brm.getValue() + dy );
}

private Rectangle normalize( int x0, int y0, int x1, int y1, Rectangle r )
{
	if ( r == null ) r = new Rectangle();
	
	double d = m_w / (double) m_h;
	if
		( d > 0 )
	{
		int w = x1 - x0;
		int h = y1 - y0;

		double k = Math.abs( w / ( h * d ) );
		if
			( k < 1 )
		{
			w = (int) ( w / k );
		} else {
			h = (int) ( h * k );
		}

		x1 = x0 + w;
		y1 = y0 + h;
	}

	int x = Math.min( x0, x1 );
	int y = Math.min( y0, y1 );
	int w = Math.abs( x1 - x0 );
	int h = Math.abs( y1 - y0 );

	if
		( x < 0 )
	{
		w += x;
		x = 0;
	}

	if
		( x + w > m_W )
	{
		w = m_W - x;
	}

	if
		( y < 0 )
	{
		h += y;
		y = 0;
	}

	if
		( y + h > m_H )
	{
		h = m_H - y;
	}

	
	r.setBounds( x, y, w, h );
	
	return r;
}

private void xor( int x0, int y0, int x1, int y1 )
{
	if ( ! m_allowZooming ) return;
	
	Graphics g = getGraphics();
	g.setXORMode( Color.white );
	g.setColor( Color.black );

	m_zoom = normalize( x0, y0, x1, y1, m_zoom );
	
	g.drawRect( m_zoom.x, m_zoom.y, m_zoom.width, m_zoom.height );
	
	g.setPaintMode();
}

protected void zoom( double deltaScale )
{
}
}