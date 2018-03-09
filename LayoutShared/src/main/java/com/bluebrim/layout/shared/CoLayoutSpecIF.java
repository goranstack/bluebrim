package com.bluebrim.layout.shared;

import com.bluebrim.base.shared.*;

/**
 * Interface layout specification.
 * 
 * @author: Dennis Malmström
 */

public interface CoLayoutSpecIF extends Cloneable, CoFactoryElementIF, java.rmi.Remote
{
	public static String LAYOUT_SPEC = "layout_spec";
public void setHeightSpec ( CoImmutableSizeSpecIF heightSpec);
public void setLocationSpec ( CoImmutableLocationSpecIF locationSpec);
public void setWidthSpec ( CoImmutableSizeSpecIF widthSpec);

public CoLayoutSpecIF deepClone();

public CoImmutableSizeSpecIF getHeightSpec ();

public CoImmutableLocationSpecIF getLocationSpec ();

public CoImmutableSizeSpecIF getWidthSpec ();

public boolean hasAbsolutePosition();

public boolean isNull();

public void set( CoImmutableLocationSpecIF locationSpec, CoImmutableSizeSpecIF widthSpec, CoImmutableSizeSpecIF heightSpec );
}