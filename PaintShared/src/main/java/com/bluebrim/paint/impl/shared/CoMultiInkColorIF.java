package com.bluebrim.paint.impl.shared;

/**
 *
 */
public interface CoMultiInkColorIF extends CoUserDefinedColorIF
{
	public static final String MULTI_INK_COLOR = "multi-ink-color";	
/*
 * Vissa f�rgblandningar �r sv�ra att �terge korrekt p� bildsk�rmen.
 * Om du liksom QXP vill varna f�r detta i anv�ndargr�nssnittet s� kan
 * du anv�nda denna metod. Vilka blandningar som �r sv�ra att �terge
 * vet jag inte men eftersom QXP har en varning s� finns v�l dylika 
 * blandningar. 
 */ 
public String getPreviewWarning( );
}