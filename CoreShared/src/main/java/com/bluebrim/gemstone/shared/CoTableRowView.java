package com.bluebrim.gemstone.shared;
import java.io.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
/**
 * Serializable view of a row in a table to be used 
 * by a table ui servant.
 * Creation date: (2000-12-06 11:04:07)
 * @author: Lasse
 */
public class CoTableRowView extends CoListElementView implements Serializable {
	private Object[] 	m_values;
	private boolean[]   m_editableFlags;

	public CoTableRowView(CoObjectIF object, Object values[])
	{
		super(null,null, object); 
		m_values	= values;
	}
	public CoTableRowView(CoObjectIF object, Object values[], boolean editableFlags[])
	{
		this(object, values);
		m_editableFlags 	= editableFlags;
	}
	public CoTableRowView(CoObjectIF object, Object values[], boolean editableFlags[], Icon icon, String text)
	{
		this(object, values, icon, text);
		m_editableFlags 	= editableFlags;
	}
	public CoTableRowView(CoObjectIF object, Object values[], Icon icon, String text)
	{
		super(icon,text, object); 
		m_values	= values;
	}
public CoObjectIF getObject()
{
	return (CoObjectIF )getElement();
}
	public Object getValue(int column)
	{
		return m_values[column];
	}
	public boolean isEditable(int column)
	{
		return  m_editableFlags != null ? m_editableFlags[column] : false;
	}
}
