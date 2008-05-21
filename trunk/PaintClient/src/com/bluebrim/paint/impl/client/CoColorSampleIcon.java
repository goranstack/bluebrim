package com.bluebrim.paint.impl.client;
import java.awt.*;

import com.bluebrim.paint.shared.*;

/**
 * Instanser av denna klass anv�nds d� anv�ndargr�nssnittet skall visa upp
 * f�rger som sm� f�rgade rektanglar. T ex en kombobox d�r varje val �r en 
 * f�rgad ruta + en text med f�rgens namn.
 * 
 */
public class CoColorSampleIcon implements javax.swing.Icon {
	protected Dimension m_size;
	protected Color m_color;
	protected Color m_fallbackColor;

	public int getIconHeight() {
		return m_size.height;
	}

	public int getIconWidth() {
		return m_size.width;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Color color = null;
		if (m_color != null) {
			color = m_color; //.getPreviewColor();
		} else {
			color = m_fallbackColor;
		}

		if (color == null)
			return;

		Color oldColor = g.getColor();
		g.setColor(color);
		g.fill3DRect(x, y, getIconWidth(), getIconHeight(), true);
		g.setColor(oldColor);
	}

	public CoColorSampleIcon(CoColorIF c) {
		this(c, (Color) null);
	}

	/**
	 * Skapar en CoColoredSquare som visar f�rgen hos CoColorIF i argumentet
	 */
	public CoColorSampleIcon(CoColorIF color, Color fallbackColor) {
		this(color, fallbackColor, new Dimension(16, 16));
	}

	public CoColorSampleIcon(CoColorIF c, Color fallbackColor, Dimension size) {
		setColor(c);
		m_size = size;
		m_fallbackColor = fallbackColor;
	}

	public CoColorSampleIcon(CoColorIF c, Dimension size) {
		this(c, null, size);
	}

	public void setColor(CoColorIF c) {

		m_color = (c == null) ? null : c.getPreviewColor();
	}
}