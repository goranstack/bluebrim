package com.bluebrim.swing.client;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;

import com.bluebrim.gui.client.CoPopupGestureListener;
import com.bluebrim.gui.client.CoRowLayout;
import com.bluebrim.gui.client.CoUserInterfaceBuilder;
import com.bluebrim.menus.client.CoPopupMenu;

/**
 * Component for controlling zoom scale
 * Creation date: (2001-09-12 11:08:26)
 * @author: Dennis Malmström
 */
 
public class CoZoomPanel extends CoPanel
{
	private CoTextField m_zoomTextField;
	private CoPopupMenu m_menu;

	private double m_decimalTruncator;
	private double m_zoomableScale = 1;
	
	private Zoomable m_zoomable;

	public interface Zoomable
	{
		double getScale();
		void setScale( double scale ); // 0 = adjust to fit
	};

	public static class ZoomableProxy implements Zoomable
	{
		private final Zoomable m_zoomable;

		public ZoomableProxy( Zoomable zoomable )
		{
			m_zoomable = zoomable;
		}

		public double getScale()
		{
			return m_zoomable.getScale();
		}
		
		public void setScale( double scale )
		{
			m_zoomable.setScale( scale );
		}
	};
public CoZoomPanel( CoUserInterfaceBuilder b, String text, int decimalCount, double [] menu, String adjustToFitLabel )
{
	super( new CoRowLayout( 3 ) );

	m_decimalTruncator = 1;
	while
		( decimalCount-- > 0 )
	{
		m_decimalTruncator *= 10;
	}
	
	b.preparePanel( this );

	setBorder( BorderFactory.createLoweredBevelBorder() );

	add( b.createLabel( text ) );
	
	m_zoomTextField = b.createSlimTextField( CoTextField.RIGHT, 4 );
	m_zoomTextField.setText( "100" );
	add( m_zoomTextField );

	
	m_zoomTextField.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				try
				{
					setZoomScale( Double.parseDouble( m_zoomTextField.getText() ) );
				}
				catch ( NumberFormatException ex )
				{
					Toolkit.getDefaultToolkit().beep();
					m_zoomTextField.selectAll();
				}
			}
		}
	);

	CoLabel l = b.createLabel( "%" );
	add( l );

	
	if
		( ( menu != null ) && ( menu.length > 0 ) )
	{
		m_menu = new CoPopupMenu();

		m_zoomTextField.addMouseListener(new CoPopupGestureListener(m_menu));


		class ScaleAction extends AbstractAction
		{
			private double m_scale;
			
			public ScaleAction( double scale )
			{
				super( Double.toString( scale ) + " %" );
				m_scale = scale;
			}
			
			public ScaleAction( String label )
			{
				super( label );
				m_scale = 0;
			}

			public void actionPerformed( ActionEvent ev )
			{
				setScale( m_scale );
			}
		};

		for
			( int i = 0; i < menu.length; i++ )
		{
			if
				( menu[ i ] == 0 )
			{
				if
					( adjustToFitLabel != null )
				{
					m_menu.add( new ScaleAction( adjustToFitLabel ) );
				}
			} else {
				m_menu.add( new ScaleAction( menu[ i ] ) );
			}
		}
	}

	setZoomable( null );
}

public void setScale( double scale )
{
	m_zoomTextField.setText( Double.toString( scale ) );
	m_zoomTextField.postActionEvent();
}
public void setZoomable( Zoomable z )
{
	setZoomable( z, 1 );
}
public void setZoomable( Zoomable z, double zoomableScale )
{
	m_zoomableScale = zoomableScale;

	m_zoomable = z;
	m_zoomTextField.setEnabled( m_zoomable != null );

	update();
}
private void setZoomScale( double scale )
{
	if
		( m_zoomable != null )
	{
		m_zoomable.setScale( scale / m_zoomableScale );
		if ( scale == 0 ) update();
	}
}
public void update()
{
	if
		( m_zoomable == null )
	{
		m_zoomTextField.setText( "" );
	} else {
		double d = Math.round( m_zoomableScale * m_zoomable.getScale() * m_decimalTruncator ) / m_decimalTruncator;
		m_zoomTextField.setText( Double.toString( d ) );
	}
}
}
