package com.bluebrim.font.shared.metrics;
import java.io.*;

/**
 * Interface for track kerning, a.k.a. tracking, metrics. Tracking is a measure of how much to kern
 * glyphs in a text differently depending on font size. Normally, the kerning between glyphs can not
 * scale linearly with font size, since that would leave the text looking too dispersed. The
 * tracking (often specified as a graph with kerning measure depending on point size, 
 * the "tracking curve") is normally a negative number, getting absolutely larger with increasing
 * point size.
 *
 * <p><b>Creation date:</b> 2001-04-12
 * <br><b>Documentation last updated:</b> 2001-10-04
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public interface CoTrackingMetrics extends Serializable, com.bluebrim.xml.shared.CoXmlEnabledIF {
/**
 * Return the tracking for the specified font size. The tracking is returned in points (1/72 inch). Negative values
 * indicates that glyphs should be moved closer together, positive values (unusual) indicates that the glyphs should
 * be moved further apart.
 *
 * @param fontSize the font size to get tracking for.
 *
 * @return the track kerning value for the font face at this font size, in points.
 */
public float getTracking(float fontSize);
}