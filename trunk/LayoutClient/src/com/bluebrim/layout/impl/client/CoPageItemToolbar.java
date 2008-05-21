package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.transact.shared.*;

//

public class CoPageItemToolbar extends CoPanel implements CoPageItemView.Listener
{
	private boolean m_updateInProgress = false;

	private CoShapePageItemView m_domain;

	private CoTextField m_xTextfield;
	private CoTextField m_yTextfield;
	private CoTextField m_widthTextfield;
	private CoTextField m_heightTextfield;
	private CoCheckBox m_doRunAroundCheckBox;
	private CoPanel m_variablePanel;
	private CardLayout m_variablePanelCardLayout;

	private CoCheckBox m_derivedCheckBox;
	private CoTextField m_columnCountTextfield;

	private CoTextField m_xScaleTextfield;
	private CoTextField m_yScaleTextfield;
	
	public static final String TOOLBAR_X = "CoPageItemToolbar.X";
	public static final String TOOLBAR_Y = "CoPageItemToolbar.Y";
	public static final String TOOLBAR_W = "CoPageItemToolbar.W";
	public static final String TOOLBAR_H = "CoPageItemToolbar.H";
	public static final String TOOLBAR_DERIVED = "CoPageItemToolbar.DERIVED";
	public static final String TOOLBAR_COLUMN_COUNT = "CoPageItemToolbar.COLUMN_COUNT";
	public static final String TOOLBAR_DO_RUN_AROUND = "CoPageItemToolbar.DO_RUN_AROUND";
	public static final String TOOLBAR_IMAGE_X_SCALE = "CoPageItemToolbar.IMAGE_X_SCALE";
	public static final String TOOLBAR_IMAGE_Y_SCALE = "CoPageItemToolbar.IMAGE_Y_SCALE";

	private static final String TOOLBAR_GRID = "CoPageItemToolbar.GRID";
	private static final String TOOLBAR_IMAGE = "CoPageItemToolbar.IMAGE";
	private static final String TOOLBAR_NULL = "CoPageItemToolbar.NULL";
	


;


	private abstract class CommandAdapter extends CoCommandAdapter
	{
		public CommandAdapter( CoUndoableCommandExecutor commandExecutor, CoUndoableCommand command )
		{
			super( commandExecutor, command );
		}
		
		protected boolean isUpdateInProgress( ActionEvent ev ) { return m_updateInProgress; }
		protected boolean isCorrectDomain( ActionEvent ev ) { return m_domain != null; }

