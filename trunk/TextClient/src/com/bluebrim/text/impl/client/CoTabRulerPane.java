package com.bluebrim.text.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2000-09-12 08:54:23)
 * @author: Dennis
 */
 
public class CoTabRulerPane extends JComponent
{
	private CoTabSetPanel.TabSetEditor m_tabSetEditor;

	private boolean m_doPaintTickValues;
	private boolean m_doTrackValue;
	
	private double m_scale = Double.NaN;

	private double m_x0;
	private double m_x1;
	private double m_regularTabStopInterval = Double.NaN;
	private CoTabSetIF m_tabSet = null;

	private static Line2D m_line = new Line2D.Double();
	
	protected static int m_handleHeight = 6;
	protected static int m_handleWidth = 4;
	
	private GeneralPath m_leftTabShape = new GeneralPath();
	private GeneralPath m_rightTabShape = new GeneralPath();
	private GeneralPath m_centerTabShape = new GeneralPath();
	private GeneralPath m_decimalTabShape = new GeneralPath();
	private GeneralPath m_alignOnTabShape = new GeneralPath();
	private GeneralPath [] m_tabShapes = new GeneralPath [ 5 ];
	{
		m_tabShapes[ CoTabStopIF.ALIGN_LEFT ] = m_leftTabShape;
		m_tabShapes[ CoTabStopIF.ALIGN_CENTER ] = m_centerTabShape;
		m_tabShapes[ CoTabStopIF.ALIGN_RIGHT ] = m_rightTabShape;
		m_tabShapes[ CoTabStopIF.ALIGN_DECIMAL ] = m_decimalTabShape;
		m_tabShapes[ CoTabStopIF.ALIGN_ON ] = m_alignOnTabShape;
	}



	// length unit modes
	
	public interface Mode
	{
		void paint( Graphics2D g, double w, double h );
		double snap( double f );
	};

	private class PointMode implements Mode
	{
		public void paint( Graphics2D g, double w, double h )
		{
			double dx = CoLengthUnit.POINTS.from( 10 );
			int n = 0;
			double x = m_x0 + n * dx;
			while
				( (int) x <= m_x0 )
			{
				m_line.setLine( x, 0, x, h / 4 );
				g.draw( m_line );
				if ( m_doPaintTickValues ) g.drawString( "" + n, (float) x + 1, (float) ( h / 4 ) );
				n++;
				x = m_x0 + n * dx;
			}
		}
		public double snap( double f )
		{
			return CoTabRulerPane.snap( f, CoLengthUnit.POINTS );
		}
	};

	private class MetricMode implements Mode
	{
		public void paint( Graphics2D g, double w, double h )
		{
			double dx = CoLengthUnit.CM.from( 0.5 );
			int n = 0;
			double x = m_x0 + n * dx * 2;
			while
				( (int) x <= m_x1 )
			{
				m_line.setLine( x, 0, x, h / 2 );
				g.draw( m_line );
				if ( m_doPaintTickValues ) g.drawString( "" + n, (float) x + 1, (float) ( h / 2 ) );
				n++;
				x = m_x0 + n * dx * 2;
			}
			n = 0;
			x = m_x0 + n * dx * 2 + dx;
			while
				( (int) x <= m_x1 )
			{
				m_line.setLine( x, 0, x, h / 4 );
				g.draw( m_line );
				n++;
				x = m_x0 + n * dx * 2 + dx;
			}
		}
		public double snap( double f )
		{
			return CoTabRulerPane.snap( f, CoLengthUnit.MM );
		}
	};

	private class InchMode implements Mode
	{
		public void paint( Graphics2D g, double w, double h )
		{
			double dx = CoLengthUnit.INCH.from( 0.25 );
			int n = 0;
			double x = m_x0 + n * dx * 4;
			while
				( (int) x <= m_x0 )
			{
				m_line.setLine( x, 0, x, h / 2 );
				g.draw( m_line );
				if ( m_doPaintTickValues ) g.drawString( "" + n, (float) x + 1, (float) ( h / 2 ) );
				n++;
				x = m_x0 + n * dx * 4;
			}
			n = 0;
			x = m_x0 + n * dx * 2 + dx;
			while
				( (int) x <= m_x0 )
			{
				m_line.setLine( x, 0, x, h / 4 );
				g.draw( m_line );
				n++;
				x = m_x0 + n * dx * 2 + dx;
			}
			n = 0;
			x = m_x0 + n * dx + dx;
			while
				( (int) x <= m_x0 )
			{
				m_line.setLine( x, 0, x, h / 8 );
				g.draw( m_line );
				n++;
				x = m_x0 + n * dx + dx;
			}
		}
		public double snap( double f )
		{
			return CoTabRulerPane.snap( 16 * f, CoLengthUnit.INCH ) / 16;
		}
	};

