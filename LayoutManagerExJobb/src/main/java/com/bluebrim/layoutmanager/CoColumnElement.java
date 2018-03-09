package com.bluebrim.layoutmanager;

/**
 * Represent a columns location, size and the free intervalls.
 * Creation date: (2000-06-06 13:39:03)
 * @author: Arvid Berg & Masod Jalalian 
 */
import java.awt.geom.*;
import java.util.*;

public class CoColumnElement 
{
	protected List intervalls;
	private double width;
	private double height;
	private double m_x;
	private double m_y;
/**
 * Default constructor
 */
public CoColumnElement() 
{
	super();
	intervalls = new ArrayList();
}
/**
 * Construct a ColumnElement with size(in_width,in_height)
 * Creation date: (2000-06-06 13:50:54)
 * @param in_height double
 * @param in_width double
 */
public CoColumnElement(double in_width,double in_height) 
{	
	this();
	setWidth(in_width);
	setHeight(in_height);
}
/**
 * Creates a ColumnElement and sets location and size.
 * Creation date: (2000-06-06 13:50:54)
 * @param in_height double
 * @param in_width double
 */
public CoColumnElement(double in_x,double in_y, double in_width,double in_height) 
{
	this(in_width,in_height);	
	m_x=in_x;
	m_y=in_y;
}
/**
 * Adds a new free intervall to the column.
 * Creation date: (2000-06-06 13:40:36)
 * @param obj columnalg.server.Intervall
 */
public void add(com.bluebrim.layoutmanager.CoInterval obj) 
{
	intervalls.add((Object)obj);
}
/**
 * Returns the height of the column.
 * Creation date: (2000-06-06 13:48:59)
 * @return double
 */
public double getHeight() 
{
	return height;
}
/**
 * Return the list of free intervalls in the column.
 * Creation date: (2000-06-06 13:44:26)
 * @return java.util.List
 */
public java.util.List getIntervalls() 
{
	return intervalls;
}
/**
 * Return the location of the column.
 * Creation date: (2000-06-20 14:58:40)
 * @return java.awt.geom.Point2D
 */
public java.awt.geom.Point2D getLoaction() 
{
	return new Point2D.Double(m_x,m_y);
}
/**
 * Return the width of the column.
 * Creation date: (2000-06-06 13:48:59)
 * @return double
 */
public double getWidth() 
{
	return width;
}
/**
 * Return the x position of the column.
 * Creation date: (2000-06-06 14:04:37)
 * @return double
 */
public double getX() 
{
	return m_x;
}
/**
 * Return the y position of the column.
 * Creation date: (2000-06-06 14:04:37)
 * @return double
 */
public double getY() 
{
	return m_y;
}
/**
 * Sets the hight of the column.
 * Creation date: (2000-06-06 13:48:59)
 * @param newHeight double
 */
public void setHeight(double newHeight) 
{
	height = newHeight;
}
/**
 * Set a new list of free intervalls.
 * Creation date: (2000-06-06 13:44:26)
 * @param newIntervalls java.util.List
 */
public void setIntervalls(List newIntervalls) 
{
	intervalls = newIntervalls;
}
/**
 * Sets the width of the column.
 * Creation date: (2000-06-06 13:48:59)
 * @param newWidth double
 */
public void setWidth(double newWidth) 
{
	width = newWidth;
}
/**
 * Sets the x position of the column.
 * Creation date: (2000-06-06 14:04:37)
 * @param newM_x double
 */
public void setX(double newM_x) 
{
	m_x = newM_x;
}
/**
 * Sets the y position of the column.
 * Creation date: (2000-06-06 14:04:37)
 * @param newM_y double
 */
public void setY(double newM_y) 
{
	m_y = newM_y;
}
}
