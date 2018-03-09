package com.bluebrim.layout.impl.shared;

import com.bluebrim.content.shared.*;
import com.bluebrim.image.shared.*;
import com.bluebrim.paint.shared.*;

/**
 * RMI-enabling interface for class CoPageItemImageContent.
 * See class for method details.
 * 
 * @author: Dennis Malmström
 */

public interface CoPageItemImageContentIF extends CoPageItemBoundedContentIF, CoOrderTaggedIF {
	public static String IMAGE_CONTENT = "IMAGE_CONTENT";
	
	// see CoPageItemIF.State
	public static class State extends CoPageItemBoundedContentIF.State {
		// page item state needed by view, see CoPageItemImageContent for details
		public CoImageContentIF m_imageContent;
		public CoColorIF m_imageColor;
		public float m_imageColorShade;
		public int m_imageTag;
		public CoWorkPieceIF m_workPiece;
	};


	public float getImageColorShade();

	public void setImageColorShade(float s);

	public CoColorIF getImageColor();

	public CoImageContentIF getImageContent();

	public void setEmbeddedPathShape(String pathName);

	public void setImageColor(CoColorIF imageColor);

	public void setImageContent(CoImageContentIF image);
}