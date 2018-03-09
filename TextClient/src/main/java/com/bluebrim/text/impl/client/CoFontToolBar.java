package com.bluebrim.text.impl.client;

import java.awt.*;
import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;


public class CoFontToolBar extends CoAbstractToolBar implements CoAttributeListenerIF
{
	private CoOptionMenu m_familyOptionMenu = new CoOptionMenu();
	private CoComboBox m_sizeComboBox = new CoSlimComboBox( 3 );

	private CoLabel m_familyLabel = new CoLabel();
	private CoLabel m_sizeLabel = new CoLabel();

	private static final NumberFormat m_formater = NumberFormat.getInstance( Locale.getDefault() );

	private static final Object [] m_attributes =
		{
			CoTextConstants.FONT_FAMILY,
			CoTextConstants.FONT_SIZE,
		};
public CoFontToolBar(Action[] actions)
{
	this( actions, null );
}
public CoFontToolBar( Action[] actions, CoAbstractTextEditor editor )
{
	super( actions );

	createToolbar();
	setEditor( editor );

	setDockingCriteria( CoToolbarDockingCriteria.HORIZONTAL );
}
public void attributesChanged(CoAttributeEvent e)
{
	if ( m_editor == null ) return;

	if
		( e.didEditableChange() )
	{
		setAllEnabled( m_editor.isEditable() );
		return;
	}
	
	int offset = e.getP0();
	int length = e.getP1() - offset;
	AttributeSet effective = m_editor.getCoStyledDocument().getAttributes( offset, length, m_attributes );
	
	if
		( e.isStraddlingParagraphs() )
	{
		updateFontComboBoxes( e.getParagraphAttributes(), effective );
	} else if
		( length == 0 )
	{
		updateFontComboBoxes( e.getParagraphAttributes(), effective );
	} else {
		updateFontComboBoxes( e.getCharacterAttributes(), effective );
	}
	

	repaint();
}
public void createFontComboBoxes(java.util.List fonts )
{
//	m_familyComboBox.setAlignmentY( Component.TOP_ALIGNMENT );
//	add( m_familyComboBox );
//	add( m_familyLabel );
	
//	m_sizeComboBox.setAlignmentY( Component.TOP_ALIGNMENT );
//	add( m_sizeComboBox );
//	add( m_sizeLabel );


	add( m_familyOptionMenu,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_familyLabel,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, m_familyOptionMenu ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_COMPONENT_RIGHT, 0, m_familyOptionMenu ) ) );
	
	add( m_sizeComboBox,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, m_familyOptionMenu ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 0, m_familyOptionMenu ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_sizeLabel,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, m_sizeComboBox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_LEFT, 0, m_sizeComboBox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_COMPONENT_RIGHT, 0, m_sizeComboBox ) ) );
	                                         

//	m_familyLabel.setHorizontalAlignment( CoLabel.CENTER );
//	m_sizeLabel.setHorizontalAlignment( CoLabel.CENTER );

	Font f = m_familyLabel.getFont().deriveFont( 10.0f );
	m_familyLabel.setFont( f );
	m_sizeLabel.setFont( f );

	
	m_familyOptionMenu.setRequestFocusEnabled(false);
	m_sizeComboBox.setRequestFocusEnabled(false);
	
	m_familyOptionMenu.addActionListener( getAction( CoStyledEditorKit.fontFamilyCharacterOrParagraphAction ) );
	m_sizeComboBox.addActionListener( getAction( CoStyledEditorKit.fontSizeCharacterOrParagraphAction ) );

	m_familyOptionMenu.setToolTipText( getResourceString( "FONT_FAMILY_TOOL_TIP" ) );
	m_sizeComboBox.setToolTipText( getResourceString( "FONT_SIZE_TOOL_TIP" ) );
	
	m_familyOptionMenu.setQuiet( true );
	m_familyOptionMenu.addNullItem( " " );
	
	if
		( fonts != null )
	{
		
		Iterator iter = fonts.iterator();
		while 
			(iter.hasNext())
		{
			m_familyOptionMenu.addItem( iter.next());
		}
//		m_familyOptionMenu.setMaximumRowCount( fonts.size() + 1 );	
	}
	else 
