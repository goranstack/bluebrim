package com.bluebrim.swing.client.maskfield;

/*
=====================================================================

	MaskLiteral.java
	
	Created by Claude Duguay
	Copyright (c) 1998

=====================================================================
*/

public class MaskLiteral implements MaskElement
{
	protected char chr;
	
	public MaskLiteral(char chr)
	{
		this.chr = chr;
	}
	public boolean match(char chr)
	{
		return this.chr == chr;
	}
	public String toString()
	{
		return "literal('" + chr + "')";
	}
}
