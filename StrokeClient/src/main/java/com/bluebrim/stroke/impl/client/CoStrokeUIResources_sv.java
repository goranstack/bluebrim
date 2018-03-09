package com.bluebrim.stroke.impl.client;

public class CoStrokeUIResources_sv extends CoStrokeUIResources
{
	static final Object[][] contents =
	{
		{CoStrokeCollectionUI.ADD,						"Lägg till"},
		{CoStrokeCollectionUI.ADD_DEFAULT_STROKES,						"Lägg till standardstreck"},
		{CoStrokeCollectionUI.REMOVE,					"Ta bort"},
		{CoStrokeCollectionUI.STROKES,					"Streck och Linjer" },

		// CoStrokeUI
	  { CoStrokeUI.NAME, "Namn" },
	  { CoStrokeUI.ADD_LAYER, "Lägg till" },
	  { CoStrokeUI.DELETE_LAYER, "Ta bort" },

		// CoStrokeLayerUI
	  { CoStrokeLayerUI.DASH_PARAMETERS, "Streckparametrar" },
	  { CoStrokeLayerUI.DASH_WIDTH_PROPORTION, "Proportionell bredd" },
	  { CoStrokeLayerUI.DASH_COLOR, "Färg" },
	  { CoStrokeLayerUI.DASH_COLOR_SHADE, "Skuggning" },
	  { CoStrokeLayerUI.DASH_FOREGROUND_COLOR, "Förgrundsfärg" },
	  { CoStrokeLayerUI.DASH_BACKGROUND_COLOR, "Bakgrundsfärg" },
	  { CoStrokeLayerUI.DASH_NO_COLOR, "Genomskinligt" },

		// CoDashUI
		{CoDashUI.SIZES, "Storlekar"},
		{CoDashUI.SEGMENTS, "Segment"},
		{CoDashUI.DASH_ATTRIBUTES, "Streckattribut"},
		{CoDashUI.NUMBER_OF_SEGMENTS, "Antal segment"},	
		{CoDashUI.CYCLE_LENGTH, "Upprepas var"},
		{CoDashUI.CYCLE_LENGTH_IN_WIDTH, "gånger bredden"},
		{CoDashUI.CYCLE_LENGTH_IN_POINTS, "punkter"},
		{CoDashUI.JOIN_STYLE, "Geringstyp"},		
		{CoDashUI.CAP_STYLE, "Ändpunktstyp"},
		{CoDashUI.JOIN_MITER, "Gering"},
		{CoDashUI.JOIN_ROUND, "Rund"},
		{CoDashUI.JOIN_BEVEL, "Rundade hörn"},
		{CoDashUI.CAP_BUTT, "Hopfogade"},
		{CoDashUI.CAP_ROUND, "Rund"},
		{CoDashUI.CAP_SQUARE, "Projecerad rektangel"},	


	};	

	

/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
}