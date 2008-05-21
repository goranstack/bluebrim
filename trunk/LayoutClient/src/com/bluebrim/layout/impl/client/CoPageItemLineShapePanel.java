package com.bluebrim.layout.impl.client;


import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.swing.client.*;

/**
 * Page item line shape property panel.
 *
 * @author: Dennis
 */

public class CoPageItemLineShapePanel extends CoPageItemShapePanel
{

	public static final String X1 = "CoPageItemLineShapePanel.X1";

	public static final String Y1	= "CoPageItemLineShapePanel.Y1";



	protected CoTextField m_x1Textfield;
	protected CoTextField m_y1Textfield;


public void doUpdate()
{
	if ( m_domain == null ) return;
	if ( ! isVisible() ) return;
	
	CoImmutableShapeIF s = (CoImmutableShapeIF) m_domain.getCoShape();

	if ( ! ( s instanceof CoImmutableLineIF ) ) return;
	
	CoImmutableLineIF l = (CoImmutableLineIF) s;
	
	m_x1Textfield.setText( CoLengthUnitSet.format( m_domain.getX(), CoLengthUnit.LENGTH_UNITS ) );
	m_y1Textfield.setText( CoLengthUnitSet.format( m_domain.getY(), CoLengthUnit.LENGTH_UNITS ) );
	m_x2Textfield.setText( CoLengthUnitSet.format( m_domain.getX() + l.getX2(), CoLengthUnit.LENGTH_UNITS ) );
	m_y2Textfield.setText( CoLengthUnitSet.format( m_domain.getY() + l.getY2(), CoLengthUnit.LENGTH_UNITS ) );
}
public void postSetDomain()
{
	if ( m_domain == null ) return;
	
	boolean isSlave = m_domain.isSlave();

	m_x1Textfield.setEnabled( ! isSlave );
	m_y1Textfield.setEnabled( ! isSlave );
	m_x2Textfield.setEnabled( ! isSlave );
	m_y2Textfield.setEnabled( ! isSlave );
}

	protected CoTextField m_x2Textfield;
	protected CoTextField m_y2Textfield;
	public static final String X2 = "CoPageItemLineShapePanel.X2";
	public static final String Y2 = "CoPageItemLineShapePanel.Y2";

public CoPageItemLineShapePanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	b.preparePanel( this );
	setLayout( createFormLayout() );

	add( b.createLabel( CoPageItemUIStringResources.getName( X1 ) ) );
	add( m_x1Textfield = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

	add( b.createLabel( CoPageItemUIStringResources.getName( Y1 ) ) );
	add( m_y1Textfield = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

	add( b.createLabel( CoPageItemUIStringResources.getName( X2 ) ) );
	add( m_x2Textfield = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

	add( b.createLabel( CoPageItemUIStringResources.getName( Y2 ) ) );
	add( m_y2Textfield = b.createSlimTextField( CoTextField.RIGHT, 6 ) );



	m_x1Textfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_LINE_X1 ) );
	m_y1Textfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_LINE_Y1 ) );
	m_x2Textfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_LINE_X2 ) );
	m_y2Textfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_LINE_Y2 ) );
}
}