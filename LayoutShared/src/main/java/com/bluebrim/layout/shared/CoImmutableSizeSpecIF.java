package com.bluebrim.layout.shared;

import com.bluebrim.base.shared.*;

//

public interface CoImmutableSizeSpecIF extends Cloneable, CoFactoryElementIF, java.io.Serializable
{
	public static String SIZE_SPEC = "width_spec";
	public static String WIDTH_SPEC = "width_spec";
	public static String HEIGHT_SPEC = "height_spec";

public CoSizeSpecIF deepClone();
	public String getDescription();

}