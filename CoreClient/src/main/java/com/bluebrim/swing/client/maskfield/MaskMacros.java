package com.bluebrim.swing.client.maskfield;

/*
=====================================================================

	MaskMacros.java
	
	Created by Claude Duguay
	Copyright (c) 1998

=====================================================================
*/

import java.util.Enumeration;
import java.util.Hashtable;

public class MaskMacros
{
	protected Hashtable table;
	protected MaskParser parser = new MaskParser();

	public MaskMacros()
	{
		table = new Hashtable();
	}
	public void addMacro(char key, String macro)
	{
		MaskElement element = parser.parseMacro(macro);
		table.put(new Character(key), element);
	}
	public boolean containsMacro(char key)
	{
		return table.containsKey(new Character(key));
	}
	public MaskElement getMacro(char key)
	{
		return (MaskElement)table.get(new Character(key));
	}
	public static void main(String[] args)
	{	
		MaskMacros macros = new MaskMacros();
		macros.addMacro('@', "[a-zA-Z]");
		macros.addMacro('#', "[0-9]");
		System.out.println(macros);
	}
	public void removeMacro(char key)
	{
		table.remove(new Character(key));
	}
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("macros\n{\n");
		Enumeration keys = table.keys();
		Enumeration enumer = table.elements();
		Character key;
		MaskElement element;
		while (keys.hasMoreElements())
		{
			key = (Character)keys.nextElement();
			element = (MaskElement)enumer.nextElement();
			buffer.append(" " + key.charValue() + "=");
			buffer.append(element.toString() + "\n");
		}
		buffer.append("}\n");
		return buffer.toString();
	}
}
