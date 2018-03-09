package com.bluebrim.image.impl.server;

import com.bluebrim.image.shared.*;

/**
 * @author Göran Stäck 2002-09-02
 */
public class CoImageServerResources_sv extends CoImageServerResources {

	static final Object[][] contents = {
		{CoImageContentIF.IMAGE_CONTENT,	"Bild"},
		{UNTITLED_IMAGE,	"Ny bild"},
	};


	public Object[][] getContents ( ) {
		return contents;
	}


}
