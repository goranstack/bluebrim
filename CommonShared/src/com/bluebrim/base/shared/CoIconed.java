package com.bluebrim.base.shared;

import javax.swing.*;

/**
 * <p>Interface for objects that can deliver an icon suitable to be displayed to the user.</p>
 *
 * <p><em>Note:</em> <code>CoVisualizableIF</code> is an interface with similar use.
 * However, the difference is that users of a CoIconed object simply has to ask for
 * the icon, not retrieve it using CoImageIconLoader. The advantage is that you do not
 * have to rely upon the (seemingly inefficient) caching mechanisms of the icon loader
 * and also get a cleaner interface.
 *
 * NOTE: Objects implementing CoIconed shouldn't implement Remote!
 *
 * Implementations are typically Serializable and have a transient instance variable
 * lazy initialized in the getIcon() method by an appropriate call to CoImageIconLoader.
 * CoIconed is intended for use in caching classes on the client. The sole method should
 * never be called on the server side although CoImageIconLoader exists there too for
 * compilation to succeed.
 *
 * PENDING: The future of this interface is uncertain. It's an experiment.
 *
 * @see CoNamed
 * @author Markus Persson 1999-11-30
 */
public interface CoIconed {
public Icon getIcon();
}
