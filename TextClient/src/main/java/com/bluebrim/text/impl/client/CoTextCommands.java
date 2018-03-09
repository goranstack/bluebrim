package com.bluebrim.text.impl.client;
import com.bluebrim.text.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Abstract class that acts a placeholder for several static inner classes
 * that handles commands working on a document in a <code>CoText</code>
 * Creation date: (2000-04-04)
 * @author: Dennis M
 */
 
public abstract class CoTextCommands
{
	public static abstract class DocumentCommand extends CoCommand
	{
		protected CoFormattedTextHolderIF m_text;
		
		protected DocumentCommand( String name, CoFormattedTextHolderIF text )
		{
			super( name );
			m_text = text;
		}
	};
	

	
	public static class SetDocumentCommand extends DocumentCommand
	{
		protected CoFormattedText m_document;
		
		public SetDocumentCommand( CoFormattedTextHolderIF text, CoFormattedText doc )
		{
			super( "SET_TEXT", text );
			m_document = doc;
		}
		
		public boolean doExecute()
		{
			m_text.setFormattedText( m_document );
			return true;
		}
	};
	

	
}