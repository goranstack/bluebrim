package com.bluebrim.text.impl.client;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;


public class CoTagToolBar extends CoAbstractToolBar implements CoAttributeListenerIF
{
	private CoOptionMenu m_paragraphTagOptionMenu = new CoOptionMenu();
	private CoOptionMenu m_characterTagOptionMenu = new CoOptionMenu();
public CoTagToolBar(Action[] actions)
{
	this( actions, null );
}
public CoTagToolBar( Action[] actions, CoAbstractTextEditor editor )
{
	super( actions );

	
	add( m_characterTagOptionMenu );
	m_characterTagOptionMenu.setAlignmentY( Component.TOP_ALIGNMENT );
	m_characterTagOptionMenu.setRequestFocusEnabled(false);
	m_characterTagOptionMenu.addActionListener( getAction( CoStyledEditorKit.tagCharacterAction ) );
	m_characterTagOptionMenu.setToolTipText( getResourceString( "CHARACTER_TAG_TOOL_TIP" ) );

	
	add( m_paragraphTagOptionMenu );
	m_paragraphTagOptionMenu.setAlignmentY( Component.TOP_ALIGNMENT );
	m_paragraphTagOptionMenu.setRequestFocusEnabled(false);
	m_paragraphTagOptionMenu.addActionListener( getAction( CoStyledEditorKit.tagParagraphAction ) );
	m_paragraphTagOptionMenu.setToolTipText( getResourceString( "PARAGRAPH_TAG_TOOL_TIP" ) );

	setEditor( editor );
	
	setDockingCriteria( CoToolbarDockingCriteria.HORIZONTAL );

	setContext( null );
}
public void attributesChanged(CoAttributeEvent e)
{
	AttributeSet as;

	if
		( e.didEditableChange() )
	{
		setAllEnabled( m_editor.isEditable() );
		return;
	}
	
	if
		(e.didParagraphChange())
	{
		as = e.getParagraphAttributes();
		updateParagraphTagComboBox(as);
	}
	
	as = e.getCharacterAttributes();
	
	updateCharacterTagComboBox(as);

	repaint();
}
protected void setAllEnabled( boolean b )
{
	if ( m_paragraphTagOptionMenu != null ) m_paragraphTagOptionMenu.setEnabled( b );
	if ( m_characterTagOptionMenu != null ) m_characterTagOptionMenu.setEnabled( b );
}
public void setContext( CoTextEditorContextIF context )
{
	m_characterTagOptionMenu.setQuiet( true );
	m_paragraphTagOptionMenu.setQuiet( true );
	
  if ( m_characterTagOptionMenu.getItemCount() > 0 ) m_characterTagOptionMenu.removeAllItems();
	m_characterTagOptionMenu.addNullItem( com.bluebrim.text.shared.CoStyledDocument.DEFAULT_TAG_NAME );

	if
		( context != null )
	{
		Iterator characterTags = context.getCharacterTagNames().iterator();
	  if
	  	( characterTags != null )
	  {
		  int i = 0;
			while
				( characterTags.hasNext() )
		 	{
			 	i++;
				m_characterTagOptionMenu.addItem( (String) characterTags.next() );
		  }
//			m_characterTagOptionMenu.setMaximumRowCount( i + 1 );
	  }
	}

  
  if ( m_paragraphTagOptionMenu.getItemCount() > 0 ) m_paragraphTagOptionMenu.removeAllItems();
  m_paragraphTagOptionMenu.addNullItem( com.bluebrim.text.shared.CoStyledDocument.DEFAULT_TAG_NAME );

	if
		( context != null )
	{
		Iterator paragraphTags = context.getParagraphTagNames().iterator();
	  if
	  	( paragraphTags != null )
	  {
		  int i = 0;
			while
				( paragraphTags.hasNext() )
		 	{
			 	i++;
				m_paragraphTagOptionMenu.addItem( (String) paragraphTags.next() );
		  }
//			m_paragraphTagOptionMenu.setMaximumRowCount( i + 1 );
	  }
	}
	
	m_characterTagOptionMenu.setQuiet( false );
	m_paragraphTagOptionMenu.setQuiet( false );
}
public void setEditor(CoAbstractTextEditor editor)
{
	super.setEditor( editor );

	setAllEnabled( m_editor != null );
}
public void updateCharacterTagComboBox (AttributeSet as)
{
	String tag = CoStyleConstants.getCharacterTag( as );

	if
		( tag == CoStyleConstants.AS_IS_STRING_VALUE )
	{
		m_characterTagOptionMenu.setAsIs();
	} else {
		m_characterTagOptionMenu.setSelectedItem( tag, true );
	}
}
public void updateParagraphTagComboBox (AttributeSet as)
{
	String tag = CoStyleConstants.getParagraphTag( as );

	if
		( tag == CoStyleConstants.AS_IS_STRING_VALUE )
	{
		m_paragraphTagOptionMenu.setAsIs();
	} else {
		m_paragraphTagOptionMenu.setSelectedItem( tag, true );
	}
	
}
}
