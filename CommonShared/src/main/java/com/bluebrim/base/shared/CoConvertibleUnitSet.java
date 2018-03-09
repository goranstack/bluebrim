package com.bluebrim.base.shared;

/**
 * Implemented by objects that represents a set of units that all represents
 * same type of quantity for example a set of length units.
 * 
 * @author Göran Stäck
 */
public interface CoConvertibleUnitSet {
	public boolean contains( String name );
	public CoConvertibleUnit getCurrentUnit();
	public CoConvertibleUnit getUnit( String name );
	public int getViewDecimalCount();
	public double to( double x_baseUnits );
	public double from( double x_units );
	public String getName();
}
