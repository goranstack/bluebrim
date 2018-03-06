package com.bluebrim.text.shared.swing;

import javax.swing.text.*;

/**
 * Replaces javax.swing.text.Utilities.
 * 
 * @author: Dennis Malmstr�m
 */
 
class CoSwingTextUtilities
{
static boolean isComposedTextAttributeDefined(AttributeSet as)
{
	return ((as != null) && (as.isDefined(StyleConstants.ComposedTextAttribute)));
}
}
