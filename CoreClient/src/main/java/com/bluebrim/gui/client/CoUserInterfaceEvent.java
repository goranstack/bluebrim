package com.bluebrim.gui.client;

import java.awt.AWTEvent;
import java.awt.Event;
/**
 	Event class for events either sent from the 
 	userinterface or from its builder.
 */
public class CoUserInterfaceEvent extends AWTEvent {

	final public static int UI_CLOSING		= 200;
	final public static int UI_CLOSED		= 201;
	final public static int UI_ACTIVATED	= 202;
	final public static int UI_DEACTIVATED	= 203;
	final public static int UI_VALIDATED	= 204;
	final public static int UI_OPENED		= 205;
/**
 * CoUserInterfaceEvent constructor comment.
 * @param event java.awt.Event
 */
public CoUserInterfaceEvent(Event event) {
	super(event);
}
/**
 * CoUserInterfaceEvent constructor comment.
 * @param source java.lang.Object
 * @param id int
 */
public CoUserInterfaceEvent(CoUserInterface source, int id) {
	super(source, id);
}
public void consume()
{
	switch (id)
	{
		case UI_CLOSING :
		case UI_VALIDATED:
			consumed = true;
			break;
		default :
			super.consume();
	}
}
public CoUserInterface getUserInterface()
{
	return (CoUserInterface )getSource();
}
public boolean isConsumed()
{
	return super.isConsumed();
}
}
