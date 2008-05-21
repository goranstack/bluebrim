package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.bluebrim.gui.client.CoColumnLayout;
import com.bluebrim.gui.client.CoUIConstants;
import com.bluebrim.gui.client.CoUserInterfaceBuilder;

/**
 * 
 */
 
public class CoLabeledBorder implements Border
{
	private String 	m_label = "";

	private Color m_background;
	private Font m_font;
	private static Map m_fontMap = new HashMap();
	private Color m_foreground;
	private Insets m_insets = null;//new Insets( 0, 5, 10, 15 );
	private boolean m_isOpaque = true;
	
	public static final Color PARENT_BACKGROUND = new Color( 0 );
	public static final CoLabeledBorder STANDARD = new CoLabeledBorder( null, null, null, null, true, null );
	public static final CoLabeledBorder TRANSPARENT = new CoLabeledBorder( null, null, null, PARENT_BACKGROUND, true, null );
	public static final CoLabeledBorder LABELED_TEXT_FIELD_BACKGROUND = new CoLabeledBorder( null, null, null, UIManager.getColor(CoUIConstants.LABELED_TEXT_FIELD_BACKGROUND), true, new Insets( 3,3,3,3) );


	private boolean m_isRecursing = false;

public void applyTo( JComponent c )
{
	Border b = c.getBorder();

	if
		( b == null )
	{
		b = this;
	} else {
		b = BorderFactory.createCompoundBorder( this, b );
	}
	
	c.setBorder( b );
}

public Insets getBorderInsets( Component c )
{
	Insets i = null;
	
	if
		( m_insets == null )
	{
		i = new Insets( 0, 0, 0, 0 );
	} else {
		i = (Insets) m_insets.clone();
	}
	
	i.top += getFont( c ).getSize() + 2;
	
	return i;
}

private Font getFont( Component c )
{
	return ( m_font == null ) ? getLabelFont( c.getFont() ) : m_font;
}

private Color getForeground( Component c )
{
	return ( m_foreground == null ) ? c.getForeground() : m_foreground;
}

private String getLabel( Component c )
{
	return ( m_label == null ) ? c.getName() : m_label;
}

private Font getLabelFont( Font f )
{
	Font lf = (Font) m_fontMap.get( f );

	if
		( lf == null )
	{
		lf = f.deriveFont( f.getSize() - 2f );
		m_fontMap.put( f, lf );
	}

	return lf;
}

public boolean isBorderOpaque()
{
	return m_isOpaque;
}

public void paintBorder( Component c, Graphics g, int x, int y, int width, int height )
{
	Font f = getFont( c );
	int h = f.getSize() + 2;
//	h = height;
	
//x += m_d;
//y += m_d;
//if ( c.getName().equals("Efternamn") )System.err.println( c + "\n" + g.getClip() );

	int top = 0;
	int bottom = 0;
	int left = 0;
	int right = 0;

	if
		( m_insets != null )
	{
		top = m_insets.top;
		bottom = m_insets.bottom;
		left = m_insets.left;
		right = m_insets.right;
	}
	
	if
		( isBorderOpaque() )
	{
		Color bg = null;
		
		if
			( m_background == PARENT_BACKGROUND )
		{
			Container p = c.getParent();
			while
				( ( p != null ) && ( ! p.isOpaque() ) )
			{
				p = p.getParent();
			}

			if
				( p != null )
			{
				bg = p.getBackground();
			}
		} else if
			( m_background != null )
		{
			bg = m_background;
		}

		if
			( bg != null )
		{
			g.setColor( bg );
			g.fillRect( x, y, width - 1, h + top );
			if ( bottom > 0 ) g.fillRect( x, height - bottom, width - 1, bottom );
			if ( left > 0 ) g.fillRect( x, y, left, height );
			if ( right > 0 ) g.fillRect( width - right, y, right, height );
		}
	}


	g.setFont( f );
	g.setColor( getForeground( c ) );
	g.drawString( getLabel( c ), x + 2 + left, y + h - 2 + top );
}



private int getWidth( Component c )
{
	Graphics g = c.getGraphics();
	if ( g == null ) return 0;
	
	FontMetrics m = g.getFontMetrics();
	return m.stringWidth( getLabel( c ) );
}

public CoLabeledBorder( String label, Font f, Color fg, Color bg, boolean isOpaque, Insets insets )
{
	m_label = label;
	
	m_font = f;

	m_background = bg;
	m_foreground = fg;

	m_isOpaque = isOpaque;

	m_insets = insets;
}

public static void main( String args[] )
{
	CoUserInterfaceBuilder b = new CoUserInterfaceBuilder(null);
	final JFrame f = new JFrame();
	f.getContentPane().setLayout( new CoColumnLayout( 1, true ) );
	f.getContentPane().setBackground( Color.pink );

	CoLabeledBorder greenBorder = new CoLabeledBorder( null, null, null, Color.green, true, new Insets( 2,2,2,2) );
	
	
	CoOptionMenu om	=  b.createOptionMenu();
	om.addItem( "arne" );
	om.addItem( "bart" );
	om.addItem( "bert" );
	om.addItem( "bengt" );
	om.addItem( "curt" );
	om.setName( "Förnamn" );
	f.getContentPane().add(om);
//	TRANSPARENT.applyTo( field );
	greenBorder.applyTo( om );
	
	CoTextField field	=  b.createTextField("Förnamn") ;
	field.setName( "Förnamn" );
	field.setText( "Arne" );
	field.setBorder(BorderFactory.createLineBorder(CoUIConstants.LABEL_DARK_BLUE));
	f.getContentPane().add(field);
//	TRANSPARENT.applyTo( field );
	greenBorder.applyTo( field );
	
	
	field	=  b.createTextField( "Efternamn" ) ;
	field.setText( "Anka" );
	field.setBorder(BorderFactory.createLineBorder(CoUIConstants.LABEL_DARK_BLUE));
	f.getContentPane().add(field);
//	STANDARD.applyTo( field );
	greenBorder.applyTo( field );


	

	field	=  b.createTextField( "e-mail" ) ;
	field.setText( "arne.anka@ankeborg.toon" );
	field.setBorder(BorderFactory.createLineBorder(CoUIConstants.LABEL_DARK_BLUE));
	f.getContentPane().add(field);
	greenBorder.applyTo( field );

	field	=  b.createTextField( "Mobil" ) ;
	field.setText( "007-1234567" );
	field.setBorder(BorderFactory.createLineBorder(CoUIConstants.LABEL_DARK_BLUE));
	f.getContentPane().add(field);
	greenBorder.applyTo( field );

	field.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
//				f.dispose();
				System.exit( 0 );
			}
		}
	);
	f.pack();
	f.show();
}
}