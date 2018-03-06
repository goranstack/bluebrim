package com.bluebrim.layout.impl.client;

import com.bluebrim.gui.client.*;

/**
 * Empty layout manager property panel.
 *
 * @author: Dennis
 */
 
public class CoEmptyLayoutManagerPanel extends CoLayoutManagerPanel
{
	private String m_label;
public CoEmptyLayoutManagerPanel( CoUserInterfaceBuilder b, CoPageItemLayoutManagerPanel domainHolder, String label )
{
	super( b, domainHolder );

	m_label = label;

	add( b.createLabel( "" ) );
}
protected void doUpdate()
{
}
}
