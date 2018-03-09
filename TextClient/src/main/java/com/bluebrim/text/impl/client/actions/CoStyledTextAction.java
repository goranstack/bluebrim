package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

/**
 * Abstract superclass of all text editor actions.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoStyledTextAction extends StyledEditorKit.StyledTextAction implements CoActionConstantsIF
{
	private static JEditorPane m_textPane; // target of actions

	private static final MutableAttributeSet m_attributes = new com.bluebrim.text.shared.CoSimpleAttributeSet();

public CoStyledTextAction(String name)
{
	super(name);
}
public final void actionPerformed( ActionEvent e )
{
	// get target
	JTextPane editor = (JTextPane) getTextPane( e );
	if (editor == null) return;
	if ( ! editor.isEditable() ) return;

	// perform operation
	doit( editor, e );

	// repaint editor
	editor.repaint();

	// restore focus
	editor.requestFocus();
}
protected final void changeCharacterOrParagraphAttributes( JEditorPane editor, com.bluebrim.text.shared.CoAttributeSetOperationIF op )
{
	int p0 = editor.getSelectionStart();
	int p1 = editor.getSelectionEnd();

	com.bluebrim.text.shared.CoStyledDocumentIF doc = (com.bluebrim.text.shared.CoStyledDocumentIF) getStyledDocument( editor );
	if
		( isParagraphSelection( editor ) )
	{
		doc.changeParagraphAttributes( p0, p1 - p0, op );
	} else {
		doc.changeCharacterAttributes( p0, p1 - p0, op );
	}
}
protected final void clearCharacterAttribute(JEditorPane editor, Object attribute)
{
	int p0 = editor.getSelectionStart();
	int p1 = editor.getSelectionEnd();
	if
		( p0 != p1 )
	{
		// selected text
		com.bluebrim.text.shared.CoStyledDocumentIF doc = (com.bluebrim.text.shared.CoStyledDocumentIF) getStyledDocument( editor );
		m_attributes.addAttribute( attribute, attribute );
		doc.unsetCharacterAttributes( p0, p1 - p0, m_attributes );
		m_attributes.removeAttributes( m_attributes );
	} else {
		// input attributes
		StyledEditorKit k = getStyledEditorKit( editor );
		MutableAttributeSet inputAttributes = k.getInputAttributes();
		inputAttributes.removeAttribute( attribute );
	}
}
protected final void clearCharacterOrParagraphAttribute(JEditorPane editor, Object attribute)
{
	if
		( isParagraphSelection( editor ) )
	{
		clearParagraphAttribute( editor, attribute );
	} else {
		clearCharacterAttribute( editor, attribute );
	}
}
protected final void clearParagraphAttribute(JEditorPane editor, Object attribute)
{
	int p0 = editor.getSelectionStart();
	int p1 = editor.getSelectionEnd();
	com.bluebrim.text.shared.CoStyledDocumentIF doc = (com.bluebrim.text.shared.CoStyledDocumentIF) getStyledDocument(editor);
	m_attributes.addAttribute(attribute, attribute);
	doc.unsetParagraphAttributes(p0, p1 - p0, m_attributes);
	m_attributes.removeAttributes(m_attributes);
}
public  void doit( JTextPane editor )
{
	doit( editor, null );
}
public abstract void doit( JTextPane editor, ActionEvent e );
protected final AttributeSet getCharacterOrParagraphAttributes( JEditorPane editor, boolean leaveAsIs )
{
	int p0 = editor.getSelectionStart();
	int p1 = editor.getSelectionEnd();

	com.bluebrim.text.shared.CoStyledDocumentIF doc = (com.bluebrim.text.shared.CoStyledDocumentIF) getStyledDocument( editor );
	AttributeSet attr = null;
	if
		( isParagraphSelection( editor ) )
	{
		attr = doc.getParagraphAttributes( p0, p1 - p0 );
	} else {
		attr = doc.getCharacterAttributes( p0, p1 - p0 );
	}

	MutableAttributeSet a = new com.bluebrim.text.shared.CoSimpleAttributeSet( attr );
	
	if
		( ! leaveAsIs )
	{
		java.util.Enumeration e = attr.getAttributeNames();
		while
			( e.hasMoreElements() )
		{
			Object key = e.nextElement();
			if
				( a.getAttribute( key ) == com.bluebrim.text.impl.shared.CoStyleConstants.AS_IS_VALUE )
			{
				a.removeAttribute( key );
			}
		}
	}

	return a;

}
protected final Object getSelectedCharacterAttributeValue( JEditorPane editor, Object attribute )
{
	int p0 = editor.getSelectionStart();
	int p1 = editor.getSelectionEnd();
	
	if
		( p0 != p1 )
	{
		// selected text
		com.bluebrim.text.shared.CoStyledDocumentIF doc = (com.bluebrim.text.shared.CoStyledDocumentIF) editor.getDocument();
		return doc.getCharacterAttribute( p0, p1 - p0, attribute );
	} else {
		// input attributes
		AttributeSet attr = getStyledEditorKit(editor).getInputAttributes();
		if
			(attr.isDefined(attribute))
		{
			return attr.getAttribute(attribute);
		} else {
			return null;
		}
	}
}
protected final Object getSelectedParagraphAttributeValue( JEditorPane editor, Object attribute  )
{
	int p0 = editor.getSelectionStart();
	int p1 = editor.getSelectionEnd();

  com.bluebrim.text.shared.CoStyledDocumentIF doc = (com.bluebrim.text.shared.CoStyledDocumentIF) editor.getDocument();
	return doc.getParagraphAttribute( p0, p1 - p0, attribute );
}
public JEditorPane getTextPane(ActionEvent e)
{
	if
		( m_textPane != null )
	{
		return m_textPane;
	} else {
		return super.getEditor(e);
	}
}
protected final boolean isParagraphSelection( JEditorPane editor )
{
	int p0 = editor.getSelectionStart();
	int p1 = editor.getSelectionEnd();

	com.bluebrim.text.shared.CoStyledDocumentIF doc = (com.bluebrim.text.shared.CoStyledDocumentIF) getStyledDocument( editor );
	if
		( p0 == p1 )
	{
		return true; // empty selection range -> is paragraph selection
	} else if
		( doc.getParagraphElement( p0 ) != doc.getParagraphElement( p1 ) )
	{
		return true; // non empty selection range spans paragraphs -> is paragraph selection
	} else {
		return false; // non empty selection range within paragraph -> is paragraph selection
	}
}
protected final void setCharacterOrParagraphAttributes( JEditorPane editor, AttributeSet attr, boolean replace )
{
	if
		( isParagraphSelection( editor ) )
	{
		setParagraphAttributes( editor, attr, replace, true );
	} else {
		setCharacterAttributes( editor, attr, replace );
	}
}
protected void setParagraphAttributes( JEditorPane editor, AttributeSet attr, boolean replace, boolean isCharacterAttribute )
{
	if
		( isCharacterAttribute )
	{
		setParagraphCharacterAttributes( editor, attr, replace );
	} else {
		setParagraphAttributes( editor, attr, replace );
	}
}
protected void setParagraphCharacterAttributes( JEditorPane editor, AttributeSet attr, boolean replace )
{
	int p0 = editor.getSelectionStart();
	int p1 = editor.getSelectionEnd();
	com.bluebrim.text.shared.CoStyledDocument doc = (com.bluebrim.text.shared.CoStyledDocument) getStyledDocument(editor);
	doc.setParagraphCharacterAttributes( p0, p1 - p0, attr, replace );
}
// set target of actions

public static void setTextPane( JEditorPane textPane )
{
	m_textPane = textPane;
}
}