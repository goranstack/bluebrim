package com.bluebrim.font.shared;
import java.util.*;

/**
 * A set of font family names. 
 * PENDING: should perhaps be a (alphabetically sorted) list instead.
 * or perhaps it should include a list instead of extending one.
 *
 * Creation date: (2001-04-24 15:39:52)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public class CoFontCatalog extends HashSet implements Set {
public CoFontCatalog() {
	super();
}
public CoFontCatalog(Collection c) {
	super(c);
}
public boolean add(Object o) {
	if (com.bluebrim.base.shared.debug.CoAssertion.ASSERT) {
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue(o instanceof String, "Only font families may be added");
	}
	return super.add(o);
}
}
