package com.bluebrim.layout.impl.shared;

import java.io.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Protocol of page item positioning algorithms.
 * 
 * @author: Dennis Malmström
 */
 
public interface CoPageItemPositionIF extends Serializable
{

	CoPoint2DDouble place( CoCompositePageItemIF parent, CoShapePageItemIF child, CoPoint2DDouble pos );
}