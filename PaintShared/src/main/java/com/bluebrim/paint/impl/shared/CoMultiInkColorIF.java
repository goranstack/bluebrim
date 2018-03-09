package com.bluebrim.paint.impl.shared;

/**
 *
 */
public interface CoMultiInkColorIF extends CoUserDefinedColorIF
{
	public static final String MULTI_INK_COLOR = "multi-ink-color";	
/*
 * Vissa färgblandningar är svåra att återge korrekt på bildskärmen.
 * Om du liksom QXP vill varna för detta i användargränssnittet så kan
 * du använda denna metod. Vilka blandningar som är svåra att återge
 * vet jag inte men eftersom QXP har en varning så finns väl dylika 
 * blandningar. 
 */ 
public String getPreviewWarning( );
}