package com.bluebrim.swing.client;

import java.awt.Component;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
/**
	Subklass till JToolBar som bygger på dess funktionalitet i två avseenden:
	<ul>
	<li>dels går det nu att explicit sätta en toolbars orientering via #setOrientation.
	<li>dels använder en CoToolBar en CoBoxLayout, en kopia av BoxLayout som ser till
	att varje komponent antar den största komponenten storlek.
	</ul>
	En CoToolBar är framförallt tänkt att användas när toolbaren är den enda komponenten i ett fönster.
 */
public class CoEqualSizeToolBar extends JToolBar {
	int orientation;
/**
 * CoToolBar constructor comment.
 */
public CoEqualSizeToolBar() {
	this(SwingConstants.VERTICAL);
}
/**
 * CoToolBar constructor comment.
 */
public CoEqualSizeToolBar(int orientation) {
	super();
	this.orientation = orientation;
	updateUI();
	setLayout(new CoBoxLayout(this, orientation));
}
public void removeAction(Action a)
{
	String tLabel			= (String)a.getValue(Action.NAME);
	int ncomponents 		= this.getComponentCount();
	Component[] component 	= this.getComponents();
	for (int i = 0; i < ncomponents; i++)
	{
		Component comp = component[i];
		if (comp instanceof JButton && ((JButton)comp).getText().equals(tLabel))
		{
			remove(comp);
			break;
		}
	}

}
/**
	PENDNING! 
*/
public void setOrientation(int o)
{
	if ( orientation != o )
	{
	    int old = orientation;
	    orientation = o;

	    if ( o == VERTICAL )
	        setLayout( new CoBoxLayout( this, BoxLayout.Y_AXIS ) );
	    else
	        setLayout( new CoBoxLayout( this, BoxLayout.X_AXIS ) );

	    firePropertyChange("orientation", old, o);
	    revalidate();
	    repaint();
	}
}
}
