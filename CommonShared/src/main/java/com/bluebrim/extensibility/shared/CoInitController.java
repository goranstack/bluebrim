package com.bluebrim.extensibility.shared;

import java.util.*;

/**
 * Interface for classes wishing to modify the
 * init process.
 * 
 * @see CoBootController
 * @see CoBooter
 * @see CoServerInitializer
 * @author Markus Persson 2002-05-23
 */
public interface CoInitController {
	public Map configInit(Map config);
}
