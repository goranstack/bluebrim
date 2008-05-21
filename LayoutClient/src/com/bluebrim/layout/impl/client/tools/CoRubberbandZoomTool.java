package com.bluebrim.layout.impl.client.tools;

import java.awt.event.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.editor.*;

/**
 * Tool for rubberbanding zooming
 * 
 * @author: Dennis Malmström
 */
 
public class CoRubberbandZoomTool extends CoRubberbandTool
{
	private static final double MIN_ZOOM_RANGE = 1;
public CoRubberbandZoomTool( CoLayoutEditor pageItemEditor )
{
	super( null, pageItemEditor );
}
protected CoTool doIt( MouseEvent ev, CoShapeIF area )
{
	area.normalize();
	
	if
		( ( area.getWidth() >= MIN_ZOOM_RANGE ) || ( area.getHeight() >= MIN_ZOOM_RANGE ) ) // avoid black holes
	{
		m_editor.zoomTo( area.getX(), area.getY(), area.getWidth(), area.getHeight(), 0 );
	}

	return m_previousTool;
}
}