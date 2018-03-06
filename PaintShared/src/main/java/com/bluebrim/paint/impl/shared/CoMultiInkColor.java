package com.bluebrim.paint.impl.shared;

/**
 * Anv�ndardefinierade f�rger specificerade med f�rgseparationer i CMYK
 * eller Hexachrome
 * 
 */
public abstract class CoMultiInkColor extends com.bluebrim.paint.impl.shared.CoUserDefinedColor implements com.bluebrim.paint.impl.shared.CoMultiInkColorIF {


public boolean equals( Object o )
{
	return super.equals( o ) && ( o instanceof CoMultiInkColor );
}
/*
 * Vissa f�rgblandningar �r sv�ra att �terge korrekt p� bildsk�rmen.
 * Om du liksom QXP vill varna f�r detta i anv�ndargr�nssnittet s� kan
 * du anv�nda denna metod. Vilka blandningar som �r sv�ra att �terge
 * vet jag inte men eftersom QXP har en varning s� finns v�l dylika 
 * blandningar. 
 */ 
public String getPreviewWarning( ) 
{
	return "";
}
public String getType ( )
{
	return MULTI_INK_COLOR;
}


public com.bluebrim.paint.shared.CoColorIF deepClone()
{
	CoMultiInkColor tColor		= (CoMultiInkColor )super.deepClone();
	return tColor;
}
}