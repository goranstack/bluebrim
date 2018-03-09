package com.bluebrim.image.impl.client;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import javax.swing.*;

/**
 * @author Markus Persson
 */
public class CoRenderedImageIcon implements Icon {
	private Dimension m_size;
	private int m_width;
	private int m_height;
	private RenderedImage m_image;
public CoRenderedImageIcon(int w, int h) {
	this(null, w, h);
}
public CoRenderedImageIcon(RenderedImage image, int w, int h) {
	m_image = image;
	m_width = w;
	m_height = h;
}
public int getIconHeight() {
	return m_height;
}
public int getIconWidth() {
	return m_width;
}
public void paintIcon(Component c, Graphics g, int x, int y) {
	if (m_image != null) {
		Graphics2D g2d = (Graphics2D)g;

		double sx = ((double)m_width) / m_image.getWidth();
		double sy = ((double)m_height) / m_image.getHeight();
		double s = Math.min(1.0, Math.min(sx, sy));
		
		double ex = m_width - m_image.getWidth()*s;
		double ey = m_height - m_image.getHeight()*s;
		AffineTransform transform = AffineTransform.getTranslateInstance(x + ex/2, y + ey/2);
		transform.scale(s, s);
		g2d.drawRenderedImage(m_image, transform);
	}
}
public void setImage(RenderedImage image) {
	m_image = image;
}
}
