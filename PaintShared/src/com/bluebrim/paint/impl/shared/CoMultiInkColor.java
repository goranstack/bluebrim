package com.bluebrim.paint.impl.shared;

/**
 * Användardefinierade färger specificerade med färgseparationer i CMYK
 * eller Hexachrome
 * 
 */
public abstract class CoMultiInkColor extends com.bluebrim.paint.impl.shared.CoUserDefinedColor implements com.bluebrim.paint.impl.shared.CoMultiInkColorIF {


public boolean equals( Object o )
{
	return super.equals( o ) && ( o instanceof CoMultiInkColor );
}
/*
 * Vissa färgblandningar är svåra att återge korrekt på bildskärmen.
 * Om du liksom QXP vill varna för detta i användargränssnittet så kan
 * du använda denna metod. Vilka blandningar som är svåra att återge
 * vet jag inte men eftersom QXP har en varning så finns väl dylika 
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