//		m_familyOptionMenu.setMaximumRowCount( 1 );

	m_familyOptionMenu.setQuiet( false );

	
	m_sizeComboBox.setQuiet( true );
	m_sizeComboBox.addNullItem( " " );
	String[] sizes = CoTextStringResources.getNames( "FONT_SIZE_OPTIONS" );
	if
		( sizes != null )
	{
		for
		 ( int i = 0; i < sizes.length; i++ )
	 {
		 m_sizeComboBox.addItem( sizes[ i ] );
	 }
	}
	m_sizeComboBox.setMaximumRowCount( sizes.length + 1 );
	m_sizeComboBox.setQuiet( false );
	m_sizeComboBox.setEditable( true );


}
protected LayoutManager2 createHorizontalLayout()
{
	return new CoAttachmentLayout();
}
/**
 * This method was created in VisualAge.
 */
public void createToolbar ()
{
	createFontComboBoxes(null);
}
protected LayoutManager2 createVerticalLayout()
{
	return createHorizontalLayout();
}
protected void setAllEnabled( boolean b )
{
	if ( m_familyOptionMenu != null ) m_familyOptionMenu.setEnabled( b );
	if ( m_sizeComboBox != null ) m_sizeComboBox.setEnabled( b );
}
public void setContext( CoTextEditorContextIF context )
{
	m_familyOptionMenu.setQuiet( true );

	// Initiate combobox with one null item
  	if ( m_familyOptionMenu.getItemCount() > 0 ) 
  		m_familyOptionMenu.removeAllItems();
	m_familyOptionMenu.addNullItem(CoTextStringResources.getName( "UNKNOWN" ) );
	
	// Add more items from context
	if
		( context != null )
	{
		java.util.List fonts = context.getFontFamilyNames();
		if (fonts != null)
		{
			Iterator iter = fonts.iterator();
			while 
				(iter.hasNext())	
			{
				m_familyOptionMenu.addItem( (String) iter.next());
		  	}
//			m_familyOptionMenu.setMaximumRowCount( fonts.size() + 1 );
	  }
	}
	
	m_familyOptionMenu.setQuiet( false);
}
public void setEditor(CoAbstractTextEditor editor)
{
	super.setEditor( editor );

	setAllEnabled( m_editor != null );
	
	if
		( m_editor == null )
	{
		m_familyLabel.setText( "" );
		m_sizeLabel.setText( "" );
	}
}
public void updateFontComboBoxes( AttributeSet as, AttributeSet as2 )
{
	// family font
	String font = CoStyleConstants.getFontFamily( as );
	if
		( font == CoStyleConstants.AS_IS_STRING_VALUE )
	{
		m_familyOptionMenu.setAsIs();
	} else {
		m_familyOptionMenu.setSelectedItem( font, true );
	}

	font = CoStyleConstants.getFontFamily( as2 );
	if
		( font == CoStyleConstants.AS_IS_STRING_VALUE )
	{
		m_familyLabel.setText( " " );
	} else {
		m_familyLabel.setText( CoViewStyleConstants.getFontFamily( as2 ) );
	}

	
	
	// font size
	Float size = CoStyleConstants.getFontSize( as );
	
	if
		( size == null )
	{
		m_sizeComboBox.setSelectedIndex( 0, true );
	} else if
		( size == CoStyleConstants.AS_IS_FLOAT_VALUE )
	{
		m_sizeComboBox.setAsIs();
	} else {
		String str = m_formater.format( size.floatValue() );
		m_sizeComboBox.setSelectedItem( str, true );
	}

	size = CoStyleConstants.getFontSize( as2 );
	if
		( size == CoStyleConstants.AS_IS_FLOAT_VALUE )
	{
		m_sizeLabel.setText( " " );
	} else {
		m_sizeLabel.setText( java.text.NumberFormat.getInstance( Locale.getDefault() ).format( CoViewStyleConstants.getFontSize( as2 ) ) );
	}

}
}
