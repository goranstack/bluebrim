package com.bluebrim.layout.impl.client;

/**
 * Interface for listening to auto scroll events.
 * see CoAutoScroller.
 *
 * @author: Dennis
 */
 
public interface CoAutoScrollListener extends java.util.EventListener
{
void autoScroll( CoAutoScrollEvent ev );
}
