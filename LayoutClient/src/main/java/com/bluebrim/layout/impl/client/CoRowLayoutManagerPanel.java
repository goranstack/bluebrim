package com.bluebrim.layout.impl.client;


import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.swing.client.*;

/**
 * Row layout manager property panel.
 *
 * @author: Dennis
 */
 
public class CoRowLayoutManagerPanel extends CoLayoutManagerPanel
{
	private CoTextField m_gapTextfield;


protected void doUpdate()
{
	m_gapTextfield.setText( CoLengthUnitSet.format( ( (CoRowLayoutManagerIF) getDomain().getLayoutManager() ).getGap(), CoLengthUnit.LENGTH_UNITS ) );
}

public CoRowLayoutManagerPanel( CoUserInterfaceBuilder b, CoPageItemLayoutManagerPanel domainHolder, CoUndoableCommandExecutor commandExecutor )
{
	super( b, domainHolder );

	setLayout( new CoFormLayout() );

	m_gapTextfield = b.createTextField( CoTextField.RIGHT, 10 );
	add( b.createLabel( "Gap" ) );
	add( m_gapTextfield );


	m_gapTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_ROW_LAYOUT_MANAGER_GAP ) );
}
}