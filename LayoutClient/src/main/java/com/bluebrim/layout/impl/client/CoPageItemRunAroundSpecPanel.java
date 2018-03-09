package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.event.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.swing.client.*;

/**
 * Page item run around spec property panel.
 *
 * @author: Dennis
 */

public class CoPageItemRunAroundSpecPanel extends CoPageItemPropertyPanel
{
	public static final String SHAPE_MARGIN = "CoPageItemRunAroundSpecPanel.SHAPE_MARGIN";
	public static final String SHAPE_USE_STROKE = "CoPageItemRunAroundSpecPanel.SHAPE_USE_STROKE";

	public static final String BOUNDS_LEFT = "CoPageItemRunAroundSpecPanel.BOUNDS_LEFT_MARGIN";
	public static final String BOUNDS_RIGHT = "CoPageItemRunAroundSpecPanel.BOUNDS_RIGHT_MARGIN";
	public static final String BOUNDS_TOP = "CoPageItemRunAroundSpecPanel.BOUNDS_TOP_MARGIN";
	public static final String BOUNDS_BOTTOM = "CoPageItemRunAroundSpecPanel.BOUNDS_BOTTOM_MARGIN";
	public static final String BOUNDS_USE_STROKE = "CoPageItemRunAroundSpecPanel.BOUNDS_USE_STROKE";

	public CoOptionMenu m_typeOptionMenu;
	public CoPanel m_attributePanel;
	public CardLayout m_attributeCardLayout;

	private CoPanel m_shape_panel;
	private CoTextField m_shape_marginTextfield;
	private CoCheckBox m_shape_useStrokeCheckBox;

	public CoPanel m_bounds_panel;
	public CoTextField m_bounds_leftTextfield;
	public CoTextField m_bounds_rightTextfield;
	public CoTextField m_bounds_topTextfield;
	public CoTextField m_bounds_bottomTextfield;
	public CoCheckBox m_bounds_useStrokeCheckBox;



public void doUpdate()
{
	CoImmutableRunAroundSpecIF r = m_domain.getRunAroundSpec();
	
	m_typeOptionMenu.setSelectedItem( r.getFactoryKey() );

	if
		( m_shape_panel.isVisible() )
	{
		CoImmutableShapeRunAroundSpecIF R = (CoImmutableShapeRunAroundSpecIF) r;
		m_shape_marginTextfield.setText( CoLengthUnitSet.format( R.getMargin(), CoLengthUnit.LENGTH_UNITS ) );
		m_shape_useStrokeCheckBox.setSelected( R.doUseStroke() );
	} else if
		( m_bounds_panel.isVisible() )
	{
		CoImmutableBoundingBoxRunAroundSpecIF R = (CoImmutableBoundingBoxRunAroundSpecIF) r;
		m_bounds_leftTextfield.setText( CoLengthUnitSet.format( R.getLeft(), CoLengthUnit.LENGTH_UNITS ) );
		m_bounds_rightTextfield.setText( CoLengthUnitSet.format( R.getRight(), CoLengthUnit.LENGTH_UNITS ) );
		m_bounds_topTextfield.setText( CoLengthUnitSet.format( R.getTop(), CoLengthUnit.LENGTH_UNITS ) );
		m_bounds_bottomTextfield.setText( CoLengthUnitSet.format( R.getBottom(), CoLengthUnit.LENGTH_UNITS ) );
		m_bounds_useStrokeCheckBox.setSelected( R.doUseStroke() );
	}
}

public CoPageItemRunAroundSpecPanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, new CoColumnLayout(), commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	add( m_typeOptionMenu = b.createOptionMenu() );
	m_typeOptionMenu.setRenderer( new CoOptionMenu.TranslatingRenderer( CoPageItemUIStringResources.getBundle() ) );
	m_typeOptionMenu.addItem( CoNoneRunAroundSpecIF.NONE_RUN_AROUND_SPEC );
	m_typeOptionMenu.addItem( CoShapeRunAroundSpecIF.SHAPE_RUN_AROUND_SPEC );
	m_typeOptionMenu.addItem( CoBoundingBoxRunAroundSpecIF.BOUNDING_BOX_RUN_AROUND_SPEC );


	
	add( m_attributePanel = b.createPanel( m_attributeCardLayout = new CardLayout() ) );
	

