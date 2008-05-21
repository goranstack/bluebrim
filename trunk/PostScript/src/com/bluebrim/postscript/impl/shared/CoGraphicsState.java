package com.bluebrim.postscript.impl.shared;

import java.awt.*;
import java.awt.geom.*;

/**
 * A simple model (actually, a subset) of the Postscript graphics state, i.e. the state that is saved with gsave.
 * This is not complete, but it is good enough for our purpose: to keep track of the Postscript printer's
 * current state, so we don't send unneccesary state change code.
 *
 * <p><b>Creation date:</b> 2001-06-29
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public final class CoGraphicsState implements Cloneable {
	private Paint m_paint;
	private Stroke m_stroke;
	private com.bluebrim.font.shared.CoFont m_font;
	private AffineTransform m_transform;
public CoGraphicsState() {
}

public com.bluebrim.font.shared.CoFont getFont() {
	return m_font;
}


public Paint getPaint() {
	return m_paint;
}


public Stroke getStroke() {
	return m_stroke;
}


public void setFont(com.bluebrim.font.shared.CoFont font) {
	m_font = font;
}


public void setPaint(Paint paint) {
	m_paint = paint;
}

public void setStroke(Stroke stroke) {
	m_stroke = stroke;
}

// Redefine clone to be public
public Object clone() throws CloneNotSupportedException {
	return super.clone();
}


public String toString() {
	return getClass().getName() + "[font=" + getFont() + ", paint=" + getPaint() + ", stroke=" + getStroke() + "]";
}


public CoGraphicsState(Paint paint, Stroke stroke, com.bluebrim.font.shared.CoFont font) {
	m_paint = paint;
	m_stroke = stroke;
	m_font = font;
}
}