	public Mode METRIC = new MetricMode();
	public Mode POINT = new PointMode();
	public Mode INCH = new InchMode();
	protected Mode m_mode = METRIC;




	// handles	
	public abstract class Handle implements CoReshapeHandleIF
	{
		boolean m_undefined = false;

		public final int getHandleWidth() { return m_handleWidth; }
		public final int getHandleHeight() { return m_handleHeight; }
		
		public boolean isUndefined() { return m_undefined; }
		public void setUndefined( boolean u ) { m_undefined = u; }
		public int getEdgeMask() { return 0; }
		public double getX() { return 0; }
		public double getY() { return 0; }
		public void move( double dx, double dy ) {}
		public void stop() {}
		public void delete() {}
		public boolean hit( double x, double y )
		{
			if ( y > getY() ) return false;
			if ( y < getY() - getHandleHeight() ) return false;
			return Math.abs( x - getX() ) <= getHandleWidth();
		}
		public abstract void paint( Graphics2D g );
	};



	class TabStopHandle extends Handle
	{
		CoTabStopIF m_tabStop;

		public TabStopHandle( CoTabStopIF tabStop ) { m_tabStop = tabStop; }


		public CoTabStopIF getTabStop() { return m_tabStop; }
		
		public double getX() { return ( m_x0 + m_tabStop.getPosition() ) * m_scale; }
		public double getY() { return getHeight(); }
		public void move( double dx, double dy )
		{
			int i = m_tabSet.getIndexOfTabStop( m_tabStop );
			m_tabSet = m_tabSet.copy();
			m_tabStop = m_tabSet.getTabStop( i );

			m_tabStop.setPosition( (float) m_mode.snap( Math.max( 0, m_tabStop.getPosition() + dx ) ) );

			m_tabSetEditor.setTabSet( m_tabSet, true );
		}
		public void stop()
		{
			m_tabSetEditor.setTabSet( m_tabSet, false );
		}
		public void delete()
		{
			m_tabSet = m_tabSet.copy();

			m_tabSet.removeTabStop( m_tabStop );
			
			if
				( m_tabSet.getTabStopCount() == 0 )
			{	
				m_tabSet = null;
				m_tabSetEditor.unsetTabSet();
			} else {
				m_tabSetEditor.setTabSet( m_tabSet, false );
			}
		}
		public void paint( Graphics2D g )
		{
			g.draw( m_tabShapes[ m_tabStop.getAlignment() ] );
		}
	};



	class RegularTabStopHandle extends Handle
	{
		int m_n;

		public RegularTabStopHandle( int n ) { m_n = n; }
		
		public double getX() { return ( m_x0 + ( 1 + m_n ) * m_regularTabStopInterval ) * m_scale; }
		public double getY() { return getHeight(); }
		public boolean isUndefined() { return m_n != 0; }
		public boolean hit( double x, double y )
		{
			return ( m_n == 0 ) && super.hit( x, y );
		}
		public void move( double dx, double dy )
		{
			m_regularTabStopInterval += dx;
			m_tabSetEditor.setRegularTabStopInterval( (float) m_mode.snap( Math.max( 0, m_regularTabStopInterval ) ), true );
		}
		public void stop()
		{
			m_tabSetEditor.setRegularTabStopInterval( (float) m_mode.snap( Math.max( 0, m_regularTabStopInterval ) ), false );
		}
		public void delete()
		{
			m_tabSetEditor.unsetRegularTabStopInterval();
		}
		public void paint( Graphics2D g ) { g.draw( m_leftTabShape ); }
	};



