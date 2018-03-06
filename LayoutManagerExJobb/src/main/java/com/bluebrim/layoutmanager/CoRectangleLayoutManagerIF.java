package com.bluebrim.layoutmanager;

import java.util.*;

import com.bluebrim.layout.impl.shared.layoutmanager.*;

/**
 * Creation date: (2000-06-13 11:15:43)
 * @author: Arvid Berg & Masod Jalalian 
 */
public interface CoRectangleLayoutManagerIF extends CoLayoutManagerIF {
	
	public final static String RECTANGLE_LAYOUT_MANAGER = "RECTANGLE_LAYOUT_MANAGER";

	public CoCalculateDistanceIF createDistanceCalculator(String key);

	public CoCalculateDistanceIF getDefaultDistanceCalculator();

	public CoCalculateDistanceIF getDistanceCalculator();

	double getGap();

	public List getKeys();

	public void setDistanceCalculator(CoCalculateDistanceIF newDistanceCalculator);

	void setGap(double g);
}