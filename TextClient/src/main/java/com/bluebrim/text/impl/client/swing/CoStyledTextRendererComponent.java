package com.bluebrim.text.impl.client.swing;

import java.awt.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.text.shared.swing.*;

/**
 * A non-editable component that uses a <code>CoStyledTextRenderer</code>
 * to render the text.
 * Creation date: (1999-11-01 15:07:57)
 * @author: Lasse S
 */
public class CoStyledTextRendererComponent extends JComponent implements CoHorizontalMarginController.MarginHolder {
	private CoStyledTextRenderer m_renderer;
	private double m_scale = 1.0;
	private int m_margin = 20;

	private CoHorizontalMarginController m_marginController;

	private class OneColumnGeometry extends CoAbstractColumnGeometry {
		private CoColumnGeometryIF.CoColumnIF m_column;

		public OneColumnGeometry(float w, float h) {
			w = w - 2 * m_margin;
			m_column = new CoRectangularColumn(Math.round(m_margin / m_scale), 0, Math.round(w / m_scale), Integer.MAX_VALUE);
		}

		public int getColumnCount() {
			return 1;
		}

		public CoColumnGeometryIF.CoColumnIF getColumn(int index) {
			return m_column;
		}

		public boolean isRectangular() {
			return m_column.isRectangular();
		}
	};

	public CoStyledTextRendererComponent() {
		super();
		m_renderer = new CoStyledTextRenderer() {
			public Container getContainer() {
				return CoStyledTextRendererComponent.this;
			}
		};

		m_renderer.setShowParagraphTags(true);
		m_renderer.setGeometry(new OneColumnGeometry(1000, 100), null, null);

		m_marginController = new CoHorizontalMarginController(this);
	}

	public JComponent getComponent() {
		return this;
	}

	public int getHorizontalMargin() {
		return m_margin;
	}

	public Dimension getPreferredSize() {
		Dimension size = super.getPreferredSize();
		size.height = (int) (m_renderer.getHeight() * m_scale);
		return size;
	}

	public double getScale() {
		return m_scale;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}

		Graphics2D G = (Graphics2D) g;

		G.scale(m_scale, m_scale);
//		G.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
//		G.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		CoScreenPaintable p = CoScreenPaintable.wrap(G);
		m_renderer.paint(p, getBounds());
		p.releaseDelegate();

		G.scale(1.0 / m_scale, 1.0 / m_scale);

		m_marginController.paint(g);
	}

	public void reshape(int x, int y, int w, int h) {
		int W = getWidth();

		super.reshape(x, y, w, h);

		setColumn();

		if (W != w) {
			// width changed -> preferred height changed -> redo layout
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					revalidate();
				}
			});
		}
	}

	private void setColumn() {
		m_renderer.setGeometry(new OneColumnGeometry(getWidth(), getHeight()), null, null);
	}

	public void setDocument(Document doc) {
		m_renderer.setDocument(doc);

		revalidate();
		repaint();
	}

	public void setHorizontalMargin(int m) {
		if (m_margin == m)
			return;

		m_margin = m;
		setColumn();

		revalidate();
		repaint();
	}

	public void setScale(double scale) {
		if (m_scale == scale)
			return;

		m_scale = scale;
		setColumn();

		revalidate();
		repaint();
	}

	public Document getDocument() {
		return m_renderer.getDocument();
	}

	public void refresh() {
		m_renderer.refresh();
	}
}