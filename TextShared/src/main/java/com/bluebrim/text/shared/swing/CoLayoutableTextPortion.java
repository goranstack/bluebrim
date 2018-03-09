package com.bluebrim.text.shared.swing;

/**
 * 
 * Creation date: (2001-10-25 12:28:37)
 * @author: Dennis
 */
 
public class CoLayoutableTextPortion
{
	private double m_height;
	private boolean m_isFirst;
	private boolean m_isLast;
public CoLayoutableTextPortion( double height, boolean isFirst, boolean isLast )
{
	m_height = height;
	m_isFirst = isFirst;
	m_isLast = isLast;
}
public double getHeight()
{
	return m_height;
}
public boolean isFirst()
{
	return m_isFirst;
}
public boolean isLast()
{
	return m_isLast;
}
public String toString()
{
	return m_height + " " + m_isFirst + " " + m_isLast;
}
}