	{
		m_attributePanel.add( b.createLabel( "" ), CoNoneRunAroundSpecIF.NONE_RUN_AROUND_SPEC );
	}
	
	{
		m_shape_panel = b.createPanel( createFormLayout() );

		m_shape_panel.add( b.createLabel( CoPageItemUIStringResources.getName( SHAPE_MARGIN ) ) );
		m_shape_panel.add( m_shape_marginTextfield = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

		m_shape_panel.add( b.createLabel( CoPageItemUIStringResources.getName( SHAPE_USE_STROKE ) ) );
		m_shape_panel.add( m_shape_useStrokeCheckBox = b.createCheckBox( "", null ) );
		
		m_attributePanel.add( m_shape_panel, CoShapeRunAroundSpecIF.SHAPE_RUN_AROUND_SPEC );
	}
	
	{
		m_bounds_panel = b.createPanel( createFormLayout() );

		m_bounds_panel.add( b.createLabel( CoPageItemUIStringResources.getName( BOUNDS_LEFT ) ) );
		m_bounds_panel.add( m_bounds_leftTextfield = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

		m_bounds_panel.add( b.createLabel( CoPageItemUIStringResources.getName( BOUNDS_RIGHT ) ) );
		m_bounds_panel.add( m_bounds_rightTextfield = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

		m_bounds_panel.add( b.createLabel( CoPageItemUIStringResources.getName( BOUNDS_TOP ) ) );
		m_bounds_panel.add( m_bounds_topTextfield = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

		m_bounds_panel.add( b.createLabel( CoPageItemUIStringResources.getName( BOUNDS_BOTTOM ) ) );
		m_bounds_panel.add( m_bounds_bottomTextfield = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

		m_bounds_panel.add( b.createLabel( CoPageItemUIStringResources.getName( BOUNDS_USE_STROKE ) ) );
		m_bounds_panel.add( m_bounds_useStrokeCheckBox = b.createCheckBox( "", null ) );
		
		m_attributePanel.add( m_bounds_panel, CoBoundingBoxRunAroundSpecIF.BOUNDING_BOX_RUN_AROUND_SPEC );
	}

	m_typeOptionMenu.addItemListener(
		new ItemListener()
		{
			public void itemStateChanged( ItemEvent ev )
			{
				if
					( ev.getStateChange() == ItemEvent.SELECTED )
				{
					m_attributeCardLayout.show( m_attributePanel, ev.getItem().toString() );
				}
			}
		}
	);


	
	
	m_typeOptionMenu.addActionListener(
		new OptionMenuCommandAdapter( commandExecutor, CoPageItemCommands.SET_RUN_AROUND_SPEC, null )
		{
			protected Object getCurrentValue()
			{
				return m_domain.getRunAroundSpec().getFactoryKey();
			}
		
			protected void prepare()
			{
				CoPageItemFactoryIF f = (CoPageItemFactoryIF) CoFactoryManager.getFactory( CoPageItemFactoryIF.PAGE_ITEM_FACTORY );
				( (CoShapePageItemSetObjectCommand) m_command ).prepare( m_domain, f.createRunAroundSpec( (String) m_value ) );
			}
		}
	);
	
	m_shape_marginTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_SHAPE_RUN_AROUND_MARGIN ) );
	m_shape_useStrokeCheckBox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_RUN_AROUND_USE_STROKE ) );

	m_bounds_leftTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_BOUNDS_RUN_AROUND_LEFT_MARGIN ) );
	m_bounds_rightTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_BOUNDS_RUN_AROUND_RIGHT_MARGIN ) );
	m_bounds_topTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_BOUNDS_RUN_AROUND_TOP_MARGIN ) );
	m_bounds_bottomTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_BOUNDS_RUN_AROUND_BOTTOM_MARGIN ) );
	m_bounds_useStrokeCheckBox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_RUN_AROUND_USE_STROKE ) );

}
}