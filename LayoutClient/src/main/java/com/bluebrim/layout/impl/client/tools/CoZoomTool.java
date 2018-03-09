package com.bluebrim.layout.impl.client.tools;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.editor.*;

/**
 * Implementation of "zoom mode"
 * 
 * @author: Dennis Malmström
 */
public class CoZoomTool extends CoAbstractTool {

	public CoZoomTool(CoLayoutEditor pageItemEditor) {
		super(null, pageItemEditor);
	}

	public void activate(Point2D pos) {
		super.activate(pos);

		m_viewPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

	}

	public CoTool mousePressed(MouseEvent e) {
		return new CoRubberbandZoomTool(m_editor);
	}

	public String getName() {
		return CoPageItemUIStringResources.getName("ZOOM_TOOL");
	}

}
