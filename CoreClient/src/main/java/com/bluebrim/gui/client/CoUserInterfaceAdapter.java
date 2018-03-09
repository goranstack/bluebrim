package com.bluebrim.gui.client;

/**
  */
public class CoUserInterfaceAdapter implements CoUserInterfaceListener {
/**
 * CoUserInterfaceAdapter constructor comment.
 */
public CoUserInterfaceAdapter() {
	super();
}
public void processUserInterfaceEvent(CoUserInterfaceEvent e)
{
	switch (e.getID())
	{
		case CoUserInterfaceEvent.UI_CLOSING:
			userInterfaceClosing(e);
			break;
		case CoUserInterfaceEvent.UI_CLOSED:
			userInterfaceClosed(e);
			break;
		case CoUserInterfaceEvent.UI_ACTIVATED:
			userInterfaceActivated(e);
			break;
		case CoUserInterfaceEvent.UI_DEACTIVATED:
			userInterfaceDeactivated(e);
			break;
		case CoUserInterfaceEvent.UI_VALIDATED:
			userInterfaceValidated(e);
			break;
		case CoUserInterfaceEvent.UI_OPENED:
			userInterfaceOpened(e);
			break;
	}
}
/**
 * userInterfaceActivated method comment.
 */
protected void userInterfaceActivated(CoUserInterfaceEvent e) {
}
/**
 * userInterfaceClosde method comment.
 */
protected void userInterfaceClosed(CoUserInterfaceEvent e) {
}
/**
 * userInterfaceClosing method comment.
 */
protected void userInterfaceClosing(CoUserInterfaceEvent e) {
}
/**
 * userInterfaceDeactivated method comment.
 */
protected void userInterfaceDeactivated(CoUserInterfaceEvent e) {
}
/**
 * userInterfaceClosing method comment.
 */
protected void userInterfaceOpened(CoUserInterfaceEvent e) {
}
/**
 * userInterfaceDeactivated method comment.
 */
protected void userInterfaceValidated(CoUserInterfaceEvent e) {
}
}
