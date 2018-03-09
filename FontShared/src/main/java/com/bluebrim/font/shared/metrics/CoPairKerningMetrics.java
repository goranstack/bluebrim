package com.bluebrim.font.shared.metrics;
import java.io.*;

import com.bluebrim.font.impl.shared.metrics.*;

/**
 * Interface for pair kerning metrics. Pair kerning is the implementation of the typographic knowledge
 * that certain glyphs, when placed next to each other, need to be kerned more tightely to look good.
 * E.g., "A" followed by "V" usually needs to be kerned together to look good. This would give the
 * char couple ("A", "V") a negative pair kernining value.<p>
 *
 * The kerning values are given for the unscaled font face, i.e. the value is given for the font at size 1 point.
 * To be useful at any other font size, it must be scaled to that particular font size.
 *
 * <p><b>Creation date:</b> 2001-04-12
 * <br><b>Documentation last updated:</b> 2001-10-04
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoPairKerningMetricsImplementation
 * @see com.bluebrim.font.shared.metrics.CoFontMetricsData
 */
public interface CoPairKerningMetrics extends Serializable, com.bluebrim.xml.shared.CoXmlEnabledIF {
	public final static String XML_TAG = "pair-kerning-metrics";

/**
 * Return the pair kerning delta for the two glyphs corresponding to the given Unicode chars.
 * The tracking is returned in points (1/72 inch). Negative values
 * indicates that glyphs should be moved closer together, positive values (unusual) indicates that the glyphs should
 * be moved further apart.
 *
 * @param ch1 The unicode character for the first glyph.
 * @param ch2 The unicode character for the second glyph.
 *
 * @return The pair kerning value for these two characters.
 */
public float getPairKerning(char ch1, char ch2);
}