package com.bluebrim.layout.impl.client;

import com.bluebrim.gui.client.*;

/**
 * Abstract superclass for page item shape property panels.
 *
 * @author: Dennis
 */

public abstract class CoPageItemShapePanel extends CoPageItemPropertyPanel
{

public CoPageItemShapePanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, null, commandExecutor );
}
}