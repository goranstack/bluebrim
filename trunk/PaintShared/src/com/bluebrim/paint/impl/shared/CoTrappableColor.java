package com.bluebrim.paint.impl.shared;

/**
 * Abstract superklass till de f�rger f�r vilka man kan 
 * registrera information om sv�llning.
 * 
 */
public abstract class CoTrappableColor extends com.bluebrim.paint.impl.shared.CoColor implements com.bluebrim.paint.impl.shared.CoTrappableColorIF
{

		protected com.bluebrim.paint.impl.shared.CoTrappingSpec trapping;


public com.bluebrim.paint.shared.CoColorIF deepClone()
{
	CoTrappableColor tColor	= (CoTrappableColor )super.deepClone();

	try
	{
		tColor.trapping			= trapping != null ? (com.bluebrim.paint.impl.shared.CoTrappingSpec )trapping.clone() : null;
	}
	catch ( CloneNotSupportedException ex )
	{
		throw new RuntimeException( trapping.getClass() + ".clone() failed" );
	}

	return tColor;
}
}