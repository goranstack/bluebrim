package com.bluebrim.swing.client;

import java.util.EventListener;

/**
 * Very simple listener. Does not even use event objects.
 *
 * @author Markus Persson 2000-04-14
 */
public interface CoSimpleSizeListener extends EventListener {
public void sizeChanged(Object source, int size);
}
