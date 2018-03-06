package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.observable.*;
import com.bluebrim.stroke.impl.client.*;

/**
 * Layout editor operation: Open ui for editing available strokes.
 * 
 * @author: Dennis
 */

class CoEditStrokes extends CoExternalUIFrameAction {

	public CoEditStrokes(String name, CoLayoutEditor e) {
		super(name, e);
	}

	public CoEditStrokes(CoLayoutEditor e) {
		super(e);
	}

	public void actionPerformed(java.awt.event.ActionEvent ev) {
		if (m_frame == null) {
			m_ui = new CoStrokeCollectionUI();
			m_frame = new CoFrame(m_ui);
			m_frame.setDefaultCloseOperation(CoFrame.HIDE_ON_CLOSE);
			m_frame.pack();

			(new CoDefaultServerObjectListener(m_ui)).initialize();

			setContext(m_editor.getContext());
		}

		m_frame.show();
	}

	void setContext(CoPageItemEditorContextIF c) {
		if (m_ui != null) {
			m_ui.setDomain(c.getPreferences());
			((CoStrokeCollectionUI) m_ui).setColorCollection(c.getPreferences());
		}
	}
}
