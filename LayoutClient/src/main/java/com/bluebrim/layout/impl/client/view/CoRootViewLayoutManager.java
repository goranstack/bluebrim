package com.bluebrim.layout.impl.client.view;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * 
 * Creation date: (2001-10-04 09:46:05)
 * @author: Dennis
 */
public abstract class CoRootViewLayoutManager {
	protected Insets m_insets = new Insets(50, 100, 50, 100);
	protected int m_spacing = 100;

	protected abstract Dimension2D doLayout(CoRootView target);

	public Insets getInsets() {
		return m_insets;
	}

	public final Dimension2D layout(CoRootView target) {
		Dimension2D targetSize = doLayout(target);

		if (target.getDoShowEntireDesktop()) {
			double w = targetSize.getWidth();
			double h = targetSize.getHeight();

			Iterator e = target.getCompositePageItem().getChildren().iterator();
			while (e.hasNext()) {
				CoShapePageItemIF pi = (CoShapePageItemIF) e.next();
				CoImmutableShapeIF s = pi.getCoShape();
				w = Math.max(w, pi.getX() + s.getWidth());
				h = Math.max(h, pi.getY() + s.getHeight());
			}

			targetSize.setSize(w, h);
		}

		return targetSize;

	}
}
