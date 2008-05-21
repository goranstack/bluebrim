package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.event.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.swing.client.*;

/**
 * Page item image property panel.
 *
 * @author: Dennis
 */

public class CoPageItemImagePanel extends CoPageItemBoundedContentPanel
{
	public static final String TAG 	= "CoPageItemImagePanel.TAG";		
	public static final String SELECT = "CoPageItemImagePanel.SELECT";

	public CoLabel m_tagLabel;
	public CoTextField m_tagTextField;
	
	private CoButton m_selectImageButton;
	private CoWorkPieceImageSelectionDialog m_imageSelectionDialog;



public void doUpdate()
{
	super.doUpdate();
	
	CoPageItemImageContentView i = (CoPageItemImageContentView) ( (CoContentWrapperPageItemView) m_domain ).getContentView();

	if
		( i instanceof CoPageItemImageContentView )
	{
		int o = ( (CoPageItemImageContentView) i ).getOrderTag();
		m_tagTextField.setText( ( o != -1 ) ? "" + o : "" );

		CoPageItemImageContentView iv = (CoPageItemImageContentView) i;
		boolean b1 = true;
		boolean b2 = true;

		if
			( iv.getContentLock() == CoPageItemImageContentIF.FIXED )
		{
			b1 = b2 = false;
		} else if
			( iv.getWorkPiece() == null )
		{
			b1= false;
		} else {
			b1 = b2 = ! iv.hasContent() || ( iv.getContentLock() == CoPageItemImageContentIF.UNLOCKED );
		}

		m_selectImageButton.setEnabled( b1 );
		m_tagTextField.setEnabled( b2 );
	}

}
private void openImageSelectionDialog()
{
	CoPageItemImageContentView tv = (CoPageItemImageContentView) ( (CoContentWrapperPageItemView) m_domain ).getContentView();

	final int i = m_imageSelectionDialog.open( tv.getWorkPiece().getImages() );

	if ( i == -1 ) return;

	CoPageItemCommands.SET_IMAGE_TAG.prepare( m_domain, i );
	m_commandExecutor.doit( CoPageItemCommands.SET_IMAGE_TAG, null );
	/*
	if
		( ( (CoPageItemImageContentView) ( (CoContentWrapperPageItemView) m_domain ).getContentView() ).getOrderTag() != i )
	{
		CoCommand c = 
			new CoCommand( "SET IMAGE TAG" )
			{
				public boolean doExecute()
				{
					CoPageItemImageContentIF t = (CoPageItemImageContentIF) ( (CoContentWrapperPageItemIF) m_domain.getPageItem() ).getContent();
					t.setOrderTag( i );
					return true;
				}
			};

		CoTransactionUtilities.execute( c, null );
	}
*/
}
public void postSetDomain()
{
	boolean isContentWrapper = ( m_domain instanceof CoContentWrapperPageItemView );

	CoPageItemContentView cv = null;
	if
		( isContentWrapper )
	{
		cv = ( (CoContentWrapperPageItemView) m_domain ).getContentView();
	}
	
	boolean isImage = ( isContentWrapper && ( cv instanceof CoPageItemImageContentView ) );

	m_tagLabel.setVisible( isImage );
	m_tagTextField.setVisible( isImage );
	m_selectImageButton.setVisible( isImage );
}

	public static final String LOCK = "CoPageItemImagePanel.LOCK";
	private CoUndoableCommandExecutor m_commandExecutor;

public CoPageItemImagePanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	m_commandExecutor = commandExecutor;

	
	super.create( b, commandExecutor );
	
	Container p = (Container) getComponent( 0 );
	
	p.add( m_tagLabel = b.createLabel( CoPageItemUIStringResources.getName( TAG ) ) );
	p.add( m_tagTextField = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

	m_selectImageButton = b.createButton( CoPageItemUIStringResources.getName( SELECT ), null );
	m_imageSelectionDialog = new CoWorkPieceImageSelectionDialog( b );
	
	p.add( b.createLabel( "" ) );
	p.add( m_selectImageButton );



	m_tagTextField.addActionListener(
		new IntegerTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_IMAGE_TAG, -2 )
		{
			protected int parse( String str )
			{
				return ( str.length() == 0 ) ? -1 : CoLengthUnitSet.parse( str, -2 );
			}
		}
	);



	m_selectImageButton.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				openImageSelectionDialog();
			}
		}
	);

}

protected String getContentLockLabel()
{
	return CoPageItemUIStringResources.getName( LOCK );
}
}