package com.bluebrim.font.shared;
import java.awt.*;
import java.util.*;

import com.bluebrim.base.shared.debug.*;

/**
 * Simple subclass to java.awt.Font providing special services required by Calvin.
 * The major extensions on this class compared to the AWT Font class is the association with a com.bluebrim.font.shared.CoFont for this
 * AWT Font, and a bug fix system for a problem with Type1 Fonts in AWT.
 * Please see {@link CoMacRomanAwtFont} for more information about this bug and the bug fix.
 *
 * <p><b>Creation date:</b> 2000-08-31
 * <br><b>Documentation last updated:</b> 2001-09-24
 *
 * @author Markus Persson 2000-08-31
 * @author Magnus Ihse (magnus.ihse@appeal.se) 2001-05-15
 *
 * @see com.bluebrim.font.shared.CoFont
 * @see java.awt.Font
 */
public class CoAwtFont extends Font {

	protected com.bluebrim.font.shared.CoFont m_font;

/**
 * Private constructor for CoAwtFont. Should only be called from <code>createAwtFont</code>.
 * <code>createAwtFont</code> should never be called directly, only from <code>com.bluebrim.font.shared.CoFont.getAwtFont</code>.
 *
 * @param font the com.bluebrim.font.shared.CoFont to wrap.
 * @param awtAttributes the attribute map to pass on to the AWT Font constructor.
 *
 * @see com.bluebrim.font.shared.CoFont#getAwtFont()
 */
protected CoAwtFont(com.bluebrim.font.shared.CoFont font, Map awtAttributes) {
	super(awtAttributes);
	m_font = font;
}


/**
 * Creates a new instance of <code>CoAwtFont</code>. 
 * <code>createAwtFont</code> should never be called directly, only from <code>com.bluebrim.font.shared.CoFont.getAwtFont</code>.
 *
 * @param font the com.bluebrim.font.shared.CoFont to wrap.
 * @param awtAttributes the attribute map to pass on to the AWT Font constructor.
 *
 * @return a new CoAwtFont.
 *
 * @see com.bluebrim.font.shared.CoFont#getAwtFont()
 */
protected static final CoAwtFont createAwtFont(com.bluebrim.font.shared.CoFont font, Map awtAttributes) {

/* By Markus:
 * PENDING: Use cache? Since all CoAwtFont instances
 * should be created from com.bluebrim.font.shared.CoFont instances, caching
 * and possibly canonicalizing those would suffice,
 * right? May not be workable however, unless using
 * weak/soft references. Decide later.
 *
 * (NOTE: com.bluebrim.font.shared.CoFontFace instances should be cached and
 * canonicalized since they are few and they hold
 * much cached data.)
 */
 
	if (CoAssertion.ASSERT) CoAssertion.notNull(font, "font");
	if (CoAssertion.ASSERT) CoAssertion.notNull(awtAttributes, "awtAttributes");

	return new CoAwtFont(font, awtAttributes);

}


/**
 * Returns the com.bluebrim.font.shared.CoFont associated with this AWT Font.
 *
 * @return the com.bluebrim.font.shared.CoFont associated with this AWT Font.
 */
public final com.bluebrim.font.shared.CoFont getFont() {
	return m_font;
}
}