package com.bluebrim.text.impl.shared;

import java.text.*;


import com.bluebrim.base.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Model for leading values
 * Creation date: (2001-10-25 15:05:03)
 * @author: Dennis
 */

public class CoLeading implements java.io.Serializable
{
	public static final int AUTO = 0;
	public static final int ABSOLUTE = 1;
	public static final int OFFSET = 2;
	public static final int PROPORTIONAL = 3;
	
	private int m_type = AUTO;
	private float m_value = 0;
	/*
		m_type == AUTO						m_value not used
		m_type == ABSOLUTE				m_value [ points ]
		m_type == OFFSET					m_value [ points ]
		m_type == PROPORTIONAL		m_value [ % ]
	*/
public CoLeading()
{
	this( AUTO, 0 );
}
public CoLeading( int type, float value )
{
	m_type = type;
	m_value = value;
}
public float calcHeight( float fontHeight )
{
	switch
		( m_type )
	{
		case AUTO: return fontHeight;
		case ABSOLUTE: return m_value;
		case OFFSET: return fontHeight + m_value;
		case PROPORTIONAL: return fontHeight * m_value / 100;
	}

	return fontHeight;
}
private static float doParse( String s ) throws ParseException
{
	double d = CoLengthUnitSet.parse( s, Double.NaN );
	if ( Double.isNaN( d ) ) throw new ParseException( "", 0 );
	return (float) d;
}
public static String format( CoLeading l )
{
  if
		( l.isAuto() )
	{
	  return "auto";
	} else if
		( l.isOffset() )
	{
	  if
	  	( l.getValue() < 0 )
	  {
		  return "" + CoLengthUnitSet.format( l.getValue() );
	  } else {
		  return "+" + CoLengthUnitSet.format( l.getValue() );
	  }
	} else {
		if
			( l.isProportional() )
		{
		  return l.getValue() + "%";
		} else {
		  return "" + CoLengthUnitSet.format( l.getValue() );
		}
	}
}
public int getType()
{
	return m_type;
}
public float getValue()
{
	return m_value;
}
public boolean isAbsolute()
{
	return m_type == ABSOLUTE;
}
public boolean isAuto()
{
	return m_type == AUTO;
}
public boolean isOffset()
{
	return m_type == OFFSET;
}
public boolean isProportional()
{
	return m_type == PROPORTIONAL;
}
public static CoLeading parse( String s )
{
	if
		( ( s == null ) || ( s.length() == 0 ) || ( s == CoTextConstants.NO_VALUE ) )
	{
		return null;
	}

	if
		( s.indexOf( "auto" ) != -1 )
	{
		return new CoLeading();
	}
	
	try
	{
		int I = s.length();
		
		for
			( int i = I - 1; i > 0; i++ )
		{
			char c = s.charAt( i );
			if
				( c == '%' )
			{
				return new CoLeading( PROPORTIONAL, doParse( s ) );
			}
			
			if ( ! Character.isWhitespace( c ) ) break;
		}

		for
			( int i = 0; i < I; i++ )
		{
			char c = s.charAt( i );
			if
				( ( c == '+' ) || ( c == '-' ) )
			{
				float l = doParse( s.substring( i + 1 ) );
				if ( c == '-' ) l = - l;
				return new CoLeading( OFFSET, l );
			}
			
			if ( ! Character.isWhitespace( c ) ) break;
		}
		
		return new CoLeading( ABSOLUTE, doParse( s ) );
		
	}
	catch ( ParseException ex )
	{
		return new CoLeading();
	}
}
public void setAbsolute( float value )
{
	m_type = ABSOLUTE;
	m_value = value;
}
public void setAuto()
{
	m_type = AUTO;
}
public void setOffset( float value )
{
	m_type = OFFSET;
	m_value = value;
}
public void setProportional( float value )
{
	m_type = PROPORTIONAL;
	m_value = value;
}
public void setValue( float value )
{
	m_value = value;
}
}
