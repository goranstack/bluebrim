package com.bluebrim.layout.client;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.swing.client.*;

/**
 * Creation date: (2001-05-30 12:55:10)
 * @author: Dennis Malmström
 */

public class CoViewableUI extends CoDomainUserInterface
{
	private CoAbstractViewPanel m_viewPanel;
/**
 * CoViewPanelUI constructor comment.
 */
public CoViewableUI() {
	super();
}
protected void createValueModels( CoUserInterfaceBuilder b )
{
	super.createValueModels( b );

	new CoViewPanelAdaptor(
		b.addAspectAdaptor(
			new CoReadOnlyAspectAdaptor( this, "VIEW" )
			{
		  	protected Object get( CoObjectIF subject )
		  	{
					return getViewFor( subject );
				}
			}
		),
		m_viewPanel
	);

}
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder b )
{
	super.createWidgets( p, b );

	m_viewPanel = new CoViewPanel();

	p.add( m_viewPanel );
}

protected CoView getViewFor( CoObjectIF subject )
{
	return ( (CoViewable) subject ).getView();
}
}