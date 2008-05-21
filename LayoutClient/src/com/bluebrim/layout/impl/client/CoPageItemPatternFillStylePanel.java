package com.bluebrim.layout.impl.client;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.swing.client.*;

/**
 * Page item fill style property panel.
 *
 * @author: Dennis
 */

public class CoPageItemPatternFillStylePanel extends CoPageItemFillStylePanel
{


public void doUpdate()
{
	if ( m_domain == null ) return;
	if ( ! isVisible() ) return;
	
	CoFillStyleIF fs = (CoFillStyleIF) m_domain.getFillStyle();

	if ( ! ( fs instanceof CoPatternFillStyleIF ) ) return;
	
	CoImmutablePatternFillStyleIF f = (CoImmutablePatternFillStyleIF) fs;

	m_patternOptionMenu.setSelectedItem( f.getPattern() );
}
public void setContext( CoPageItemEditorContextIF context )
{
}

	public CoOptionMenu m_patternOptionMenu;
	public static final String PATTERN = "Mönster";

public CoPageItemPatternFillStylePanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	setLayout( new CoFormLayout( true ) );
	
	add( b.createLabel( CoPageItemUIStringResources.getName( PATTERN ) ) );
	add( m_patternOptionMenu = b.createOptionMenu() );

	m_patternOptionMenu.addItem( CoImmutablePatternFillStyleIF.CHESSBOARD );
	m_patternOptionMenu.addItem( CoImmutablePatternFillStyleIF.DOTS );
	m_patternOptionMenu.addItem( CoImmutablePatternFillStyleIF.MESH );
	m_patternOptionMenu.addItem( CoImmutablePatternFillStyleIF.SCALES );
	
	m_patternOptionMenu.addActionListener( new OptionMenuCommandAdapter( commandExecutor, CoPageItemCommands.SET_FILL_PATTERN, null ) );
}
}