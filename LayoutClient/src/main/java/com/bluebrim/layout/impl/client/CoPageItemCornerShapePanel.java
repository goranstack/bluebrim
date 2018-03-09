package com.bluebrim.layout.impl.client;


import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.swing.client.*;

/**
 * Page item cornered rectangle shape property panel.
 *
 * @author: Dennis
 */
 
public class CoPageItemCornerShapePanel extends CoPageItemRectangularShapePanel
{
	public static final String RADIUS = "CoPageItemCornerShapePanel.RADIUS";
	
	protected CoTextField m_radiusTextfield;





public void doUpdate()
{
	super.doUpdate();
	
	if ( m_domain == null ) return;
	if ( ! isVisible() ) return;
	
	CoImmutableShapeIF s = m_domain.getCoShape();

	if ( ! ( s instanceof CoImmutableCornerRectangleIF ) ) return;
	
	m_radiusTextfield.setText( CoLengthUnitSet.format( ( (CoImmutableCornerRectangleIF) s ).getCornerRadius() ) );
}
public void postSetDomain()
{
	super.postSetDomain();
	
	if ( m_domain == null ) return;
	
	boolean isSlave = m_domain.isSlave();

	m_radiusTextfield.setEnabled( ! isSlave );
}

public CoPageItemCornerShapePanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super.create( b, commandExecutor );

	add( b.createLabel( CoPageItemUIStringResources.getName( RADIUS ) ) );
	add( m_radiusTextfield = b.createSlimTextField( CoTextField.RIGHT, 6 ) );


	m_radiusTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_CORNER_RADIUS ) );
}
}