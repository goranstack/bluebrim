package com.bluebrim.layout.impl.client;

import java.awt.event.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.swing.client.*;

/**
 * Page item rectangular shape property panel.
 *
 * @author: Dennis
 */

public class CoPageItemRectangularShapePanel extends CoPageItemShapePanel
{
	public static final String X = "CoPageItemRectangularShapePanel.X";
	public static final String Y = "CoPageItemRectangularShapePanel.Y";
	public static final String WIDTH 	= "CoPageItemRectangularShapePanel.WIDTH";
	public static final String HEIGHT 	= "CoPageItemRectangularShapePanel.HEIGHT";

	private CoTextField m_xTextfield;
	private CoTextField m_yTextfield;
	private CoTextField m_widthTextfield;
	private CoTextField m_heightTextfield;


public void doUpdate()
{
	if ( m_domain == null ) return;
	
	CoImmutableShapeIF s = m_domain.getCoShape();
	
	m_xTextfield.setText( CoLengthUnitSet.format( m_domain.getX(), CoLengthUnit.LENGTH_UNITS ) );
	m_yTextfield.setText( CoLengthUnitSet.format( m_domain.getY(), CoLengthUnit.LENGTH_UNITS ) );
	m_widthTextfield.setText( CoLengthUnitSet.format( s.getWidth(), CoLengthUnit.LENGTH_UNITS ) );
	m_heightTextfield.setText( CoLengthUnitSet.format( s.getHeight(), CoLengthUnit.LENGTH_UNITS ) );
}
public void postSetDomain()
{
	if ( m_domain == null ) return;
	
	boolean isSlave = m_domain.isSlave();

	m_xTextfield.setEnabled( ! isSlave );
	m_yTextfield.setEnabled( ! isSlave );
	m_widthTextfield.setEnabled( ! isSlave );
	m_heightTextfield.setEnabled( ! isSlave );
}

public CoPageItemRectangularShapePanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	b.preparePanel( this );
	setLayout( createFormLayout() );

	add( b.createLabel( CoPageItemUIStringResources.getName( X ) ) );
	add( m_xTextfield = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

	add( b.createLabel( CoPageItemUIStringResources.getName( Y ) ) );
	add( m_yTextfield = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

	add( b.createLabel( CoPageItemUIStringResources.getName( WIDTH ) ) );
	add( m_widthTextfield = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

	add( b.createLabel( CoPageItemUIStringResources.getName( HEIGHT ) ) );
	add( m_heightTextfield = b.createSlimTextField( CoTextField.RIGHT, 6 ) );



	m_xTextfield.addActionListener(
		new CommandAdapter( commandExecutor, CoPageItemCommands.SET_POSITION )
		{
			private double m_x;
			
			protected boolean isValueValid( ActionEvent ev ) { return ! Double.isNaN( m_x ); }

			protected boolean isNewValue( ActionEvent ev )
			{
				m_x = CoLengthUnitSet.parse( m_xTextfield.getText(), Double.NaN, CoLengthUnit.LENGTH_UNITS );
				return isNotEqual( m_domain.getX(), m_x );
			}
			
			protected void prepare()
			{
				( (CoShapePageItemSetPositionCommand) m_command ).prepare( m_domain, m_x, m_domain.getY() );
				m_command.setName( "SET X" );
			}
		}
	);

	m_yTextfield.addActionListener(
		new CommandAdapter( commandExecutor, CoPageItemCommands.SET_POSITION )
		{
			private double m_y;
			
			protected boolean isValueValid( ActionEvent ev ) { return ! Double.isNaN( m_y ); }

			protected boolean isNewValue( ActionEvent ev )
			{
				m_y = CoLengthUnitSet.parse( m_yTextfield.getText(), Double.NaN, CoLengthUnit.LENGTH_UNITS );
				return isNotEqual( m_domain.getY(), m_y );
			}
			
			protected void prepare()
			{
				( (CoShapePageItemSetPositionCommand) m_command ).prepare( m_domain, m_domain.getX(), m_y );
				m_command.setName( "SET Y" );
			}
		}
	);

	m_widthTextfield.addActionListener(
		new CommandAdapter( commandExecutor, CoPageItemCommands.SET_DIMENSIONS )
		{
			private double m_w;
			
			protected boolean isValueValid( ActionEvent ev ) { return ! Double.isNaN( m_w ); }

			protected boolean isNewValue( ActionEvent ev )
			{
				m_w = CoLengthUnitSet.parse( m_widthTextfield.getText(), Double.NaN, CoLengthUnit.LENGTH_UNITS );
				return isNotEqual( m_domain.getWidth(), m_w );
			}
			
			protected void prepare()
			{
				( (CoShapePageItemSetDimensionsCommand) m_command ).prepare( m_domain, m_w, m_domain.getHeight() );
				m_command.setName( "SET WIDTH" );
			}
		}
	);

	m_heightTextfield.addActionListener(
		new CommandAdapter( commandExecutor, CoPageItemCommands.SET_DIMENSIONS )
		{
			private double m_h;
			
			protected boolean isValueValid( ActionEvent ev ) { return ! Double.isNaN( m_h ); }

			protected boolean isNewValue( ActionEvent ev )
			{
				m_h = CoLengthUnitSet.parse( m_heightTextfield.getText(), Double.NaN, CoLengthUnit.LENGTH_UNITS );
				return isNotEqual( m_domain.getHeight(), m_h );
			}
			
			protected void prepare()
			{
				( (CoShapePageItemSetDimensionsCommand) m_command ).prepare( m_domain, m_domain.getWidth(), m_h );
				m_command.setName( "SET HEIGHT" );
			}
		}
	);
}
}