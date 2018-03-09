package com.bluebrim.layout.impl.client;

import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.swing.client.*;

/**
 * Page item grid property panel.
 *
 * @author: Dennis
 */

public class CoPageItemGridPanel extends CoPageItemPropertyPanel
{
	public static final String DERIVED = "CoPageItemGridPanel.DERIVED";
	public static final String COLUMNS = "CoPageItemGridPanel.COLUMN_GRID";
	public static final String COUNT = "CoPageItemGridPanel.COLUMN_COUNT";
	public static final String SPACING = "CoPageItemGridPanel.SPACING";
	public static final String MARGINS = "CoPageItemGridPanel.MARGIN_GRID";
	public static final String LEFT = "CoPageItemGridPanel.LEFT_MARGIN";
	public static final String RIGHT = "CoPageItemGridPanel.RIGHT_MARGIN";
	public static final String TOP = "CoPageItemGridPanel.TOP_MARGIN";
	public static final String BOTTOM = "CoPageItemGridPanel.BOTTOM_MARGIN";

//	public static final String DERIVED = "CoPageItemGridPanel.DERIVED";
	public static final String BASELINE_GRID = "CoPageItemGridPanel.BASELINE_GRID";
	public static final String Y0 = "CoPageItemGridPanel.Y0";
	public static final String DELTA_Y = "CoPageItemGridPanel.DELTA_Y";

	public CoCheckBox m_columnGridDerivedCheckBox;
	public CoTextField m_countTextField;
	public CoTextField m_spacingTextField;
	public CoTextField m_leftTextField;
	public CoTextField m_topTextField;
	public CoTextField m_rightTextField;
	public CoTextField m_bottomTextField;

	public CoCheckBox m_baselineGridDerivedCheckBox;
	public CoTextField m_y0TextField;
	public CoTextField m_deltaYTextField;




public void doUpdate()
{
	CoImmutableColumnGridIF g = m_domain.getColumnGrid();

	m_columnGridDerivedCheckBox.setSelected( g.isDerived() );
	if
		( g.isDerived() )
	{
		m_countTextField.setText( "" );
		m_spacingTextField.setText( "" );
		m_leftTextField.setText( "" );
		m_topTextField.setText( "" );
		m_rightTextField.setText( "" );
		m_bottomTextField.setText( "" );
		m_isLeftOutsideSensitiveCheckBox.setSelected( false );
	} else {
		m_countTextField.setText( "" + g.getColumnCount() );
		m_spacingTextField.setText( CoLengthUnitSet.format( g.getColumnSpacing(), CoLengthUnit.LENGTH_UNITS ) );
		m_leftTextField.setText( CoLengthUnitSet.format( g.getLeftMargin(), CoLengthUnit.LENGTH_UNITS ) );
		m_rightTextField.setText( CoLengthUnitSet.format( g.getRightMargin(), CoLengthUnit.LENGTH_UNITS ) );
		m_topTextField.setText( CoLengthUnitSet.format( g.getTopMargin(), CoLengthUnit.LENGTH_UNITS ) );
		m_bottomTextField.setText( CoLengthUnitSet.format( g.getBottomMargin(), CoLengthUnit.LENGTH_UNITS ) );
		m_isLeftOutsideSensitiveCheckBox.setSelected( g.isLeftOutsideSensitive() );
	}

	CoImmutableBaseLineGridIF blg = m_domain.getBaseLineGrid();

	m_baselineGridDerivedCheckBox.setSelected( blg.isDerived() );
	if
		( blg.isDerived() )
	{
		m_y0TextField.setText( "" );
		m_deltaYTextField.setText( "" );
	} else {

		m_y0TextField.setText( CoLengthUnitSet.format( blg.getY0(), CoLengthUnit.LENGTH_UNITS ) );
		m_deltaYTextField.setText( CoLengthUnitSet.format( blg.getDeltaY(), CoLengthUnit.LENGTH_UNITS ) );
	}
}

