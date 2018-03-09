package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.gui.client.*;

/**
 * Abstract superclass for layout editor operations that involve opening other ui's in dialogs.
 * 
 * @author: Dennis
 */
 
abstract class CoExternalUIDialogAction extends CoExternalUIAction
{
	CoDialog m_dialog;
/**
 * CoExternalUIFrameAction constructor comment.
 * @param name java.lang.String
 * @param e com.bluebrim.layout.impl.client.editor.CoLayoutEditor
 */
public CoExternalUIDialogAction(String name, CoLayoutEditor e) {
	super(name, e);
}
/**
 * CoExternalUIFrameAction constructor comment.
 * @param e com.bluebrim.layout.impl.client.editor.CoLayoutEditor
 */
public CoExternalUIDialogAction(CoLayoutEditor e) {
	super(e);
}
void close()
{
	if ( m_dialog != null ) m_dialog.setVisible( false );
}
}
