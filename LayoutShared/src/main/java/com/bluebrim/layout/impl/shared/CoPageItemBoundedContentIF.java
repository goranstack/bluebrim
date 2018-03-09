package com.bluebrim.layout.impl.shared;

import com.bluebrim.layout.shared.*;


/**
 * RMI-enabling interface for class CoPageItemBoundedContent.
 * See class for method details.
 * 
 * @author: Dennis Malmström
 */
public interface CoPageItemBoundedContentIF extends CoPageItemContentIF {
	// adjustment dimension mask values
	public static int X = 1;
	public static int Y = 2;
	public static int XY = X | Y;

	// content lock values
	public static int UNLOCKED = 0; // content can be set and replaced
	public static int LOCKED = 1; // content can be set but not replaced
	public static int FIXED = 2; // content can't be set or replaced

	// see CoPageItemIF.State
	public static class State extends CoPageItemContentIF.State {
		// page item state needed by view, see CoPageItemBoundedContent for details
		public double m_scaleX;
		public double m_scaleY;
		public double m_x;
		public double m_y;
		public boolean m_flipX;
		public boolean m_flipY;
		public boolean m_hasCaption;
		public int m_contentLock;
	};
	
	public void adjustContentToFit(int dimensionMask);
	public void adjustContentToFitKeepAspectRatio(int dimensionMask);
	public void adjustToContentSize(int dimensionMask);
	public void adjustToScaledContentSize(int dimensionMask);

	boolean getFlipX();
	boolean getFlipY();
	public double getScaleX();
	public double getScaleY();
	double getX();
	double getY();
	public boolean isEmpty();
	void setFlipX(boolean b);
	void setFlipY(boolean b);

	boolean hasCaption();
	void setCaptionPosition(int slavePositionKey);
	CoShapePageItemIF removeCaption();
	void setCaption(CoShapePageItemIF caption);

	int getContentLock();
	void setContentLock(int l);

	public void setScaleAndPosition(double sx, double sy, double x, double y);
}