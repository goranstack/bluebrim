package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.event.*;

/**
 * Auto scroll event.
 * see CoAutoScroller.
 *
 * @author: Dennis
 */

public class CoAutoScrollEvent extends MouseEvent
{
	public static final int ID = AWTEvent.RESERVED_ID_MAX + 42;
public CoAutoScrollEvent( Component source )
{
	super( source, ID, System.currentTimeMillis(), 0, 0, 0, 0, false );
}
}