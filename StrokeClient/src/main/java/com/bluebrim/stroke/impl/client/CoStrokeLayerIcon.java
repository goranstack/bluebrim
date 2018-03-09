package com.bluebrim.stroke.impl.client;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;

import javax.swing.Icon;

import com.bluebrim.gui.client.CoListCellRenderer;
import com.bluebrim.paint.shared.CoColorIF;
import com.bluebrim.stroke.shared.CoStrokeLayerIF;
import com.bluebrim.stroke.shared.CoStrokePropertiesIF;

class CoStrokeLayerIcon extends CoListCellRenderer implements Icon {
	private CoStrokeLayerIF m_strokeLayer;
	private CoStrokePropertiesIF m_strokeProperties;
	private int m_height;
	private int m_width;
	private Shape m_shape;

	public CoStrokeLayerIcon(CoStrokeLayerIF strokeLayer, int w, int h, CoStrokePropertiesIF strokeProperties) {
		m_strokeProperties = strokeProperties;
		m_strokeLayer = strokeLayer;

		m_width = w;
		m_height = h;

		m_shape = new Line2D.Double(0, m_height / 2, m_width, m_height / 2);
	}

	public int getIconHeight() {
		return m_height;
	}

	public int getIconWidth() {
		return m_width;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g.create();

		if (m_strokeLayer != null) {
			CoColorIF color = m_strokeLayer.getColor().getDashColor(m_strokeProperties);
			//		CoColorShadeIF cs = m_strokeLayer.getColorSpec().getDashColorShade( m_strokeProperties );
			if (color != null) {
				float w = m_strokeLayer.getWidthProportion() * m_strokeProperties.getWidth();
				if (w < 1)
					w = 1;
				g2.setStroke(m_strokeLayer.getDash().createStroke(w, 0));
				//			g2.setPaint( color.getShadedPreviewColor( m_strokeLayer.getColorSpec().getDashShade( m_strokeProperties ) ) );
				g2.setPaint(color.getShadedPreviewColor(m_strokeLayer.getColor().getDashShade(m_strokeProperties)));

				g2.translate(x, y);
				g2.draw(m_shape);
				g2.translate(-x, -y);
			}
		}

		g2.dispose();
	}
}