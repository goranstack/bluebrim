package com.bluebrim.layout.impl.client;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.swing.client.*;

/**
 * Page item layout area property panel.
 *
 * @author: Dennis
 */

public class CoPageItemLayoutAreaPanel extends CoPageItemPropertyPanel
{


public void doUpdate()
{
	CoLayoutAreaView lav = (CoLayoutAreaView) m_domain;
	
	m_workPieceLockCheckBox.setSelected( lav.getWorkPieceLock() != CoLayoutAreaIF.UNLOCKED );
	m_workPieceLockCheckBox.setEnabled( lav.getWorkPieceLock() != CoLayoutAreaIF.FIXED );
	m_acceptsWorkPieceCheckBox.setSelected( lav.acceptsWorkPiece() );
	
	m_clearWorkPieceButton.setEnabled( lav.hasWorkPiece() );
	
	if
		( lav.hasWorkPiece() )
	{
		m_nameLabel.setText( lav.getWorkPiece().getName() );
		m_workPieceButton.setEnabled( true );
	} else {
		m_nameLabel.setText( " " );
		m_workPieceButton.setEnabled( false );
	}
}

	public static final String ACCEPTS_WORKPIECE = "CoPageItemLayoutAreaPanel.ACCEPTS_WORKPIECE";
	public static final String CLEAR_WORKPIECE = "CoPageItemLayoutAreaPanel.CLEAR_WORKPIECE";
	private CoCheckBox m_acceptsWorkPieceCheckBox;
	private CoButton m_clearWorkPieceButton;
	private CoLabel m_nameLabel;
	private CoButton m_workPieceButton;
	public static final String SELECT_WORKPIECE = "CoPageItemLayoutAreaPanel.SELECT_WORKPIECE";

	private CoUndoableCommandExecutor m_commandExecutor;
	private CoCheckBox m_workPieceLockCheckBox;
	public static final String WORKPIECE_LOCK = "CoPageItemLayoutAreaPanel.WORKPIECE_LOCK";

public CoPageItemLayoutAreaPanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, new CoAttachmentLayout(), commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	m_commandExecutor = commandExecutor;

	
	m_clearWorkPieceButton = b.createButton( CoPageItemUIStringResources.getName( CLEAR_WORKPIECE ), null );

	m_clearWorkPieceButton.addActionListener( 
		new CoCommandAdapter( commandExecutor, CoPageItemCommands.SET_WORKPIECE )
		{
			protected void prepare()
			{
				( (CoShapePageItemSetObjectCommand) m_command ).prepare( m_domain, null );
			}
		}
	);




	m_acceptsWorkPieceCheckBox = b.createCheckBox( CoPageItemUIStringResources.getName( ACCEPTS_WORKPIECE ), null );
	m_acceptsWorkPieceCheckBox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_ACCEPTS_WORKPIECE ) );
	m_workPieceLockCheckBox = b.createCheckBox( CoPageItemUIStringResources.getName( WORKPIECE_LOCK ), null );
	m_workPieceLockCheckBox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_WORKPIECE_LOCK ) );

	
	m_nameLabel = b.createLabel( " " );
	m_workPieceButton = b.createButton( CoPageItemUIStringResources.getName( OPEN_ADM_UI ), null );		
	m_workPieceButton.addActionListener( createOpenAdmUIAction() );


	add( m_clearWorkPieceButton,
	   new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );

	add( m_acceptsWorkPieceCheckBox,
	   new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 5, m_clearWorkPieceButton ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_nameLabel,
	   new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, m_workPieceButton ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_BOTTOM, 0, m_workPieceButton ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_workPieceButton,
	   new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 5, m_acceptsWorkPieceCheckBox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, m_nameLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_workPieceLockCheckBox,
	   new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, m_acceptsWorkPieceCheckBox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, m_acceptsWorkPieceCheckBox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );


	/*
{ 
  6
  "m_workPieceLockCheckBox" 95 30 90 22  "class javax.swing.JCheckBox"
  "m_workPieceButton" 43 57 79 25  "class javax.swing.JButton"
  "m_nameLabel" 0 57 38 25  "class javax.swing.JLabel"
  "m_acceptsWorkPieceCheckBox" 0 30 90 22  "class javax.swing.JCheckBox"
  "m_selectWorkPieceButton" 84 0 79 25  "class javax.swing.JButton"
  "m_clearWorkPieceButton" 0 0 79 25  "class javax.swing.JButton"

  LEFT_COMPONENT_RIGHT 5 0.0 "m_acceptsWorkPieceCheckBox"
  RIGHT_NO 0 0.0 
  TOP_COMPONENT_TOP 0 0.0 "m_acceptsWorkPieceCheckBox"
  BOTTOM_NO 0 0.0 

  LEFT_COMPONENT_RIGHT 5 0.0 "m_nameLabel"
  RIGHT_NO 0 0.0 
  TOP_COMPONENT_BOTTOM 5 0.0 "m_acceptsWorkPieceCheckBox"
  BOTTOM_NO 0 0.0 

  LEFT_CONTAINER 0 0.0 
  RIGHT_NO 0 0.0 
  TOP_COMPONENT_TOP 0 0.0 "m_workPieceButton"
  BOTTOM_COMPONENT_BOTTOM 0 0.0 "m_workPieceButton"

  LEFT_CONTAINER 0 0.0 
  RIGHT_NO 0 0.0 
  TOP_COMPONENT_BOTTOM 5 0.0 "m_clearWorkPieceButton"
  BOTTOM_NO 0 0.0 

  LEFT_COMPONENT_RIGHT 5 0.0 "m_clearWorkPieceButton"
  RIGHT_NO 0 0.0 
  TOP_COMPONENT_TOP 0 0.0 "m_clearWorkPieceButton"
  BOTTOM_NO 0 0.0 

  LEFT_CONTAINER 0 0.0 
  RIGHT_NO 0 0.0 
  TOP_CONTAINER 0 0.0 
  BOTTOM_NO 0 0.0 

}

	*/
}
}