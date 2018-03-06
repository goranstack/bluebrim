package com.bluebrim.layout.impl.client;

import com.bluebrim.gui.client.*;

/**
 * Abstract superclass for page item shape property panels.
 *
 * @author: Dennis
 */

public abstract class CoPageItemFillStylePanel extends CoPageItemPropertyPanel
{

public CoPageItemFillStylePanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, null, commandExecutor );
}
}