package com.bluebrim.text.impl.client;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.transact.shared.*;

//

public class CoFontFamilyCollectionUI extends CoDomainUserInterface
{
	public static final String INSTALLED = "CoFontFamilyCollectionUI.INSTALLED";
	public static final String AVAILABLE = "CoFontFamilyCollectionUI.AVAILABLE";

	private CoList m_installedFontsList;
	private CoList m_availableFontsList;

	private FontsListModel m_installedFontsListModel = new InstalledFontsListModel();
	private FontsListModel m_availableFontsListModel = new AvailableFontsListModel();


	private abstract class FontsListModel extends AbstractListModel
	{
		public void touch()
		{
			int maxIndex = getSize() - 1;
			fireContentsChanged( this, 0, maxIndex );
		}
	};

	private class AvailableFontsListModel extends FontsListModel
	{
		public int getSize()
		{
			CoFontFamilyCollectionIF c = getFontFamilyCollection();
			return ( c == null ) ? 0 : c.getFontFamilyNames().size();
		}

		public Object getElementAt( int i )
		{
			CoFontFamilyCollectionIF c = getFontFamilyCollection();
			return ( c == null ) ? null : c.getFontFamilyNames().get( i );
		}
	};

	
	private class InstalledFontsListModel extends FontsListModel
	{
		private List m_fonts = new ArrayList();

		public void touch()
		{
			m_fonts.clear();
			m_fonts.addAll( com.bluebrim.font.shared.CoAbstractFontMapper.getFontMapper().getFontFamilies() );
			CoFontFamilyCollectionIF c = getFontFamilyCollection();
			if ( c != null ) m_fonts.removeAll( c.getFontFamilyNames() );
			super.touch();
		}
		
		public int getSize()
		{
			return m_fonts.size();
		}

		public Object getElementAt( int i )
		{
			return m_fonts.get( i );
		}
	};

	private CoChooserPanel m_chooserPanel;
	private int m_startOfMutableFamilies;
;

public CoFontFamilyCollectionUI()
{
	super();
}
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder b )
{
	p.setLayout( new BorderLayout() );
	
	m_chooserPanel =
		new CoChooserPanel( CoTextStringResources.getName( INSTALLED ),
		                    CoTextStringResources.getName( AVAILABLE ),
		                    CoChooserPanel.SOURCE_TO_THE_LEFT,
		                    CoChooserPanel.REMOVE_FROM_SOURCE )
		{
			public void enableRemoveButton( boolean state )
			{
				if
					( state )
				{
					int index [] = getDestinationBox().getList().getSelectedIndices();
					for
						( int i = 0; i < index.length; i++ )
					{
						if
							( index[ i ] < m_startOfMutableFamilies )
						{
							state = false;
							break;
						}
					}
				}

				super.enableRemoveButton( state );
			}
		};

	b.prepareChooserPanel( m_chooserPanel );
	/*
	m_chooserPanel = b.createChooserPanel( CoTextStringResources.getName( INSTALLED ),
		                                     CoTextStringResources.getName( AVAILABLE ),
		                                     CoChooserPanel.SOURCE_TO_THE_LEFT,
		                                     CoChooserPanel.REMOVE_FROM_SOURCE );
	*/
	
	p.add( m_chooserPanel, BorderLayout.CENTER );

	m_chooserPanel.setExtraInsets( new Insets( 2, 2, 2 ,2 ) );

	m_installedFontsList = m_chooserPanel.getSourceBox().getList();
	m_installedFontsList.setModel( m_installedFontsListModel );

	m_availableFontsList = m_chooserPanel.getDestinationBox().getList();
	m_availableFontsList.setModel( m_availableFontsListModel );
	m_availableFontsList.setCellRenderer(
		new CoImmutableAsItalicListCellRenderer()
		{
			protected boolean isMutable( Object value, int index )
			{
				return index >= m_startOfMutableFamilies;
			}
		}
	);

	
	abstract class FontsCommand extends CoCommand
	{
		protected List m_fonts;
		
		public FontsCommand( String name )
		{
			super( name );
		}
		
		public void setFonts( List fonts )
		{
			m_fonts = fonts;
		}
	};


	
	final FontsCommand addAvailableFonts = new FontsCommand( "ADD AVAILABLE FONTS" )
	{
		public boolean doExecute()
		{
			CoFontFamilyCollectionIF c = getFontFamilyCollection();
			Iterator i = m_fonts.iterator();
			while
				( i.hasNext() )
			{
				c.addFontFamily( (String) i.next() );
			}
			return true;
		}
		public void finish()
		{
			super.finish();
			m_installedFontsListModel.touch();
			m_availableFontsListModel.touch();
			
		}
	};

//TOUCH ???
	
	final FontsCommand removeAvailableFonts = new FontsCommand( "REMOVE AVAILABLE FONTS" )
	{
		public boolean doExecute()
		{
			CoFontFamilyCollectionIF c = getFontFamilyCollection();
			c.removeFontFamilyNames( m_fonts.toArray() );
			return true;
		}
		public void finish()
		{
			super.finish();
			m_installedFontsListModel.touch();
			m_availableFontsListModel.touch();
		}
	};



	m_chooserPanel.addChooserEventListener(
		new CoChooserEventListener()
		{
			public void handleChooserEvent( CoChooserEvent e )
			{
				if
					( e.isAddEvent() )
				{
					addAvailableFonts.setFonts( Arrays.asList( e.getElements() ) );
					CoTransactionUtilities.execute( addAvailableFonts, getFontFamilyCollection() );
				} else {
					removeAvailableFonts.setFonts( Arrays.asList( e.getElements() ) );
					CoTransactionUtilities.execute( removeAvailableFonts, getFontFamilyCollection() );
				}
			}
		}
	);

}
private CoFontFamilyCollectionIF getFontFamilyCollection()
{
	return (CoFontFamilyCollectionIF) getDomain();
}
protected void postDomainChange( CoObjectIF d )
{
	super.postDomainChange( d );
	
	m_installedFontsListModel.touch();
	m_availableFontsListModel.touch();
	
	CoFontFamilyCollectionIF c = getFontFamilyCollection();
	m_startOfMutableFamilies = ( c == null ) ? 0 : c.getImmutableFontFamilyCount();
	m_chooserPanel.enableRemoveButton( true );
}

public void valueHasChanged()
{
	m_installedFontsListModel.touch();
	m_availableFontsListModel.touch();
	
	CoFontFamilyCollectionIF c = getFontFamilyCollection();
	m_startOfMutableFamilies = ( c == null ) ? 0 : c.getImmutableFontFamilyCount();
	m_chooserPanel.enableRemoveButton( true );
}
}