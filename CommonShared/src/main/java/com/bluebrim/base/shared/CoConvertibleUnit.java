
package com.bluebrim.base.shared;

/**
 * Represents a unit that is convertible to other units in the same unit set.
 * For example a length unit is possible to convert to an other length unit. 
 * @author Göran Stäck
 */
public interface CoConvertibleUnit extends CoUnit {
	public String getName();
	public double from( double x_units );
	public double to( double x_baseUnits );
	public float from( float x_units );
	public float to( float x_baseUnits );
}
