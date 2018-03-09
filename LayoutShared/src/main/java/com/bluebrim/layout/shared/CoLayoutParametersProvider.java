package com.bluebrim.layout.shared;
/**
 * Implemented by objects that can provide a <code>CoLayoutParameters</code>
 * <br>
 * This is an attempt to create an interface that can replace 
 * <code>CoPageItemPreferencesOwnerIF</code> when refered from outside the layout domain. The
 * reason for doing this is to minimize what is exposed in the core part of the
 * layout domain. 
 * <br>
 *
 * @author Göran Stäck 2002-04-22
 */
public interface CoLayoutParametersProvider {
	public CoLayoutParameters getLayoutParameters();
}
