package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.swing.client.*;

/**
 * Page item bounded content property panel.
 *
 * @author: Dennis
 */

public abstract class CoPageItemBoundedContentPanel extends CoPageItemPropertyPanel
{
	public static final String X 		= "CoPageItemBoundedContentPanel.X";
	public static final String Y 		= "CoPageItemBoundedContentPanel.Y";	
	public static final String SCALE_X 		= "CoPageItemBoundedContentPanel.SCALE_X";	
	public static final String SCALE_Y 	= "CoPageItemBoundedContentPanel.SCALE_Y";		
	public static final String FLIP_X 		= "CoPageItemBoundedContentPanel.FLIP_X";	
	public static final String FLIP_Y 	= "CoPageItemBoundedContentPanel.FLIP_Y";
	public static final String CAPTION = "CoPageItemBoundedContentPanel.CAPTION";
	public static final String CAPTION_TEXT_SHORTHAND = "CoPageItemBoundedContentPanel.CAPTION_TEXT_SHORTHAND";

	public CoTextField m_xTextField;
	public CoTextField m_yTextField;
	public CoTextField m_scaleXTextField;
	public CoTextField m_scaleYTextField;
	public CoCheckBox m_flipXCheckBox;
	public CoCheckBox m_flipYCheckBox;
	public CoCheckBox m_captionCheckBox;
	public CoToggleButton m_topCaptionButton;
	public CoToggleButton m_topLeftCaptionButton;
	public CoToggleButton m_bottomLeftCaptionButton;
	public CoToggleButton m_bottomCaptionButton;
	public CoToggleButton m_topRightCaptionButton;
	public CoToggleButton m_bottomRightCaptionButton;
	public CoToggleButton m_topInsideCaptionButton;
	public CoToggleButton m_bottomInsideCaptionButton;
	public CoButtonGroup m_captionButtonGroup;



public void doUpdate()
{
	CoPageItemBoundedContentView i = (CoPageItemBoundedContentView) ( (CoContentWrapperPageItemView) m_domain ).getContentView();

	m_xTextField.setText( CoLengthUnitSet.format( i.getX(), CoLengthUnit.LENGTH_UNITS ) );
	m_yTextField.setText( CoLengthUnitSet.format( i.getY(), CoLengthUnit.LENGTH_UNITS ) );
	m_scaleXTextField.setText( CoLengthUnitSet.format( i.getScaleX() * 100 ) );
	m_scaleYTextField.setText( CoLengthUnitSet.format( i.getScaleY() * 100 ) );
	m_flipXCheckBox.setSelected( i.getFlipX() );
	m_flipYCheckBox.setSelected( i.getFlipY() );

	m_captionCheckBox.setSelected( i.hasCaption() );
	m_captionButtonGroup.setSelected( "" + (char) m_domain.getSlavePosition() );

	m_contentLockCheckBox.setSelected( i.getContentLock() != CoPageItemBoundedContentIF.UNLOCKED );
	m_contentLockCheckBox.setEnabled( i.getContentLock() != CoPageItemBoundedContentIF.FIXED );
}

