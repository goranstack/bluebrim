package com.bluebrim.text.shared.swing;

import java.io.*;

/**
 * Floating point version of javax.swing.SizeRequirements
 * 
 * @author: Dennis Malmström
 */

public class CoSizeRequirements implements Serializable
{
	public float m_minimum;
	public float m_preferred;
	public float m_maximum;

	public float m_alignment;

public CoSizeRequirements()
{
	m_minimum = 0;
	m_preferred = 0;
	m_maximum = 0;
	m_alignment = 0.5f;
}
public CoSizeRequirements(float min, float pref, float max, float a)
{
	m_minimum = min;
	m_preferred = pref;
	m_maximum = max;
	m_alignment = a > 1.0f ? 1.0f : a < 0.0f ? 0.0f : a;
}
public CoSizeRequirements(int min, int pref, int max, float a)
{
	System.err.println( "KNAS: CoSizeRequirements.CoSizeRequirements( int , int , int , float ) called." );
	Thread.dumpStack();
}
/**
 * Creates a bunch of offset/span pairs specifying how to
 * lay out a set of components with the specified alignments.
 * The resulting span allocations will overlap, with each one
 * fitting as well as possible into the given total allocation.
 * This method requires that you specify
 * the total amount of space to be allocated,
 * the size requirements for each component to be placed
 * (specified as an array of SizeRequirements), and
 * the total size requirements of the set of components
 * (only the alignment field of which is actually used).
 * You can get the total size requirement by invoking
 * getAlignedSizeRequirements.
 *
 * @param allocated the total span to be allocated >= 0.
 * @param total     the total of the children requests.
 * @param children  the size requirements for each component.
 * @param offsets   the offset from 0 for each child where
 *   the spans were allocated (determines placement of the span).
 * @param spans     the span allocated for each child to make the
 *   total target span.
 */
public static void calculateAlignedPositions( float allocated,
	                                            CoSizeRequirements total,
	                                            CoSizeRequirements[] children,
	                                            float[] offsets,
	                                            float[] spans)
{
	float totalAscent = allocated * total.m_alignment;
	float totalDescent = allocated - totalAscent;
	for
		(int i = 0; i < children.length; i++)
	{
		CoSizeRequirements req = children[i];
		float maxAscent = req.m_maximum * req.m_alignment;
		float maxDescent = req.m_maximum - maxAscent;
		float ascent = Math.min( totalAscent, maxAscent );
		float descent = Math.min( totalDescent, maxDescent );
		offsets[i] = totalAscent - ascent;
		spans[i] = Math.min( ascent + descent, Integer.MAX_VALUE );
	}
}
/**
 * Creates a bunch of offset/span pairs representing how to
 * lay out a set of components end-to-end.
 * This method requires that you specify
 * the total amount of space to be allocated,
 * the size requirements for each component to be placed
 * (specified as an array of SizeRequirements), and
 * the total size requirement of the set of components.
 * You can get the total size requirement
 * by invoking the getTiledSizeRequirements method.
 *
 * @param allocated the total span to be allocated >= 0.
 * @param total     the total of the children requests.  This argument
 *  is optional and may be null.
 * @param children  the size requirements for each component.
 * @param offsets   the offset from 0 for each child where
 *   the spans were allocated (determines placement of the span).
 * @param spans     the span allocated for each child to make the
 *   total target span.
 */
public static void calculateTiledPositions( float allocated,
	                                          CoSizeRequirements total,
	                                          CoSizeRequirements[] children,
	                                          float[] offsets,
	                                          float[] spans )
{
	// The total argument turns out to be a bad idea since the
	// total of all the children can overflow the integer used to
	// hold the total.  The total must therefore be calculated and
	// stored in long variables.
	float min = 0;
	float pref = 0;
	float max = 0;
	for
		(int i = 0; i < children.length; i++)
	{
		min += children[i].m_minimum;
		pref += children[i].m_preferred;
		max += children[i].m_maximum;
	}
	if
		(allocated >= pref)
	{
		expandedTile(allocated, min, pref, max, children, offsets, spans);
	} else {
		compressedTile(allocated, min, pref, max, children, offsets, spans);
	}
}
private static void compressedTile( float allocated,
	                                  float min,
	                                  float pref,
	                                  float max,
	                                  CoSizeRequirements[] request,
	                                  float[] offsets,
	                                  float[] spans )
{

	// ---- determine what we have to work with ----
	float totalPlay = Math.min( pref - allocated, pref - min );
	float factor = (pref - min == 0) ? 0.0f : totalPlay / (pref - min);

	// ---- make the adjustments ----
	float totalOffset = 0;
	for
		(int i = 0; i < spans.length; i++)
	{
		offsets[i] = totalOffset;
		CoSizeRequirements req = request[i];
		float play = factor * (req.m_preferred - req.m_minimum);
		spans[i] = req.m_preferred - play;
		totalOffset = Math.min( totalOffset + spans[i], Integer.MAX_VALUE);
	}
}
private static void expandedTile( float allocated,
	                                float min,
	                                float pref,
	                                float max,
	                                CoSizeRequirements[] request,
	                                float[] offsets,
	                                float[] spans )
{

	// ---- determine what we have to work with ----
	float totalPlay = Math.min(allocated - pref, max - pref);
	float factor = (max - pref == 0) ? 0.0f : totalPlay / (max - pref);

	// ---- make the adjustments ----
	float totalOffset = 0;
	for (int i = 0; i < spans.length; i++)
	{
		offsets[i] = totalOffset;
		CoSizeRequirements req = request[i];
		float play = (factor * (req.m_maximum - req.m_preferred));
		spans[i] = Math.min( req.m_preferred +  play, Integer.MAX_VALUE);
		totalOffset = Math.min( totalOffset + spans[i], Integer.MAX_VALUE);
	}
}
/**
 * Determines the total space necessary to
 * align a set of components.  The needs
 * of each component in the set are represented by an entry in the
 * passed-in SizeRequirements array.  The total space required will
 * never be more than Integer.MAX_VALUE.
 *
 * @param children  the set of child requirements.  If of zero length,
 *  the returns result will be a default instance of SizeRequirements.
 * @return  the total space requirements.
 */
public static CoSizeRequirements getAlignedSizeRequirements( CoSizeRequirements[] children )
{
	CoSizeRequirements totalAscent = new CoSizeRequirements();
	CoSizeRequirements totalDescent = new CoSizeRequirements();
	for
		(int i = 0; i < children.length; i++)
	{
		CoSizeRequirements req = children[i];
		float ascent = req.m_alignment * req.m_minimum;
		float descent = req.m_minimum - ascent;
		totalAscent.m_minimum = Math.max(ascent, totalAscent.m_minimum);
		totalDescent.m_minimum = Math.max(descent, totalDescent.m_minimum);
		ascent = req.m_alignment * req.m_preferred;
		descent = req.m_preferred - ascent;
		totalAscent.m_preferred = Math.max(ascent, totalAscent.m_preferred);
		totalDescent.m_preferred = Math.max(descent, totalDescent.m_preferred);
		ascent = req.m_alignment * req.m_maximum;
		descent = req.m_maximum - ascent;
		totalAscent.m_maximum = Math.max(ascent, totalAscent.m_maximum);
		totalDescent.m_maximum = Math.max(descent, totalDescent.m_maximum);
	}
	float min = Math.min( totalAscent.m_minimum + totalDescent.m_minimum, Integer.MAX_VALUE);
	float pref = Math.min( totalAscent.m_preferred + totalDescent.m_preferred, Integer.MAX_VALUE);
	float max = Math.min( totalAscent.m_maximum + totalDescent.m_maximum, Integer.MAX_VALUE);
	float alignment = 0.0f;
	if
		(min > 0)
	{
		alignment = (float) totalAscent.m_minimum / min;
		alignment = alignment > 1.0f ? 1.0f : alignment < 0.0f ? 0.0f : alignment;
	}
	return new CoSizeRequirements(min, pref, max, alignment);
}
/**
 * Determines the total space necessary to
 * place a set of components end-to-end.  The needs
 * of each component in the set are represented by an entry in the
 * passed-in SizeRequirements array.
 * The returned SizeRequirements object has an alignment of 0.5
 * (centered).  The space requirement is never more than
 * Integer.MAX_VALUE.
 *
 * @param children  the space requirements for a set of components.
 *   The vector may be of zero length, which will result in a
 *   default SizeRequirements object instance being passed back.
 * @return  the total space requirements.
 */
public static CoSizeRequirements getTiledSizeRequirements( CoSizeRequirements[] children )
{
	CoSizeRequirements total = new CoSizeRequirements();
	for
		(int i = 0; i < children.length; i++)
	{
		CoSizeRequirements req = children[i];
		total.m_minimum = Math.min( total.m_minimum + req.m_minimum, Integer.MAX_VALUE);
		total.m_preferred = Math.min( total.m_preferred + req.m_preferred, Integer.MAX_VALUE);
		total.m_maximum = Math.min( total.m_maximum + req.m_maximum, Integer.MAX_VALUE);
	}
	return total;
}
public String toString()
{
	return "[" + m_minimum + "," + m_preferred + "," + m_maximum + "]@" + m_alignment;
}
}
