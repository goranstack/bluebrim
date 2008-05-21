package com.bluebrim.image.impl.shared;

import java.awt.image.*;

/**
 * Various constants used in imaging.
 *
 * @author Markus Persson 1999-11-25
 */
public interface CoImageConstants {
	public static final double POINTS_PER_CM = 72 / 2.54;
	public static final double POINTS_PER_METER = POINTS_PER_CM * 100;
	public static final double POINTS_PER_INCH = 72.0;

	public final static int IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;
}
