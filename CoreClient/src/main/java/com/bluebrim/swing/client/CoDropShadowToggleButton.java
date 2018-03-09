package com.bluebrim.swing.client;

import java.awt.Insets;
import java.awt.Point;
import java.beans.Beans;

import javax.swing.JToggleButton;

import com.bluebrim.resource.shared.CoResourceLoader;

/**
 * Subklass till JToggleButton som har en mjuk skugga som border. 
 */
public class CoDropShadowToggleButton extends JToggleButton {
	CoButtonGroup buttonGroup = null;
/**
 * DropShadowButton constructor comment.
 */
public CoDropShadowToggleButton() {
	super();
	setDropOffset(8,8);
	if (Beans.isDesignTime())
	{
		setText("Namnlös");
		setIcon(CoResourceLoader.loadIcon(getClass(),"DefaultIcon.gif"));
	}	
}
/**
 * @param aGroup SE.corren.calvin.userinterface.CoButtonGroup
 */
public CoButtonGroup getButtonGroup () {
	return buttonGroup;
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
 * @param aGroup SE.corren.calvin.userinterface.CoButtonGroup
 */
public void setButtonGroup ( CoButtonGroup aGroup) {
	buttonGroup = aGroup;
	aGroup.add(this);
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
	setUI(new CoDropShadowToggleButtonUI());
}
}
