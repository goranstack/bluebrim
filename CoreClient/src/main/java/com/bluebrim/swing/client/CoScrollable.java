package com.bluebrim.swing.client;

import java.awt.Dimension;

import javax.swing.Scrollable;

/**
 * @author Markus Persson 1999-09-15
 */
public interface CoScrollable extends Scrollable {
public Dimension getScrollableSize(Dimension viewPortSize);
}
