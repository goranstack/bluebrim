package com.bluebrim.layout.impl.client;

import java.util.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.swing.client.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-05-03 11:28:18)
 * @author: Dennis
 */

public class CoPageItemPageLayerPanel extends CoPageItemPropertyPanel
{
	public static final String PAGE_SIZE = "CoPageItemPageLayerPanel.PAGE_SIZE";
	
	private static final String NO_PAGE_SIZE = " ";

	private CoOptionMenu m_pageSizeOptionMenu;

	private com.bluebrim.page.shared.CoPageSizeCollectionIF m_pageSizes;

public CoPageItemPageLayerPanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, new CoColumnLayout(), commandExecutor );
}
private void buildPageSizeMenu()
{
	m_pageSizeOptionMenu.removeAllItems();
	m_pageSizeOptionMenu.addItem( NO_PAGE_SIZE );

	if
		( m_pageSizes != null )
	{
		Iterator i = m_pageSizes.getPageSizes().iterator();
		while
			( i.hasNext() )
		{
			com.bluebrim.page.shared.CoPageSizeIF s = (com.bluebrim.page.shared.CoPageSizeIF) i.next();	
			m_pageSizeOptionMenu.addItem( s.getName() );
		}
	}

}
protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	CoPanel p = b.createPanel( new CoFormLayout() );
	add( p );

	p.add( b.createLabel( CoPageItemUIStringResources.getName( PAGE_SIZE ) ) );
	m_pageSizeOptionMenu = b.createOptionMenu();
	p.add( m_pageSizeOptionMenu );

	p.add( b.createLabel( CoPageItemUIStringResources.getName( PAGE_SPREAD ) ) );
	m_spreadCheckBox = b.createCheckBox( "", null );
	p.add( m_spreadCheckBox );
			
	p.add( b.createLabel( CoPageItemUIStringResources.getName( GRID_SPREAD ) ) );
	p.add( m_gridSpreadCheckBox = b.createCheckBox( "", null ) );


	
	buildPageSizeMenu();


	
	m_pageSizeOptionMenu.addActionListener(
		new OptionMenuCommandAdapter( commandExecutor, CoPageItemCommands.SET_PAGE_SIZE, new Object() )
		{
			protected Object getCurrentValue()
			{
				return ( (CoPageLayoutAreaView) m_domain ).getPageSizeName();
			}
			
			protected void prepare()
			{
				if
					( m_value == NO_PAGE_SIZE )
				{
					( (CoShapePageItemSetObjectCommand) m_command ).prepare( m_domain, null );
				} else {
					( (CoShapePageItemSetObjectCommand) m_command ).prepare( m_domain, m_pageSizes.getPageSizeByName( (String) m_value ) );
				}
			}
		}
	);

	m_spreadCheckBox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_PAGE_SPREAD ) );
	m_gridSpreadCheckBox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_COLUMN_GRID_SPREAD ) );
}

protected void doUpdate()
{
	CoPageLayoutAreaView pllav = (CoPageLayoutAreaView) m_domain;
	String pageSizeName = pllav.getPageSizeName();
	
	if
		( pageSizeName == null )
	{
		m_pageSizeOptionMenu.setSelectedIndex( 0 );
	} else {
		m_pageSizeOptionMenu.setSelectedItem( pageSizeName );
	}

	m_spreadCheckBox.setSelected( pllav.isSpread() );

	m_gridSpreadCheckBox.setSelected( pllav.getColumnGrid().isSpread() );
	m_gridSpreadCheckBox.setEnabled( ! pllav.getColumnGrid().isDerived() );

}
public void setContext( CoPageItemEditorContextIF context )
{
	super.setContext( context );

	m_pageSizes = context.getPreferences().getPageSizeCollection();
	buildPageSizeMenu();
}

	public static final String GRID_SPREAD = "CoPageItemPageLayerPanel.GRID_SPREAD";
	public CoCheckBox m_gridSpreadCheckBox;
	private CoCheckBox m_spreadCheckBox;
	public static final String PAGE_SPREAD = "CoPageItemPageLayerPanel.PAGE_SPREAD";
}