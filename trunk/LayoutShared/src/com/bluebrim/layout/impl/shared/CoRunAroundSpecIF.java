package com.bluebrim.layout.impl.shared;


/**
 * Interface for a serializable and mutable run around specification.
 * 
 * @author: Dennis Malmström
 */

public interface CoRunAroundSpecIF extends CoImmutableRunAroundSpecIF
{
	public interface Owner
	{
		void runAroundSpecChanged();
	}
public CoRunAroundSpecIF getMutableProxy( CoRunAroundSpecIF.Owner owner );

public void setUseStroke( boolean b );
}