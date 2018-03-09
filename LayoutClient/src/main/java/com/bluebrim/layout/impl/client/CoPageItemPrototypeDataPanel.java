package com.bluebrim.layout.impl.client;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.transact.shared.*;

/**
 * Page item prototype property panel.
 *
 * @author: Dennis
 */

public class CoPageItemPrototypeDataPanel extends CoPageItemPropertyPanel
{


public void doUpdate()
{
	m_descriptionTextField.setText( m_prototype.getDescription() );
/*
	CoPageItemAbstractTextContentView t = (CoPageItemAbstractTextContentView) ( (CoContentWrapperPageItemView) m_domain ).getContentView();

	m_firstBaselineTypeOptionMenu.setSelectedItem( t.getFirstBaselineType() );

	double d = t.getFirstBaselineOffset();
	m_firstBaselineOffsetTextField.setText( Double.isNaN( d ) ? "" : CoUnitSet.format( d, CoUnit.LENGTH_UNITS ) );

	m_verticalAlignmentTypeOptionMenu.setSelectedItem( t.getVerticalAlignmentType() );

	d = t.getVerticalAlignmentMaxInter();
	m_verticalAlignmentMaxIntervalTextField.setText( Double.isNaN( d ) ? "" : CoUnitSet.format( d, CoUnit.LENGTH_UNITS ) );

	d = t.getMargin();
	m_marginTextField.setText( Double.isNaN( d ) ? "" : CoUnitSet.format( d, CoUnit.LENGTH_UNITS ) );
	*/
}

	public static final String DESCRIPTION = "CoPageItemPrototypeDataPanel.DESCRIPTION";
//	public static final String ICON = "CoPageItemPrototypeDataPanel.ICON";

	protected CoTextField m_descriptionTextField;
//	protected CoOptionMenu m_iconMenu;

	protected CoPageItemPrototypeIF m_prototype;

public void postSetPrototype()
{
}

public final void prototypeHasChanged()
{
	if ( m_prototype == null ) return;
	if ( getParent() == null ) return;

	m_updateInProgress = true;

	doUpdate();
	
	m_updateInProgress = false;
}

public void setPrototype( CoPageItemPrototypeIF prototype )
{
	m_prototype = prototype;
	
	postSetPrototype();
	
	prototypeHasChanged();
}

public CoPageItemPrototypeDataPanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, new CoAttachmentLayout(), commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	CoLabel l = b.createLabel( CoPageItemUIStringResources.getName( DESCRIPTION ) );
	m_descriptionTextField = b.createSlimTextField();
//	m_iconMenu = b.createOptionMenu();
	
	add( l,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, m_descriptionTextField ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_BOTTOM, 0, m_descriptionTextField ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_descriptionTextField,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 0, l ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );

	// not undoable
	m_descriptionTextField.addActionListener(
		new CoCommand( "SET PAGE ITEM PROTOTYPE DESCRIPTION" )
		{
			public boolean doExecute()
			{
				m_prototype.setDescription( m_descriptionTextField.getText() );
				return true;
			}
		}
	);

}
}