		protected void handleInvalidValue( ActionEvent ev )
		{
			domainHasChanged();
		}
	}


	


;


private void domainHasChanged()
{
	updateCardLayout();
	
	if
		( m_domain == null )
	{
		m_xTextfield.setText( "" );
		m_yTextfield.setText( "" );
		m_widthTextfield.setText( "" );
		m_heightTextfield.setText( "" );
		m_doRunAroundCheckBox.setSelected( false );
		m_derivedCheckBox.setSelected( false );
		m_columnCountTextfield.setText( "" );
		m_xScaleTextfield.setText( "" );
		m_yScaleTextfield.setText( "" );
		return;
	}

	m_updateInProgress = true;
	
	CoImmutableShapeIF s = m_domain.getCoShape();
	m_xTextfield.setText( CoLengthUnitSet.format( m_domain.getX(), CoLengthUnit.LENGTH_UNITS ) );
	m_yTextfield.setText( CoLengthUnitSet.format( m_domain.getY(), CoLengthUnit.LENGTH_UNITS ) );
	m_widthTextfield.setText( CoLengthUnitSet.format( s.getWidth(), CoLengthUnit.LENGTH_UNITS ) );
	m_heightTextfield.setText( CoLengthUnitSet.format( s.getHeight(), CoLengthUnit.LENGTH_UNITS ) );
	m_doRunAroundCheckBox.setSelected( m_domain.getDoRunAround() );

	m_xTextfield.setEnabled( ! m_domain.isSlave() );
	m_yTextfield.setEnabled( ! m_domain.isSlave() );
	m_widthTextfield.setEnabled( ! m_domain.isSlave() );
	m_heightTextfield.setEnabled( ! m_domain.isSlave() );
	

	CoImmutableColumnGridIF g = m_domain.getColumnGrid();
	if
		( g != null )
	{
		m_derivedCheckBox.setSelected( g.isDerived() );
		if
			( g.isDerived() )
		{
			m_columnCountTextfield.setText( "" );
		} else {
			m_columnCountTextfield.setText( "" + g.getColumnCount() );
		}
	}

	if
		( m_domain instanceof CoContentWrapperPageItemView )
	{
		CoPageItemContentView cv = ( (CoContentWrapperPageItemView) m_domain ).getContentView();
		if
			( cv instanceof CoPageItemBoundedContentView )
		{
			CoPageItemBoundedContentView i = (CoPageItemBoundedContentView) ( (CoContentWrapperPageItemView) m_domain ).getContentView();
			m_xScaleTextfield.setText( CoLengthUnitSet.format( i.getScaleX() * 100 ) );
			m_yScaleTextfield.setText( CoLengthUnitSet.format( i.getScaleY() * 100 ) );
		}
	}

	m_updateInProgress = false;
}
public JComponent getDerivedCheckBox()
{
	return m_derivedCheckBox;
}
public CoShapePageItemView getDomain()
{
	return m_domain;
}
public JComponent getXScaleTextField()
{
	return m_xScaleTextfield;
}
public JComponent getXTextField()
{
	return m_xTextfield;
}
public void setDomain( CoShapePageItemView domain )
{
	if
		( m_domain != null )
	{
		m_domain.removeViewListener( this );
	}

	m_domain = domain;
	
	if
		( m_domain != null )
	{
		m_domain.addViewListener( this );
	}

	updateCardLayout();
	domainHasChanged();
}
protected void updateCardLayout()
{
	setEnabled( m_domain != null );

	boolean hasGrid = ( m_domain != null ) && ( m_domain.getColumnGrid() != null );
	boolean isBoundedContent = ( m_domain != null ) && ( m_domain instanceof CoContentWrapperPageItemView ) && ( ( (CoContentWrapperPageItemView) m_domain ).getContentView() instanceof CoPageItemBoundedContentView );

	if
		( hasGrid )
	{
		m_variablePanelCardLayout.show( m_variablePanel, TOOLBAR_GRID );
	} else if
		( isBoundedContent )
	{
		m_variablePanelCardLayout.show( m_variablePanel, TOOLBAR_IMAGE );
	} else {
		m_variablePanelCardLayout.show( m_variablePanel, TOOLBAR_NULL );
	}

	invalidate();
	Container w = getTopLevelAncestor();
//	if ( w != null ) w.validate();
	repaint();
}
public void valueHasChanged()
{
	domainHasChanged();
}




protected CoCheckBox createCheckBox( CoUserInterfaceBuilder b, String name )
{
	CoCheckBox cb = b.createCheckBox( name, null );

	cb.setMargin( new Insets( 0, 0, 0, 0 ) );
	cb.setBorder( BorderFactory.createLineBorder( Color.black ) );

	return cb;
}

protected CoTextField createTextField( CoUserInterfaceBuilder b, int alignment, int columnCount )
{
	CoTextField tf = b.createTextField( alignment, columnCount );
	tf.setActivateWhenLosingFocus( true );
	tf.setSelectWhenGainingFocus( true );
	tf.setMargin( new Insets( 0, 0, 0, 1 ) );
	tf.setBorder( BorderFactory.createLineBorder( Color.black ) );
	tf.setFont( tf.getFont().deriveFont( tf.getFont().getSize2D() - 2 ) );

	return tf;
}



public CoPageItemToolbar( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	// create ui
	
	b.preparePanel( this );


	setLayout( new CoRowLayout( 5 ) );
	setOpaque( true );

	{
		CoPanel p = b.createPanel( new CoFormLayout( 2, 2 ) );
		p.add( b.createLabel( CoPageItemUIStringResources.getName( TOOLBAR_X ), null, CoLabel.RIGHT ) );
		p.add( m_xTextfield = createTextField( b, CoTextField.RIGHT, 10 ) );
		p.add( b.createLabel( CoPageItemUIStringResources.getName( TOOLBAR_Y ), null, CoLabel.RIGHT ) );
		p.add( m_yTextfield = createTextField( b, CoTextField.RIGHT, 10 ) );
		add( p );
	}

	{
		CoPanel p = b.createPanel( new CoFormLayout( 2, 2 ) );
		p.add( b.createLabel( CoPageItemUIStringResources.getName( TOOLBAR_W ), null, CoLabel.RIGHT ) );
		p.add( m_widthTextfield = createTextField( b, CoTextField.RIGHT, 10 ) );
		p.add( b.createLabel( CoPageItemUIStringResources.getName( TOOLBAR_H ), null, CoLabel.RIGHT ) );
		p.add( m_heightTextfield = createTextField( b, CoTextField.RIGHT, 10 ) );
		add( p );
	}

	add( m_doRunAroundCheckBox = createCheckBox( b, CoPageItemUIStringResources.getName( TOOLBAR_DO_RUN_AROUND ) ) );

	m_variablePanel = b.createPanel( m_variablePanelCardLayout = new CardLayout() );
	add( m_variablePanel );

	
	m_variablePanel.add( b.createPanel(), TOOLBAR_NULL );


	{
		CoPanel p = b.createPanel( new CoFormLayout( 2, 2 ), TOOLBAR_IMAGE );
		p.add( b.createLabel( CoPageItemUIStringResources.getName( TOOLBAR_IMAGE_X_SCALE ), null, CoLabel.RIGHT ) );
		p.add( m_xScaleTextfield = createTextField( b, CoTextField.RIGHT, 5 ) );
		p.add( b.createLabel( CoPageItemUIStringResources.getName( TOOLBAR_IMAGE_Y_SCALE ), null, CoLabel.RIGHT ) );
		p.add( m_yScaleTextfield = createTextField( b, CoTextField.RIGHT, 5 ) );
		m_variablePanel.add( p, TOOLBAR_IMAGE );
	}


	{
		CoPanel p = b.createPanel( new CoAttachmentLayout(), TOOLBAR_GRID );
		m_derivedCheckBox = createCheckBox( b, CoPageItemUIStringResources.getName( TOOLBAR_DERIVED ) );
		CoLabel l = b.createLabel( CoPageItemUIStringResources.getName( TOOLBAR_COLUMN_COUNT ), null, CoLabel.RIGHT );
		m_columnCountTextfield = createTextField( b, CoTextField.RIGHT, 2 );

		p.add( m_derivedCheckBox,
				   new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
									                             new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ) ) );

