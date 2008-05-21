package com.bluebrim.layout.impl.client;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.swing.client.*;

/**
 * Insert the class' description here.
 * Creation date: (2000-09-05 14:06:49)
 * @author: Arvid Berg
 */
 
public class CoRectangleLayoutManagerPanel extends CoLayoutManagerPanel
{
	public static final String TYPE	= "CoRectangleLayoutManagerPanel.TYPE";
	
	private CoPanel m_buttons;
	private CoTextField m_gapTextfield;


;

	private com.bluebrim.swing.client.CoButtonGroup m_buttonGroup;



protected void doUpdate()
{
	m_gapTextfield.setText( CoLengthUnitSet.format( ( (com.bluebrim.layoutmanager.CoRectangleLayoutManagerIF) getDomain().getLayoutManager() ).getGap(), CoLengthUnit.LENGTH_UNITS ) );
	m_buttonGroup.setSelected( ( (com.bluebrim.layoutmanager.CoRectangleLayoutManagerIF) getDomain().getLayoutManager() ).getDistanceCalculator().getName() );
}

public CoRectangleLayoutManagerPanel( CoUserInterfaceBuilder b, CoPageItemLayoutManagerPanel domainHolder, CoUndoableCommandExecutor commandExecutor )
{
	super( b, domainHolder );

	setLayout( new CoRowLayout() );
	CoPanel tPanel=b.createPanel(new CoFormLayout());
	m_gapTextfield = b.createTextField( CoTextField.RIGHT, 10 );
	tPanel.add( b.createLabel( "Gap" ) );
	tPanel.add( m_gapTextfield );
	//m_gapTextfield = b.createTextField( CoTextField.RIGHT, 10 );
	m_buttons = b.createPanel( new CoColumnLayout( true ) );
	m_buttons.setBorder( BorderFactory.createTitledBorder( CoLayouteditorUIStringResources.getName(TYPE)  ) );
	add( m_buttons );
	add(tPanel);
	m_buttonGroup=b.createButtonGroup();


	
	m_gapTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_RECTANGLE_LAYOUT_MANAGER_GAP ) );

	ButtonCommandAdapter c = new ButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_DISTANCE_CALCULATOR );

		
	String key = com.bluebrim.layout.impl.shared.layoutmanager.CoDistRectangleIF.RECTANGLE_DIST_CALC;
	CoRadioButton tb = b.createRadioButton(	CoLayouteditorUIStringResources.getName(key),
											m_buttonGroup,
											key );
	tb.setHorizontalAlignment( JButton.LEFT );
	m_buttons.add( tb );
	tb.addActionListener( c );

	key = com.bluebrim.layout.impl.shared.layoutmanager.CoDistTriangleIF.TRIANGLE_DIST_CALC;
	tb = b.createRadioButton(	CoLayouteditorUIStringResources.getName(key),
								m_buttonGroup,
								key );
	tb.setHorizontalAlignment( JButton.LEFT );
	m_buttons.add( tb );
	tb.addActionListener( c );	
	
	key = com.bluebrim.layout.impl.shared.layoutmanager.CoDistVerticalIF.VERTICAL_DIST_CALC;
	tb = b.createRadioButton(	CoLayouteditorUIStringResources.getName(key),
								m_buttonGroup,
								key );
	tb.setHorizontalAlignment( JButton.LEFT );
	m_buttons.add( tb );
	tb.addActionListener( c );

	key = com.bluebrim.layout.impl.shared.layoutmanager.CoDistHorizontalIF.HORIZONTAL_DIST_CALC;
	tb = b.createRadioButton(	CoLayouteditorUIStringResources.getName(key),
								m_buttonGroup,
								key );
	tb.setHorizontalAlignment( JButton.LEFT );
	m_buttons.add( tb );
	tb.addActionListener( c );

	key = com.bluebrim.layout.impl.shared.layoutmanager.CoDistConvexIF.CONVEX_DIST_CALC;
	tb = b.createRadioButton(	CoLayouteditorUIStringResources.getName(key),
								m_buttonGroup,
								key );
	tb.setHorizontalAlignment( JButton.LEFT );
	m_buttons.add( tb );
	tb.addActionListener( c );

	key = com.bluebrim.layout.impl.shared.layoutmanager.CoDistConcaveIF.CONCAVE_DIST_CALC;
	tb = b.createRadioButton(	CoLayouteditorUIStringResources.getName(key),
								m_buttonGroup,
								key );
	tb.setHorizontalAlignment( JButton.LEFT );
	m_buttons.add( tb );
	tb.addActionListener( c );	
	
}
}