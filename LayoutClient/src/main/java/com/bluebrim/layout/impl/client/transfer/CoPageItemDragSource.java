package com.bluebrim.layout.impl.client.transfer;

import java.util.*;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;

/**
 * Protocol for objects from which page items can be dragged.
 * Creation date: (2001-08-09 16:09:14)
 * @author: Dennis
 */
 
public interface CoPageItemDragSource
{
boolean canStartDrag();
// called only if getSnappingPageItemView returns null

CoShapePageItemIF getSnappingPageItem();
// Return a page item view that is used for dragging around in the drop target.
// This method can return null in which case the method getSnappingPageItem must return a
// page item from which the view is created.
// If views are available it is much faster to use them than to create new ones
// (creating new ones involves server calls).

CoShapePageItemView getSnappingPageItemView();
// called only if getTransferablePageItemViews returns null

List getTransferablePageItems();
// Return page item view for the page items that are transfered.
// This method can return null in which case the method getTransferablePageItemd must return
// page items from which the views are created.
// If views are available it is much faster to use them than to create new ones
// (creating new ones involves server calls).

List getTransferablePageItemViews();
}
