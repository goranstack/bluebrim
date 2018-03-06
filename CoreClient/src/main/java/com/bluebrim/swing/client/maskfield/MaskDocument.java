package com.bluebrim.swing.client.maskfield;

/*
=====================================================================

	MaskDocument.java
	
	Created by Claude Duguay
	Copyright (c) 1998

=====================================================================
*/
/**
	990126	Lasse.
	<br>
	Changed <code>insertString</code> to make it throw an 
	<code>MatchException</code> if it's unable to match the
	inserted string.
*/

import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class MaskDocument extends PlainDocument
{
	protected char templateChar;
	protected MaskMacros macros;
	protected MaskTokenizer tokenizer;
	protected Vector pattern = new Vector();
	protected MaskParser parser = new MaskParser();
		
	public MaskDocument(String mask,
		MaskMacros macros, char templateChar)
	{
		this.templateChar = templateChar;
		this.macros = macros;
		parse(mask);
	}
	public MaskElement getRule(int index)
	{
		return index < pattern.size() ? (MaskElement)pattern.elementAt(index) : null;
	}
public void insertString(int pos, String text, AttributeSet attr) throws BadLocationException
{
	int len = text != null ? text.length() : 0;
	if (len == 0)
		return;
	if (len > 1)
	{
		for (int i = pos; i < len; i++)
			insertString(pos, "" + text.charAt(i), attr);
	}
	else
	{
		if (match(pos, text.charAt(0)))
		{
			super.remove(pos, 1);
			super.insertString(pos, text, attr);
		}
		else
		{
			Toolkit.getDefaultToolkit().beep();
			throw new MatchException("insertString: "+text+" in "+pos+" doesn't match the mask");
		}
	}
}
	public boolean match(int pos, char chr)
	{
		MaskElement element = getRule(pos);
		if (element != null)
		{
			if (element instanceof MaskLiteral)
			{
				char macro = ((MaskLiteral)element).chr;
				if (macros.containsMacro(macro))
				{
					return macros.getMacro(macro).match(chr);
				}
			}
			return element.match(chr);
		}
		else
			return false;
	}
	public void parse(String text)
	{
		MaskTokenizer tokenizer =
			new MaskTokenizer("&|![](){} ", "");
		pattern.removeAllElements();
		tokenizer.tokenize(text);
		while (tokenizer.hasMoreTokens())
		{
			parseElement(tokenizer);
		}
	}
	private void parseElement(MaskTokenizer tokenizer)
	{
		MaskToken next = tokenizer.nextToken();
		if (next.equals('{'))
		{
			pattern.addElement(parser.parseCondition(tokenizer));
			MaskParser.expect(tokenizer.nextToken(), '}');
		}
		else
		{
			String text = next.text;
			for (int i = 0; i < text.length(); i++)
			{
				pattern.addElement(new MaskLiteral(text.charAt(i)));
			}
		}
	}
	public void remove(int pos, int length)
		throws BadLocationException
	{
		if (length > 1)
		{
			for (int i = pos; i < length; i++)
				remove(pos, 1); 
			return;
		}
		else
		{
			if (length == 0 && getLength() == 0)
			{
				String template = template();
				super.insertString(pos, template, null);
				return;
			}
			if (pos == getLength()) return;
			
			String text = "" + template(pos);
			super.remove(pos, 1);
			super.insertString(pos, text, null);
		}
	}
	public String template()
	{
		int length = pattern.size();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++)
		{
			buffer.append(template(i));
		}
		return buffer.toString();
	}
	public char template(int pos)
	{
		MaskElement rule = getRule(pos);
		if (rule instanceof MaskLiteral)
		{
			char literal = ((MaskLiteral)rule).chr;
			if (!macros.containsMacro(literal))
				return literal;
		}
		return templateChar;
	}
}
