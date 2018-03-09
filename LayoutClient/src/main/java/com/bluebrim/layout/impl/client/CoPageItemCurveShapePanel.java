package com.bluebrim.layout.impl.client;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.swing.client.*;

/**
 * Page item curve shape property panel.
 *
 * @author: Dennis
 */
 
public class CoPageItemCurveShapePanel extends CoPageItemRectangularShapePanel
{
	public static final String CLOSED = "CoPageItemCurveShapePanel.CLOSED";
	public static final String REORDER = "CoPageItemCurveShapePanel.REORDER";
	
	protected CoCheckBox m_closedCheckBox;





public void doUpdate()
{
	super.doUpdate();
	
	if ( m_domain == null ) return;
	if ( ! isVisible() ) return;
	
	CoImmutableShapeIF s = m_domain.getCoShape();

	if ( ! ( s instanceof CoCurveShapeIF ) ) return;
	
	m_closedCheckBox.setSelected( ( (CoCurveShapeIF) s ).isClosed() );
}
public void postSetDomain()
{
	super.postSetDomain();
	
	if ( m_domain == null ) return;
	
	boolean isSlave = m_domain.isSlave();

	m_closedCheckBox.setEnabled( ! isSlave );
}

public CoPageItemCurveShapePanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super.create( b, commandExecutor );

	add( b.createLabel( CoPageItemUIStringResources.getName( CLOSED ) ) );
	add( m_closedCheckBox = b.createCheckBox( "", null ) );

	add( b.createLabel( "" ) );
	CoButton reorderButton = b.createButton( CoPageItemUIStringResources.getName( REORDER ), null );
	add( reorderButton );


	m_closedCheckBox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_CURVE_CLOSED ) );
	reorderButton.addActionListener( new CommandAdapter( commandExecutor, CoPageItemCommands.REORDER_CURVE ) );
}
}