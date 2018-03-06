package com.bluebrim.text.impl.client;
import java.awt.datatransfer.*;
import java.io.*;

import javax.swing.text.*;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.text.impl.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2000-09-14 13:51:41)
 * @author Dennis
 */ 
public class CoCharacterStyleSelection implements Transferable, ClipboardOwner
{
	public static final DataFlavor FLAVOR = CoDataTransferKit.localFlavor( CoCharacterStyleSelection.class, null );


	private DataFlavor m_flavors [] = { FLAVOR };

	private AttributeSet m_attributes;
	private boolean m_isParagraph;

	/*
		public CopyStyle( AttributeSet as, boolean isParagraph )
		{
			m_attributes = as;
			m_isParagraph = isParagraph;
		}
		
		public void paste()
		{
			if
				( m_isParagraph )
			{
				pasteParagraphStyles( m_attributes );
			} else {
				pasteCharacterStyles( m_attributes );
			}
		}
*/
public CoCharacterStyleSelection( AttributeSet as, boolean isParagraph )
{
	m_attributes = as;
	m_isParagraph = isParagraph;
}


public synchronized Object getTransferData( DataFlavor flavor ) throws UnsupportedFlavorException, IOException
{
	if
		( flavor.equals( FLAVOR ) )
	{
		return this;
	} else {
	   throw new UnsupportedFlavorException(flavor);
	}
}


public synchronized DataFlavor[] getTransferDataFlavors()
{
	return m_flavors;
}


public boolean isDataFlavorSupported( DataFlavor flavor )
{
	return flavor.equals( FLAVOR );
}


public void lostOwnership( Clipboard clipboard, Transferable contents )
{
}


public void paste( com.bluebrim.text.shared.CoStyledDocumentIF doc, int offset, int length )
{
	if
		( m_isParagraph )
	{
		if
			( m_attributes.isDefined( CoStyleConstants.PARAGRAPH_TAG ) )
		{
			Object tag = m_attributes.getAttribute( CoStyleConstants.PARAGRAPH_TAG );
			doc.setParagraphTag( offset, length, (String) tag );
			( (MutableAttributeSet) m_attributes ).removeAttribute( CoStyleConstants.PARAGRAPH_TAG );
		}
		doc.setParagraphAttributes( offset, length, m_attributes, false );
	} else {
		if
			( m_attributes.isDefined( CoStyleConstants.CHARACTER_TAG ) )
		{
			Object tag = m_attributes.getAttribute( CoStyleConstants.CHARACTER_TAG );
			doc.setCharacterTag( offset, length, (String) tag );
			( (MutableAttributeSet) m_attributes ).removeAttribute( CoStyleConstants.CHARACTER_TAG );
		}
		doc.setCharacterAttributes( offset, length, m_attributes, false );
	}
}
}