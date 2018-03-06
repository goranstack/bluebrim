package com.bluebrim.base.shared;

import java.awt.geom.*;
import java.io.*;

public class CoRectangle2DFloat extends Rectangle2D implements Serializable {
	public float x;
	public float y;
	public float width;
	public float height;
	public CoRectangle2DFloat() {
		this(0, 0, 0, 0);
	}
	public CoRectangle2DFloat(double x, double y, double w, double h) {
		setRect(x, y, w, h);
	}
	public java.awt.geom.Rectangle2D createIntersection(Rectangle2D r) {
		Rectangle2D dest = new CoRectangle2DFloat();
		Rectangle2D.intersect(this, r, dest);
		return dest;
	}
	public java.awt.geom.Rectangle2D createUnion(Rectangle2D r) {
		Rectangle2D dest = new CoRectangle2DFloat();
		Rectangle2D.union(this, r, dest);
		return dest;
	}
	public double getHeight() {
		return height;
	}
	public double getWidth() {
		return width;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public boolean isEmpty() {
		return (width <= 0.0) || (height <= 0.0);
	}
	public int outcode(double x, double y) {
		int out = 0;
		if (this.width <= 0) {
			out |= OUT_LEFT | OUT_RIGHT;
		} else if (x < this.x) {
			out |= OUT_LEFT;
		} else if (x > this.x + this.width) {
			out |= OUT_RIGHT;
		}

		if (this.height <= 0) {
			out |= OUT_TOP | OUT_BOTTOM;
		} else if (y < this.y) {
			out |= OUT_TOP;
		} else if (y > this.y + this.height) {
			out |= OUT_BOTTOM;
		}

		return out;
	}
	public void setRect(double x, double y, double w, double h) {
		this.x = (float) x;
		this.y = (float) y;
		this.width = (float) w;
		this.height = (float) h;
	}
}