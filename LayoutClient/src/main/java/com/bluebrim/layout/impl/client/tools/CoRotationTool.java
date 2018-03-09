package com.bluebrim.layout.impl.client.tools;

import java.awt.event.*;

import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Implementation of "rotate mode"
 * 
 * @author: Dennis Malmström
 */

public class CoRotationTool extends CoAbstractTool {

	public CoRotationTool(CoLayoutEditor pageItemEditor) {
		super(null, pageItemEditor);
	}

	public CoTool mousePressed(MouseEvent e) {
		CoShapePageItemView view = m_viewPanel.getRootView().findTopMostViewContaining(getLocation(e), null, false, false, -1);
		//getViewAt( getLocation( e ), false );

		if (isReshapeable(view)) {
			CoShapePageItemView v = view;
			while (v != null) {
				if (m_viewPanel.getSelectionManager().isSelected(v) && isReshapeable(v)) {
					view = v;
					break;
				}
				v = v.getParent();
			}

			return new CoRubberbandRotationTool(m_editor, this, view, e);
		}

		return this;
	}

	public String getName() {
		return CoPageItemUIStringResources.getName("ROTATION_TOOL");
	}

}