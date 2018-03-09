package com.bluebrim.content.shared;

import com.bluebrim.image.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Implemented by objects that can deliver content by key.
 * 
 * @author Göran Stäck 2002-09-15
 *
 */
public interface CoContentRegistry {
	public CoLayoutContentIF lookupLayoutContent(CoGOI goi);
	public CoImageContentIF lookupImageContent(CoGOI goi);
	public CoTextContentIF lookupTextContent(CoGOI goi);
	public CoWorkPieceIF lookupWorkPiece(CoGOI goi);

}
