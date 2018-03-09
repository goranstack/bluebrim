package com.bluebrim.shape.client;

import com.bluebrim.swing.client.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import com.bluebrim.base.shared.geom.CoPolygonShape;
import com.bluebrim.base.shared.CoReshapeHandleIF;

/**
 * Insert the type's description here.
 * Creation date: (2000-08-09 17:11:59)
 * @author: Dennis
 */
 
public class CoPolygonEditor extends JComponent
{
	private double m_xSpan = 72;
	private double m_ySpan = 1;
	private CoPolygonShape m_shape;

	private CoArrowButton m_increaseYButton;
	private CoArrowButton m_decreaseYButton;
	private CoArrowButton m_increaseXButton;
	private CoArrowButton m_decreaseXButton;
	
	private transient double m_mouseX;
	private transient double m_mouseY;
	private transient CoReshapeHandleIF m_handle;
	private transient boolean m_isFirstHandle;
	private transient double m_xMin;
	private transient double m_xMax;

	private transient Line2D m_line = new Line2D.Double();
	private transient Rectangle2D m_rect = new Rectangle2D.Double();
	private transient Stroke m_stroke0 = new BasicStroke( 0 );
	private transient Stroke m_stroke1 = new BasicStroke( 1 );
	

public CoPolygonEditor()
{
	super();
	
	setOpaque( true );
	setBackground( Color.white );

	setBorder( BorderFactory.createEmptyBorder( 2, 2, 0, 0 ) );
	
	setLayout( new CoAttachmentLayout() );
	
	m_increaseYButton = new CoArrowButton( SwingConstants.NORTH );
	m_decreaseYButton = new CoArrowButton( SwingConstants.SOUTH );
	m_increaseXButton = new CoArrowButton( SwingConstants.EAST );
	m_decreaseXButton = new CoArrowButton( SwingConstants.WEST );

	m_decreaseXButton.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent e ) { setXSpan( getXSpan() / 2 ); } } );
	m_increaseXButton.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent e ) { setXSpan( getXSpan() * 2 ); } } );
	m_decreaseYButton.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent e ) { setYSpan( getYSpan() / 2 ); } } );
	m_increaseYButton.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent e ) { setYSpan( getYSpan() * 2 ); } } );

	add( m_increaseYButton,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );
	add( m_decreaseYButton,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, m_increaseYButton ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );
	add( m_increaseXButton,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_COMPONENT_LEFT, 0, m_decreaseYButton ) ) );
	add( m_decreaseXButton,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_BOTTOM, 0, m_increaseXButton ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_COMPONENT_LEFT, 0, m_increaseXButton ) ) );




	
	addKeyListener(
		new KeyAdapter()
		{
			public void keyPressed( KeyEvent e ) { CoPolygonEditor.this.keyPressed( e ); }
		}
	);

	addMouseListener(
		new MouseAdapter()
		{
			public void mouseReleased( MouseEvent e ) { CoPolygonEditor.this.mouseReleased( e ); }
			public void mouseEntered( MouseEvent e ) { requestFocus(); }
		}
	);

	addMouseMotionListener(
		new MouseMotionAdapter()
		{
			public void mouseDragged( MouseEvent e ) { CoPolygonEditor.this.mouseDragged( e ); }
			public void mouseMoved( MouseEvent e ) { CoPolygonEditor.this.mouseMoved( e ); }
		}
	);

	

}
private void calcMousePosition( MouseEvent e )
{
	Insets i = getInsets();

	double w = getWidth() - i.left - i.right - getExtraXInset();
	double h = getHeight() - i.top - i.bottom - getExtraYInset();
	
	if
		( ( w > 0 ) && ( h > 0 ) )
	{
		m_mouseX = ( e.getX() - i.left ) * m_xSpan / w;
		m_mouseY = m_ySpan - ( ( e.getY() - i.top ) * m_ySpan * 2 / h );
		
		m_mouseX = snapX( m_mouseX );
		m_mouseY = snapY( m_mouseY );
	} else {
		m_mouseX = Double.NaN;
		m_mouseY = Double.NaN;
	}
}
private double getExtraXInset()
{
	return m_decreaseYButton.getWidth() + 2;
}
private double getExtraYInset()
{
	return m_decreaseXButton.getWidth() + 2;
}
public CoPolygonShape getPolygon()
{
	return m_shape;
}
public int getXSpan()
{
	return (int) m_xSpan;
}
public double getYSpan()
{
	return m_ySpan;
}
private void keyPressed( KeyEvent e )
{
	if ( m_shape == null ) return;
	
	if
		( ( e.getKeyCode() == KeyEvent.VK_DELETE ) || ( e.getKeyCode() == KeyEvent.VK_BACK_SPACE ) )
	{
		if
			( m_handle != null )
		{
			if
				( m_shape.getPointCount() > 2 )
			{
				m_shape.removePoint( m_handle.getX(), m_handle.getY() );
				m_handle = null;
				setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
				repaint();
			}
		}
		
	} else if
		( e.getKeyCode() == KeyEvent.VK_A )
	{
		if
			( m_handle != null )
		{
			if
				( m_handle.getX() < m_xMax )
			{
				CoPolygonShape.m_usePointHandles = true;
				CoReshapeHandleIF H [] = m_shape.getReshapeHandles();
				CoPolygonShape.m_usePointHandles = false;

				for
					( int n = 0; n < H.length; n++ )
				{
					if
						( H[ n ] == m_handle )
					{
						m_shape.insertPoint( n + 1, m_handle.getX() + 1, m_handle.getY() );
						repaint();
						break;
					}
				}
			}
		}
	}



}
/**
 * Insert the method's description here.
 * Creation date: (2000-08-09 17:37:40)
 * @param args java.lang.String[]
 */
