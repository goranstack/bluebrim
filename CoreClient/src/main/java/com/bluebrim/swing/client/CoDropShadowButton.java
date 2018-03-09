package com.bluebrim.swing.client;

import java.awt.Insets;
import java.awt.Point;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
	Buttonklass som har en border i form av en mjuk skugga.
 */
public class CoDropShadowButton extends CoButton {
/**
 * DropShadowButton constructor comment.
 */
public CoDropShadowButton() {
	super();
	setDropOffset(8,8);
}
/**
 * DropShadowButton constructor comment.
 */
public CoDropShadowButton(String label) {
	super(label);
	setDropOffset(8,8);
}
/**
 * DropShadowButton constructor comment.
 */
public CoDropShadowButton(String label, Icon icon) {
	super(label,icon);
	setDropOffset(8,8);
}
/**
 * DropShadowButton constructor comment.
 */
public CoDropShadowButton(AbstractAction a) {
	super(a);
	setDropOffset(8,8);
}
/**
 * DropShadowButton constructor comment.
 */
public CoDropShadowButton(Icon icon) {
	super(icon);
	setDropOffset(8,8);
}
/**
 * This method was imported from a .class file.
 * No source code is available.
 */
public Point getDropOffset()
{
	Insets tBorderInsets = getBorder().getBorderInsets(this);
	return new Point(tBorderInsets.right, tBorderInsets.bottom);
}
/**
 */
public void setDropOffset(int x, int y)
{
	setBorder(new CoDropShadowButtonBorder(x,y));
}
/**
 */
public void updateUI()
{
	setUI(new CoDropShadowButtonUI());
}
}
