package com.bluebrim.base.shared;

import java.util.*;

//

public interface CoShapeFactoryIF extends CoFactoryIF {
	public static final String SHAPE_FACTORY = "shape_factory";
	CoShapeIF createShape(String key);
	CoShapeIF createShape(String key, CoImmutableShapeIF geometry);
	Collection getShapeKeys();
}
