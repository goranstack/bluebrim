package com.bluebrim.layout.impl.client.editor;

import java.awt.*;

import javax.swing.*;

import com.bluebrim.gui.client.*;

/**
 * Dialog containing a layout editor.
 * The layout editor isn't created until the dialog is opened.
 * Also see the method CoLayoutEditor.createLayoutEditorDialog.
 *
 * Creation date: (2001-03-26 12:40:15)
 * @author: Dennis
 */

public abstract class CoLayoutEditorDialog {
	private CoLayoutEditor m_editor;
	private String m_title;
	private CoDialog m_dialog;
	private static Frame m_frame;

	public CoLayoutEditorDialog() {
	}

	public abstract CoLayoutEditor createEditor();

	public CoDialog getCoDialog() {
		return m_dialog;
	}

	public CoLayoutEditor getEditor() {
		if (m_editor == null)
			m_editor = createEditor();
		return m_editor;
	}

	public boolean isVisible() {
		return (m_dialog != null) && m_dialog.isVisible();
	}

	public void open(JComponent windowOwner, String title) {
		if (title != null)
			m_title = title;

		Container w = (windowOwner == null) ? null : windowOwner.getTopLevelAncestor();
		if ((m_dialog == null) || (m_dialog.getOwner() != w)) {
			if (w instanceof Frame) {
				m_dialog = new CoDialog((Frame) w, m_title, false, getEditor());
			} else if (w instanceof Dialog) {
				m_dialog = new CoDialog((Dialog) w, m_title, false, getEditor());
			} else {
				if (m_frame == null) {
					m_frame = new Frame();
				}
				m_dialog = new CoDialog(m_frame, m_title, false, getEditor());
			}
		}

		m_dialog.setTitle(m_title);
		m_dialog.setVisible(true);
	}
}