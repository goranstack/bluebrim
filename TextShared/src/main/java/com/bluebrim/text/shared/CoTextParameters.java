package com.bluebrim.text.shared;

import com.bluebrim.xml.shared.*;

/**
 * A <code>CoTextParameters</code> is a set of properties whose values determine 
 * the looks of a styled text.
 * <br>
 * This is an attempt to create an interface that can replace 
 * <code>CoStyledTextPreferencesIF</code> when refered from outside the text domain. The
 * reason for doing this is to minimize what is exposed in the core part of the
 * text domain. 
 * <br>
 * 
 * @author: Göran Stäck 2002-05-08
 */

public interface CoTextParameters extends CoXmlExportEnabledIF {
	public CoTypographyContextIF getTypographyContext();
	public float getKernAboveSize( boolean local );
	public boolean doUseQxpJustification();
	public void buildTextStyleApplier( CoTextStyleApplier a );
	public CoTypographyRuleIF getTypographyRule();
}
