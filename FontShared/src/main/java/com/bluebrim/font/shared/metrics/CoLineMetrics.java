package com.bluebrim.font.shared.metrics;
import java.io.*;

/**
 * Interface for font line metrics. The line metrics of a font is the metrics associated with the vertical distances,
 * in opposite to other metrics (advance, pair kerning and tracking) which is associated with the horizontal distances.
 *
 * <p><b>Documentation last updated:</b> 2001-10-05
 *
 * @author Dennis
 * @author Magnus Ihse (magnus.ihse@appeal.se) (2001-05-03)
 *
 * @see com.bluebrim.font.shared.metrics.CoFontMetricsData
 */
public interface CoLineMetrics extends Serializable, com.bluebrim.xml.shared.CoXmlEnabledIF {
	public final static String XML_TAG = "line-metrics";
	public final static String XML_ASCENT = "ascent";
	public final static String XML_DESCENT = "descent";
	public final static String XML_HEIGHT = "height";
	public final static String XML_LEADING = "leading";
	public final static String XML_STRIKETHROUGH_OFFSET = "strikethrough-offset";
	public final static String XML_STRIKETHROUGH_THICKNESS = "strikethrough-thickness";
	public final static String XML_UNDERLINE_OFFSET = "underline-offset";
	public final static String XML_UNDERLINE_THICKNESS = "underline-thickness";
	public final static String XML_X_HEIGHT = "x-height";
	public final static String XML_CAP_HEIGHT = "cap-height";
/**
 * Returns the ascent of the font face.  The ascent is the distance from the baseline to the ascender line.
 * The ascent usually represents the the height of the capital letters of the text. Some characters
 * can extend above the ascender line. The ascent is given in points (1/72 inch).
 *
 * @return the ascent of the font face, in points.
 */
public float getAscent();

/**
 * Returns the descent of the font face.  The descent is the distance from the baseline to the descender line.
 * The descent usually represents the distance to the bottom of lower case letters like p'.  
 * Some characters can extend below the descender line. The descent is returned as points (1/72 inch).
 *
 * @return the descent of the font face, in points.
 */
public float getDescent();

/**
 * Returns the height of the font face. The height is per definition equal to the sum of the ascent, the
 * descent and the leading. The height is returned as points (1/72 inch).
 *
 * @return the height of the font face, in points.
 */
public float getHeight();

/**
 * Returns the leading of the font face. The leading is the recommended distance from the bottom of the 
 * descender line to the top of the next line. The leading is returned as points (1/72 inch).
 *
 * @return the leading of the font face, in points.
 */
public float getLeading();

/**
 * Returns the position of the strike-through line 
 * relative to the baseline.
 * @return the position of the strike-through line.
 */
public float getStrikethroughOffset();

public float getStrikethroughThickness();

public float getUnderlineOffset();

public float getUnderlineThickness();

/**
 * Returns the "Cap height", i.e. the heigh of a normal <b>upper case</b> capital letter in the font,
 * typically "H".
 * Creation date: (2001-05-10 13:30:16)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */

public float getCapHeight();

/**
 * Returns the "x height", i.e. the heigh of a normal <b>lower case</b> letter without ascenders in the font,
 * typically "x".
 * Creation date: (2001-05-10 13:30:16)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */

public float getXHeight();
}