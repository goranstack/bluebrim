package com.bluebrim.base.shared;

import java.awt.*;
import java.awt.geom.*;

public interface CoImmutableTransformIF extends Cloneable, CoFactoryElementIF, java.io.Serializable {
	public void applyOn(AffineTransform transform);

	public void applyOn(Graphics2D g2d);

	public void applyOn(CoPaintable g2d);

	public AffineTransform createAffineTransform();

	public AffineTransform createInverseAffineTransform();

	public CoTransformIF deepClone();

	public double getRotation();

	public double getRotationPointX();

	public double getRotationPointY();

	boolean isIdentity();

	void transform(Point2D p);

	public void unapplyOn(Graphics2D g2d);

	public void unapplyOn(CoPaintable g2d);

	void untransform(Point2D p);
}