	public CoCheckBox m_contentLockCheckBox;

public CoPageItemBoundedContentPanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, new CoRowLayout(), commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, final CoUndoableCommandExecutor commandExecutor )
{
	CoPanel p = b.createPanel( createFormLayout() );
	p.setBorder( BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
	add( p );

	p.add( b.createLabel( CoPageItemUIStringResources.getName( X ) ) );
	p.add( m_xTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

	p.add( b.createLabel( CoPageItemUIStringResources.getName( Y ) ) );
	p.add( m_yTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

	p.add( b.createLabel( CoPageItemUIStringResources.getName( SCALE_X ) ) );
	p.add( m_scaleXTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

	p.add( b.createLabel( CoPageItemUIStringResources.getName( SCALE_Y ) ) );
	p.add( m_scaleYTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

	p.add( b.createLabel( CoPageItemUIStringResources.getName( FLIP_X ) ) );
	p.add( m_flipXCheckBox = b.createCheckBox( "", null ) );

	p.add( b.createLabel( CoPageItemUIStringResources.getName( FLIP_Y ) ) );
	p.add( m_flipYCheckBox = b.createCheckBox( "", null ) );

	p.add( b.createLabel( getContentLockLabel() ) );
	p.add( m_contentLockCheckBox = b.createCheckBox( "", null ) );
	
	p.add( b.createLabel( "" ) );
	CoButton bu = null;
	p.add( bu = b.createButton( CoPageItemUIStringResources.getName( OPEN_ADM_UI ), null ) );

	
	p = b.createPanel( new CoAttachmentLayout() );
	p.setBorder( BorderFactory.createEtchedBorder() );
	add( p );

	m_captionCheckBox = b.createCheckBox( CoPageItemUIStringResources.getName( CAPTION ), null );
	CoLabel imageLabel = b.createLabel( 
		new Icon()
		{
			private int m_w = 100;
			private int m_h = 70;
			
			public int getIconWidth() { return m_w; }
			public int getIconHeight() { return m_h; }
			public void paintIcon( Component c, Graphics g, int x, int y )
			{
				int d = 1;
				g.translate( x, y );
				g.setColor( Color.black );
				g.drawLine( d, d, m_w - d, m_h - d );
				g.drawLine( m_w - d, d, d, m_h - d );
				g.drawRect( d, d, m_w - d * 2, m_h - d * 2 );
				g.translate( -x, -y );
			}			
		}
	);

	m_captionButtonGroup = b.createButtonGroup();
	m_topLeftCaptionButton = b.createToggleButton( CoPageItemUIStringResources.getName( CAPTION_TEXT_SHORTHAND ), m_captionButtonGroup, "" + (char) CoShapePageItemIF.SLAVE_TOP_LEFT_POSITION );
	m_bottomLeftCaptionButton = b.createToggleButton( CoPageItemUIStringResources.getName( CAPTION_TEXT_SHORTHAND ), m_captionButtonGroup, "" + (char) CoShapePageItemIF.SLAVE_BOTTOM_LEFT_POSITION );
	m_topCaptionButton = b.createToggleButton( CoPageItemUIStringResources.getName( CAPTION_TEXT_SHORTHAND ), m_captionButtonGroup, "" + (char) CoShapePageItemIF.SLAVE_TOP_POSITION );
	m_topRightCaptionButton = b.createToggleButton( CoPageItemUIStringResources.getName( CAPTION_TEXT_SHORTHAND ), m_captionButtonGroup, "" + (char) CoShapePageItemIF.SLAVE_TOP_RIGHT_POSITION );
	m_bottomRightCaptionButton = b.createToggleButton( CoPageItemUIStringResources.getName( CAPTION_TEXT_SHORTHAND ), m_captionButtonGroup, "" + (char) CoShapePageItemIF.SLAVE_BOTTOM_RIGHT_POSITION );
	m_topInsideCaptionButton = b.createToggleButton( CoPageItemUIStringResources.getName( CAPTION_TEXT_SHORTHAND ), m_captionButtonGroup, "" + (char) CoShapePageItemIF.SLAVE_TOP_INSIDE_POSITION );
	m_bottomInsideCaptionButton = b.createToggleButton( CoPageItemUIStringResources.getName( CAPTION_TEXT_SHORTHAND ), m_captionButtonGroup, "" + (char) CoShapePageItemIF.SLAVE_BOTTOM_INSIDE_POSITION );
	m_bottomCaptionButton = b.createToggleButton( CoPageItemUIStringResources.getName( CAPTION_TEXT_SHORTHAND ), m_captionButtonGroup, "" + (char) CoShapePageItemIF.SLAVE_BOTTOM_POSITION );

	p.add( m_topLeftCaptionButton,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, imageLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	p.add( m_topCaptionButton,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, m_captionCheckBox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_LEFT, 0, imageLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_COMPONENT_RIGHT, 0, imageLabel ) ) );
	p.add( m_bottomLeftCaptionButton,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_BOTTOM, 0, imageLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_COMPONENT_RIGHT, 0, m_topLeftCaptionButton ) ) );
	p.add( m_bottomRightCaptionButton,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_BOTTOM, 0, imageLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 0, imageLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	p.add( m_topRightCaptionButton,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, imageLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 0, imageLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	p.add( m_bottomCaptionButton,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, imageLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_LEFT, 0, imageLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_COMPONENT_RIGHT, 0, imageLabel ) ) );
	p.add( imageLabel,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, m_topCaptionButton ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 0, m_topLeftCaptionButton ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	p.add( m_topInsideCaptionButton,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, imageLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_LEFT, 0, imageLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_COMPONENT_RIGHT, 0, imageLabel ) ) );
	p.add( m_bottomInsideCaptionButton,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_BOTTOM, 0, imageLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_LEFT, 0, imageLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_COMPONENT_RIGHT, 0, imageLabel ) ) );
	p.add( m_captionCheckBox,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );



	
	m_captionCheckBox.addItemListener(
		new ItemListener()
		{
			public void itemStateChanged( ItemEvent ev )
			{
				m_captionButtonGroup.enableDisableButtons( ev.getStateChange() == ItemEvent.SELECTED );
			}
		}
	);


	


	bu.addActionListener( createOpenAdmUIAction() );



	m_xTextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_BOUNDED_CONTENT_X ) );
	m_yTextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_BOUNDED_CONTENT_Y ) );

	m_scaleXTextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_BOUNDED_CONTENT_SCALE_X, false, 1 / 100f, 0 ) );
	m_scaleYTextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_BOUNDED_CONTENT_SCALE_Y, false, 1 / 100f, 0 ) );
	m_flipXCheckBox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_BOUNDED_CONTENT_FLIP_X ) );
	m_flipYCheckBox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_BOUNDED_CONTENT_FLIP_Y ) );
	m_contentLockCheckBox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_BOUNDED_CONTENT_LOCK ) );

	
	m_captionCheckBox.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				if
					( m_captionCheckBox.isSelected() )
				{
					CoPageItemCommands.ADD_CAPTION.prepare( m_domain );
					commandExecutor.doit( CoPageItemCommands.ADD_CAPTION, null );
				} else {
					CoPageItemCommands.REMOVE_CAPTION.prepare( m_domain );
					commandExecutor.doit( CoPageItemCommands.REMOVE_CAPTION, null );
				}
			}
		}
	);


	ActionListener l =
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				int pos = (int) ev.getActionCommand().charAt( 0 );
				CoPageItemCommands.SET_CAPTION_POSITION.prepare( m_domain, pos );
				commandExecutor.doit( CoPageItemCommands.SET_CAPTION_POSITION, null );
			}
		};

	m_topLeftCaptionButton.addActionListener( l );
	m_bottomLeftCaptionButton.addActionListener( l );
	m_topCaptionButton.addActionListener( l );
	m_topRightCaptionButton.addActionListener( l );
	m_bottomRightCaptionButton.addActionListener( l );
	m_topInsideCaptionButton.addActionListener( l );
	m_bottomInsideCaptionButton.addActionListener( l );
	m_bottomCaptionButton.addActionListener( l );
/*
	m_captionButtonGroup.addSelectedButtonListener(
		new CoSelectedButtonListener()
		{
			public void selectedButtonChanged( final CoSelectedButtonEvent ev )
			{
				int pos = (int) ev.getSelectedButton().getActionCommand().charAt( 0 );
				CoPageItemCommands.SET_CAPTION_POSITION.prepare( m_domain, pos );
				commandExecutor.doit( CoPageItemCommands.SET_CAPTION_POSITION, null );
			}
		}
	);
*/

}

protected abstract String getContentLockLabel();
}