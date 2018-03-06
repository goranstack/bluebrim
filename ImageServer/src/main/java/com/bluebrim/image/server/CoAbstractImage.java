package com.bluebrim.image.server;

import java.io.*;

import com.bluebrim.image.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Abstract superclass to objects that wrapps different kind of images.
 * 
 * @author Göran Stäck 2003-04-04
 */
public abstract class CoAbstractImage implements CoImage {

	protected File m_imageFile; 	// the file containing the image

	protected CoAbstractImage() {
	}

	protected CoAbstractImage(File file) {
		m_imageFile = file;
	}

	public void write(OutputStream stream) throws IOException {

	}

	public void xmlVisit(CoXmlVisitorIF visitor) {
		visitor.exportAttachementFile(m_imageFile, "image");
	}

}
