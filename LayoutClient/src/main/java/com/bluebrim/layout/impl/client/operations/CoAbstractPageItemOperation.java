package com.bluebrim.layout.impl.client.operations;

import java.util.*;

import javax.swing.*;

import com.bluebrim.layout.impl.client.*;

/**
 * Abstract subclass for custom layout editor operations
 *
 * @author: Dennis
 */

public abstract class CoAbstractPageItemOperation implements CoPageItemOperationIF
{
	private String m_name;
	private KeyStroke m_keyStroke;
public CoAbstractPageItemOperation( String name )
{
	this( name, null );
}
public CoAbstractPageItemOperation( String name, KeyStroke keystroke )
{
	m_name = name;
	m_keyStroke = keystroke;
}
public String getName()
{
	return m_name;
}
public KeyStroke getShortcut()
{
	return m_keyStroke;
}
public boolean isValidOperand( int operandCount )
{
	return operandCount > 0;
}
public boolean isValidOperand( List operands /* CoPageItemView */ )
{
	return true;
}
}
