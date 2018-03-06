package com.bluebrim.swing.client;

import java.awt.event.*;

import javax.swing.*;
/**
 	Subklass till JRadioButton som har en instansvariabel 'buttonGroup' som håller den 
 	grupp som radioknappen tillhör. Normalt så är det gruppen som laddas med sina 
 	knappar men på detta sätt går det att knyta en radioknapp till sin grupp i VCE.
 	@author Lasse Svadängs 971010
 */
public class CoToggleButton extends JToggleButton {
	CoButtonGroup buttonGroup;

	private boolean m_isFocusTraversable = true;
	/**
	 * Subklass till JToggleButton.ToggleButtonModel som tillåter avselektering
	 * av knappar i en ButtonGroup.
	 */
	public static class ToggleButtonModel extends JToggleButton.ToggleButtonModel
	{
		public void setSelected(boolean b)
		{
			if
				( group != null )
			{
					if
						( ! b && isSelected() )
					{
						group.setSelected( null, true );
					} else {
						group.setSelected( this, b );
					}
			} else {
				if
					( b )
				{
					stateMask |= SELECTED;
				} else {
					stateMask &= ~SELECTED;
				}
			}

			fireStateChanged();

			fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, this, this.isSelected() ? ItemEvent.SELECTED : ItemEvent.DESELECTED));
		}	
	}

/**
 * JRadioButton constructor comment.
 */
public CoToggleButton() {
	super();
}
/**
 * JRadioButton constructor comment.
 * @param arg1 java.lang.String
 */
public CoToggleButton(String arg1) {
	super(arg1);
}
/**
 * JRadioButton constructor comment.
 * @param text java.lang.String
 * @param icon javax.swing.Icon
 */
public CoToggleButton(String text, Icon icon) {
	super(text, icon);
}
/**
 * JRadioButton constructor comment.
 */
public CoToggleButton(String text, Icon icon, boolean state) {
	super(text, icon, state);
}
/**
 * JRadioButton constructor comment.
 * @param arg1 java.lang.String
 * @param arg2 boolean
 */
public CoToggleButton(String arg1, boolean arg2) {
	super(arg1, arg2);
}
/**
 * JRadioButton constructor comment.
 * @param icon javax.swing.Icon
 */
public CoToggleButton(Icon icon) {
	super(icon);
}
/**
 * JRadioButton constructor comment.
 * @param icon javax.swing.Icon
 * @param state boolean
 */
public CoToggleButton(Icon icon, boolean state) {
	super(icon, state);
}
/**
 * @param aGroup SE.corren.calvin.userinterface.CoButtonGroup
 */
public CoButtonGroup getButtonGroup () {
	return buttonGroup;
}
public boolean isFocusTraversable()
{
	return m_isFocusTraversable;
}
/**
 * @param aGroup SE.corren.calvin.userinterface.CoButtonGroup
 */
public void setButtonGroup ( CoButtonGroup aGroup) {
	buttonGroup = aGroup;
	aGroup.add(this);
}
public void setFocusTraversable( boolean b )
{
	m_isFocusTraversable = b;
}
}
