package com.bluebrim.swing.client.maskfield;

/*
=====================================================================

	JMaskField.java
	
	Created by Claude Duguay
	Copyright (c) 1998

=====================================================================
*/
/**
	990126	Lasse.
	<br>
	Implemented <code>replaceSelection</code> so that the
	replaced text is inserted again again in case of an invalid insertion.
	To make this work I had to change <code>MaskDocument.insertString</code>
	so it throws an <code>MatchException</code> if it's unable to match the
	inserted string.
*/

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;


public class JMaskField extends JTextField implements 
	DocumentListener, KeyListener, FocusListener
{
	protected MaskMacros macros;
	protected MaskDocument doc;
	protected boolean bspace;
	protected boolean delete;
	protected int pos = -1;

	public JMaskField(String mask)
	{
		this(mask, new MaskMacros(), '_');
	}
	public JMaskField(String mask, MaskMacros macros)
	{
		this(mask, macros, '_');
	}
	public JMaskField(String mask,
		MaskMacros macros, char templateChar)
	{
		setMacros(macros);
		doc = new MaskDocument(mask, macros, templateChar);
		doc.addDocumentListener(this);
		addFocusListener(this);
		addKeyListener(this);
		setDocument(doc);
		setText("");
		setPreferredSize(new Dimension(128, 23));
	}
	private void adjustCaretBackward(int pos)
	{
		while (isLiteral(pos - 1)) {pos--;}
		if (pos <= 0)
		{
			adjustCaretForward(0);
		}
		else setCaretPosition(pos);
	}
	private void adjustCaretForward(int pos)
	{
		while (isLiteral(pos)) {pos++;}
		if (pos > doc.getLength()) pos = doc.getLength();
		setCaretPosition(pos);
	}
	public void changedUpdate(DocumentEvent event) {}
	private int findNextLiteralFrom(int pos, boolean forward)
	{
		int max = doc.pattern.size();
		if (pos <= 0 && !forward || pos >= max && forward)
			return -1;
		if (forward)
		{
			int _pos = pos+1;
			while (!isLiteral(_pos) && _pos <= max) 
			{
				_pos++;
			}
			return _pos;
		}
		else
		{
			int _pos = pos-1;
			while (!isLiteral(_pos) && _pos >= 0) 
			{
				_pos--;
			}
			return _pos;
		}
	}
	public void focusGained(FocusEvent event)
	{
		if (pos < 0) adjustCaretForward(0);
		else setCaretPosition(pos);
	}
	public void focusLost(FocusEvent event)
	{
		pos = getCaretPosition();
	}
	public MaskMacros getMacros()
	{
		return macros;
	}
	public void insertUpdate(DocumentEvent event)
	{
		int pos = event.getOffset();
		int len = event.getLength();
		if (bspace)
		{
			adjustCaretBackward(pos);
		}
		else if (delete)
		{
			setCaretPosition(pos);
		}
		else
		{
			adjustCaretForward(pos + 1);
		}
	}
	private boolean isLiteral(int pos)
	{
		if (pos < 0 || pos >= doc.pattern.size())
			return false;
		MaskElement rule = doc.getRule(pos);
		if (rule instanceof MaskLiteral)
		{
			char literal = (((MaskLiteral)rule).chr);
			return !doc.macros.containsMacro(literal);
		}
		return false;
	}
	public void keyPressed(KeyEvent event)
	{
		bspace = event.getKeyCode() == KeyEvent.VK_BACK_SPACE;
		delete = event.getKeyCode() == KeyEvent.VK_DELETE;
	}
	public void keyReleased(KeyEvent event) {}
	public void keyTyped(KeyEvent event) {}
	public void removeUpdate(DocumentEvent event) {}
public void replaceSelection(String content)
{
	if ((!isEditable()) || (!isEnabled()))
	{
		getToolkit().beep();
		return;
	}
	Document doc = getDocument();
	if (doc != null)
	{
		String	tReplacedText 	= null;
		final Caret tCaret 		= getCaret();
		int p0 					= Math.min(tCaret.getDot(), tCaret.getMark());
		int p1 					= Math.max(tCaret.getDot(), tCaret.getMark());
		try
		{
			if (p0 != p1)
			{
				tReplacedText	= doc.getText(p0,p1 - p0);
				doc.remove(p0, p1 - p0);
			}
			if (content != null && content.length() > 0)
			{
				doc.insertString(p0, content, null);
			}
		}
		catch (MatchException e )
		{
			if (tReplacedText != null && tReplacedText.length() > 0)
			{
				try
				{
					doc.insertString(p0,tReplacedText, null);
				}
				catch (BadLocationException execption)
				{
					System.out.println("We're not supposed to get here!");
				}
			}
		}
		catch (BadLocationException e)
		{
			getToolkit().beep();
		}
	}
}
	public void setMacros(MaskMacros macros)
	{
		this.macros = macros;
	}
}
