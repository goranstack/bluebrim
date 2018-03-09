package com.bluebrim.layout.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;

//

public interface CoImmutableLocationSpecIF extends CoFactoryElementIF, java.io.Serializable
{
	public static String LOCATION_SPEC	= "location_spec";
public CoLocationSpecIF deepClone();
public String getType ( );
public boolean isAbsolutePosition ();
}