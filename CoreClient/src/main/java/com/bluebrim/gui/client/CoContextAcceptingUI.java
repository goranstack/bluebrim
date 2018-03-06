package com.bluebrim.gui.client;

import com.bluebrim.base.shared.*;

/**
 * Add-on interface for UI:s requiring state that they cannot obtained from their
 * domain object. Implementors of this interface should also have a constructor
 * with the parameters (CoObjectIF, CoUIContext) in that order.
 *
 * IMPORTANT: Please read the interface description of CoUIContext as it
 * contains vital information about implementing this interface.
 *
 * @see CoUIContext
 * @author Markus Persson 2000-10-02
 */
public interface CoContextAcceptingUI {
/**
 * Returns a context representing the required state for the current
 * domain object. Note that the context has been typed to CoGenericUIContext
 * to signify that it also must be serializable.
 *
 * This is intended to be used when creating bookmarks only. If the UI
 * isn't bookmarkable, this method may return null. None of this is
 * currently implemented (but will be soon, I hope).
 *
 * When there is no current domain object, null can be returned.
 *
 * @author Markus Persson 2000-10-02
 */
public CoGenericUIContext getCopyOfCurrentRequiredUIContext();
/**
 * Set the context in preparation for the next domain object.
 *
 * No action should be taken in the UI except setting an instance
 * variable. There should be no difference between changing the
 * context object itself and replacing it with another. If the UI
 * requires state from the context during normal operation on a
 * single domain object, that state should be saved internally
 * when the domain object is installed!
 *
 * @author Markus Persson 2000-10-02
 */
public void setUIContext(CoUIContext context);
}
