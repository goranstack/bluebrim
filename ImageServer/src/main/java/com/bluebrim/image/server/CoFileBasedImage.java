package com.bluebrim.image.server;

import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Represents an image that is stored in a file. 
 * 
 * @author Göran Stäck 2003-04-04
 */
public class CoFileBasedImage extends CoAbstractImage {
	private BufferedImage m_image;


	public CoFileBasedImage(File file, BufferedImage image) throws IOException {
		super(file);
		m_image = image;
	}
	
	public CoFileBasedImage(File file) throws IOException {
		super(file);
		m_image = ImageIO.read(file);
	}
	
	public void draw(CoPaintable canvas) {
		canvas.drawBufferedImage(m_image);
	}

	public int getWidth() {
		return m_image.getWidth();
	}

	public int getHeight() {
		return m_image.getHeight();
	}
	
	public void xmlVisit(CoXmlVisitorIF visitor) {
		visitor.exportAttachementFile(m_imageFile, "image");
	}


}
