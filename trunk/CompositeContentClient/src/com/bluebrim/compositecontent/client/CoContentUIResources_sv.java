package com.bluebrim.compositecontent.client;

import com.bluebrim.layout.impl.client.*;

public class CoContentUIResources_sv extends CoContentUIResources {
	static final Object[][] contents =
	{

		// CoContentCollectionUI
		{CONTENT_COLLECTION, "Material" },

		// CoContentCollectionEditor
		{CoContentCollectionEditor.NEW_TEXT,			"Ny text"},
		{CoContentCollectionEditor.NEW_IMAGE,			"Ny bild"},
		{CoContentCollectionEditor.NEW_LAYOUT,			"Ny layout"},
		{CoContentCollectionEditor.NEW_WORKPIECE, 		"Nytt alster" },
		{CoContentCollectionEditor.REMOVE,				"Ta bort"},			
		{CoContentCollectionEditor.COPY,				"Kopiera"},
		{CoContentCollectionEditor.PASTE,				"Klistra in"},
			
		// CoLayoutContentUI
		{CoLayoutContentUI.LAYOUT_TAB,					"Layout"},

		// CoWorkPieceUI
		{CoWorkPieceUI.ATOMIC_CONTENT_TAB,				"Material"},


		// CoEditorialNumberSeriesUI
		{WORKPIECE_SERIE,												"Alsternummer"},
		{TEXT_SERIE,													"Textnummer"},
		{IMAGE_SERIE,													"Bildnummer"},
		{LAYOUT_SERIE,													"Layoutnummer"},			
			
		// Probably temporary
		{VISIBLE,				"Synlig"},
		{EDITABLE,				"Ändringsbar"},
		{REQUIRED,				"Nödvändig"},
		{QUICKLIST,				"Snabbval"},
		{QUICKLIST_EDITABLE,	"Ändringsbart snabbval"},
	

			
	};
/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
}