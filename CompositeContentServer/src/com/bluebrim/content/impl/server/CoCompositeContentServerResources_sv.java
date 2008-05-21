package com.bluebrim.content.impl.server;

import com.bluebrim.content.shared.*;

/**
 * @author Göran Stäck 2002-09-02
 */
public class CoCompositeContentServerResources_sv extends CoCompositeContentServerResources {

	static final Object[][] contents = {
		{CoWorkPieceIF.WORK_PIECE,	"Alster"},
		{UNTITLED_WORKPIECE,	"Nytt alster"},
	};


	public Object[][] getContents ( ) {
		return contents;
	}


}
