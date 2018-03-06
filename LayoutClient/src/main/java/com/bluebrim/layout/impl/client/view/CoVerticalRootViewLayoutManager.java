package com.bluebrim.layout.impl.client.view;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Stacks the pages vertical the same way as most word processor and 
 * layout program do. It's possible that the root views vary in size.
 * The left and right margin is  
 * 
 * @author: Göran Stäck 2002-11-13
 */
public class CoVerticalRootViewLayoutManager extends CoRootViewLayoutManager {
	private static int TOP_MARGIN = 50;
	private static int BOTTOM_MARGIN = TOP_MARGIN;

	protected Dimension2D doLayout(CoRootView target) {
		
		double x = 0;
		double y = TOP_MARGIN;
		double maxViewWidth = 0;		

		// Calculate max width
		Iterator iter = target.getModels().iterator();
		while (iter.hasNext()) {
			CoLayoutEditorModel model = (CoLayoutEditorModel) iter.next();
			maxViewWidth = Math.max(maxViewWidth, model.getView().getWidth());
		};
		
		double leftMargin = maxViewWidth/2;	// Right margin as well
				
		iter = target.getModels().iterator();
		while (iter.hasNext()) {
			CoLayoutEditorModel model = (CoLayoutEditorModel) iter.next();
			CoView rootView = model.getView();
			double viewWidth = rootView.getWidth();
			model.setViewPosition(leftMargin, y);
			y += rootView.getHeight() + m_spacing;
		}

		return new CoDimension2D(maxViewWidth*2, y - m_spacing + BOTTOM_MARGIN);
	}

	public Insets getInsets() {
		return null;
	}

}
