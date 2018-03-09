package com.bluebrim.layout.impl.client.editor;

import java.awt.event.*;

import com.bluebrim.content.shared.*;
import com.bluebrim.image.impl.client.*;
import com.bluebrim.image.shared.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Layout editor operation: Import image from file system.
 * 
 * @author: Dennis
 */

class CoImportImage extends CoLayoutEditorAction {
	protected CoImageFileChooser m_imageFileChooser;

	public CoImportImage(String name, CoLayoutEditor e) {
		super(name, e);
	}

	public void actionPerformed(ActionEvent ev) {
		final CoPageItemImageContentView v = (CoPageItemImageContentView) m_editor.getCurrentImageContentView();
		CoContentReceiver receiver = (CoContentReceiver)m_editor.getContext().getContentRecievers().get(0);

		if (v != null)  {
			// NOTE: Lazy to improve startup speed.
			if (m_imageFileChooser == null) {
				m_imageFileChooser = new CoImageFileChooser();
			}

			final CoImageContentIF image = m_imageFileChooser.chooseImage(m_editor.getWorkspace());
			if (image != null) {
				receiver.add(image);
				CoPageItemCommands.SET_IMAGE.prepare(v.getOwner(), image);
				m_editor.getCommandExecutor().doit(CoPageItemCommands.SET_IMAGE, null);

			}
		}
	}
}