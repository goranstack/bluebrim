package com.bluebrim.image.impl.server;

import java.io.*;

import com.bluebrim.image.shared.*;

/**
 * Factory used for non distributed applications without RMI
 * 
 * @author Göran Stäck
 *
 */
public class CoLocalImageClient implements CoImageContentFactory 
{
    public CoImageContentIF createImageContent(File file)
    {
        try {
			return new CoImageContent(file);
		} catch (IOException e) {
            throw new RuntimeException("Unable to create image content", e);
		}  
    }

	
}
