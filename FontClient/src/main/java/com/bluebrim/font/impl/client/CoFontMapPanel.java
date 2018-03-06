package com.bluebrim.font.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

import com.bluebrim.font.impl.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;


/**
 * 
 * 
 * Creation date: (2001-01-19 16:48:37)
 * @author: Dennis
 */
 
public class CoFontMapPanel extends CoPanel
{
	private Map m_map;
	private List m_fontFamilies = new ArrayList();
	private List m_faces;
	
	private CoList m_familyList;
	private CoTextField m_familyTextField;
	private CoPanel m_mapPanel;
	private CoButton m_addMapEntryButton;
	private CoButton m_deleteFamiliesButton;

	private String m_selectedFamily;


	private class FamilyListModel extends AbstractListModel
	{
		public int getSize()
		{
			return m_fontFamilies.size();
		}
		
		public Object getElementAt( int i )
		{
			return m_fontFamilies.get( i );
		}
		
		public void touch()
		{
			fireContentsChanged( this, 0, getSize() - 1 );
		}
	};
	
	private FamilyListModel m_familyListModel = new FamilyListModel();


	

	private CoUserInterfaceBuilder m_builder;

public CoFontMapPanel( CoUserInterfaceBuilder builder )
{
	super( new CoAttachmentLayout() );

	m_builder = builder;
	m_builder.preparePanel( this );


	
	m_familyList = new CoList();
	m_builder.prepareList( m_familyList );
	
	m_familyList.setModel( m_familyListModel );
	JScrollPane sp = new JScrollPane( m_familyList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );

	m_mapPanel = m_builder.createPanel( new CoColumnLayout() );

	m_familyList.addListSelectionListener(
		new ListSelectionListener()
		{
			public void valueChanged( ListSelectionEvent ev )
			{
				fontSelected();
			}
		}
	);

	m_addMapEntryButton = m_builder.createButton( CoFontResources.getName( "ADD" ), null );
	m_addMapEntryButton.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				addToMap();
			}
		}
	);

	m_familyTextField = m_builder.createTextField();
	m_familyTextField.setActivateWhenLosingFocus( true );
	m_familyTextField.setSelectWhenGainingFocus( true );
	m_familyTextField.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				setFontName();
			}
		}
	);

	CoPanel buttons = m_builder.createPanel( new CoRowLayout() );
	{
		CoButton b1 = new CoButton( CoFontResources.getName( "ADD" ) );
		buttons.add( b1 );
		b1.addActionListener(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent ev )
				{
					addFont();
				}
			}
		);

		m_deleteFamiliesButton = m_builder.createButton( CoFontResources.getName( "DELETE" ), null );
		buttons.add( m_deleteFamiliesButton );
		m_deleteFamiliesButton.addActionListener(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent ev )
				{
					deleteFonts();
				}
			}
		);

	}

	
	add( sp,
	     new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_TOP, 0, m_familyTextField ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_mapPanel,
	     new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 0, sp ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );

	add( buttons,
	     new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_familyTextField,
	     new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_TOP, 0, buttons ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_COMPONENT_RIGHT, 0, sp ) ) );



}
private void addFont()
{
	String name = "new";
	while
		( m_fontFamilies.contains( name ) )
	{
		name += "_";
	}

	if
		( addFont( name, (com.bluebrim.font.shared.CoFontFace) m_faces.get( 0 ) ) )
	{
		Collections.sort( m_fontFamilies );
		m_familyListModel.touch();
		
		m_familyList.setSelectedValue( name, true );
	}
}
private boolean addFont( String name, com.bluebrim.font.shared.CoFontFace face )
{
	if ( name.length() == 0 ) return false;
	if ( m_fontFamilies.contains( name ) ) return false;

	com.bluebrim.font.shared.CoFontFaceSpec spec = new com.bluebrim.font.shared.CoFontFaceSpec( name, com.bluebrim.font.shared.CoFontConstants.DEFAULT, com.bluebrim.font.shared.CoFontConstants.DEFAULT, com.bluebrim.font.shared.CoFontConstants.DEFAULT, com.bluebrim.font.shared.CoFontConstants.DEFAULT );
	m_map.put( spec, face );

	m_fontFamilies.add( name );

	return true;
}
private void addToMap()
{
	CoFontMapPane fmp = new CoFontMapPane( m_builder, m_faces );
	com.bluebrim.font.shared.CoFontFace face = (com.bluebrim.font.shared.CoFontFace) m_map.get( com.bluebrim.font.shared.CoFontFaceSpec.getKey( m_selectedFamily, com.bluebrim.font.shared.CoFontConstants.NORMAL_WEIGHT, com.bluebrim.font.shared.CoFontConstants.NORMAL_STYLE, com.bluebrim.font.shared.CoFontConstants.NORMAL_VARIANT, com.bluebrim.font.shared.CoFontConstants.NORMAL_STRETCH ) );
	if ( face == null ) face = (com.bluebrim.font.shared.CoFontFace) m_faces.get( 0 );
	fmp.set( face.getSpec(), face );
	m_mapPanel.add( fmp, m_mapPanel.getComponentCount() - 1 );

	m_mapPanel.revalidate();
}
void createFontFor( com.bluebrim.font.shared.CoFontFace face )
{
	String name = face.getSpec().getFamilyName();

	if
		( addFont( name, face ) )
	{
		Collections.sort( m_fontFamilies );
		m_familyListModel.touch();
		
		m_familyList.setSelectedValue( name, true );
	}
}
private void deleteFonts()
{
	Object [] tmp = m_familyList.getSelectedValues();

	for
		( int n = 0; n < tmp.length; n++ )
	{
		String familyName = (String) tmp[ n ];
		
		Iterator i = m_map.entrySet().iterator();
		while
			( i.hasNext() )
		{
			Map.Entry e = (Map.Entry) i.next();
			com.bluebrim.font.shared.CoFontFaceSpec spec = (com.bluebrim.font.shared.CoFontFaceSpec) e.getKey();
			if
				( spec.getFamilyName().equals( familyName ) )
			{
				i.remove();
			}
		}

		m_fontFamilies.remove( familyName );
	}


	Collections.sort( m_fontFamilies );
	m_familyListModel.touch();

	m_familyList.clearSelection();
}
private void fontSelected()
{
	if
		( m_selectedFamily != null )
	{
		Iterator i = m_map.entrySet().iterator();
		while
			( i.hasNext() )
		{
			Map.Entry e = (Map.Entry) i.next();
			com.bluebrim.font.shared.CoFontFaceSpec spec = (com.bluebrim.font.shared.CoFontFaceSpec) e.getKey();
			if
				( spec.getFamilyName().equals( m_selectedFamily ) )
			{
				i.remove();
			}
		}

		Component [] c = m_mapPanel.getComponents();
		for
			( int n = 0; n < c.length - 1; n++ )
		{
			CoFontMapPane fmp = (CoFontMapPane) c[ n ];
			com.bluebrim.font.shared.CoFontFaceSpec spec = fmp.createSpec( m_selectedFamily );
			com.bluebrim.font.shared.CoFontFace face = fmp.getFace();
			m_map.put( spec, face );
		}
	}

	Object [] tmp = m_familyList.getSelectedValues();
	String family = ( tmp.length == 1 ) ? (String) tmp[ 0 ] : null;

	if
		( family == null )
	{
		m_mapPanel.removeAll();
		m_selectedFamily = null;
		m_familyTextField.setText( "" );
		m_familyTextField.setEnabled( false );
	} else {
		if ( family.equals( m_selectedFamily ) ) return;
		m_selectedFamily = family;
		
		m_familyTextField.setText( m_selectedFamily );
		m_familyTextField.setEnabled( true );
		
		m_mapPanel.removeAll();

		CoDefaultFontMapPane dfmp = null;
		Iterator i = m_map.entrySet().iterator();
		while
			( i.hasNext() )
		{
			Map.Entry e = (Map.Entry) i.next();
			com.bluebrim.font.shared.CoFontFaceSpec spec = (com.bluebrim.font.shared.CoFontFaceSpec) e.getKey();
			if
				( spec.getFamilyName().equals( m_selectedFamily ) )
			{
				com.bluebrim.font.shared.CoFontFace face = (com.bluebrim.font.shared.CoFontFace) e.getValue();
				if
					( spec.getWeight() == com.bluebrim.font.shared.CoFontConstants.DEFAULT )
				{
					dfmp = new CoDefaultFontMapPane( m_builder, m_faces );
					dfmp.set( spec, face );
				} else {
					CoFontMapPane fmp = new CoFontMapPane( m_builder, m_faces );
					fmp.set( spec, face );
					m_mapPanel.add( fmp );
				}
			}
		}

		if
			( dfmp == null )
		{
			dfmp = new CoDefaultFontMapPane( m_builder, m_faces );
			com.bluebrim.font.shared.CoFontFace face = (com.bluebrim.font.shared.CoFontFace) m_map.get( com.bluebrim.font.shared.CoFontFaceSpec.getKey( m_selectedFamily, com.bluebrim.font.shared.CoFontConstants.NORMAL_WEIGHT, com.bluebrim.font.shared.CoFontConstants.NORMAL_STYLE, com.bluebrim.font.shared.CoFontConstants.NORMAL_VARIANT, com.bluebrim.font.shared.CoFontConstants.NORMAL_STRETCH ) );
			if ( face == null ) face = (com.bluebrim.font.shared.CoFontFace) m_faces.get( 0 );
			dfmp.set( face.getSpec(), face );
		}

		m_mapPanel.add( dfmp, 0 );
		m_mapPanel.add( m_addMapEntryButton );
	}

	m_mapPanel.revalidate();
}
void set( Map map, List faces )
{
	m_map = map;
	m_faces = faces;

	m_fontFamilies.clear();
	Iterator i = m_map.keySet().iterator();
	while
		( i.hasNext() )
	{
		com.bluebrim.font.shared.CoFontFaceSpec s = (com.bluebrim.font.shared.CoFontFaceSpec) i.next();
		if ( ! m_fontFamilies.contains( s.getFamilyName() ) ) m_fontFamilies.add( s.getFamilyName() );
	}
	Collections.sort( m_fontFamilies );

	m_familyListModel.touch();
}
private void setFontName()
{
	String familyName = m_familyTextField.getText();
	if
		( ( familyName.length() == 0 ) || ( m_fontFamilies.contains( familyName ) ) )
	{
		m_familyTextField.setText( m_selectedFamily );
		return;
	}

	List tmp = new ArrayList();
	
	Iterator i = m_map.entrySet().iterator();
	while
		( i.hasNext() )
	{
		Map.Entry e = (Map.Entry) i.next();
		com.bluebrim.font.shared.CoFontFaceSpec spec = (com.bluebrim.font.shared.CoFontFaceSpec) e.getKey();
		if
			( spec.getFamilyName().equals( m_selectedFamily ) )
		{
			tmp.add( spec );
			tmp.add( e.getValue() ); // face
			i.remove();
		}
	}

	i = tmp.iterator();
	while
		( i.hasNext() )
	{
		com.bluebrim.font.shared.CoFontFaceSpec spec = (com.bluebrim.font.shared.CoFontFaceSpec) i.next();
		com.bluebrim.font.shared.CoFontFace face = (com.bluebrim.font.shared.CoFontFace) i.next();
		spec = new com.bluebrim.font.shared.CoFontFaceSpec( familyName, spec.getWeight(), spec.getStyle(), spec.getVariant(), spec.getStretch() );
		m_map.put( spec, face );
	}

	m_fontFamilies.remove( m_selectedFamily );
	m_fontFamilies.add( familyName );
	Collections.sort( m_fontFamilies );
	m_familyListModel.touch();

	m_familyList.setSelectedValue( familyName, true );
}
public void setVisible( boolean v )
{
	if
		( ! v )
	{
		if ( m_familyList != null ) m_familyList.clearSelection();
	}
	
	super.setVisible( v );
}
}