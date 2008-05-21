package com.bluebrim.image.server;

import java.awt.geom.*;
import java.awt.image.*;

import com.bluebrim.base.shared.*;

/**
 * Wraps an EPS image
 * 
 * @author Göran Stäck 2003-04-04
 */
public class CoEpsImage extends CoAbstractImage {
	private byte[] m_postScript;
	private BufferedImage m_preview;
	private Rectangle2D m_boundingBox;


	public void draw(CoPaintable canvas) {		
		canvas.drawBufferedImage(getPreview());
	}
		
	public BufferedImage getPreview() {
		return m_preview;
	}
	
	public byte[] getPostScript() {
		return m_postScript;
	}
	
	public Rectangle2D getBoundingBox() {
		return m_boundingBox;
	}
	
	public int getWidth() {
		return (int) m_boundingBox.getWidth();
	}

	public int getHeight() {
		return (int) m_boundingBox.getHeight();
	}

}
