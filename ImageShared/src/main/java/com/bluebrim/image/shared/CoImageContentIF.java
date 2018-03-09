package com.bluebrim.image.shared;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.text.shared.*;

/**
 * 
 * @since 2001-08-30 Helena Åberg: the interface now implements CoDagEditorCollectionIF
 */
public interface CoImageContentIF extends CoAtomicContentIF, CoViewable 
{
	String IMAGE_CONTENT = "image_content";
	String FACTORY_KEY = "image_content";
	String ICON_NAME = "CoImageContentIF.gif";

	int PREVIEW_WIDTH = 64;
	int PREVIEW_HEIGHT = 64;

	/**
	 * Returns the map of embedded paths in the image or null
	 * if embedded paths are not supported. The map contains
	 * pairs of path names and GeneralPath objects
	 * (@see CoTIFFDecoder)
	 * 
	 * @author: Helena Åberg <helena.aberg@appeal.se> (2001-10-09 14:52:44)
	 */
	public Map getEmbeddedPathMap(); // [ String -> GeneralPath ]

	/**
	 * Return image height in points (1/72:s of an inch).
	 *
	 */
	public int getHeight();

	public CoImage getImage();
	/**
	 * Return image width in points (1/72:s of an inch).
	 *
	 */
	public int getWidth();
	/**
	 * Returns true if the image contains one or more
	 * embedded paths. Returns false if no paths exist
	 * or if the image does not support embedded paths.
	 *
	 * Creation date: (2001-10-09 16:14:31)
	 * @author Helena Åberg <helena.aberg@appeal.se>
	 */
	public boolean hasEmbeddedPath();
	
	public CoFormattedTextHolderIF getCaption();

}