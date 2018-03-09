package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.lang.reflect.*;
import java.util.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.swing.client.*;

/**
 * Page item layout manager property panel.
 *
 * @author: Dennis
 */

public class CoPageItemLayoutManagerPanel extends CoPageItemPropertyPanel
{
	private static final CoLayoutManagerFactoryIF m_layoutManagerFactory = (CoLayoutManagerFactoryIF) CoFactoryManager.getFactory( CoLayoutManagerIF.LAYOUT_MANAGER );

	private CoButtonGroup m_buttonGroup;

	private CardLayout m_propertyPanelCardLayout;
	private CoPanel m_propertyPanel;
	private CoLayoutManagerPanel m_currentLayoutManagerPanel;



public void doUpdate()
{
	m_buttonGroup.setSelected( ( (CoCompositePageItemView) m_domain ).getLayoutManager().getFactoryKey() );

	if
		( m_currentLayoutManagerPanel != null )
	{
		m_currentLayoutManagerPanel.domainHasChanged();
	}
}

public CoPageItemLayoutManagerPanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, new CoColumnLayout( 5 ), commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	CoPanel P = b.createPanel( new CoRowLayout() );
	add( P );
	
	m_buttonGroup = b.createButtonGroup();


	ToggleButtonCommandAdapter c =
		new ToggleButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_LAYOUT_MANAGER )
		{
			protected String getCurrentValue() { return ( (CoCompositePageItemView) m_domain ).getLayoutManager().getFactoryKey(); }
		
			protected void prepare()
			{
				( (CoShapePageItemSetObjectCommand) m_command ).prepare( m_domain, m_layoutManagerFactory.getLayoutManager( (String) m_value ) );
			}
		};
		
	CoPanel buttons = b.createPanel( new CoColumnLayout( true ) );
	P.add( buttons );
	m_propertyPanel = b.createPanel( m_propertyPanelCardLayout = new CardLayout() );
	P.add( m_propertyPanel );
	
	String key = CoNoLayoutManagerIF.NO_LAYOUT_MANAGER;
	CoToggleButton tb = b.create3DToggleButton( m_layoutManagerFactory.getLocalizedName( key ),//CoPageItemUIStringResources.getName( key ),
		                                          null, // icon ?
		                                          m_buttonGroup,
		                                          key );
	tb.setHorizontalAlignment( JButton.LEFT );
	buttons.add( tb );
	tb.addActionListener( c );
	CoLayoutManagerPanel ui = null;
	ui = new CoEmptyLayoutManagerPanel( b, this, key );
	b.preparePanel( ui );
	m_propertyPanel.add( ui, key );

	Iterator i = m_layoutManagerFactory.getKeys().iterator();
	while
		( i.hasNext() )
	{
		key = (String) i.next();
		tb = b.create3DToggleButton( m_layoutManagerFactory.getLocalizedName( key ),//CoPageItemUIStringResources.getName( key ),
			                           null, // icon ?
			                           m_buttonGroup,
			                           key );
		tb.setHorizontalAlignment( JButton.LEFT );
		buttons.add( tb );
		tb.addActionListener( c );

		String className = m_layoutManagerFactory.getLayoutManagerPanelClassName( key );
		ui = null;
		if
			( className != null )
		{
			try
			{
				Constructor cc = 
					Class.forName( className ).getConstructor(
						new Class []
						{
							CoUserInterfaceBuilder.class,
							CoPageItemLayoutManagerPanel.class,
							CoUndoableCommandExecutor.class
						}
					);
					
				ui = (CoLayoutManagerPanel) cc.newInstance( new Object [] { b, this, commandExecutor } );
			}
			catch ( Exception ex )
			{
				if ( com.bluebrim.base.shared.debug.CoAssertion.TRACE ) com.bluebrim.base.shared.debug.CoAssertion.trace( "Exception while creating layout manager panel for " + key + ":\n" + ex.getLocalizedMessage() );
			}
		}
			
		if ( ui == null ) ui = new CoEmptyLayoutManagerPanel( b, this, key );

		b.preparePanel( ui );
		m_propertyPanel.add( ui, key );
	}

	

	m_buttonGroup.addSelectedButtonListener(
		new CoSelectedButtonListener()
		{
			public void selectedButtonChanged( CoSelectedButtonEvent ev )
			{
				m_propertyPanelCardLayout.show( m_propertyPanel, ev.getSelectedButton().getActionCommand());

				Component cs [] = m_propertyPanel.getComponents();
				for
					( int n = 0; n < cs.length; n++ )
				{
					if
						( cs[ n ].isVisible() )
					{
						m_currentLayoutManagerPanel = (CoLayoutManagerPanel) cs[ n ];
						break;
					}
				}

			}
		}
	);
}
}