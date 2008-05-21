package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.gui.client.*;

/**
 * Abstract superclass for layout editor operations that involve opening other ui's in frames.
 * 
 * @author: Dennis
 */
 
abstract class CoExternalUIFrameAction extends CoExternalUIAction
{
	CoFrame m_frame;
/**
 * CoExternalUIFrameAction constructor comment.
 * @param name java.lang.String
 * @param e com.bluebrim.layout.impl.client.editor.CoLayoutEditor
 */
public CoExternalUIFrameAction(String name, CoLayoutEditor e) {
	super(name, e);
}
/**
 * CoExternalUIFrameAction constructor comment.
 * @param e com.bluebrim.layout.impl.client.editor.CoLayoutEditor
 */
public CoExternalUIFrameAction(CoLayoutEditor e) {
	super(e);
}
void close()
{
	if ( m_frame != null ) m_frame.setVisible( false );
}
}
