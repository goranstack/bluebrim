package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import com.bluebrim.gui.client.CoUIConstants;

/**
 * Mostly copied from CoLabeledTextField. Unneccesary
 * stuff removed.
 *
 * @author Markus Persson 2001-03-20
 */
public class CoIconLabeledTextField extends CoTextField {
	private Icon m_icon;
	private String m_label;
	private Color m_labelForeground = UIManager.getColor(CoUIConstants.LABELED_TEXT_FIELD_FOREGROUND);
	private Color m_labelBackground = UIManager.getColor(CoUIConstants.LABELED_TEXT_FIELD_BACKGROUND);
	private Font m_labelFont 		= UIManager.getFont(CoUIConstants.LABELED_TEXT_FIELD_FONT);
	private int m_dh;
	private Border m_textFieldBorder;

	public static final Color PARENT_BACKGROUND = new Color(0);









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

	m_icon.paintIcon(this, g, 1, 1);
	g.setColor(m_labelForeground);
	g.setFont(m_labelFont);
	g.drawString(m_label, m_icon.getIconWidth() + 3, m_dh - 2);
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
public void setLabelFont(Font f) {
	m_labelFont = f;
	m_dh = Math.max(m_icon.getIconHeight() + 1, m_labelFont.getSize() + 2);
	repaint();
}
public void setLabelForeground( Color fg )
{
	m_labelForeground = fg;
	repaint();
}
public void setTextFieldBorder(Border b) {
	m_textFieldBorder = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(m_dh, 0, 0, 0), b);
	super.setBorder(m_textFieldBorder);
}



public CoIconLabeledTextField(int columns, Icon icon, String label) {
	super(null, columns, 0);
	init(icon, label);
}

public CoIconLabeledTextField(String text, int columns, int maxColumns, Icon icon, String label) {
	super(text, columns, maxColumns);
	init(icon, label);
}

public CoIconLabeledTextField(Icon icon, String label) {
	super(null, 0, 0);
	init(icon, label);
}

private void init(Icon icon, String label) {
	m_icon = icon;
	m_label = label;
	m_dh = Math.max(m_icon.getIconHeight() + 1, m_labelFont.getSize() + 2);
	setTextFieldBorder(super.getBorder());
}
}