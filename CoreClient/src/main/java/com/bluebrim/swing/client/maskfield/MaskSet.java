package com.bluebrim.swing.client.maskfield;

/*
=====================================================================

	MaskSet.java
	
	Created by Claude Duguay
	Copyright (c) 1998

=====================================================================
*/

public class MaskSet implements MaskElement
{
	protected boolean negate;
	protected String set;
	
	public MaskSet(boolean negate, String set)
	{
		this.negate = negate;
		this.set = set;
	}
	public boolean match(char chr)
	{
		boolean member = set.indexOf(chr) > -1;
		if (negate) return !member;
		else return member;
	}
	public String toString()
	{
		return (negate ? "not(" : "set(") + set + ")";
	}
}
