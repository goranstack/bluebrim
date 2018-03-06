package com.bluebrim.layout.impl.shared;

import com.bluebrim.layout.shared.*;


/**
 */
public interface CoContentSizeIF extends CoSizeSpecIF, CoImmutableContentSizeIF
{

public void setRelativeOffset( double r );

public void setAbsoluteOffset( double a );
}