package com.bluebrim.layout.impl.client.editor;


import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;

/**
 * Layout editor preferences dialog.
 *
 * @author: Dennis
 */

public class CoLayoutEditorPrefsUI extends CoUserInterface
{
	public static final String LENGTH_UNIT = "LENGTH_UNIT";

public CoLayoutEditorPrefsUI()
{
	super();
}
protected void createValueModels( CoUserInterfaceBuilder b )
{
	super.createValueModels( b );
}
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder b )
{
	super.createWidgets( p, b );

	CoTabbedPane tp = b.createTabbedPane();

	{
		CoLengthUnitSetUI ui = new CoLengthUnitSetUI();
		ui.setDomain( CoLengthUnit.LENGTH_UNITS );

		tp.addTab( CoLayouteditorUIStringResources.getName( LENGTH_UNIT ), b.createSubcanvas( ui ) );
	}

	p.add( tp );
}
}
