package com.bluebrim.base.shared;
import java.text.*;
import java.util.*;

import com.bluebrim.formula.shared.*;


public class CoLengthUnitSet implements CoObjectIF, CoConvertibleUnitSet
{
public CoLengthUnitSet( int viewDecimalCount, int snapDecimalCount )
{
	m_viewDecimalCount = viewDecimalCount;
	m_snapDecimalCount = snapDecimalCount;
}

	private Map m_units = new HashMap();
	
	private CoConvertibleUnit m_currentUnit;
	private int m_viewDecimalCount;
	private int m_snapDecimalCount;
	
	private double m_base = 1;

	
	public static final String CURRENT_UNIT_PROPERTY = "CURRENT_UNIT_PROPERTY";
	public static final String VIEW_DECIMAL_COUNT_PROPERTY = "VIEW_DECIMAL_COUNT_PROPERTY";
	public static final String SNAP_DECIMAL_COUNT_PROPERTY = "SNAP_DECIMAL_COUNT_PROPERTY";
	
	private CoEventListenerList m_listeners = null;




	private static CoFormula m_formula = new CoFormula();
	private static NumberFormat m_numberFormatter = NumberFormat.getInstance( Locale.getDefault() );


public void add( CoConvertibleUnit unit )
{
	m_units.put( unit.getName(), unit );
	if ( m_currentUnit == null ) m_currentUnit = unit;
}


public void addPropertyChangeListener( CoPropertyChangeListener l )
{
	if ( m_listeners == null ) m_listeners = new CoEventListenerList();
	
	m_listeners.add( CoPropertyChangeListener.class, l );
}


public boolean contains( String name )
{
	return m_units.containsKey( name );
}


/**
 */
public void firePropertyChange( Object source, String propertyName, String changeID, Object oldValue, Object newValue )
{
	if
		( oldValue != null && oldValue.equals( newValue ) )
	{
		return;
	}
	
	Object targets[];
	synchronized ( this )
	{
		if
			( m_listeners == null )
		{
			return;
		}
		targets = (Object[]) m_listeners.getListenerList().clone();
	}
	CoPropertyChangeEvent ev = new CoPropertyChangeEvent( source, this, propertyName, changeID, oldValue, newValue );
	for
		( int i = 0; i < targets.length; i += 2 )
	{
		if
			( (Class) targets[ i ] == CoPropertyChangeListener.class )
		{
			CoPropertyChangeListener target = (CoPropertyChangeListener) targets[ i + 1 ];
			target.propertyChange( ev );
		}
	}
}


/**
 */
public void firePropertyChange( String propertyName, Object oldValue, Object newValue )
{
	firePropertyChange( this, propertyName, CoPropertyChangeEvent.NO_ID, oldValue, newValue );
}


public static String format( double d ) 
{
	return format( d, null );
}


public static String format( double d, CoConvertibleUnit u, int maximumFractionDigits ) 
{
	if
		( u != null )
	{
		d = u.to( d );
		m_numberFormatter.setMaximumFractionDigits( maximumFractionDigits );
	}

//	System.err.println( m_numberFormatter.format( new Double( d ) ) + " # " + m_numberFormatter.format( new Double( d ) ) );
	
	String str = m_numberFormatter.format( new Double( d ) );
//	m_numberFormatter.setMaximumFractionDigits( tmp );
	
	if
		( u != null )
	{
		String unitStr = u.getName();
		if
			( unitStr.length() > 0 )
		{
			str += " " + unitStr;
		}
	}

	return str;
}


public static String format( double d, CoConvertibleUnitSet us ) 
{
	return format( d, 
		             ( us == null ) ? null : us.getCurrentUnit(), 
		             ( us == null ) ? 0 : us.getViewDecimalCount()
		            );
}


public double from( double x_units )
{
	return m_currentUnit.from( x_units );
}


public float from( float x_units )
{
	return m_currentUnit.from( x_units );
}


public CoConvertibleUnit getCurrentUnit()
{
	return m_currentUnit;
}


public String getName()
{
	return m_currentUnit.getName();
}


public int getSnapDecimalCount()
{
	return m_snapDecimalCount;
}


public CoConvertibleUnit getUnit( String name )
{
	return (CoConvertibleUnit) m_units.get( name );
}


public Map getUnitMap()
{
	return m_units;
}


public int getViewDecimalCount()
{
	return m_viewDecimalCount;
}


public static double parse( String s ) throws ParseException
{
	return parse( s, null );
}


public static double parse( String s, double nullValue )
{
	return parse( s, nullValue, null );
}


public static double parse( String s, double nullValue, CoConvertibleUnitSet us )
{
	if ( s.length() == 0 ) return nullValue;

	try
	{
		m_formula.setUnits( us );
		m_formula.evaluate( s );
		s = m_numberFormatter.format( m_formula.getResult() );		
	}
	catch ( CoFormulaException ex )
	{
	}
	catch ( IllegalArgumentException ex2 )
	{
	}

	try
	{
		return m_numberFormatter.parse( s ).doubleValue();
	}
	catch ( ParseException ex )
	{
		return nullValue;
	}
}


public static int parse( String s, int nullValue )
{
	return parse( s, nullValue, null );
}


public static int parse( String s, int nullValue, CoConvertibleUnitSet us )
{
	if ( s.length() == 0 ) return nullValue;

	try
	{
		m_formula.setUnits( us );
		m_formula.evaluate( s );
		s = m_numberFormatter.format( m_formula.getResult() );
	}
	catch ( CoFormulaException ex )
	{
	}
	catch ( IllegalArgumentException ex2 )
	{
	}

	try
	{
		return m_numberFormatter.parse( s ).intValue();
	}
	catch ( ParseException ex )
	{
		return nullValue;
	}
}


public static double parse( String s, CoConvertibleUnitSet us ) throws ParseException
{
	try
	{
		m_formula.setUnits( us );
		m_formula.evaluate( s );
		s = m_numberFormatter.format( m_formula.getResult() );
	}
	catch ( CoFormulaException ex )
	{
	}
	catch ( IllegalArgumentException ex2 )
	{
	}

	return m_numberFormatter.parse( s ).doubleValue();
}


/**
 */
public void removePropertyChangeListener( CoPropertyChangeListener l )
{
	if ( m_listeners != null ) m_listeners.remove( CoPropertyChangeListener.class, l );
}


public double round( double x )
{
	return Math.round( x * m_base ) / m_base;
}


public void setCurrentUnit( CoConvertibleUnit unit )
{
	CoConvertibleUnit old = m_currentUnit;
	m_currentUnit = unit;

	firePropertyChange( CURRENT_UNIT_PROPERTY, old, m_currentUnit );
}


public void setSnapDecimalCount( int c )
{
	m_snapDecimalCount = c;
	m_base = Math.pow( 10, m_snapDecimalCount );

	firePropertyChange( SNAP_DECIMAL_COUNT_PROPERTY, null, null );
}


public void setViewDecimalCount( int c )
{
	m_viewDecimalCount = c;

	firePropertyChange( VIEW_DECIMAL_COUNT_PROPERTY, null, null );
}


public double to( double x_baseUnits )
{
	return m_currentUnit.to( x_baseUnits );
}


public float to( float x_baseUnits )
{
	return m_currentUnit.to( x_baseUnits );
}
}