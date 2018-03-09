package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.resource.shared.*;
import com.bluebrim.swing.client.*;

/**
 * Page item layout spec property panel.
 *
 * @author: Dennis
 */

public class CoPageItemLayoutSpecPanel extends CoPageItemPropertyPanel
{
	private static final CoLayoutSpecFactoryIF m_layoutSpecFactory = (CoLayoutSpecFactoryIF) CoFactoryManager.getFactory( CoLayoutSpecIF.LAYOUT_SPEC );

		
	public static final String LOCATION = CoLocationSpecIF.LOCATION_SPEC;
	public static final String LOCATION_AGGRESSIVE = "CoPageItemLayoutSpecPanel.LOCATION_AGGRESSIVE";
	public static final String LOCATION_NONE = "CoPageItemLayoutSpecPanel.LOCATION_NONE";
	public static final String LOCATION_INSET = "CoPageItemLayoutSpecPanel.LOCATION_INSET";

	public static final String SIZE_NONE = "CoPageItemLayoutSpecPanel.SIZE_NONE";
	public static final String SIZE_ABSOLUTE = "CoPageItemLayoutSpecPanel.SIZE_ABSOLUTE";
	public static final String SIZE_DISTANCE = "CoPageItemLayoutSpecPanel.SIZE_DISTANCE";
	public static final String SIZE_CONTENT = "CoPageItemLayoutSpecPanel.SIZE_CONTENT";
	public static final String SIZE_ABSOLUTE_OFFSET = "CoPageItemLayoutSpecPanel.SIZE_ABSOLUTE_OFFSET";
	public static final String SIZE_RELATIVE_OFFSET = "CoPageItemLayoutSpecPanel.SIZE_RELATIVE_OFFSET";
	public static final String SIZE_RELATIVE = "CoPageItemLayoutSpecPanel.SIZE_RELATIVE";
	public static final String SIZE_PROPORTION = "CoPageItemLayoutSpecPanel.SIZE_PROPORTION";

	public static final String SIZE_WIDTH = CoSizeSpecIF.WIDTH_SPEC;
	public static final String SIZE_HEIGHT = CoSizeSpecIF.HEIGHT_SPEC;


	private static final Icon m_topLeftLocationIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoTopLeftLocation.gif" );
	private static final Icon m_topLocationIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoTopLocation.gif" );
	private static final Icon m_topRightLocationIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoTopRightLocation.gif" );
	private static final Icon m_leftLocationIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoLeftLocation.gif" );
	private static final Icon m_centerLocationIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoCenterLocation.gif" );
	private static final Icon m_rightLocationIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoRightLocation.gif" );
	private static final Icon m_bottomLeftLocationIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoBottomLeftLocation.gif" );
	private static final Icon m_bottomLocationIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoBottomLocation.gif" );
	private static final Icon m_bottomRightLocationIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoBottomRightLocation.gif" );
	private static final Icon m_noLocationIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoNoLocation.gif" );

	private static final Icon m_topOutsideLocationIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoTopOutsideLocation.gif" );
	private static final Icon m_topInsideLocationIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoTopInsideLocation.gif" );
	private static final Icon m_bottomOutsideLocationIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoBottomOutsideLocation.gif" );
	private static final Icon m_bottomInsideLocationIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoBottomInsideLocation.gif" );
	

	private static final Icon m_noWidthIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoNoWidthSpec.gif" );
	private static final Icon m_absoluteWidthIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoAbsoluteWidthSpec.gif" );
	private static final Icon m_fillWidthIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoFillWidthSpec.gif" );
	private static final Icon m_contentWidthIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoContentWidthSpec.gif" );
	private static final Icon m_proportionalWidthIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoProportionalWidthSpec.gif" );

