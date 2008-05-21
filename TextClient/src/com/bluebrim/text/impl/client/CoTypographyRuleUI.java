package com.bluebrim.text.impl.client;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.event.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.browser.client.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.observable.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2000-10-31 15:29:12)
 * @author: Dennis
 */
 
public abstract class CoTypographyRuleUI extends CoDomainUserInterface
{
	public static final String ADD = "CoTypographyRuleUI.ADD";
	public static final String REMOVE = "CoTypographyRuleUI.REMOVE";
	public static final String CHARACTER_STYLE = "CoTypographyRuleUI.CHARACTER_STYLE";
	public static final String PARAGRAPH_STYLE = "CoTypographyRuleUI.PARAGRAPH_STYLE";
	public static final String CHARACTER_STYLE_INITIAL_NAME = "CoTypographyRuleUI.CHARACTER_STYLE_INITIAL_NAME";
	public static final String PARAGRAPH_STYLE_INITIAL_NAME = "CoTypographyRuleUI.PARAGRAPH_STYLE_INITIAL_NAME";


		
	private CoListBox m_styleListBox;
	private CoSubcanvas m_styleSubcanvas;
	private CoTextStyleUI m_styleUI;
	private CoPopupMenu m_popupMenu;
	
	private CoAppendingListProxy m_listOfAllStyles;

	
	private static abstract class Command extends CoCommand
	{
		public Command( String name )
		{
			super( name );
		}
		
		public void actionPerformed( ActionEvent ev )
		{
			CoTransactionUtilities.execute( this, null );
		}
	};

	
	private CoCommand m_addCharacterStyle =
		new Command( CoTextStringResources.getName( CHARACTER_STYLE ) )
		{
			public boolean doExecute()
			{
				addCharacterStyle();
				return true;
			}
		};
		
	private CoCommand m_addParagraphStyle =
		new Command( CoTextStringResources.getName( PARAGRAPH_STYLE ) )
		{
			public boolean doExecute()
			{
				addParagraphStyle();
				return true;
			}
		};
	
	private CoCommand m_removeStyles =
		new Command( CoTextStringResources.getName( REMOVE ) )
		{
			public boolean doExecute()
			{
				removeStyles();
				return true;
			}
		};
		
public CoTypographyRuleUI()
{
	super();
}

private void addCharacterStyle()
{
	CoCharacterStyleIF p = getTypographyRule().addCharacterStyle( CoTextStringResources.getName( CHARACTER_STYLE_INITIAL_NAME ) );
	m_styleListBox.getList().setSelectedValue( p, true );
}

private void addParagraphStyle()
{
	CoParagraphStyleIF p = getTypographyRule().addParagraphStyle( CoTextStringResources.getName( PARAGRAPH_STYLE_INITIAL_NAME ) );
	m_styleListBox.getList().setSelectedValue( p, true );
}

protected void createListeners()
{
	super.createListeners();

	m_styleListBox.getList().addListSelectionListener(
		new ListSelectionListener()
		{
			public void valueChanged( ListSelectionEvent ev )
			{
				handleSelection();
			}
		}
	);

	m_styleListBox.getList().addMouseListener(new CoPopupGestureListener(m_popupMenu));

	( new CoDefaultServerObjectListener( this ) ).initialize();
}

protected void createValueModels( CoUserInterfaceBuilder b )
{
	CoAbstractListAspectAdaptor a = 
		new CoAbstractListAspectAdaptor.Default( this, "STYLES" )
		{
			protected Object get( CoObjectIF subject ) 
			{
				getListOfAllStyles().set( ( (CoTypographyRuleIF) subject ).getCharacterStyles(),
															    ( (CoTypographyRuleIF) subject ).getParagraphStyles() );
				return getListOfAllStyles();
			}
		};
	
	b.addAspectAdaptor( a );
	b.createListBoxAdaptor( a, m_styleListBox );
}

protected void createWidgets( CoPanel p, CoUserInterfaceBuilder b )
{
	super.createWidgets( p, b );

	m_styleListBox = b.createListBox();
	m_styleListBox.getList().setCellRenderer( new CoCatalogListCellRenderer() );

	
	m_styleUI = new CoTextStyleUI()
	{
		protected CoTextParameters getTextParameters()
		{
			return CoTypographyRuleUI.this.getTextParameters();
		}
	};
	
	m_styleSubcanvas = b.createSubcanvas( m_styleUI );


	CoSplitPane sp = b.createSplitPane( true, m_styleListBox, m_styleSubcanvas );
	p.add( sp );
	

	CoMenuBuilder mb = getMenuBuilder();
	m_popupMenu = mb.createPopupMenu();
	
	CoMenu mi = mb.addPopupSubMenu( m_popupMenu, CoTextStringResources.getName( ADD ) );
	mb.addMenuItem( mi, m_addCharacterStyle );
	mb.addMenuItem( mi, m_addParagraphStyle );

	mb.addPopupMenuItem( m_popupMenu, m_removeStyles );
}

protected Insets getDefaultPanelInsets()
{
	return null;
}

private CoAppendingListProxy getListOfAllStyles()
{
	if ( m_listOfAllStyles == null ) m_listOfAllStyles = new CoAppendingListProxy( null, null );
	return m_listOfAllStyles;
}

protected abstract CoTextParameters getTextParameters();

private void handleSelection()
{
	Object [] o = m_styleListBox.getList().getSelectedValues();
	if
		( o.length == 1 )
	{
		m_styleUI.setDomain( (CoObjectIF) o[ 0 ] );
	} else {
		m_styleUI.setDomain( null );
	}

	m_removeStyles.setEnabled( o.length > 0 );
}

protected void preDomainChange( CoObjectIF d )
{
	super.preDomainChange( d );

	m_listOfAllStyles = null;

	m_styleListBox.getList().clearSelection();

//	m_styleUI.setContext( (CoTypographyRuleIF) d );
}

private void removeStyles()
{
	Object [] o = m_styleListBox.getList().getSelectedValues();
	m_styleListBox.getList().clearSelection();
	
	( (CoTypographyRuleIF) getDomain() ).removeStyles( Arrays.asList( o ) );
}


protected CoCharacterStyleIF getSelectedStyle()
{
	CoList l = m_styleListBox.getList();

	Object [] o = l.getSelectedValues();
	
	return ( o.length == 1 ) ? (CoCharacterStyleIF) o[ 0 ] : null;
}

protected CoTypographyRuleIF getTypographyRule()
{
	return (CoTypographyRuleIF) getDomain();
}

protected void postDomainChange( CoObjectIF d )
{
	super.postDomainChange( d );

	m_styleUI.setContext( (CoTypographyRuleIF) d );
}

public void setContext( CoTypographyContextIF c )
{
	m_styleUI.setContext( c );
}
}