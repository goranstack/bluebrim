package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.layout.impl.client.view.*;

/**
 * Most user operations in the layout editor are implemented as separate objects.
 * The main reason for this is to avoid bloating CoLayoutEditor (it's quite large anyway).
 * This class is an abstract superclass for such objects.
 * Its only feature is a refrence to the layout editor that is the target of the operation.
 * 
 * @author: Dennis
 */
 
abstract class CoLayoutEditorAction extends CoPrepareableAction
{
	protected CoLayoutEditor m_editor;
public CoLayoutEditorAction( String name, CoLayoutEditor e )
{
	super( name );

	m_editor = e;
}
public CoLayoutEditorAction( CoLayoutEditor e )
{
	super();

	m_editor = e;
}
}
