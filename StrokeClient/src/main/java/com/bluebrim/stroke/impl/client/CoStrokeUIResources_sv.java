package com.bluebrim.stroke.impl.client;

public class CoStrokeUIResources_sv extends CoStrokeUIResources
{
	static final Object[][] contents =
	{
		{CoStrokeCollectionUI.ADD,						"L�gg till"},
		{CoStrokeCollectionUI.ADD_DEFAULT_STROKES,						"L�gg till standardstreck"},
		{CoStrokeCollectionUI.REMOVE,					"Ta bort"},
		{CoStrokeCollectionUI.STROKES,					"Streck och Linjer" },

		// CoStrokeUI
	  { CoStrokeUI.NAME, "Namn" },
	  { CoStrokeUI.ADD_LAYER, "L�gg till" },
	  { CoStrokeUI.DELETE_LAYER, "Ta bort" },

		// CoStrokeLayerUI
	  { CoStrokeLayerUI.DASH_PARAMETERS, "Streckparametrar" },
	  { CoStrokeLayerUI.DASH_WIDTH_PROPORTION, "Proportionell bredd" },
	  { CoStrokeLayerUI.DASH_COLOR, "F�rg" },
	  { CoStrokeLayerUI.DASH_COLOR_SHADE, "Skuggning" },
	  { CoStrokeLayerUI.DASH_FOREGROUND_COLOR, "F�rgrundsf�rg" },
	  { CoStrokeLayerUI.DASH_BACKGROUND_COLOR, "Bakgrundsf�rg" },
	  { CoStrokeLayerUI.DASH_NO_COLOR, "Genomskinligt" },

		// CoDashUI
		{CoDashUI.SIZES, "Storlekar"},
		{CoDashUI.SEGMENTS, "Segment"},
		{CoDashUI.DASH_ATTRIBUTES, "Streckattribut"},
		{CoDashUI.NUMBER_OF_SEGMENTS, "Antal segment"},	
		{CoDashUI.CYCLE_LENGTH, "Upprepas var"},
		{CoDashUI.CYCLE_LENGTH_IN_WIDTH, "g�nger bredden"},
		{CoDashUI.CYCLE_LENGTH_IN_POINTS, "punkter"},
		{CoDashUI.JOIN_STYLE, "Geringstyp"},		
		{CoDashUI.CAP_STYLE, "�ndpunktstyp"},
		{CoDashUI.JOIN_MITER, "Gering"},
		{CoDashUI.JOIN_ROUND, "Rund"},
		{CoDashUI.JOIN_BEVEL, "Rundade h�rn"},
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