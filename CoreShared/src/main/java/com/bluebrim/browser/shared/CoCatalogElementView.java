package com.bluebrim.browser.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.resource.shared.*;

/**
 * An implementation of <code>CoListElementViewIF</code>.
 * <br>
 * NB: Shouldn't this implement <code>CoCatalogElementIF</code>?
 * <br>
 */
public class CoCatalogElementView extends CoListElementView
{
	public  CoCatalogElementView(CoCatalogElementIF element)
	{
		super(CoResourceLoader.loadIcon(element.getIconResourceAnchor(), element.getSmallIconName()), element.getIdentity(), element);
	}
	public CoCatalogElementIF getCatalogElement()
	{
		return (CoCatalogElementIF) getElement();
	}
}
