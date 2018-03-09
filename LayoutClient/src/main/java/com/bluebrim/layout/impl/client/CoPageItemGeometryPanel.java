package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.swing.client.*;

/**
 * Page item geometry property panel.
 *
 * @author: Dennis
 */

public class CoPageItemGeometryPanel extends CoPageItemPropertyPanel
{
	private static final CoShapeFactoryIF m_shapeFactory = (CoShapeFactoryIF) CoFactoryManager.getFactory( CoShapeFactoryIF.SHAPE_FACTORY );

	public static final String ROTATION 	= "CoPageItemGeometryPanel.ROTATION";
	public static final String DO_RUN_AROUND = "CoPageItemGeometryPanel.DO_RUN_AROUND";
	public static final String SUPRESS_PRINTOUT = "CoPageItemGeometryPanel.SUPRESS_PRINTOUT";

	private static final String RECTANGULAR = "RECTANGULAR";
	private static final String CORNER = "CORNER";
	private static final String LINE = "LINE";
	private static final String BOXED_LINE = "BOXED_LINE";
	private static final String CURVE = "CURVE";

	public CoPanel m_shapeAttributePanel;
	public CardLayout m_shapeAttributeCardLayout;
	public CoOptionMenu m_shapeOptionMenu;
	public CoTextField m_rotationTextfield;
	public CoCheckBox m_runAroundCheckbox;
	public CoCheckBox m_supressPrintoutCheckbox;

	public CoPageItemShapePanel m_rectangularPanel;
	public CoPageItemShapePanel m_cornerPanel;
	public CoPageItemShapePanel m_linePanel;
	public CoPageItemShapePanel m_boxedLinePanel;
	public CoPageItemShapePanel m_curvePanel;




public void doUpdate()
{
	CoImmutableTransformIF t = m_domain.getTransform();
	CoImmutableShapeIF s = m_domain.getCoShape();
	
	m_shapeOptionMenu.setSelectedItem( s.getFactoryKey() );
	m_rotationTextfield.setText( CoLengthUnitSet.format( t.getRotation() * 180.0 / Math.PI ) );
	m_runAroundCheckbox.setSelected( m_domain.getDoRunAround() );
	m_supressPrintoutCheckbox.setSelected( m_domain.getSupressPrintout() );
	m_isLocationLockedCheckbox.setSelected( m_domain.isLocationLocked() );
	m_areDimensionsLockedCheckbox.setSelected( m_domain.areDimensionsLocked() );

	if
		( m_domain instanceof CoCompositePageItemView )
	{
		m_areChildrenLockedCheckbox.setSelected( ( (CoCompositePageItemView) m_domain ).areChildrenLocked() );
	} else {
		m_areChildrenLockedCheckbox.setSelected( false );
	}
	
	m_rectangularPanel.doUpdate();
	m_cornerPanel.doUpdate();
	m_linePanel.doUpdate();
	m_boxedLinePanel.doUpdate();
	m_curvePanel.doUpdate();
}
public void postSetDomain()
{
	super.postSetDomain();
	
	m_rectangularPanel.setDomain( m_domain );
	m_cornerPanel.setDomain( m_domain );
	m_linePanel.setDomain( m_domain );
	m_boxedLinePanel.setDomain( m_domain );
	m_curvePanel.setDomain( m_domain );

	m_areChildrenLockedCheckbox.setVisible( m_domain instanceof CoCompositePageItemView );
}

	public static final String CHILDREN_LOCKED = "CoPageItemGeometryPanel.CHILDREN_LOCKED";
	public static final String DIMENSIONS_LOCKED = "CoPageItemGeometryPanel.DIMENSIONS_LOCKED";
	public static final String LOCATION_LOCKED = "CoPageItemGeometryPanel.LOCATION_LOCKED";
	public CoCheckBox m_areChildrenLockedCheckbox;
	public CoCheckBox m_areDimensionsLockedCheckbox;
	public CoCheckBox m_isLocationLockedCheckbox;

public CoPageItemGeometryPanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, new CoAttachmentLayout(), commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	m_shapeOptionMenu = b.createOptionMenu();
	CoPanel geometryAttributePanel = createGeometryAttributePanel( b, commandExecutor );
	m_shapeAttributePanel = b.createPanel( m_shapeAttributeCardLayout = new CardLayout() );


	add( m_shapeOptionMenu,
			       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
				                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
				                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
				                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( geometryAttributePanel,
			       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, m_shapeAttributePanel ),
				                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
				                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 10, m_shapeAttributePanel ),
				                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_shapeAttributePanel,
			       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, m_shapeOptionMenu ),
				                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
				                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 5 ),
				                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );


	
	m_shapeAttributePanel.add( m_rectangularPanel = new CoPageItemRectangularShapePanel( b, commandExecutor ), RECTANGULAR );
	m_shapeAttributePanel.add( m_cornerPanel = new CoPageItemCornerShapePanel( b, commandExecutor ), CORNER );
	m_shapeAttributePanel.add( m_linePanel = new CoPageItemLineShapePanel( b, commandExecutor ), LINE );
	m_shapeAttributePanel.add( m_boxedLinePanel = new CoPageItemBoxedLineShapePanel( b, commandExecutor ), BOXED_LINE );
	m_shapeAttributePanel.add( m_curvePanel = new CoPageItemCurveShapePanel( b, commandExecutor ), CURVE );


	add( m_shapeOptionMenu,
							       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
								                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
								                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
								                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );

	
	m_shapeOptionMenu.addItem( CoRectangleShapeIF.RECTANGLE_SHAPE );
	m_shapeOptionMenu.addItem( CoEllipseShapeIF.ELLIPSE_SHAPE );
	m_shapeOptionMenu.addItem( CoRoundedCornerIF.ROUNDED_CORNER );
	m_shapeOptionMenu.addItem( CoBeveledCornerIF.BEVELED_CORNER );
	m_shapeOptionMenu.addItem( CoConcaveCornerIF.CONCAVE_CORNER );
	m_shapeOptionMenu.addItem( CoBoxedLineShapeIF.BOXED_LINE );
	m_shapeOptionMenu.addItem( CoLineIF.LINE );
	m_shapeOptionMenu.addItem( CoLineIF.ORTHOGONAL_LINE );
	m_shapeOptionMenu.addItem( CoPolygonShapeIF.POLYGON_SHAPE );
	m_shapeOptionMenu.addItem( CoCubicBezierCurveShapeIF.CUBIC_BEZIER_CURVE );