		p.add( l,
				   new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, m_columnCountTextfield ),
									                             new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_BOTTOM, m_columnCountTextfield ),
									                             new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_LEFT, m_derivedCheckBox ) ) );

		p.add( m_columnCountTextfield,
				   new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, m_derivedCheckBox ),
									                             new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 2, l ) ) );
		m_variablePanel.add( p, TOOLBAR_GRID );
	}






	// ui behavior
	
	m_derivedCheckBox.addItemListener(
		new ItemListener()
		{
			public void itemStateChanged( ItemEvent ev )
			{
				m_columnCountTextfield.setEnabled( ev.getStateChange() == ItemEvent.DESELECTED );
			}
		}
	);






	// set up connections to model

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


	m_doRunAroundCheckBox.addActionListener(
		new CommandAdapter( commandExecutor, CoPageItemCommands.SET_DO_RUN_AROUND )
		{
			private boolean m_runAround;

			protected boolean isNewValue( ActionEvent ev )
			{
				m_runAround = m_doRunAroundCheckBox.isSelected();
				return m_runAround != m_domain.getDoRunAround();
			}
			
			protected void prepare()
			{
				( (CoShapePageItemSetBooleanCommand) m_command ).prepare( m_domain, m_runAround );
			}
		}
	);


	m_xScaleTextfield.addActionListener(
		new CommandAdapter( commandExecutor, CoPageItemCommands.SET_BOUNDED_CONTENT_SCALE_X )
		{
			private double m_sx;
			
			protected boolean isValueValid( ActionEvent ev ) { return ! Double.isNaN( m_sx ); }

			protected boolean isNewValue( ActionEvent ev )
			{
				m_sx = CoLengthUnitSet.parse( m_xScaleTextfield.getText(), Double.NaN ) / 100f;
				return isNotEqual( ( (CoPageItemBoundedContentView) ( (CoContentWrapperPageItemView) m_domain ).getContentView() ).getScaleX(), m_sx );
			}
			
			protected void prepare()
			{
				( (CoShapePageItemSetDoubleCommand) m_command ).prepare( m_domain, m_sx );
			}
		}
	);

	m_yScaleTextfield.addActionListener(
		new CommandAdapter( commandExecutor, CoPageItemCommands.SET_BOUNDED_CONTENT_SCALE_Y )
		{
			private double m_sy;
			
			protected boolean isValueValid( ActionEvent ev ) { return ! Double.isNaN( m_sy ); }

			protected boolean isNewValue( ActionEvent ev )
			{
				m_sy = CoLengthUnitSet.parse( m_yScaleTextfield.getText(), Double.NaN ) / 100f;
				return isNotEqual( ( (CoPageItemBoundedContentView) ( (CoContentWrapperPageItemView) m_domain ).getContentView() ).getScaleY(), m_sy );
			}
			
			protected void prepare()
			{
				( (CoShapePageItemSetDoubleCommand) m_command ).prepare( m_domain, m_sy );
			}
		}
	);


	m_derivedCheckBox.addActionListener(
		new CommandAdapter( commandExecutor, CoPageItemCommands.SET_COLUMN_GRID_DERIVED )
		{
			private boolean m_isDerived;

			protected boolean isNewValue( ActionEvent ev )
			{
				m_isDerived = m_derivedCheckBox.isSelected();
				return m_isDerived != m_domain.getColumnGrid().isDerived();
			}
			
			protected void prepare()
			{
				( (CoShapePageItemSetColumnGridDerivedCommand) m_command ).prepare( m_domain, m_isDerived );
			}
		}
	);



	m_columnCountTextfield.addActionListener(
		new CommandAdapter( commandExecutor, CoPageItemCommands.SET_COLUMN_COUNT )
		{
			private int m_c;
			
			protected boolean isValueValid( ActionEvent ev ) { return ( m_c != -1 ) && ( m_c <= 100 ); }

			protected boolean isNewValue( ActionEvent ev )
			{
				m_c = CoLengthUnitSet.parse( m_columnCountTextfield.getText(), -1 );
				return m_domain.getColumnGrid().getColumnCount() != m_c;
			}
			
			protected void prepare()
			{
				( (CoShapePageItemSetIntegerCommand) m_command ).prepare( m_domain, m_c );
			}
		}
	);

}

public void viewChanged( CoPageItemView.Event ev )// view, boolean boundsChanged, int structuralChange, CoShapePageItemView child, int index )
{
	domainHasChanged();
}
}