	private static final Icon m_noHeightIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoNoHeightSpec.gif" );
	private static final Icon m_absoluteHeightIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoAbsoluteHeightSpec.gif" );
	private static final Icon m_fillHeightIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoFillHeightSpec.gif" );
	private static final Icon m_contentHeightIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoContentHeightSpec.gif" );
	private static final Icon m_proportionalHeightIcon = CoResourceLoader.loadIcon( CoPageItemLayoutSpecPanel.class, "CoProportionalHeightSpec.gif" );
	

	
	private CoButtonGroup m_locationSpec_buttonGroup;
	private CoPanel m_locationSpec_attributePanel;
	private CardLayout m_locationSpec_attributeCardLayout;
	private CoButtonGroup m_pageLocationSpec_buttonGroup;

	private CoButtonGroup m_widthSpec_buttonGroup;
	private CoPanel m_widthSpec_attributePanel;
	private CardLayout m_widthSpec_attributeCardLayout;
	private CoTextField m_widthSpec_absoluteTextfield;
	private CoTextField m_widthSpec_contentAbsoluteOffsetTextfield;
	private CoTextField m_widthSpec_contentRelativeOffsetTextfield;
	private CoTextField m_widthSpec_proportionTextfield;

	private CoButtonGroup m_heightSpec_buttonGroup;
	private CoPanel m_heightSpec_attributePanel;
	private CardLayout m_heightSpec_attributeCardLayout;
	private CoTextField m_heightSpec_absoluteTextfield;
	private CoTextField m_heightSpec_contentAbsoluteOffsetTextfield;
	private CoTextField m_heightSpec_contentRelativeOffsetTextfield;
	private CoTextField m_heightSpec_proportionTextfield;
	private CoTextField m_insetLocationSpec_insetXTextfield;
	private CoCheckBox m_insetLocationSpec_aggressiveCheckbox;
	private CoButtonGroup m_insetLocationSpec_buttonGroup;






public CoPageItemLayoutSpecPanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, new CoColumnLayout( 5 ), commandExecutor );
}
protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	CoPanel p;
	
	add( p = createLocationSpecPanel( b, commandExecutor ) );
	p.setBorder( BorderFactory.createTitledBorder( CoPageItemUIStringResources.getName( LOCATION ) ) );
	
	add( p = createWidthSpecPanel( b, commandExecutor ) );
	p.setBorder( BorderFactory.createTitledBorder( CoPageItemUIStringResources.getName( SIZE_WIDTH ) ) );
	
	add( p = createHeightSpecPanel( b, commandExecutor ) );
	p.setBorder( BorderFactory.createTitledBorder( CoPageItemUIStringResources.getName( SIZE_HEIGHT ) ) );
}
private CoPanel createHeightSpecPanel( CoUserInterfaceBuilder builder, CoUndoableCommandExecutor commandExecutor )
{
	CoPanel panel = builder.createPanel( new CoAttachmentLayout() );

	m_heightSpec_buttonGroup = builder.createButtonGroup();

	CoCommandAdapter c =
		new ToggleButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_HEIGHT_SPEC )
		{
			protected String getCurrentValue() { return m_domain.getHeightSpec().getFactoryKey(); }
		
			protected void prepare()
			{
				CoImmutableSizeSpecIF ls = m_layoutSpecFactory.getSizeSpec( (String) m_value, m_domain.getShapePageItem() );
				( (CoShapePageItemSetObjectCommand) m_command ).prepare( m_domain, ls );
			}
		};

	CoPanel p = builder.createPanel( new CoRowLayout( true ) );
	panel.add( p );
	{
		CoToggleButton b;
		b = builder.create3DToggleButton( "", m_noHeightIcon, m_heightSpec_buttonGroup, CoNoSizeSpecIF.NO_SIZE_SPEC );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_absoluteHeightIcon, m_heightSpec_buttonGroup, CoAbsoluteHeightSpecIF.ABSOLUTE_HEIGHT_SPEC );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_fillHeightIcon, m_heightSpec_buttonGroup, CoFillHeightSpecIF.FILL_HEIGHT_SPEC );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_contentHeightIcon, m_heightSpec_buttonGroup, CoContentHeightSpecIF.CONTENT_HEIGHT_SPEC );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_proportionalHeightIcon, m_heightSpec_buttonGroup, CoProportionalHeightSpecIF.PROPORTIONAL_HEIGHT_SPEC );
		p.add( b );
		b.addActionListener( c );
	}


	
	m_heightSpec_attributePanel = builder.createPanel( m_heightSpec_attributeCardLayout = new CardLayout() );
	
	{
		m_heightSpec_attributePanel.add( builder.createLabel( "" ), SIZE_NONE );
	}
	
	{
		CoPanel tmp = builder.createPanel( createFormLayout() );
		tmp.add( builder.createLabel( CoPageItemUIStringResources.getName( SIZE_DISTANCE ) ) );
		tmp.add( m_heightSpec_absoluteTextfield = builder.createSlimTextField( CoTextField.RIGHT, 6 ) );
		m_heightSpec_attributePanel.add( tmp, SIZE_ABSOLUTE );
	}
	
	{
		CoPanel tmp = builder.createPanel( createFormLayout() );
		tmp.add( builder.createLabel( CoPageItemUIStringResources.getName( SIZE_ABSOLUTE_OFFSET ) ) );
		tmp.add( m_heightSpec_contentAbsoluteOffsetTextfield = builder.createSlimTextField( CoTextField.RIGHT, 6 ) );
		tmp.add( builder.createLabel( CoPageItemUIStringResources.getName( SIZE_RELATIVE_OFFSET ) ) );
		tmp.add( m_heightSpec_contentRelativeOffsetTextfield = builder.createSlimTextField( CoTextField.RIGHT, 6 ) );
		m_heightSpec_attributePanel.add( tmp, SIZE_CONTENT );
	}
	
	{
		CoPanel tmp = builder.createPanel( createFormLayout() );
		tmp.add( builder.createLabel( CoPageItemUIStringResources.getName( SIZE_PROPORTION ) ) );
		tmp.add( m_heightSpec_proportionTextfield = builder.createSlimTextField( CoTextField.RIGHT, 6 ) );
		m_heightSpec_attributePanel.add( tmp, SIZE_RELATIVE );
	}

	final Map m = new HashMap();
	{
		m.put( CoNoSizeSpecIF.NO_SIZE_SPEC, SIZE_NONE );
		m.put( CoAbsoluteHeightSpecIF.ABSOLUTE_HEIGHT_SPEC, SIZE_ABSOLUTE );
		m.put( CoFillHeightSpecIF.FILL_HEIGHT_SPEC, SIZE_NONE );
		m.put( CoContentHeightSpecIF.CONTENT_HEIGHT_SPEC, SIZE_CONTENT );
		m.put( CoProportionalHeightSpecIF.PROPORTIONAL_HEIGHT_SPEC, SIZE_RELATIVE );
	}
	
	m_heightSpec_buttonGroup.addSelectedButtonListener(
		new CoSelectedButtonListener()
		{
			public void selectedButtonChanged( CoSelectedButtonEvent ev )
			{
				m_heightSpec_attributeCardLayout.show( m_heightSpec_attributePanel,
					                                    (String) m.get( ev.getSelectedButton().getActionCommand() ) );
			}
		}
	);

	
	panel.add( p,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	panel.add( m_heightSpec_attributePanel,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, p ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );

		

	m_heightSpec_absoluteTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_ABSOLUTE_HEIGHT_SPEC_DISTANCE ) );
	m_heightSpec_contentAbsoluteOffsetTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_CONTENT_HEIGHT_SPEC_ABSOLUTE_OFFSET ) );
	m_heightSpec_contentRelativeOffsetTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_CONTENT_HEIGHT_SPEC_RELATIVE_OFFSET, false, 1 / 100f, 0 ) );
	m_heightSpec_proportionTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_PROPORTIONAL_HEIGHT_SPEC_PROPORTION, false, 1 / 100f, 0 ) );

	return panel;
}
private CoPanel createLocationSpecPanel( CoUserInterfaceBuilder builder, CoUndoableCommandExecutor commandExecutor )
{
	CoPanel panel = builder.createPanel( new CoAttachmentLayout() );

	m_locationSpec_buttonGroup = builder.createButtonGroup();
	m_pageLocationSpec_buttonGroup=builder.createButtonGroup();
	CoToggleButton b;

	
	CoCommandAdapter c =
		new ToggleButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_LOCATION_SPEC )
		{
			protected String getCurrentValue() { return m_domain.getLocationSpec().getFactoryKey(); }
		
			protected void prepare()
			{
				CoImmutableLocationSpecIF ls = m_layoutSpecFactory.getLocationSpec( (String) m_value );
				( (CoShapePageItemSetObjectCommand) m_command ).prepare( m_domain, ls );
			}
		};
	
	CoPanel p = builder.createPanel( new GridLayout( 3, 3 ) );
	{
		b = builder.create3DToggleButton( "", m_topLeftLocationIcon, m_locationSpec_buttonGroup, CoCornerLocationSpecIF.TOP_LEFT );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_topLocationIcon, m_locationSpec_buttonGroup, CoTopLocationIF.TOP_LOCATION );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_topRightLocationIcon, m_locationSpec_buttonGroup, CoCornerLocationSpecIF.TOP_RIGHT );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_leftLocationIcon, m_locationSpec_buttonGroup, CoLeftLocationIF.LEFT_LOCATION );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_centerLocationIcon, m_locationSpec_buttonGroup, CoCenterLocationIF.CENTER_LOCATION );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_rightLocationIcon, m_locationSpec_buttonGroup, CoRightLocationIF.RIGHT_LOCATION );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_bottomLeftLocationIcon, m_locationSpec_buttonGroup, CoCornerLocationSpecIF.BOTTOM_LEFT );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_bottomLocationIcon, m_locationSpec_buttonGroup, CoBottomLocationIF.BOTTOM_LOCATION );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_bottomRightLocationIcon, m_locationSpec_buttonGroup, CoCornerLocationSpecIF.BOTTOM_RIGHT );
		p.add( b );
		b.addActionListener( c );
	}
		
	

	CoPanel tPanel=builder.createPanel(new GridLayout(2,2));
	{
		b=builder.create3DToggleButton("", m_topOutsideLocationIcon, m_locationSpec_buttonGroup,CoCornerLocationSpecIF.TOP_OUTSIDE);
		tPanel.add(b);	
		b.addActionListener(c);

		b=builder.create3DToggleButton("", m_topInsideLocationIcon, m_locationSpec_buttonGroup,CoCornerLocationSpecIF.TOP_INSIDE);
		tPanel.add(b);	
		b.addActionListener(c);

		b=builder.create3DToggleButton("", m_bottomOutsideLocationIcon, m_locationSpec_buttonGroup,CoCornerLocationSpecIF.BOTTOM_OUTSIDE);
		tPanel.add(b);	
		b.addActionListener(c);

		b=builder.create3DToggleButton("", m_bottomInsideLocationIcon, m_locationSpec_buttonGroup,CoCornerLocationSpecIF.BOTTOM_INSIDE);
		tPanel.add(b);	
		b.addActionListener(c);
	}
	
	b = builder.create3DToggleButton( "", m_noLocationIcon, m_locationSpec_buttonGroup, CoNoLocationIF.NO_LOCATION );
	panel.add( b );
	b.addActionListener( c );	
	
	CoPanel apanel = builder.createPanel( new CoColumnLayout() );
	
	{		
		CoPanel tmp = builder.createPanel( createFormLayout() );	
		tmp.add( builder.createLabel( CoPageItemUIStringResources.getName(LOCATION_INSET ) ) );		
		
		tmp.add( m_insetLocationSpec_insetXTextfield = builder.createSlimTextField( CoTextField.RIGHT, 6 ) );
		m_insetLocationSpec_aggressiveCheckbox = builder.createCheckBox( CoPageItemUIStringResources.getName( LOCATION_AGGRESSIVE ), null );	
		
		apanel.add( tmp, SIZE_CONTENT );
		apanel.add( m_insetLocationSpec_aggressiveCheckbox, SIZE_CONTENT );
	}


	
	m_insetLocationSpec_insetXTextfield.addActionListener( new IntegerTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_CORNER_LOCATION_SPEC_INSET_X, -1 ) );
	m_insetLocationSpec_aggressiveCheckbox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_LOCATION_SPEC_AGGRESSIVE ) );

	
	m_locationSpec_attributePanel = builder.createPanel( m_locationSpec_attributeCardLayout = new CardLayout() );
	m_locationSpec_attributePanel.add( builder.createLabel( "" ), LOCATION_NONE );
	m_locationSpec_attributePanel.add( apanel, LOCATION_INSET );
	
	final Map m = new HashMap();
	{
		m.put( CoTopLocationIF.TOP_LOCATION, LOCATION_NONE );
		m.put( CoBottomLocationIF.BOTTOM_LOCATION, LOCATION_NONE );
		m.put( CoLeftLocationIF.LEFT_LOCATION, LOCATION_NONE );
		m.put( CoCenterLocationIF.CENTER_LOCATION, LOCATION_NONE );
		m.put( CoRightLocationIF.RIGHT_LOCATION, LOCATION_NONE );
		m.put( CoNoLocationIF.NO_LOCATION, LOCATION_NONE );
		
		m.put( CoCornerLocationSpecIF.TOP_LEFT, LOCATION_INSET );
		m.put( CoCornerLocationSpecIF.TOP_RIGHT, LOCATION_INSET );
		m.put( CoCornerLocationSpecIF.BOTTOM_LEFT, LOCATION_INSET );
		m.put( CoCornerLocationSpecIF.BOTTOM_RIGHT, LOCATION_INSET );

		m.put( CoCornerLocationSpecIF.TOP_OUTSIDE, LOCATION_INSET );
		m.put( CoCornerLocationSpecIF.TOP_INSIDE, LOCATION_INSET );
		m.put( CoCornerLocationSpecIF.BOTTOM_OUTSIDE, LOCATION_INSET );
		m.put( CoCornerLocationSpecIF.BOTTOM_INSIDE, LOCATION_INSET );
	}

	m_locationSpec_buttonGroup.addSelectedButtonListener(
		new CoSelectedButtonListener()
		{
			public void selectedButtonChanged( CoSelectedButtonEvent ev )
			{
				String key = ev.getSelectedButton().getActionCommand();
				m_locationSpec_attributeCardLayout.show( m_locationSpec_attributePanel, (String) m.get( key ) );
			}
		}
	);

	panel.add( p,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	panel.add( b,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 0, p ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	
	panel.add( tPanel,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0 ,b),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 0, b ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	
	panel.add( m_locationSpec_attributePanel,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, tPanel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );

	
	return panel;
}
private CoPanel createWidthSpecPanel( CoUserInterfaceBuilder builder, CoUndoableCommandExecutor commandExecutor )
{
	CoPanel panel = builder.createPanel( new CoAttachmentLayout() );

	m_widthSpec_buttonGroup = builder.createButtonGroup();

	CoCommandAdapter c =
		new ToggleButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_WIDTH_SPEC )
		{
			protected String getCurrentValue() { return m_domain.getWidthSpec().getFactoryKey(); }
		
			protected void prepare()
			{
				CoImmutableSizeSpecIF ls = m_layoutSpecFactory.getSizeSpec( (String) m_value, m_domain.getShapePageItem() );
				( (CoShapePageItemSetObjectCommand) m_command ).prepare( m_domain, ls );
			}
		};
	
	CoPanel p = builder.createPanel( new CoRowLayout( true ) );
	panel.add( p );
	{
		CoToggleButton b;
		b = builder.create3DToggleButton( "", m_noWidthIcon, m_widthSpec_buttonGroup, CoNoSizeSpecIF.NO_SIZE_SPEC );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_absoluteWidthIcon, m_widthSpec_buttonGroup, CoAbsoluteWidthSpecIF.ABSOLUTE_WIDTH_SPEC );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_fillWidthIcon, m_widthSpec_buttonGroup, CoFillWidthSpecIF.FILL_WIDTH_SPEC );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_contentWidthIcon, m_widthSpec_buttonGroup, CoContentWidthSpecIF.CONTENT_WIDTH_SPEC );
		p.add( b );
		b.addActionListener( c );

		b = builder.create3DToggleButton( "", m_proportionalWidthIcon, m_widthSpec_buttonGroup, CoProportionalWidthSpecIF.PROPORTIONAL_WIDTH_SPEC );
		p.add( b );
		b.addActionListener( c );
	}


	
	m_widthSpec_attributePanel = builder.createPanel( m_widthSpec_attributeCardLayout = new CardLayout() );
	
	{
		m_widthSpec_attributePanel.add( builder.createLabel( "" ), SIZE_NONE );
	}
	
	{
		CoPanel tmp = builder.createPanel( createFormLayout() );
		tmp.add( builder.createLabel( CoPageItemUIStringResources.getName( SIZE_DISTANCE ) ) );
		tmp.add( m_widthSpec_absoluteTextfield = builder.createSlimTextField( CoTextField.RIGHT, 6 ) );
		m_widthSpec_attributePanel.add( tmp, SIZE_ABSOLUTE );
	}
	
	{
		CoPanel tmp = builder.createPanel( createFormLayout() );
		tmp.add( builder.createLabel( CoPageItemUIStringResources.getName( SIZE_ABSOLUTE_OFFSET ) ) );
		tmp.add( m_widthSpec_contentAbsoluteOffsetTextfield = builder.createSlimTextField( CoTextField.RIGHT, 6 ) );
		tmp.add( builder.createLabel( CoPageItemUIStringResources.getName( SIZE_RELATIVE_OFFSET ) ) );
		tmp.add( m_widthSpec_contentRelativeOffsetTextfield = builder.createSlimTextField( CoTextField.RIGHT, 6 ) );
		m_widthSpec_attributePanel.add( tmp, SIZE_CONTENT );
	}
	
	{
		CoPanel tmp = builder.createPanel( createFormLayout() );
		tmp.add( builder.createLabel( CoPageItemUIStringResources.getName( SIZE_PROPORTION ) ) );
		tmp.add( m_widthSpec_proportionTextfield = builder.createSlimTextField( CoTextField.RIGHT, 6 ) );
		m_widthSpec_attributePanel.add( tmp, SIZE_RELATIVE );
	}

	final Map m = new HashMap();
	{
		m.put( CoNoSizeSpecIF.NO_SIZE_SPEC, SIZE_NONE );
		m.put( CoAbsoluteWidthSpecIF.ABSOLUTE_WIDTH_SPEC, SIZE_ABSOLUTE );
		m.put( CoFillWidthSpecIF.FILL_WIDTH_SPEC, SIZE_NONE );
		m.put( CoContentWidthSpecIF.CONTENT_WIDTH_SPEC, SIZE_CONTENT );
		m.put( CoProportionalWidthSpecIF.PROPORTIONAL_WIDTH_SPEC, SIZE_RELATIVE );
	}
	
	m_widthSpec_buttonGroup.addSelectedButtonListener(
		new CoSelectedButtonListener()
		{
			public void selectedButtonChanged( CoSelectedButtonEvent ev )
			{
				m_widthSpec_attributeCardLayout.show( m_widthSpec_attributePanel,
					                                    (String) m.get( ev.getSelectedButton().getActionCommand() ) );
			}
		}
	);

	
	panel.add( p,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	panel.add( m_widthSpec_attributePanel,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, p ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );


	m_widthSpec_absoluteTextfield.addActionListener( new IntegerTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_ABSOLUTE_WIDTH_SPEC_DISTANCE, -1 ) );
	m_widthSpec_contentAbsoluteOffsetTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_CONTENT_WIDTH_SPEC_ABSOLUTE_OFFSET ) );
	m_widthSpec_contentRelativeOffsetTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_CONTENT_WIDTH_SPEC_RELATIVE_OFFSET, false, 1 / 100f, 0 ) );
	m_widthSpec_proportionTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_PROPORTIONAL_WIDTH_SPEC_PROPORTION, false, 1 / 100f, 0 ) );

	return panel;

}
public void doUpdate()
{
	CoImmutableLocationSpecIF l = m_domain.getLocationSpec();
	
	m_locationSpec_buttonGroup.setSelected( l.getFactoryKey() );
	
	
	if
		( l instanceof CoImmutableCornerLocationSpecIF )
	{
		CoImmutableCornerLocationSpecIF ins = (CoImmutableCornerLocationSpecIF) l;		
		int i = ins.getXInset();
		m_insetLocationSpec_insetXTextfield.setText(CoLengthUnitSet.format(i));
		m_insetLocationSpec_aggressiveCheckbox.setSelected( ( (CoImmutableCornerLocationSpecIF) l ).isAggressive() );
	}
	
	CoImmutableSizeSpecIF w = m_domain.getWidthSpec();
	
	m_widthSpec_buttonGroup.setSelected( w.getFactoryKey() );
	if
		( w instanceof CoImmutableAbsoluteSizeSpecIF )
	{
		int i = (int) ( (CoImmutableAbsoluteSizeSpecIF) w ).getDistance();
		m_widthSpec_absoluteTextfield.setText( CoLengthUnitSet.format( i ) );
	} else if
		( w instanceof CoImmutableContentSizeIF )
	{
		double a = ( (CoImmutableContentSizeIF) w ).getAbsoluteOffset();
		m_widthSpec_contentAbsoluteOffsetTextfield.setText( CoLengthUnitSet.format( a, CoLengthUnit.LENGTH_UNITS ) );	
		double r = 100 * ( (CoImmutableContentSizeIF) w ).getRelativeOffset();
		m_widthSpec_contentRelativeOffsetTextfield.setText( CoLengthUnitSet.format( r ) );
	} else if
		( w instanceof CoImmutableProportionalSizeSpecIF )
	{
		double d = 100 * ( (CoImmutableProportionalSizeSpecIF) w ).getProportion();
		m_widthSpec_proportionTextfield.setText( CoLengthUnitSet.format( d ) );
	}

	
	CoImmutableSizeSpecIF h = m_domain.getHeightSpec();
	
	m_heightSpec_buttonGroup.setSelected( h.getFactoryKey() );
	if
		( h instanceof CoImmutableAbsoluteSizeSpecIF )
	{
		double d = ( (CoImmutableAbsoluteSizeSpecIF) h ).getDistance();
		m_heightSpec_absoluteTextfield.setText( CoLengthUnitSet.format( d, CoLengthUnit.LENGTH_UNITS ) );
	} else if
		( h instanceof CoImmutableContentSizeIF )
	{
		double a = ( (CoImmutableContentSizeIF) h ).getAbsoluteOffset();
		m_heightSpec_contentAbsoluteOffsetTextfield.setText( CoLengthUnitSet.format( a, CoLengthUnit.LENGTH_UNITS ) );			
		double r = 100 * ( (CoImmutableContentSizeIF) h ).getRelativeOffset();
		m_heightSpec_contentRelativeOffsetTextfield.setText( CoLengthUnitSet.format( r ) );
	} else if
		( h instanceof CoImmutableProportionalSizeSpecIF )
	{
		double d = 100 * ( (CoImmutableProportionalSizeSpecIF) h ).getProportion();
		m_heightSpec_proportionTextfield.setText( CoLengthUnitSet.format( d ) );
	}
}
}
