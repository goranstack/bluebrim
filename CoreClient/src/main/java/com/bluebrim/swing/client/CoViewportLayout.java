package com.bluebrim.swing.client;

import java.awt.AWTError;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JViewport;
import javax.swing.ViewportLayout;

/**
 * Subclass to ViewportLayout that can handle CoScrollable:s,
 * that is Components whose dimensions are related. Typically,
 * the height increases when the width decreases.
 *
 * @see CoScrollPaneLayout
 * @see CoScrollable
 * @author Markus Persson 1999-09-15
 */
public class CoViewportLayout extends ViewportLayout {
	/**
	 * Called by the AWT when the specified container needs to be laid out.
	 *
	 * @param parent  the container to lay out
	 *
	 * @exception AWTError  if the target isn't the container specified to the
	 *                      BoxLayout constructor
	 */
	public void layoutContainer(Container parent)
	{
	JViewport vp = (JViewport)parent;
	Component view = vp.getView();
	CoScrollable scrollableView = null;

	if (view == null) {
	    return;
	} else if (view instanceof CoScrollable) {
	    scrollableView = (CoScrollable) view;
	}

	/* All of the dimensions below are in view coordinates, except
	 * vpSize which we're converting.
	 */

	Insets insets = vp.getInsets();
	Dimension vpSize = vp.getSize();
	Dimension extentSize = vp.toViewCoordinates(vpSize);
	Dimension viewSize;

	if (scrollableView != null) {
		viewSize = scrollableView.getScrollableSize(vpSize);
	} else {
		viewSize = view.getPreferredSize();
	}

	Point viewPosition = vp.getViewPosition();

	/* If the new viewport size would leave empty space to the
	 * right of the view, right justify the view or left justify
	 * the view when the width of the view is smaller than the
	 * container.
	 */
	if ((viewPosition.x + extentSize.width) > viewSize.width) {
	    viewPosition.x = Math.max(0, viewSize.width - extentSize.width);
	}

	/* If the new viewport size would leave empty space below the
	 * view, bottom justify the view or top justify the view when
	 * the height of the view is smaller than the container.
	 */
	if ((viewPosition.y + extentSize.height) > viewSize.height) {
	    viewPosition.y = Math.max(0, viewSize.height - extentSize.height);
	}

	/* If we haven't been advised about how the viewports size 
	 * should change wrt to the viewport, i.e. if the view isn't
	 * an instance of Scrollable, then adjust the views size as follows.
	 * 
	 * If the orgin of the view is showing and the viewport is
	 * bigger than the views preferred size, then make the view
	 * the same size as the viewport.
	 */
	if (scrollableView == null) {
		if ((viewPosition.x == 0) && (vpSize.width > viewSize.width)) {
			viewSize.width = vpSize.width;
		}
		if ((viewPosition.y == 0) && (vpSize.height > viewSize.height)) {
			viewSize.height = vpSize.height;
		}
	}
	vp.setViewPosition(viewPosition);
	vp.setViewSize(viewSize);
	}
}
