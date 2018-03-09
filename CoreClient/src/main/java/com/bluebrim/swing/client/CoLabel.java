package com.bluebrim.swing.client;

import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
	Subklassar JLabel för att implementera #setFont och #getFont.
	Skall en egenskap kunna sättas i VCE så måste klassen implementera
	både get- och setmetod. Det räcker inte att den ena eller andra
	redan är implementerade i superklassen!!!
 */
public class CoLabel extends JLabel {
/**
 * CoLabel constructor comment.
 */
public CoLabel() {
	super();
}
/**
 * CoLabel constructor comment.
 */
public CoLabel(String label) {
	super(label);
}
/**
 * CoLabel constructor comment.
 */
public CoLabel(String label, Icon icon, int alignment) {
	super(label, icon, alignment);
}
/**
 * CoLabel constructor comment.
 */
public CoLabel(Icon anIcon) {
	super(anIcon);
}
	/**
	 * Gets the font of the component. If the component does
	 * not have a font, the font of its parent is returned.
	 * @see #setFont
	 */
	public Font getFont() {
		return super.getFont();
	}
/**
 */
public void setFont(java.awt.Font arg1)
{
	super.setFont(arg1);
}
}