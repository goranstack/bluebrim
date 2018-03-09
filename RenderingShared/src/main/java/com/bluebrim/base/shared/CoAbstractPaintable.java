package com.bluebrim.base.shared;

import java.awt.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-08-07 13:41:16)
 * @author: Dennis
 */

public abstract class CoAbstractPaintable implements CoPaintable {
	// Should not be used like this. /Magnus Ihse
	//	public final static AffineTransform IDENTITY_TRANSFORM = new AffineTransform();

	public CoAbstractPaintable() {
		super();
	}

	public Color getColor() {
		Paint paint = getPaint();
		if (paint instanceof Color) {
			return (Color) paint;
		} else {
			return null;
		}
	}
	// We shouldn't really use both Color and Paint...
	public void setColor(Color color) {
		setPaint(color);
	}
}