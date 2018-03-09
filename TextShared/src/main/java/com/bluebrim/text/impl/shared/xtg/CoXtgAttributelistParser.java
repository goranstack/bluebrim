package com.bluebrim.text.impl.shared.xtg;

/**
 * Subclass of CoXtgParser used for parsing xtg style ettribute lists.
 * Used internally by the xtg parsing mechanism.
 * 
 * @author: Dennis Malmström
 */
 
class CoXtgAttributelistParser extends CoXtgParser
{
/**
 * CoXtgTaglistParser constructor comment.
 * @param r java.io.Reader
 */
public CoXtgAttributelistParser( java.io.Reader r )
{
	super( r );
}
/**
 * CoXtgTaglistParser constructor comment.
 * @param r java.io.Reader
 */
public CoXtgAttributelistParser( java.io.Reader r, CoXtgLogger l )
{
	super( r, l );
}
protected CoXtgParseNode createRootNode()
{
	return new CoXtgAttributelistParseNode();
}
}
