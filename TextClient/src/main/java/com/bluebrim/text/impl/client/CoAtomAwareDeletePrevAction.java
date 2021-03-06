package com.bluebrim.text.impl.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.text.*;

public class CoAtomAwareDeletePrevAction extends TextAction {
public CoAtomAwareDeletePrevAction() {
	super( DefaultEditorKit.deletePrevCharAction );
}
public void actionPerformed( ActionEvent e )
{
	JTextComponent tmp = (JTextComponent) e.getSource();
	boolean beep = true;
	if
		( tmp != null && tmp.isEditable() )
	{
		try
		{
			if (tmp.getDocument() instanceof com.bluebrim.text.shared.CoStyledDocumentIF) 
			{
				com.bluebrim.text.shared.CoStyledDocumentIF doc = (com.bluebrim.text.shared.CoStyledDocumentIF)tmp.getDocument();
				Caret caret = tmp.getCaret();
				int dot = caret.getDot();
				int mark = caret.getMark();
				if
					( dot != mark )
				{
					doc.remove( Math.min( dot, mark ), Math.abs( dot - mark ) );
					beep = false;
				}
					else if ( dot > 0 )
				{
					Element elem = doc.getCharacterElement( dot - 1 );
					if
						( doc.isAtomic( elem ) )
					{
						int start = elem.getStartOffset();
						int length = elem.getEndOffset() - start;
						doc.remove( start, length );
					} else {
						doc.remove( dot - 1, 1 );
					}
					beep = false;
				}
				
			} else {
				Document doc = tmp.getDocument();
				Caret caret = tmp.getCaret();
				int dot = caret.getDot();
				int mark = caret.getMark();
				if (dot != mark) {
					doc.remove(Math.min(dot, mark), Math.abs(dot - mark));
					beep = false;
				} else if (dot > 0) {
					doc.remove(dot - 1, 1);
					beep = false;
				}
			}
		}
		catch ( BadLocationException bl )
		{
		}
	}
	if
		( beep )
	{
		Toolkit.getDefaultToolkit().beep();
	}
}
}
