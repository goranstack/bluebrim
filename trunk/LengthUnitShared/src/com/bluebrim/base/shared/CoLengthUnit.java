package com.bluebrim.base.shared;



//

public class CoLengthUnit implements CoConvertibleUnit
{
	private String m_name;
	private double m_unitsPerBaseUnit;

		
	
	public static final CoLengthUnit POINTS = new CoLengthUnit( "POINTS", 1.0 );
	public static final CoLengthUnit MM = new CoLengthUnit( "MM", 25.4 / 72.0 );
	public static final CoLengthUnit CM = new CoLengthUnit( "CM", 2.54 / 72.0 );
	public static final CoLengthUnit INCH = new CoLengthUnit( "INCH", 1.0 / 72.0 );


	
	public static final CoLengthUnitSet LENGTH_UNITS = new CoLengthUnitSet( 3, 0 );
	static
	{
		LENGTH_UNITS.add( POINTS );
		LENGTH_UNITS.add( MM );
		LENGTH_UNITS.add( CM );
		LENGTH_UNITS.add( INCH );

		LENGTH_UNITS.setCurrentUnit( MM );
	}


public CoLengthUnit( String name, double unitsPerBaseUnit )
{
	m_name = name;
	m_unitsPerBaseUnit = unitsPerBaseUnit;
}


public double from( double x_units )
{
	return x_units / m_unitsPerBaseUnit;
}


public float from( float x_units )
{
	return (float) ( x_units / m_unitsPerBaseUnit );
}


public String getName()
{
	return CoLengthUnitStringResources.getName( m_name );
}

public String getKey() {
	return m_name;
}

public double to( double x_baseUnits )
{
	return x_baseUnits * m_unitsPerBaseUnit;
}


public float to( float x_baseUnits )
{
	return (float) ( x_baseUnits * m_unitsPerBaseUnit );
}
}