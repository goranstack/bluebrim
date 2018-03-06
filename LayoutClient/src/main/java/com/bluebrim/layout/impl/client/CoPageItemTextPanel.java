package com.bluebrim.layout.impl.client;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.swing.client.*;

/**
 * Page item text property panel.
 *
 * @author: Dennis
 */

public class CoPageItemTextPanel extends CoPageItemPropertyPanel
{
	public static final String FIRST_BASELINE 		= "CoPageItemTextPanel.FIRST_BASELINE";
	public static final String VERTICAL_ALIGNMENT 	= "CoPageItemTextPanel.VERTICAL_ALIGNMENT";	
	public static final String FIRST_BASELINE_TYPE 		= "CoPageItemTextPanel.FIRST_BASELINE_TYPE";
	public static final String FIRST_BASELINE_OFFSET 		= "CoPageItemTextPanel.FIRST_BASELINE_OFFSET";	
	public static final String VERTICAL_ALIGNMENT_TYPE 		= "CoPageItemTextPanel.VERTICAL_ALIGNMENT_TYPE";	
	public static final String VERTICAL_ALIGNMENT_MAX_INTERVAL 	= "CoPageItemTextPanel.VERTICAL_ALIGNMENT_MAX_INTERVAL";		
		

	protected CoOptionMenu m_firstBaselineTypeOptionMenu;
	protected CoTextField m_firstBaselineOffsetTextField;
	protected CoOptionMenu m_verticalAlignmentTypeOptionMenu;
	protected CoTextField m_verticalAlignmentMaxIntervalTextField;




public void doUpdate()
{
	CoPageItemAbstractTextContentView t = (CoPageItemAbstractTextContentView) ( (CoContentWrapperPageItemView) m_domain ).getContentView();

	m_firstBaselineTypeOptionMenu.setSelectedItem( t.getFirstBaselineType() );

	double d = t.getFirstBaselineOffset();
	m_firstBaselineOffsetTextField.setText( Double.isNaN( d ) ? "" : CoLengthUnitSet.format( d, CoLengthUnit.LENGTH_UNITS ) );

	m_verticalAlignmentTypeOptionMenu.setSelectedItem( t.getVerticalAlignmentType() );

	d = t.getVerticalAlignmentMaxInter();
	m_verticalAlignmentMaxIntervalTextField.setText( Double.isNaN( d ) ? "" : CoLengthUnitSet.format( d, CoLengthUnit.LENGTH_UNITS ) );

	d = t.getTopMargin();
	m_topMarginTextField.setText( Double.isNaN( d ) ? "" : CoLengthUnitSet.format( d, CoLengthUnit.LENGTH_UNITS ) );

	d = t.getBottomMargin();
	m_bottomMarginTextField.setText( Double.isNaN( d ) ? "" : CoLengthUnitSet.format( d, CoLengthUnit.LENGTH_UNITS ) );

	d = t.getLeftMargin();
	m_leftMarginTextField.setText( Double.isNaN( d ) ? "" : CoLengthUnitSet.format( d, CoLengthUnit.LENGTH_UNITS ) );

	d = t.getRightMargin();
	m_rightMarginTextField.setText( Double.isNaN( d ) ? "" : CoLengthUnitSet.format( d, CoLengthUnit.LENGTH_UNITS ) );
}

