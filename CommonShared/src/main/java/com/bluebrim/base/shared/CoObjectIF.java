package com.bluebrim.base.shared;

/**
 	Interface for all business objects. Has protocol for add and remove of listeners of
 	<code>CoPropertyChangeEvent</code>. The business object is responsible for calling
 	<code>firePropertyChange</code> from for example the set methods.
 	@see CoPropertyChangeEvent
 */
public interface CoObjectIF {
/**
 * @param l CoPropertyChangeListener
 */
public void addPropertyChangeListener ( CoPropertyChangeListener listener);
/**
 * @param l CoPropertyChangeListener
 */
public void removePropertyChangeListener ( CoPropertyChangeListener listener);
}