//	m_shapeOptionMenu.setMaximumRowCount( m_shapeOptionMenu.getItemCount() );
	m_shapeOptionMenu.setRenderer( new CoOptionMenu.TranslatingRenderer( CoPageItemStringResources.getBundle() ) );

	final Map m = new HashMap();
	{
		m.put( CoRectangleShapeIF.RECTANGLE_SHAPE, RECTANGULAR );
		m.put( CoEllipseShapeIF.ELLIPSE_SHAPE, RECTANGULAR );
		m.put( CoRoundedCornerIF.ROUNDED_CORNER, CORNER );
		m.put( CoBeveledCornerIF.BEVELED_CORNER, CORNER );
		m.put( CoConcaveCornerIF.CONCAVE_CORNER, CORNER );
		m.put( CoBoxedLineShapeIF.BOXED_LINE, BOXED_LINE );
		m.put( CoLineIF.LINE, LINE );
		m.put( CoLineIF.ORTHOGONAL_LINE, LINE );
		m.put( CoPolygonShapeIF.POLYGON_SHAPE, CURVE );
		m.put( CoCubicBezierCurveShapeIF.CUBIC_BEZIER_CURVE, CURVE );
	}

	
	m_shapeOptionMenu.addItemListener(
		new ItemListener()
		{
			public void itemStateChanged( ItemEvent ev )
			{
				if
					( ev.getStateChange() == ItemEvent.SELECTED )
				{
					String key = (String) m.get( ev.getItem().toString() );
					m_shapeAttributeCardLayout.show( m_shapeAttributePanel, key );
				}
			}
		}
	);



	m_shapeOptionMenu.addActionListener(
		new OptionMenuCommandAdapter( commandExecutor, CoPageItemCommands.RESHAPE, null )
		{
			protected Object getCurrentValue() { return m_domain.getCoShape().getFactoryKey(); }
		
			protected void prepare()
			{
				CoShapeIF shape = m_shapeFactory.createShape( (String) m_value, m_domain.getCoShape() );
				( (CoShapePageItemReshapeCommand) m_command ).prepare( m_domain, shape, false );
			}
		}
	);

}

private CoPanel createGeometryAttributePanel( CoUserInterfaceBuilder builder, CoUndoableCommandExecutor commandExecutor )
{
	CoPanel panel = builder.createPanel( new CoAttachmentLayout() );

	CoLabel rotationLabel = builder.createLabel( CoPageItemUIStringResources.getName( ROTATION ) );
	m_rotationTextfield = builder.createSlimTextField( CoTextField.RIGHT, 5 );
	m_runAroundCheckbox = builder.createCheckBox( CoPageItemUIStringResources.getName( DO_RUN_AROUND ), null );
	m_supressPrintoutCheckbox = builder.createCheckBox( CoPageItemUIStringResources.getName( SUPRESS_PRINTOUT ), null );
	m_isLocationLockedCheckbox = builder.createCheckBox( CoPageItemUIStringResources.getName( LOCATION_LOCKED ), null );
	m_areDimensionsLockedCheckbox = builder.createCheckBox( CoPageItemUIStringResources.getName( DIMENSIONS_LOCKED ), null );
	m_areChildrenLockedCheckbox = builder.createCheckBox( CoPageItemUIStringResources.getName( CHILDREN_LOCKED ), null );

	panel.add( rotationLabel,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, m_rotationTextfield ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_BOTTOM, 0, m_rotationTextfield ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	panel.add( m_rotationTextfield,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, rotationLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	panel.add( m_runAroundCheckbox,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, m_rotationTextfield ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_LEFT, 0, rotationLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	panel.add( m_supressPrintoutCheckbox,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, m_runAroundCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_LEFT, 0, m_runAroundCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	panel.add( m_isLocationLockedCheckbox,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, m_supressPrintoutCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_LEFT, 0, m_supressPrintoutCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	panel.add( m_areDimensionsLockedCheckbox,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, m_isLocationLockedCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_LEFT, 0, m_isLocationLockedCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	panel.add( m_areChildrenLockedCheckbox,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, m_areDimensionsLockedCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_LEFT, 0, m_areDimensionsLockedCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );




	m_rotationTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_ROTATION, false, Math.PI / 180.0, 0 ) 	);
	m_runAroundCheckbox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_DO_RUN_AROUND ) );
	m_supressPrintoutCheckbox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_SUPRESS_PRINTOUT ) );
	m_isLocationLockedCheckbox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_LOCATION_LOCKED ) );
	m_areDimensionsLockedCheckbox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_DIMENSIONS_LOCKED ) );
	m_areChildrenLockedCheckbox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_CHILDREN_LOCKED ) );

	return panel;
}
}