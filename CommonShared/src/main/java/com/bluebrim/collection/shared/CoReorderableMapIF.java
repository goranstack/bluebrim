package com.bluebrim.collection.shared;

import java.util.*;
/**
 * <p>An extension of the <tt>Map</tt> interface that ensures consistent
 * ordering via its iterator and also permits manipulation of that order
 * by extending the CoReorderable interface.</p>
 *
 * @author Markus Persson 1999-07-16
 */
public interface CoReorderableMapIF extends Map, CoReorderable {
}
