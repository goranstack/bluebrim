package com.bluebrim.swing.client;

import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.event.EventListenerList;

import com.bluebrim.gui.client.CoButtonGroupAdaptor;

/**
 * Subklass till ButtonGroup som postar ett CoSelectedButtonEvent
 * n�r en ny knapp har selekterats.
 * <br>Kan anv�ndas tillsammans med en CoButtonGroupAdaptor f�r att
 * koppla ihop sig med en CoAspectAdaptor vars v�rde h�ller 'actionCommand'
 * f�r selekterad knapp.
 *
 * @see CoButtonGroupAdaptor 
 * @author Lasse Svad�ngs 971004
 *
 */
public class CoButtonGroup extends ButtonGroup {
	protected transient EventListenerList listenerList = new EventListenerList();
/**
 * JButtonGroup constructor comment.
 */
public CoButtonGroup() {
	super();
}
/**
 * @param l CoSelectedButtonListener
 */
public void addSelectedButtonListener ( CoSelectedButtonListener l) {
	listenerList.add(CoSelectedButtonListener.class, l);
}
/**
 	Returns the button with <code>actionCommand</code>
 	equal to the <code>anActionCommand>/code> argument.
 */
public AbstractButton commandToButton(String anActionCommand)
{
	Enumeration tButtons = getElements();
	while (tButtons.hasMoreElements())
	{
		AbstractButton eButton 	= (AbstractButton) tButtons.nextElement();
		String ac 				= eButton.getActionCommand();
		if ((ac == anActionCommand) || (ac.equals(anActionCommand)))
			return eButton;
	}
	return null;
}
/**
 */
public void enableDisableButtons(boolean enable)
{
	Enumeration tButtons 	= getElements();
	while (tButtons.hasMoreElements())
	{
		AbstractButton eButton = (AbstractButton)tButtons.nextElement();
		eButton.setEnabled(enable);		
		eButton.repaint();
	}
}
/**
 */
protected void fireSelectedButtonEvent()
{
	Object tListeners[] 				= listenerList.getListenerList();
	CoSelectedButtonEvent tEvent 		= null;
	Class tListenerClass				= CoSelectedButtonListener.class;
	for (int i = tListeners.length - 2; i >= 0; i -= 2)
	{
		if (tListeners[i] == tListenerClass)
		{
			if (tEvent == null)
				tEvent = new CoSelectedButtonEvent(this, getSelection());
			((CoSelectedButtonListener)tListeners[i + 1]).selectedButtonChanged(tEvent);
		}
	}

}
/**
 * @param l CoSelectedButtonListener
 */
public void removeSelectedButtonListener ( CoSelectedButtonListener l) {
	listenerList.remove(CoSelectedButtonListener.class, l);
}
/**
 Selektera knappen vars 'actionCommand' �r lika med 'anActionCommand'. 
 */
public void setSelected(String anActionCommand)
{
	Enumeration tButtons 	= getElements();
	while (tButtons.hasMoreElements())
	{
		AbstractButton eButton = (AbstractButton)tButtons.nextElement();
		String ac = eButton.getActionCommand();

		if
			( ( ac == anActionCommand ) ||
				( ( ac != null ) && ( ac.equals( anActionCommand ) ) ) )
		{
			setSelected( eButton.getModel(),true );		
			eButton.repaint();
		}
	}
}
/**
	* Selektera/avselektera knappen som har modellen 'aButtonModel'.
	* Om knappen skall selekteras (state == true) s� postar jag ett
	* CoSelectedButtonEvent f�r att meddela alla som registrerat sig 
	* som lyssnar efter s�dana event.
 */
public void setSelected(ButtonModel aButtonModel, boolean state)
{
	boolean tFire = state && (getSelection() != aButtonModel);
	super.setSelected(aButtonModel, state);
	if (tFire)
		fireSelectedButtonEvent();
}
}
