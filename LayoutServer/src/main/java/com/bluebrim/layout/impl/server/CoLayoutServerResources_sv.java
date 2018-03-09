package com.bluebrim.layout.impl.server;

import com.bluebrim.layout.shared.*;

/**
 * @author Göran Stäck 2002-09-02
 */
public class CoLayoutServerResources_sv extends CoLayoutServerResources {

	static final Object[][] contents = {
		{CoLayoutContentIF.LAYOUT_CONTENT,	"Layout"},	        
		{UNTITLED_LAYOUT,	"Ny layout"},
		{WRAPPED_IMAGE_CONTENT,	"Bildblock"},
		{WRAPPED_TEXT_CONTENT,	"Textblock"},
		{WRAPPED_LAYOUT_CONTENT,	"Layoutblock"},
		{WRAPPED_WORKPIECE_TEXT_CONTENT,	"Projicerande textblock"},

		{TEXT_BOX_PROTOTYPE,	"Textblockprototyp"},
		{IMAGE_BOX_PROTOTYPE,	"Bildblockprototyp"},
		{CAPTION_BOX_PROTOTYPE,	"Bildtextblockprototyp"},
		{LAYOUT_AREA_PROTOTYPE,	"Layoutytaprototyp"}

	};


	public Object[][] getContents ( ) {
		return contents;
	}


}
