package com.bluebrim.image.shared;

import java.io.*;

/**
 * @author G�ran St�ck
 *
 */
public interface CoImageContentFactory 
{
	public CoImageContentIF createImageContent(File file);
}
