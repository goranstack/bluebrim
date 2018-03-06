package com.bluebrim.image.shared;

import java.io.*;

/**
 * @author Göran Stäck
 *
 */
public interface CoImageContentFactory 
{
	public CoImageContentIF createImageContent(File file);
}