	public static final String INSIDE = "CoPageItemGridPanel.INSIDE_MARGIN";
	public static final String LEFT_OUTSIDE_SENSITIVE = "CoPageItemGridPanel.LEFT_OUTSIDE_SENSITIVE";
	public CoCheckBox m_isLeftOutsideSensitiveCheckBox;
	public CoLabel m_leftLabel;
	public CoLabel m_rightLabel;
	public static final String OUTSIDE = "CoPageItemGridPanel.OUTSIDE_MARGIN";

public CoPageItemGridPanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, new CoRowLayout(), commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	CoPanel p0 = b.createPanel( new CoColumnLayout( 5, true ) );
	p0.setBorder( BorderFactory.createTitledBorder( CoPageItemUIStringResources.getName( COLUMNS ) ) );
	add( p0 );
	{
		p0.add( m_columnGridDerivedCheckBox = b.createCheckBox( CoPageItemUIStringResources.getName( DERIVED ), null ) );
		
		CoPanel p00 = b.createPanel( createFormLayout() );
		p0.add( p00 );
		{
			p00.add( b.createLabel( CoPageItemUIStringResources.getName( COUNT ) ) );
			p00.add( m_countTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

			p00.add( b.createLabel( CoPageItemUIStringResources.getName( SPACING ) ) );
			p00.add( m_spacingTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );
		}
		
		CoPanel p01 = b.createPanel( createFormLayout() );
		p01.setBorder( BorderFactory.createTitledBorder( CoPageItemUIStringResources.getName( MARGINS ) ) );
		p0.add( p01 );
		{
			p01.add( m_leftLabel = b.createLabel( CoPageItemUIStringResources.getName( LEFT ) ) );
			p01.add( m_leftTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );
			
			p01.add( m_rightLabel = b.createLabel( CoPageItemUIStringResources.getName( RIGHT ) ) );
			p01.add( m_rightTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

			p01.add( b.createLabel( CoPageItemUIStringResources.getName( TOP ) ) );
			p01.add( m_topTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );
			
			p01.add( b.createLabel( CoPageItemUIStringResources.getName( BOTTOM ) ) );
			p01.add( m_bottomTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );
			
			p01.add( b.createLabel( CoPageItemUIStringResources.getName( LEFT_OUTSIDE_SENSITIVE ) ) );
			p01.add( m_isLeftOutsideSensitiveCheckBox = b.createCheckBox( "", null ) );
		}
	}
	
	CoPanel p1 = b.createPanel( new CoColumnLayout( 5, true ) );
	p1.setBorder( BorderFactory.createTitledBorder( CoPageItemUIStringResources.getName( BASELINE_GRID ) ) );
	add( p1 );
	{
		p1.add( m_baselineGridDerivedCheckBox = b.createCheckBox( CoPageItemUIStringResources.getName( DERIVED ), null ) );
		
		CoPanel p10 = b.createPanel( createFormLayout() );
		p1.add( p10 );
		{
			p10.add( b.createLabel( CoPageItemUIStringResources.getName( Y0 ) ) );
			p10.add( m_y0TextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

			p10.add( b.createLabel( CoPageItemUIStringResources.getName( DELTA_Y ) ) );
			p10.add( m_deltaYTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );
		}
	}


	m_columnGridDerivedCheckBox.addItemListener(
		new ItemListener()
		{
			public void itemStateChanged( ItemEvent ev )
			{
				boolean e = ev.getStateChange() == ItemEvent.DESELECTED;
				m_countTextField.setEnabled( e );
				m_spacingTextField.setEnabled( e );
				m_leftTextField.setEnabled( e );
				m_topTextField.setEnabled( e );
				m_rightTextField.setEnabled( e );
				m_bottomTextField.setEnabled( e );
				m_isLeftOutsideSensitiveCheckBox.setEnabled( e );
			}
		}
	);

	m_baselineGridDerivedCheckBox.addItemListener(
		new ItemListener()
		{
			public void itemStateChanged( ItemEvent ev )
			{
				boolean e = ev.getStateChange() == ItemEvent.DESELECTED;
				m_y0TextField.setEnabled( e );
				m_deltaYTextField.setEnabled( e );
			}
		}
	);

	m_isLeftOutsideSensitiveCheckBox.addItemListener(
		new ItemListener()
		{
			public void itemStateChanged( ItemEvent ev )
			{
				boolean selected = ev.getStateChange() == ItemEvent.SELECTED;
				m_leftLabel.setText( CoPageItemUIStringResources.getName( selected ? OUTSIDE : LEFT ) );
				m_rightLabel.setText( CoPageItemUIStringResources.getName( selected ? INSIDE : RIGHT ) );
			}
		}
	);





	m_columnGridDerivedCheckBox.addActionListener(
		new CommandAdapter( commandExecutor, CoPageItemCommands.SET_COLUMN_GRID_DERIVED )
		{
			private boolean m_isDerived;

			protected boolean isNewValue( ActionEvent ev )
			{
				m_isDerived = m_columnGridDerivedCheckBox.isSelected();
				return m_isDerived != m_domain.getColumnGrid().isDerived();
			}
			
			protected void prepare()
			{
				( (CoShapePageItemSetColumnGridDerivedCommand) m_command ).prepare( m_domain, m_isDerived );
			}
		}
	);

	m_countTextField.addActionListener(
		new IntegerTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_COLUMN_COUNT, -1 )
		{
			protected boolean isValueValid( ActionEvent ev ) { return super.isValueValid( ev ) && m_value <= 100; }
		}
	);

	m_spacingTextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_COLUMN_SPACING ) );
	m_leftTextField.addActionListener(    new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_COLUMN_GRID_LEFT_MARGIN ) );
	m_rightTextField.addActionListener(   new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_COLUMN_GRID_RIGHT_MARGIN ) );
	m_topTextField.addActionListener(     new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_COLUMN_GRID_TOP_MARGIN ) );
	m_bottomTextField.addActionListener(  new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_COLUMN_GRID_BOTTOM_MARGIN ) );
	m_isLeftOutsideSensitiveCheckBox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_COLUMN_GRID_LEFT_OUTSIDE_SENSITIVE ) );
	




	m_baselineGridDerivedCheckBox.addActionListener(
		new CommandAdapter( commandExecutor, CoPageItemCommands.SET_BASE_LINE_GRID_DERIVED )
		{
			private boolean m_isDerived;

			protected boolean isNewValue( ActionEvent ev )
			{
				m_isDerived = m_baselineGridDerivedCheckBox.isSelected();
				return m_isDerived != m_domain.getBaseLineGrid().isDerived();
			}
			
			protected void prepare()
			{
				( (CoShapePageItemSetBaseLineGridDerivedCommand) m_command ).prepare( m_domain, m_isDerived );
			}
		}
	);

	m_y0TextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_BASELINE_GRID_Y0 ) );
	m_deltaYTextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_BASELINE_GRID_DY ) );
}
}