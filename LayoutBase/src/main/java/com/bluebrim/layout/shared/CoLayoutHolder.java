package com.bluebrim.layout.shared;

import java.util.List;

import com.bluebrim.base.shared.CoNamed;

/**
 * A <code>CoLayoutHolder</code> is implemented by objects that has an <code>CoLayout</code>.
 * Its common that objects with only one <code>CoLayout</code> implements this interface.
 * 
 * This is an attempt to create an interface that can replace 
 * <code>CoPageItemHolderIF</code> when refered from outside the layout domain. The
 * reason for doing this is to minimize what is exposed in the core part of the
 * layout domain. 
 * <br>
 * The working strategi is to replace all external references to code>CoPageItemHolderIF</code>
 * with this interface and add requested methods until everyone is happy.  
 * <br>
 * 
 * @author: Göran Stäck 2002-04-12
 */

public interface CoLayoutHolder extends CoNamed {
	public String getDescription();
	public List getLayouts();
	public void layoutsChanged();
}