	private List m_handles = new ArrayList();
	private Handle m_handle;

	
	private CoTabStopIF m_popupMenuOwner;
	private CoPopupMenu m_popupMenu;
	private CoButtonGroup m_alignmentButtonGroup = new CoButtonGroup();
	private CoButtonGroup m_leaderButtonGroup = new CoButtonGroup();
	private CoTextField m_positionTextField;
	private CoTextField m_alignOnTextField;
public CoTabRulerPane( CoTabSetPanel.TabSetEditor editor )
{
	super();

	m_tabSetEditor = editor;
	
	createPopupMenu();
	
	addMouseListener(
		new MouseAdapter()
		{
			public void mouseExited( MouseEvent e )
			{
				CoTabRulerPane.this.repaint();
			}
			public void mousePressed( MouseEvent e )
			{
				CoTabRulerPane.this.mousePressed( e );
			}
			public void mouseClicked( MouseEvent e )
			{
				CoTabRulerPane.this.mouseClicked( e );
			}
			public void mouseReleased( MouseEvent e )
			{
				CoTabRulerPane.this.mouseReleased( e );
			}
		}
	);

	addMouseMotionListener(
		new MouseMotionAdapter()
		{
			public void mouseMoved( MouseEvent e )
			{
				CoTabRulerPane.this.mouseMoved( e );
			}
			public void mouseDragged( MouseEvent e )
			{
				CoTabRulerPane.this.mouseDragged( e );
			}
		}
	);

	registerKeyboardAction(
		new ActionListener() { public void actionPerformed( ActionEvent ev ) { setAlignment( CoTabStopIF.ALIGN_LEFT ); } },
		KeyStroke.getKeyStroke( KeyEvent.VK_LEFT, KeyEvent.CTRL_MASK ),
		WHEN_FOCUSED
	);

	registerKeyboardAction(
		new ActionListener() { public void actionPerformed( ActionEvent ev ) { setAlignment( CoTabStopIF.ALIGN_CENTER ); } },
		KeyStroke.getKeyStroke( KeyEvent.VK_UP, KeyEvent.CTRL_MASK ),
		WHEN_FOCUSED
	);

	registerKeyboardAction(
		new ActionListener() { public void actionPerformed( ActionEvent ev ) { setAlignment( CoTabStopIF.ALIGN_RIGHT ); } },
		KeyStroke.getKeyStroke( KeyEvent.VK_RIGHT, KeyEvent.CTRL_MASK ),
		WHEN_FOCUSED
	);

	registerKeyboardAction(
		new ActionListener() { public void actionPerformed( ActionEvent ev ) { setAlignment( CoTabStopIF.ALIGN_DECIMAL ); } },
		KeyStroke.getKeyStroke( KeyEvent.VK_DOWN, KeyEvent.CTRL_MASK ),
		WHEN_FOCUSED
	);

	
	registerKeyboardAction(
		new ActionListener() { public void actionPerformed( ActionEvent ev ) { setLeader( CoTabStopIF.LEADER_NONE ); } },
		KeyStroke.getKeyStroke( KeyEvent.VK_SPACE, 0 ),
		WHEN_FOCUSED
	);
	
	registerKeyboardAction(
		new ActionListener() { public void actionPerformed( ActionEvent ev ) { setLeader( CoTabStopIF.LEADER_DOTS ); } },
		KeyStroke.getKeyStroke( KeyEvent.VK_PERIOD, 0 ),
		WHEN_FOCUSED
	);
	
	registerKeyboardAction(
		new ActionListener() { public void actionPerformed( ActionEvent ev ) { setLeader( CoTabStopIF.LEADER_UNDERLINE ); } },
		KeyStroke.getKeyStroke( KeyEvent.VK_MINUS, KeyEvent.SHIFT_MASK ),
		WHEN_FOCUSED
	);
	
	registerKeyboardAction(
		new ActionListener() { public void actionPerformed( ActionEvent ev ) { setLeader( CoTabStopIF.LEADER_HYPHENS ); } },
		KeyStroke.getKeyStroke( KeyEvent.VK_MINUS, 0 ),
		WHEN_FOCUSED
	);
	
	registerKeyboardAction(
		new ActionListener() { public void actionPerformed( ActionEvent ev ) { setLeader( CoTabStopIF.LEADER_EQUALS ); } },
		KeyStroke.getKeyStroke( KeyEvent.VK_EQUALS, 0 ),
		WHEN_FOCUSED
	);
}
private static CoCheckboxMenuItem createCheckBoxMenuItem( CoMenuBuilder b, CoMenu m, String name, CoButtonGroup g )
{
	CoCheckboxMenuItem mi = b.addCheckBoxMenuItem( m, name );
	mi.setActionCommand( name );
	g.add( mi );
	return mi;
}
protected void createHandles( List handles )
{
	if
		( m_tabSet != null )
	{
		int I = m_tabSet.getTabStopCount();
		for
			( int i = 0; i < I; i++ )
		{
			CoTabStopIF t = m_tabSet.getTabStop( i );
			TabStopHandle h = new TabStopHandle( t );
			handles.add( h );
		}
	} else {
		
		if
			( ! Double.isNaN( m_regularTabStopInterval ) )
		{
			int n = 0;
			while
				( ( n + 1 ) * m_regularTabStopInterval < m_x1 - m_x0 )
			{
				RegularTabStopHandle h = new RegularTabStopHandle( n );
				handles.add( h );
				n++;
				if ( m_regularTabStopInterval <= 0 ) break;
			}
		}
	}
	
}
private void createPopupMenu()
{
	CoMenuBuilder b = new CoMenuBuilder( null);

	m_popupMenu = b.createPopupMenu();

	CoMenu m;


	
	m = b.addPopupSubMenu( m_popupMenu, CoTextStringResources.getName( CoTabSetPanel.TAB_ALIGNMENT ) );
	
	class AlignmentAction implements ActionListener
	{
		private int m_value;

		public AlignmentAction( int v ) { m_value = v; }
		
		public void actionPerformed( ActionEvent ev )
		{
			setAlignment( m_popupMenuOwner, m_value );
		}
	};
	
	for
		( int i = 0; i < CoTabSetPanel.ALIGNMENT_VALUES.length; i++ )
	{
		CoCheckboxMenuItem mi = createCheckBoxMenuItem( b, m, CoTabSetPanel.ALIGNMENT_VALUES[ i ], m_alignmentButtonGroup );
		mi.addActionListener( new AlignmentAction( i ) );
	}


	
	m = b.addPopupSubMenu( m_popupMenu, CoTextStringResources.getName( CoTabSetPanel.TAB_POSITION ) );
	m.add( m_positionTextField = new CoNumericTextField( CoLengthUnit.LENGTH_UNITS, 15 ) );
	m_positionTextField.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				m_positionTextField.getParent().setVisible( false );
				m_popupMenu.setVisible( false );
				if ( m_tabSet == null ) return;

				Number n = null;
				try
				{
					n = NumberFormat.getInstance( Locale.getDefault() ).parse( m_positionTextField.getText() );
				}
				catch ( ParseException ex )
				{
					return;
				}

				int i = m_tabSet.getIndexOfTabStop( m_popupMenuOwner );
				m_tabSet = m_tabSet.copy();
				m_popupMenuOwner = m_tabSet.getTabStop( i );
				m_popupMenuOwner.setPosition( n.floatValue() );
				m_tabSetEditor.setTabSet( m_tabSet, false );
				repaint();
			}
		}
	);


	
	m = b.addPopupSubMenu( m_popupMenu, CoTextStringResources.getName( CoTabSetPanel.TAB_LEADER ) );
	
	class LeaderAction implements ActionListener
	{
		private int m_value;

		public LeaderAction( int v ) { m_value = v; }
		
		public void actionPerformed( ActionEvent ev )
		{
			setLeader( m_popupMenuOwner, m_value );
		}
	};

	for
		( int i = 0; i < CoTabSetPanel.LEADER_VALUES.length; i++ )
	{
		CoCheckboxMenuItem mi = createCheckBoxMenuItem( b, m, CoTabSetPanel.LEADER_VALUES[ i ], m_leaderButtonGroup );	
		mi.addActionListener( new LeaderAction( i ) );
	}

	
	
	m = b.addPopupSubMenu( m_popupMenu, CoTextStringResources.getName( CoTabSetPanel.TAB_ALIGN_ON ) );
	m.add( m_alignOnTextField = new CoTextField( 10 ) );
	m_alignOnTextField.addActionListener(
		new AbstractAction()
		{
			public void actionPerformed( ActionEvent ev )
			{
				m_alignOnTextField.getParent().setVisible( false );
				m_popupMenu.setVisible( false );
				if ( m_tabSet == null ) return;
				int i = m_tabSet.getIndexOfTabStop( m_popupMenuOwner );
				m_tabSet = m_tabSet.copy();
				m_popupMenuOwner = m_tabSet.getTabStop( i );
				m_popupMenuOwner.setAlignOn( m_alignOnTextField.getText() );
				m_tabSetEditor.setTabSet( m_tabSet, false );
				repaint();
			}
		}
	);
}
protected List getHandles()
{
	if
		( m_handles.isEmpty() )
	{
		createHandles( m_handles );
	}
	
	return m_handles;
}
public Mode getMode()
{
	return m_mode;
}
public boolean getPaintTickValues()
{
	return m_doPaintTickValues;
}
public final double getScale()
{
	return m_scale;
}
public boolean getTrackValue()
{
	return m_doTrackValue;
}
public final double getX0()
{
	return m_x0;
}
public final double getX1()
{
	return m_x1;
}
private boolean handlePopupMenu( MouseEvent e )
{
	if ( ! e.isPopupTrigger() ) return false;

	Handle h = hit( e.getX(), e.getY() );
	if
		( ( h != null ) && ( h instanceof TabStopHandle ) )
	{
	
		m_popupMenuOwner = ( (TabStopHandle) h ).getTabStop();

		m_alignmentButtonGroup.setSelected( CoTabSetPanel.ALIGNMENT_VALUES[ m_popupMenuOwner.getAlignment() ] );
		m_leaderButtonGroup.setSelected( CoTabSetPanel.LEADER_VALUES[ m_popupMenuOwner.getLeader() ] );
		m_positionTextField.setText( NumberFormat.getInstance( Locale.getDefault() ).format( m_popupMenuOwner.getPosition() ) );
		m_alignOnTextField.setText( new String( m_popupMenuOwner.alignOn() ) );
		boolean b = ( m_popupMenuOwner.getAlignment() == CoTabStopIF.ALIGN_ON );
		m_alignOnTextField.setEnabled( b );
		m_alignOnTextField.setEditable( b );
		
		m_popupMenu.show( this, e.getX(), e.getY() );
	}

	return true;
}
private Handle hit( int x, int y )
{
	Iterator i = getHandles().iterator();
	while
		( i.hasNext() )
	{
		Handle H = (Handle) i.next();

		if
			( H.hit( x, y ) )
		{
			return H;
		}
	}

	return null;
}
protected void invalidateHandles()
{
	m_handles.clear();
}
private void mouseClicked( MouseEvent e )
{
	if
		( handlePopupMenu( e ) )
	{
		return;
	}

	if ( ( e.getModifiers() & InputEvent.BUTTON1_MASK ) == 0 ) return;
	
	int x = e.getX();
	int y = e.getY();
	
	if
		( m_tabSet == null )
	{
		m_tabSet = new com.bluebrim.text.impl.shared.CoTabSet();
	} else {
		m_tabSet = m_tabSet.copy();
	}

	CoTabStopIF s = m_tabSet.addTabStop();
	s.setPosition( (float) m_mode.snap( x / m_scale - m_x0 ) );

	m_tabSetEditor.setTabSet( m_tabSet, false );
	repaint();
}
private void mouseDragged( MouseEvent e )
{
	if ( ( e.getModifiers() & InputEvent.BUTTON1_MASK ) == 0 ) return;
	
	if ( m_handle == null ) return;

	double dx = e.getX() - m_handle.getX();

	m_handle.move( dx / m_scale, 0 );
	repaint();
}
private void mouseMoved( MouseEvent e )
{
	int x = e.getX();
	int y = e.getY();

	m_handle = hit( x, y );

	if
		( m_handle == null )
	{
		setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
	} else {
		setCursor( Cursor.getPredefinedCursor( Cursor.W_RESIZE_CURSOR ) );
	}
}
private void mousePressed( MouseEvent e )
{
	if
		( handlePopupMenu( e ) )
	{
		return;
	}
	
	if ( ( e.getModifiers() & InputEvent.BUTTON1_MASK ) == 0 ) return;
	
	int x = e.getX();
	int y = e.getY();

	m_handle = hit( x, y );
	repaint();
}
private void mouseReleased( MouseEvent e )
{
	if
		( handlePopupMenu( e ) )
	{
		return;
	}
	
	if ( ( e.getModifiers() & InputEvent.BUTTON1_MASK ) == 0 ) return;
	
	if ( m_handle == null ) return;

	if
		( e.getY() < 0 )
	{
		m_handle.delete();
	} else {
		m_handle.stop();
	}
	
	m_handle = null;
	repaint();
}
protected void paintComponent( Graphics g )
{
	super.paintComponent( g );

	Graphics2D G = (Graphics2D) g;

	double s = CoBaseUtilities.getXScale( ( (Graphics2D) g ).getTransform() );
	if
		( s != m_scale )
	{
		m_scale = s;
		updateShapes();
	}
	
	double w = getWidth() / m_scale;
	double h = getHeight() / m_scale;
	
	G.setColor( getBackground() );
	G.fillRect( 0, 0, (int) w, (int) h );
	
	G.setColor( getForeground() );
	m_line.setLine( m_x0, 0, m_x1, 0 );
	G.draw( m_line );

	m_mode.paint( G, w, h );

	
	Iterator i = getHandles().iterator();
	while
		( i.hasNext() )
	{
		Handle H = (Handle) i.next();

		G.setColor( H.isUndefined() ? Color.lightGray : getForeground() );
		G.translate( H.getX() / m_scale, H.getY() / m_scale );
		H.paint( G );
		G.translate( - H.getX() / m_scale, - H.getY() / m_scale );	
	}

	if
		( m_doTrackValue && ( m_handle != null ) )
	{
		Font f = G.getFont();
		G.setColor( getForeground() );
		G.drawString( CoLengthUnitSet.format( m_handle.getX() / m_scale, CoLengthUnit.LENGTH_UNITS ), (float) ( 20 + m_handle.getX() ), getHeight() );//1 + f.getSize2D() );
	}
}
private void setAlignment( int a )
{
	if
		( ( m_handle != null ) && ( m_handle instanceof TabStopHandle ) )
	{
		CoTabStopIF s = ( (TabStopHandle) m_handle ).getTabStop();
		setAlignment( s, a );
	}
}
private void setAlignment( CoTabStopIF s, int a )
{
	if ( m_tabSet == null ) return;					
	int i = m_tabSet.getIndexOfTabStop( s );
	m_tabSet = m_tabSet.copy();
	s = m_tabSet.getTabStop( i );
	s.setAlignment( a );
	
	m_tabSetEditor.setTabSet( m_tabSet, false );
	repaint();
}
private void setLeader( int l )
{
	if
		( ( m_handle != null ) && ( m_handle instanceof TabStopHandle ) )
	{
		CoTabStopIF s = ( (TabStopHandle) m_handle ).getTabStop();
		setLeader( s, l );
	}
}
private void setLeader( CoTabStopIF s, int l )
{
	if ( m_tabSet == null ) return;					
	int i = m_tabSet.getIndexOfTabStop( s );
	m_tabSet = m_tabSet.copy();
	s = m_tabSet.getTabStop( i );
	s.setLeader( l );
	
	m_tabSetEditor.setTabSet( m_tabSet, false );
	repaint();
}
public void setMode( Mode m )
{
	m_mode = m;
	repaint();
}
public void setPaintTickValues( boolean b )
{
	m_doPaintTickValues = b;
	repaint();
}
public void setRange( double x0, double x1 )
{
	m_x0 = x0;
	m_x1 = x1;

	repaint();
}
public void setRegularTabStopInterval( double regularTabStopInterval )
{
	m_regularTabStopInterval = regularTabStopInterval;
	invalidateHandles();
	repaint();
}
public void setTabSet( CoTabSetIF s )
{
	m_tabSet = s;
	invalidateHandles();
	repaint();
}
public void setTrackValue( boolean b )
{
	m_doTrackValue = b;
}
private static double snap( double f, CoLengthUnit u )
{
	return u.from( Math.round( u.to( f ) ) );
}
protected void updateShapes()
{
	float w = (float) ( m_handleWidth / m_scale) ;
	float h = - (float) ( m_handleHeight / m_scale );
	
	m_leftTabShape.reset();
	m_leftTabShape.moveTo( w, h );
	m_leftTabShape.lineTo( 0, h );
	m_leftTabShape.lineTo( 0, 0 );
	
	m_rightTabShape.reset();
	m_rightTabShape.moveTo( -w, h );
	m_rightTabShape.lineTo( 0, h );
	m_rightTabShape.lineTo( 0, 0 );
	
	m_centerTabShape.reset();
	m_centerTabShape.moveTo( w / 2, h );
	m_centerTabShape.lineTo( -w / 2, h );
	m_centerTabShape.moveTo( 0, h );
	m_centerTabShape.lineTo( 0, 0 );

	m_decimalTabShape.reset();
	m_decimalTabShape.moveTo( w, h );
	m_decimalTabShape.lineTo( w, h );
	m_decimalTabShape.moveTo( 0, h );
	m_decimalTabShape.lineTo( 0, 0 );

	float d = (float) ( 1 / m_scale );
	m_alignOnTabShape.reset();
	m_alignOnTabShape.moveTo( w - d, h );
	m_alignOnTabShape.lineTo( w + d, h + 2 * d );
	m_alignOnTabShape.moveTo( w - d, h + 2 * d );
	m_alignOnTabShape.lineTo( w + d, h );
	m_alignOnTabShape.moveTo( 0, h );
	m_alignOnTabShape.lineTo( 0, 0 );
}
}