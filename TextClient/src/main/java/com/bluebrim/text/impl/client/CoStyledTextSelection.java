package com.bluebrim.text.impl.client;
import java.awt.datatransfer.*;
import java.io.*;

import javax.swing.text.*;

import com.bluebrim.base.client.datatransfer.*;

/**
 * Insert the type's description here.
 * Creation date: (2000-09-14 13:51:41)
 * @author Dennis
 */
public class CoStyledTextSelection implements Transferable, ClipboardOwner
{
	public static final DataFlavor FLAVOR = CoDataTransferKit.localFlavor( CoStyledTextSelection.class, null );


	private DataFlavor m_flavors [] = { DataFlavor.stringFlavor, DataFlavor.plainTextFlavor, FLAVOR };
	
	private static class StyleSelection implements Serializable
	{
		int m_length;
		AttributeSet m_attributes;
		StyleSelection m_next;
	}

	private String m_data;
	private StyleSelection m_styleSelection;

public CoStyledTextSelection( com.bluebrim.text.shared.CoStyledDocumentIF doc, int offset, int length )
{
	m_data = getText( doc, offset, length );
	
	if
		( length > 0 )
	{
		int lastEnd = Integer.MAX_VALUE;

		StyleSelection TMP = new StyleSelection();
		StyleSelection tmp = TMP;
	
		// iterera över alla text-element.
		for
			( int pos = offset; pos < ( offset + length ); pos = lastEnd )
		{
			Element run = doc.getCharacterElement( pos );
			lastEnd = run.getEndOffset();

			tmp.m_length = Math.min( lastEnd, offset + length ) - pos;
			tmp.m_attributes = new com.bluebrim.text.shared.CoSimpleAttributeSet( run.getAttributes() );
			tmp.m_next = new StyleSelection();
			tmp = tmp.m_next;
		}

		m_styleSelection = TMP;
	}
}


private static String getText( com.bluebrim.text.shared.CoStyledDocumentIF doc, int offset, int length )
{
	try
	{
		return doc.getText( offset, length );
	}
	catch ( BadLocationException ex )
	{
		return "";
	}
}


public synchronized Object getTransferData( DataFlavor flavor ) throws UnsupportedFlavorException, IOException
{
	if
		( flavor.equals( FLAVOR ) )
	{
		return this;
	} else if
		( flavor.equals( DataFlavor.stringFlavor ) )
	{
	   return m_data;
	} else if
		( flavor.equals( DataFlavor.plainTextFlavor ) )
	{
	   return new StringReader( m_data );
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
	return ( flavor.equals( FLAVOR ) || flavor.equals( DataFlavor.stringFlavor ) || flavor.equals( DataFlavor.plainTextFlavor ) );
}


public void lostOwnership( Clipboard clipboard, Transferable contents )
{
}


public void paste( com.bluebrim.text.shared.CoStyledDocumentIF doc, int pos )
{
	StyleSelection tmp = m_styleSelection;
	
	while
		( tmp.m_next != null )
	{
		if
			( tmp.m_attributes.getAttributeCount() > 0 )
		{
			doc.setCharacterAttributes( pos, tmp.m_length, tmp.m_attributes, true );
		}

		pos += tmp.m_length;
		tmp = tmp.m_next;
	}
}
}