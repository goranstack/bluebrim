package com.bluebrim.layout.impl.client;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.swing.client.*;

/**
 * Page item content property panel.
 *
 * @author: Dennis
 */

public class CoPageItemContentPanel extends CoPageItemPropertyPanel
{
	public CoOptionMenu m_contentOptionMenu;



public void doUpdate()
{
//	m_contentOptionMenu.removeAllItems();

	CoPageItemContentView cv = ( (CoContentWrapperPageItemView) m_domain ).getContentView();
/*
	CoPageItemContentView.Deriver [] tmp = cv.getDerivers();
	int n = -1;
	for
		( int i = 0; i < tmp.length; i++ )
	{
		m_contentOptionMenu.addItem( tmp[ i ] );
		if ( tmp[ i ].getType().equals( cv.getType() ) ) n = i;
	}
//	m_contentCombobox.setMaximumRowCount( tmp.length );
	
	m_contentOptionMenu.setSelectedIndex( n, true );
*/

	m_contentOptionMenu.setSelectedItem( cv.getType(), true );
}

	private static CoPageItemFactoryIF m_pageItemFactory;

public CoPageItemContentPanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, new CoAttachmentLayout(), commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	m_contentOptionMenu = b.createOptionMenu();
	m_contentOptionMenu.setRenderer( new CoOptionMenu.TranslatingRenderer( CoPageItemStringResources.getBundle() ) );
	m_contentOptionMenu.addItem( CoPageItemNoContentIF.NO_CONTENT );
	m_contentOptionMenu.addItem( CoPageItemTextContentIF.TEXT_CONTENT );
	m_contentOptionMenu.addItem( CoPageItemWorkPieceTextContentIF.WORKPIECE_TEXT_CONTENT );
	m_contentOptionMenu.addItem( CoPageItemImageContentIF.IMAGE_CONTENT );
	m_contentOptionMenu.addItem( CoPageItemLayoutContentIF.LAYOUT_CONTENT );

	add( m_contentOptionMenu );


	m_contentOptionMenu.addActionListener(
		new OptionMenuCommandAdapter( commandExecutor, CoPageItemCommands.SET_PAGE_ITEM_CONTENT, null )
		{
			protected Object getCurrentValue()
			{
				return ( (CoContentWrapperPageItemView) m_domain ).getContentView().getType();
			}
		
			protected void prepare()
			{
				CoPageItemContentIF content = getPageItemFactory().createPageItemContent( (String) m_value, ( (CoContentWrapperPageItemView) m_domain ).getContentView().getPageItemContent() );
				( (CoShapePageItemSetObjectCommand) m_command ).prepare( m_domain, content );
			}
		}
	);

}

private static CoPageItemFactoryIF getPageItemFactory()
{
	if
		( m_pageItemFactory == null )
	{
		return m_pageItemFactory = (CoPageItemFactoryIF) CoFactoryManager.getFactory( CoPageItemFactoryIF.PAGE_ITEM_FACTORY );
	}

	return m_pageItemFactory;
}
}