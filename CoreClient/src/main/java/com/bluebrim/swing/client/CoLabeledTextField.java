package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.bluebrim.gui.client.CoLookAndFeelManager;
import com.bluebrim.gui.client.CoMetalTheme;
import com.bluebrim.gui.client.CoUIConstants;
import com.bluebrim.gui.client.CoUserInterfaceBuilder;

/**
 * Background color enhanced. /Markus 2000-04-10
 */
public class CoLabeledTextField extends CoTextField {
	private String 	m_label 			= "";
	private Color m_labelForeground 	= UIManager.getColor(CoUIConstants.LABELED_TEXT_FIELD_FOREGROUND);
	private Color m_labelBackground 	= UIManager.getColor(CoUIConstants.LABELED_TEXT_FIELD_BACKGROUND);
	private Font m_labelFont 			= UIManager.getFont(CoUIConstants.LABELED_TEXT_FIELD_FONT);
	private int 	m_dh 				= m_labelFont.getSize() + 2;
	private Border 	m_textFieldBorder;

	public static final Color PARENT_BACKGROUND = new Color( 0 );



public CoLabeledTextField(int columns, String label)
{
	super(null, columns, 0);
	init( label );
}
public CoLabeledTextField( String label)
{
	super(null, 0, 0);
	init( label );
}
public CoLabeledTextField( String text, int columns, int maxColumns, String label )
{
	super( text, columns, maxColumns );
	
	init( label );
}
public CoLabeledTextField(String text, int columns, String label )
{
	super(text, columns, 0);
	init( label );
}
public CoLabeledTextField(String text, String label)
{
	super(text, 0, 0);
	init( label );
}
public Border _getBorder()
{
	CompoundBorder b = (CompoundBorder) super.getBorder();
	return ( b == null ) ? null : b.getInsideBorder();
}
private Insets _insets()
{
	Insets insets 	= getInsets();
	if (m_textFieldBorder != null)
	{
		Insets _insets = m_textFieldBorder.getBorderInsets(this);
		insets.left 	-= _insets.left;
		insets.top  	-= _insets.top;
		insets.right  	-= _insets.right;
		insets.bottom  	-= _insets.bottom;
	}
	return insets;
}
public String getLabel()
{
	return m_label;
}
public Color getLabelBackground()
{
	return m_labelBackground;
}
public Font getLabelFont()
{
	return m_labelFont;
}
public Color getLabelForeground()
{
	return m_labelForeground;
}
private void init( String label )
{
	m_label = label;
	setTextFieldBorder(super.getBorder());
}
public static void main( String args[] )
{
	FocusListener focusListener = new FocusListener() {
		public void focusGained(FocusEvent e)
		{
			e.getComponent().repaint();
		}
		public void focusLost(FocusEvent e)
		{
			e.getComponent().repaint();
		}
	};
	MetalLookAndFeel.setCurrentTheme( new CoMetalTheme());
	CoLookAndFeelManager.setLookAndFeel( new MetalLookAndFeel());

	CoUserInterfaceBuilder b = new CoUserInterfaceBuilder(null);
	JFrame f = new JFrame();
	f.getContentPane().setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
	
	JPanel panel1	= new JPanel();
	panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS)); 
	CoLabeledTextField field	=  b.createLabeledTextField(20, 30, "Förnamn" , "Förnamn") ;
	field.addFocusListener(focusListener);
	field.setBorder(BorderFactory.createLineBorder(CoUIConstants.LABEL_DARK_BLUE));
	panel1.add(field);
	
	field	=  b.createLabeledTextField( 20, 30, "Efternamn", "Efternamn" ) ;
	field.addFocusListener(focusListener);
	field.setBorder(BorderFactory.createLineBorder(CoUIConstants.LABEL_DARK_BLUE));
	panel1.add(field);

	f.getContentPane().add(panel1);

	JPanel panel2	= new JPanel();
	panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS)); 

	field	=  b.createLabeledTextField( 20, 30, "e-mail", "e-mail" ) ;
	field.addFocusListener(focusListener);
	field.setBorder(BorderFactory.createLineBorder(CoUIConstants.LABEL_DARK_BLUE));
	field.setLabelBackground( CoLabeledTextField.PARENT_BACKGROUND );
	panel2.add(field);

	field	=  b.createLabeledTextField( 20, 30, "Mobil", "Mobil" ) ;
	field.addFocusListener(focusListener);
	field.setBorder(BorderFactory.createLineBorder(CoUIConstants.LABEL_DARK_BLUE));
	field.setLabelBackground( Color.green );
	panel2.add(field);

	f.getContentPane().add(panel2);
	f.pack();
	f.show();
}
public void paint(Graphics g) {
	boolean wasOpaque = isOpaque();

	Insets insets = _insets();
	if (wasOpaque && (m_labelBackground != PARENT_BACKGROUND) && (m_labelBackground != null)) {
		g.setColor(m_labelBackground);
		g.fillRect(insets.left, insets.top, getWidth() - insets.left - insets.right, getHeight() - insets.top - insets.bottom);
		setOpaque(false);
		super.paint(g);
		setOpaque(wasOpaque);
	} else {
		super.paint(g);
	}

	g.setColor(m_labelForeground);
	g.setFont(m_labelFont);
	g.drawString(m_label, 2, m_dh - 1);
}
public void setBorder( Border b )
{
	b = BorderFactory.createCompoundBorder( b, m_textFieldBorder );
	super.setBorder( b );
}
public void setLabel( String label )
{
	m_label = label;
	repaint();
}
public void setLabelBackground( Color bg )
{
	m_labelBackground = bg;
	repaint();
}
public void setLabelFont( Font f )
{
	m_labelFont = f;
	m_dh		= m_labelFont.getSize() + 2;
	repaint();
}
public void setLabelForeground( Color fg )
{
	m_labelForeground = fg;
	repaint();
}
public void setTextFieldBorder( Border b )
{
	m_textFieldBorder = BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder( m_dh, 0, 0, 0 ), b);
	super.setBorder(m_textFieldBorder);
}
}