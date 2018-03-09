package com.bluebrim.image.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Implemented by objects that wrapps different kind of images. A buffered image
 * and an Encapsulated PostScript image is treated wery differntly when drawing 
 * on the screen and drawing on a Postscript device. This interface enables use
 * of the double dispatching pattern to control how different kind of images are
 * drawn on different kind of output devices.
 * 
 * @author Göran Stäck 2003-04-04
 *
 */
public interface CoImage {
	void draw(CoPaintable canvas);
	
	int getWidth();
	
	int getHeight();
	
	void xmlVisit(CoXmlVisitorIF visitor);
}
