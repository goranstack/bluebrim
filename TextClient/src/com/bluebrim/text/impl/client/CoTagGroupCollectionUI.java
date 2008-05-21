package com.bluebrim.text.impl.client;

import java.awt.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.browser.client.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.text.impl.shared.*;

/**
 * Insert the type's description here.
 * @author: Dennis
 */
 
public class CoTagGroupCollectionUI extends CoAbstractCatalogUI
{
	private int m_startOfMutableGroups;



public CoTagGroupCollectionUI()
{
	super();
}
protected CoListCatalogEditor createCatalogEditor()
{
	return new CoGsListCatalogEditor(this)
	{
		public void enableDisableMenuItems()
		{
			super.enableDisableMenuItems();
			
			if ( m_removeElementAction != null && m_removeElementAction.isEnabled() ) m_removeElementAction.setEnabled( getCatalogList().getSelectedIndex() >= m_startOfMutableGroups );
		}
	};
}
/**
 * createCatalogElementUI method comment.
 */
protected com.bluebrim.gui.client.CoUserInterface createCatalogElementUI()
{
	CoTagGroupUI ui = 
		new CoTagGroupUI()
		{
			public boolean canBeEnabled()
			{
				return super.canBeEnabled() && ( getCatalogList().getSelectedIndex() >= m_startOfMutableGroups );
			}
		};
	
	return ui;
}
protected CoListValueable.Mutable createCatalogHolder()
{
	return new CoAbstractListAspectAdaptor.Mutable( this, "ELEMENTS" )
	{
		protected Object get( CoObjectIF subject )
		{
			return ( (CoTagGroupCollectionIF) subject ).getTagGroups();
		}
		
		public CoAbstractListModel.Mutable createListModel()
		{
			return new CoCollectionListModel.Mutable( this )
			{
				protected boolean doRemoveElement( Object element )
				{
					( (CoTagGroupCollectionIF) getDomain() ).removeTagGroup( (com.bluebrim.text.shared.CoTagGroupIF) element );
					return true;
				}
				protected void doAddElement( Object element )
				{
					( (CoTagGroupCollectionIF) getDomain() ).addTagGroup( (com.bluebrim.text.shared.CoTagGroupIF) element );
				}
			};
		}

	};
}
protected ListCellRenderer createCatalogListCellRenderer(CoUserInterfaceBuilder builder) {
	return new CoImmutableAsItalicCatalogListCellRenderer() {
		protected boolean isMutable(Object value, int index) {
			return index >= m_startOfMutableGroups;
		}
	};
}
public void doAfterCreateUserInterface()
{
	super.doAfterCreateUserInterface();

	updateContext();
}
protected Object getCatalogSubcanvasLayoutConstraint()
{
	return BorderLayout.CENTER;
}
private CoTagGroupCollectionIF getTagGroupCollection()
{
	return (CoTagGroupCollectionIF) getDomain();
}
/**
 * newCatalogElement method comment.
 */
protected com.bluebrim.browser.shared.CoCatalogElementIF newCatalogElement()
{
	return ( (CoTagGroupCollectionIF) getDomain() ).createTagGroup( com.bluebrim.base.shared.CoStringResources.getName( com.bluebrim.base.shared.CoConstants.UNTITLED ) );
}
protected void postDomainChange( CoObjectIF d )
{
	super.postDomainChange( d );

	updateContext();

	CoTagGroupCollectionIF c = getTagGroupCollection();
	m_startOfMutableGroups = ( c == null ) ? 0 : c.getImmutableGroupCount();
}
private void updateContext() {
	CoTagGroupUI groupUI = (CoTagGroupUI) getCurrentElementUI();
	if (groupUI != null) {
		groupUI.setContext((com.bluebrim.text.shared.CoStyledTextPreferencesIF) getDomain());
	}
}
public void valueHasChanged()
{
	super.valueHasChanged();

	updateContext();

	CoTagGroupCollectionIF c = getTagGroupCollection();
	m_startOfMutableGroups = ( c == null ) ? 0 : c.getImmutableGroupCount();
}
}