	public static final String BOTTOM_MARGIN 	= "CoPageItemTextPanel.BOTTOM_MARGIN";
	public static final String LEFT_MARGIN 	= "CoPageItemTextPanel.LEFT_MARGIN";
	protected CoTextField m_bottomMarginTextField;
	protected CoTextField m_leftMarginTextField;
	protected CoTextField m_rightMarginTextField;
	protected CoTextField m_topMarginTextField;
	public static final String RIGHT_MARGIN 	= "CoPageItemTextPanel.RIGHT_MARGIN";
	public static final String TOP_MARGIN 	= "CoPageItemTextPanel.TOP_MARGIN";

public CoPageItemTextPanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, new CoColumnLayout(), commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	CoPanel p0 = b.createPanel( createFormLayout() );
	add( p0 );
	p0.setBorder( BorderFactory.createTitledBorder( CoPageItemUIStringResources.getName( FIRST_BASELINE ) ) );
	{
		p0.add( b.createLabel( CoPageItemUIStringResources.getName( FIRST_BASELINE_TYPE ) ) );
		p0.add( m_firstBaselineTypeOptionMenu = b.createOptionMenu() );
		m_firstBaselineTypeOptionMenu.addItem( CoPageItemAbstractTextContentIF.CAP_HEIGHT );
		m_firstBaselineTypeOptionMenu.addItem( CoPageItemAbstractTextContentIF.CAP_ACCENT );
		m_firstBaselineTypeOptionMenu.addItem( CoPageItemAbstractTextContentIF.ASCENT );
//		m_firstBaselineTypeOptionMenu.setMaximumRowCount( 3 );
		m_firstBaselineTypeOptionMenu.setRenderer( new CoOptionMenu.TranslatingRenderer( CoPageItemStringResources.getBundle() ) );
		m_firstBaselineTypeOptionMenu.setEnabled( false ); //not implemented

		p0.add( b.createLabel( CoPageItemUIStringResources.getName( FIRST_BASELINE_OFFSET ) ) );
		p0.add( m_firstBaselineOffsetTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );
	}

	CoPanel p1 = b.createPanel( createFormLayout() );
	add( p1 );
	p1.setBorder( BorderFactory.createTitledBorder( CoPageItemUIStringResources.getName( VERTICAL_ALIGNMENT ) ) );
	{
		p1.add( b.createLabel( CoPageItemUIStringResources.getName( VERTICAL_ALIGNMENT_TYPE ) ) );
		p1.add( m_verticalAlignmentTypeOptionMenu = b.createOptionMenu() );
		m_verticalAlignmentTypeOptionMenu.addItem( CoPageItemAbstractTextContentIF.ALIGN_TOP );
		m_verticalAlignmentTypeOptionMenu.addItem( CoPageItemAbstractTextContentIF.ALIGN_CENTERED );
		m_verticalAlignmentTypeOptionMenu.addItem( CoPageItemAbstractTextContentIF.ALIGN_BOTTOM );
		m_verticalAlignmentTypeOptionMenu.addItem( CoPageItemAbstractTextContentIF.ALIGN_JUSTIFIED );
		m_verticalAlignmentTypeOptionMenu.setRenderer( new CoOptionMenu.TranslatingRenderer( CoPageItemStringResources.getBundle() ) );

		p1.add( b.createLabel( CoPageItemUIStringResources.getName( VERTICAL_ALIGNMENT_MAX_INTERVAL ) ) );
		p1.add( m_verticalAlignmentMaxIntervalTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );
	}

	CoPanel p2 = b.createPanel( createFormLayout() );
	add( p2 );
	{
		p2.add( b.createLabel( CoPageItemUIStringResources.getName( TOP_MARGIN ) ) );
		p2.add( m_topMarginTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

		p2.add( b.createLabel( CoPageItemUIStringResources.getName( BOTTOM_MARGIN ) ) );
		p2.add( m_bottomMarginTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

		p2.add( b.createLabel( CoPageItemUIStringResources.getName( LEFT_MARGIN ) ) );
		p2.add( m_leftMarginTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

		p2.add( b.createLabel( CoPageItemUIStringResources.getName( RIGHT_MARGIN ) ) );
		p2.add( m_rightMarginTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );
	}


	m_firstBaselineTypeOptionMenu.addActionListener( new OptionMenuCommandAdapter( commandExecutor, CoPageItemCommands.SET_FIRST_BASE_LINE_TYPE, null ) );
	m_firstBaselineOffsetTextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_TEXT_FIRST_BASE_LINE_OFFSET ) );
	m_verticalAlignmentTypeOptionMenu.addActionListener( new OptionMenuCommandAdapter( commandExecutor, CoPageItemCommands.SET_VERTICAL_ALIGNMENT_TYPE, null ) );
	m_verticalAlignmentMaxIntervalTextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_TEXT_VERTICAL_ALIGNMENT_MAX_INTERVAL ) );
	m_topMarginTextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_TEXT_TOP_MARGIN ) );
	m_bottomMarginTextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_TEXT_BOTTOM_MARGIN ) );
	m_leftMarginTextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_TEXT_LEFT_MARGIN ) );
	m_rightMarginTextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_TEXT_RIGHT_MARGIN ) );

}
}