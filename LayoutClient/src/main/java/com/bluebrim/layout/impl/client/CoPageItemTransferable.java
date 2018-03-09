package com.bluebrim.layout.impl.client;
import java.awt.datatransfer.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;

/**
 *
 *
 * @author: Dennis
 */
public class CoPageItemTransferable implements Transferable, ClipboardOwner {
	public static final DataFlavor FLAVOR = CoDataTransferKit.domainFlavor(CoPageItemTransferable.class, null);

	private DataFlavor[] m_flavors = { DataFlavor.stringFlavor, DataFlavor.plainTextFlavor, FLAVOR };

	private List m_pageItems = new ArrayList(); // [ CoShapePageItemIF ]
	private Rectangle2D m_bounds;

	public CoPageItemTransferable(Collection pageitems) {
		this(pageitems.iterator());
	}

	public CoPageItemTransferable(Iterator pageitems) {
		double x0 = Double.MAX_VALUE;
		double y0 = Double.MAX_VALUE;
		double x1 = Double.MIN_VALUE;
		double y1 = Double.MIN_VALUE;

		Point2D p = new Point2D.Double();

		while (pageitems.hasNext()) {
			CoShapePageItemIF pi = null;

			Object o = pageitems.next();
			if (o instanceof CoShapePageItemIF) {
				pi = (CoShapePageItemIF) o;
				if (pi.isSlave())
					continue;

				pi = (CoShapePageItemIF) pi.deepClone();
				m_pageItems.add(pi);
			} else if (o instanceof CoShapePageItemView) {
				CoShapePageItemView v = (CoShapePageItemView) o;
				if (v.isSlave())
					continue;

				p.setLocation(0, 0);
				v.transformToGlobal(p);
				pi = (CoShapePageItemIF) v.getPageItem().deepClone();
				pi.setPosition(p.getX(), p.getY());
				m_pageItems.add(pi);
			} else {
				continue;
			}

			double x = pi.getX();
			double y = pi.getY();
			CoImmutableShapeIF s = pi.getCoShape();
			double w = s.getWidth();
			double h = s.getHeight();

			x0 = Math.min(x0, x);
			y0 = Math.min(y0, y);
			x1 = Math.max(x1, x + w);
			y1 = Math.max(y1, y + h);
		}

		m_bounds = new Rectangle2D.Double(x0, y0, x1 - x0, y1 - y0);
	}

	public Rectangle2D getBounds() {
		return m_bounds;
	}

	public List getPageItems() {
		return m_pageItems;
	}

	public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if (flavor.equals(FLAVOR)) {
			return this;
		} else if (flavor.equals(DataFlavor.stringFlavor)) {
			StringBuffer s = new StringBuffer();
			Iterator i = m_pageItems.iterator();
			while (i.hasNext()) {
				s.append(((CoShapePageItemIF) i.next()).toString());
				s.append("\n");
			}

			return s.toString();
		} else if (flavor.equals(DataFlavor.plainTextFlavor)) {
			StringBuffer s = new StringBuffer();
			Iterator i = m_pageItems.iterator();
			while (i.hasNext()) {
				s.append(((CoShapePageItemIF) i.next()).toString());
				s.append("\n");
			}
			return new StringReader(s.toString());
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}

	public synchronized DataFlavor[] getTransferDataFlavors() {
		return m_flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(FLAVOR);
	}

	public void lostOwnership(Clipboard clipboard, Transferable contents) {
	}
}