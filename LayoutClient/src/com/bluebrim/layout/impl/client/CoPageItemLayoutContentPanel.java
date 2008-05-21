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
 * Page item layout content property panel.
 *
 * @author: Dennis
 */

public class CoPageItemLayoutContentPanel extends CoPageItemBoundedContentPanel
{
	public static final String TAG 	= "CoPageItemLayoutContentPanel.TAG";		
	public static final String SELECT = "CoPageItemLayoutContentPanel.SELECT";

	public CoLabel m_tagLabel;
	public CoTextField m_tagTextField;
	
	private CoButton m_selectLayoutButton;
	private CoWorkPieceLayoutSelectionDialog m_layoutSelectionDialog;



public void doUpdate()
{
	super.doUpdate();
	
	CoPageItemLayoutContentView i = (CoPageItemLayoutContentView) ( (CoContentWrapperPageItemView) m_domain ).getContentView();

	if
		( i instanceof CoPageItemLayoutContentView )
	{
		CoPageItemLayoutContentView iv = (CoPageItemLayoutContentView) i;

		m_recursiveLevelCountTextField.setText( Integer.toString( iv.getRecursiveLevelMaxCount() ) );
	
		int o = iv.getOrderTag();
		m_tagTextField.setText( ( o != -1 ) ? "" + o : "" );

		boolean b = true;

		if
			( iv.getContentLock() == CoPageItemLayoutContentIF.FIXED )
		{
			b = false;
		} else if
			( iv.getWorkPiece() == null )
		{
			b = false;
		} else {
			b = ! iv.hasContent() || ( iv.getContentLock() == CoPageItemLayoutContentIF.UNLOCKED );
		}

		m_selectLayoutButton.setEnabled( b );
		m_tagTextField.setEnabled( b );

	}

}
private void openLayoutSelectionDialog()
{
	CoPageItemLayoutContentView tv = (CoPageItemLayoutContentView) ( (CoContentWrapperPageItemView) m_domain ).getContentView();

	final int i = m_layoutSelectionDialog.open( tv.getWorkPiece().getLayouts() );

	if ( i == -1 ) return;

	CoPageItemCommands.SET_LAYOUT_TAG.prepare( m_domain, i );
	m_commandExecutor.doit( CoPageItemCommands.SET_LAYOUT_TAG, null );
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
	
	boolean isLayout = ( isContentWrapper && ( cv instanceof CoPageItemLayoutContentView ) );

	m_tagLabel.setVisible( isLayout );
	m_tagTextField.setVisible( isLayout );
	m_selectLayoutButton.setVisible( isLayout );
}

	public static final String LOCK = "CoPageItemLayoutContentPanel.LOCK";
	private CoUndoableCommandExecutor m_commandExecutor;
	public CoTextField m_recursiveLevelCountTextField;
	public static final String RECURSIVE_LEVEL_COUNT = "CoPageItemLayoutContentPanel.RECURSIVE_LEVEL_COUNT";

public CoPageItemLayoutContentPanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
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

	m_selectLayoutButton = b.createButton( CoPageItemUIStringResources.getName( SELECT ), null );
	m_layoutSelectionDialog = new CoWorkPieceLayoutSelectionDialog( b );
	
	p.add( b.createLabel( "" ) );
	p.add( m_selectLayoutButton );

	
	p.add( b.createLabel( CoPageItemUIStringResources.getName( RECURSIVE_LEVEL_COUNT ) ) );
	m_recursiveLevelCountTextField = b.createSlimTextField( CoTextField.RIGHT, 6 );
	p.add( m_recursiveLevelCountTextField );



	

	m_tagTextField.addActionListener(
		new IntegerTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_LAYOUT_TAG, -2 )
		{
			protected int parse( String str )
			{
				return ( str.length() == 0 ) ? -1 : CoLengthUnitSet.parse( str, -2 );
			}
		}
	);


	m_recursiveLevelCountTextField.addActionListener(
		new IntegerTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_RECURSIVE_LEVEL_COUNT, -1 )
		{
			protected int parse( String str )
			{
				return CoLengthUnitSet.parse( str, -1 );
			}
		}
	);



	m_selectLayoutButton.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				openLayoutSelectionDialog();
			}
		}
	);

}

protected String getContentLockLabel()
{
	return CoPageItemUIStringResources.getName( LOCK );
}
}