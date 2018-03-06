package com.bluebrim.layoutmanager.test;

import java.awt.geom.*;

import com.bluebrim.layoutmanager.*;

/**
 * Insert the type's description here.
 * Creation date: (2000-09-01 10:25:29)
 * @author: 
 */
public class CoRectClass {
	private int align;
	public Rectangle2D rect;
	/**
	 * CoRectClass constructor comment.
	 */
	public CoRectClass() {
		super();

	}
	/**
	 * Insert the method's description here. Creation date: (2000-09-01
	 * 10:28:25)
	 * 
	 * @param r
	 *            java.awt.geom.Rectangle2D
	 */
	public CoRectClass(Rectangle2D r) {
		super();
		rect = r;
		align = CoCornerUtilities.BOTTOM_RIGHT;
	}
	/**
	 * Insert the method's description here. Creation date: (2000-09-01
	 * 10:27:36)
	 * 
	 * @param r
	 *            java.awt.geom.Rectangle2D
	 * @param a
	 *            int
	 */
	public CoRectClass(Rectangle2D r, int a) {
		super();
		align = a;
		rect = r;
	}
	/**
	 * Insert the method's description here. Creation date: (2000-09-01
	 * 10:26:05)
	 * 
	 * @return int
	 */
	public int getAlign() {
		return align;
	}
	/**
	 * Insert the method's description here. Creation date: (2000-09-01
	 * 10:26:26)
	 * 
	 * @return java.awt.geom.Rectangle2D
	 */
	public java.awt.geom.Rectangle2D getRect() {
		return rect;
	}
	/**
	 * Insert the method's description here. Creation date: (2000-09-01
	 * 10:26:05)
	 * 
	 * @param newAlign
	 *            int
	 */
	public void setAlign(int newAlign) {
		align = newAlign;
	}
	/**
	 * Insert the method's description here. Creation date: (2000-09-01
	 * 10:26:26)
	 * 
	 * @param newRect
	 *            java.awt.geom.Rectangle2D
	 */
	public void setRect(java.awt.geom.Rectangle2D newRect) {
		rect = newRect;
	}
}