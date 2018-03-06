package com.bluebrim.swing.client.maskfield;

/*
=====================================================================

	MaskExpression.java
	
	Created by Claude Duguay
	Copyright (c) 1998

=====================================================================
*/

public class MaskExpression implements MaskElement
{
	protected MaskElement element;
	
	public MaskExpression(MaskElement element)
	{
		this.element = element;
	}
	public boolean match(char chr)
	{
		return element.match(chr);
	}
	public String toString()
	{
		return "expression(" + element.toString() + ")";
	}
}