public static void main(String[] args)
{
	CoPolygonEditor p = new CoPolygonEditor();
//	p.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );

	CoPolygonShape s = new CoPolygonShape();
	s.addPoint( 0, 0 );
	s.addPoint( 36, 0 );
	s.addPoint( 72, 1 );
	s.addPoint( 73, 1 );
	p.setPolygon( s );

	JFrame f = new JFrame();
	f.getContentPane().add( p );
	
	f.pack();
	f.show();
}
private void mouseDragged( MouseEvent e )
{
	if ( m_shape == null ) return;
	
	if ( m_handle == null ) return;
	
	calcMousePosition( e );
	
	double x = m_mouseX;
	if      ( m_isFirstHandle ) x = 0;
	else if ( x < m_xMin )      x = m_xMin;
	else if ( x > m_xMax )      x = m_xMax;

	double dx = x - m_handle.getX();
	double dy = m_mouseY - m_handle.getY();
	
	m_handle.move( dx, dy );

	repaint();
}
private void mouseMoved( MouseEvent e )
{
	if ( m_shape == null ) return;
	
	CoReshapeHandleIF oldHandle = m_handle;
	
	calcMousePosition( e );

	Insets i = getInsets();
	double w = getWidth() - i.left - i.right - getExtraXInset();
	double h = getHeight() - i.top - i.bottom - getExtraYInset();
	
	CoPolygonShape.m_usePointHandles = true;
	CoReshapeHandleIF H [] = m_shape.getReshapeHandles();
	CoPolygonShape.m_usePointHandles = false;

	m_handle = null;
	for
		( int n = 0; n < H.length; n++ )
	{
		double dx = ( H[ n ].getX() - m_mouseX ) * w / m_xSpan;
		double dy = ( H[ n ].getY() - m_mouseY ) * h / ( m_ySpan * 2 );

		if
			( dx * dx + dy * dy < 9 )
		{
			m_handle = H[ n ];
			if
				( n == 0 )
			{
				m_xMin = 0;
			} else {
				m_xMin = H[ n - 1 ].getX() + 1;
			}
			if
				( n == H.length - 1 )
			{
				m_xMax = Double.POSITIVE_INFINITY;
			} else {
				m_xMax = H[ n + 1 ].getX() - 1;
			}
			m_isFirstHandle = n == 0;
			setCursor( Cursor.getPredefinedCursor( Cursor.CROSSHAIR_CURSOR ) );
			break;
		}
	}

	if
		( oldHandle != m_handle )
	{
		if
			( m_handle == null )
		{
			setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
		}
		repaint();
	}
}
private void mouseReleased( MouseEvent e )
{
	if ( m_shape == null ) return;
	
	if ( m_handle == null ) return;

	mouseMoved( e );

	repaint();
}
protected void paintComponent( Graphics g )
{
	super.paintComponent( g );
	
	Graphics2D G = (Graphics2D) g;

	Insets i = getInsets();

	G.setColor( getBackground() );
	G.fillRect( 0, 0, (int) getWidth(), (int) getHeight() );
	
	G.translate( i.left, i.top );

	double w = getWidth() - i.left - i.right - getExtraXInset();
	double h = getHeight() - i.top - i.bottom - getExtraYInset();

	G.setColor( getForeground() );
	
	if
		( ( w > 0 ) && ( h > 0 ) )
	{
		double xScale = w / m_xSpan;
		double yScale = h / ( m_ySpan * 2 );

		G.scale( xScale, yScale );
		G.translate( 0, m_ySpan );

		Stroke s = G.getStroke();
		G.setStroke( m_stroke1 );

		// y-axis
		{
			float x = 5.0f;// / xScale;
			for
				( int n = 1; n <= 5; n++ )
			{
				double y = m_ySpan * n / 5.0;

				if ( n == 5 ) x *= 2;
				
				m_line.setLine( 0, 0, x, 0 );
				G.translate( 0, y );
				G.scale( 1 / xScale, 1 / yScale );
				G.draw( m_line );
				G.scale( xScale, yScale );
				G.translate( 0, - 2 * y );
				G.scale( 1 / xScale, 1 / yScale );
				G.draw( m_line );
				G.scale( xScale, yScale );
				G.translate( 0, y );
			}
				
			G.setFont( getFont() );
			float dy = getFont().getSize2D();
			double y = m_ySpan;

			G.translate( 0, - y );
			G.scale( 1 / xScale, 1 / yScale );
			G.drawString( y + "", x + 3, dy );
			G.scale( xScale, yScale );
			G.translate( 0, y );
			
			G.translate( 0, y );
			G.scale( 1 / xScale, 1 / yScale );
			G.drawString( ( - y ) + "", x + 3, -2 );
			G.scale( xScale, yScale );
			G.translate( 0, - y );
		}

		// x-axis
		{
			G.scale( 1 / xScale, 1 / yScale );
			m_line.setLine( 0, 0, w, 0 );
			G.draw( m_line );
			G.scale( xScale, yScale );


			for
				( int n = 1; n <= 10; n++ )
			{
				double x = m_xSpan * n / 10.0;
				double y = 5.0;
				
				G.translate( x, 0 );
				G.scale( 1 / xScale, 1 / yScale );
				m_line.setLine( 0, y, 0, -y );
				G.draw( m_line );
				G.scale( xScale, yScale );
				G.translate( -x, 0 );
				
			}

			String str = Integer.toString( (int) m_xSpan );
			float strW = (float) getFont().getStringBounds( str, G.getFontRenderContext() ).getWidth();
			
			G.translate( m_xSpan, 0 );
			G.scale( 1 / xScale, 1 / yScale );
			G.drawString( str, - strW - 2, -10f );
			G.scale( xScale, yScale );
			G.translate( -m_xSpan, 0 );
		}

		if
			( m_shape != null )
		{
			// curve
			G.scale( 1, -1 );
			G.setStroke( m_stroke0 );
			G.draw( m_shape.getShape() );

			// handles
			{
				G.setStroke( m_stroke1 );
				
				CoPolygonShape.m_usePointHandles = true;
				CoReshapeHandleIF H [] = m_shape.getReshapeHandles();
				CoPolygonShape.m_usePointHandles = false;
				m_rect.setRect( - 2, - 2, 5.0, 5.0 );
				int N = H.length;
				for
					( int n = 0; n < N; n++ )
				{
					G.translate( H[ n ].getX(), H[ n ].getY() );
					G.scale( 1 / xScale, 1 / yScale );
					G.setColor( getBackground() );
					G.fill( m_rect );
					G.setColor( getForeground() );
					G.draw( m_rect );
					G.scale( xScale, yScale );
					G.translate( - H[ n ].getX(), - H[ n ].getY() );
				}

				// extrapolation
				if
					( H[ N - 1 ].getX() < m_xSpan )
				{
					G.setStroke( m_stroke0 );
					double x0 = H[ N - 2 ].getX();
					double x1 = H[ N - 1 ].getX();
					double y0 = H[ N - 2 ].getY();
					double y1 = H[ N - 1 ].getY();
					double x = m_xSpan;
					double y = ( ( x0 - x ) * y1 + ( x - x1 ) * y0 ) / ( x0 - x1 );
					m_line.setLine( x1, y1, x, y );
					G.draw( m_line );
				}
			}
			
			G.scale( 1, -1 );

			// handle position
			if
				( m_handle != null )
			{
				double y = ( (double) Math.round( m_handle.getY() * 10 ) ) / 10.0;
				String str = Integer.toString( (int) m_handle.getX() ) + " " + y;
				float strW = (float) getFont().getStringBounds( str, G.getFontRenderContext() ).getWidth();
				
				G.scale( 1 / xScale, 1 / yScale );
				G.drawString( str, (float) ( w - strW ), (float) ( h / 2 ) );
				G.scale( xScale, yScale );
			}
		}
		
		
		G.setStroke( s );
		
		G.translate( 0, - m_ySpan );
		G.scale( 1 / xScale, 1 / yScale );
	}
	
	G.translate( - getInsets().left, - getInsets().top );
}
public void setPolygon( CoPolygonShape shape )
{
	if
		( shape != m_shape )
	{
		m_shape = shape;
	
		firePropertyChange( "poplygon", Boolean.TRUE, Boolean.FALSE );

		repaint();
	}
}
public void setXSpan( int x )
{
	if ( x < 1 ) x = 1;
		
	if
		( x != (int) m_xSpan )
	{
		m_xSpan = x;
	
		firePropertyChange( "x-span", Boolean.TRUE, Boolean.FALSE );

		repaint();
	}
}
public void setYSpan( double y )
{
	if ( y <= 0 ) y = 1;
	
	if
		( y != m_ySpan )
	{
		m_ySpan = y;
	
		firePropertyChange( "y-span", null, null );

		repaint();
	}
}

protected double snapX( double x )
{
	return Math.round( x );
}

protected double snapY( double y )
{
	return Math.round( y );
}
}