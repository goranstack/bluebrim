package com.bluebrim.stroke.shared;
import com.bluebrim.base.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Interface for a serializable and immutable stroke properties object
 * 
 * @author: Dennis Malmström
 */

public interface CoImmutableStrokePropertiesIF extends Cloneable, CoFactoryElementIF, java.io.Serializable {
	public static String STROKE_PROPERTIES = "stroke_properties";
	public static int ALIGN_CENTER = 0;
	public static int ALIGN_INSIDE = 1;
	public static int ALIGN_OUTSIDE = 2;
	public static int ALIGN_BY_OFFSET = 3;

	public CoStrokePropertiesIF deepClone();

	public int getAlignment();

	public float getAlignOffset();

	public float getBackgroundShade();

	public float getForegroundShade();

	public float getInsideWidth();

	public float getOutsideWidth();

	public CoStrokeIF getStroke();

	public float getWidth();

	public String getSymmetry();

	public CoColorIF getBackgroundColor();

	public CoRef getBackgroundColorId();

	public CoColorIF getForegroundColor();

	public CoRef getForegroundColorId();

}