package com.bluebrim.stroke.shared;
import java.awt.*;

//

public class CoBasicStrokeProxy implements Stroke, java.io.Serializable
{
	private float m_width;
	private int m_join;
	private int m_cap;
	private float m_miterlimit;
	private float m_dash [];
	private float m_dash_phase;

	private transient BasicStroke m_implementation;
public CoBasicStrokeProxy( float width, int cap, int join, float miterlimit, float dash [], float dash_phase )
{
	m_width = width;
	m_cap = cap;
	m_join = join;
	m_miterlimit = miterlimit;
	m_dash = dash;
	m_dash_phase = dash_phase;
}
public Shape createStrokedShape( Shape p )
{	
	return getImplementation().createStrokedShape( p );
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-06 15:29:13)
 * @return java.awt.BasicStroke
 */
public BasicStroke getImplementation() 
{
	if( m_implementation == null )
	{
		m_implementation = new BasicStroke( m_width, m_cap, m_join, m_miterlimit, m_dash, m_dash_phase );
	}
	return m_implementation;
}
}
