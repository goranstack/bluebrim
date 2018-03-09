package com.bluebrim.swing.client;
import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.bluebrim.base.client.datatransfer.CoDragSourceListener;

/**
 * Subclass to <code>CoDragSourceListener</code> handling drag started in a<code>CoTree</code>.
 * It's assumed that the elements in the tree implements <code>CoTreeCatalogElementIF</code>.
 * If this isn't the case it's necessary to subclass and reimplement <code>getSelectedElements</code>.
 */
public class CoTreeDragSourceListener extends CoDragSourceListener {

public CoTreeDragSourceListener(Component c) {
	super(c);

}


public boolean canStartDrag(DragGestureEvent e) {
	if (super.canStartDrag(e)) {
		Point p = e.getDragOrigin();
		int x = (new Double(p.getX())).intValue();
		int y = (new Double(p.getY())).intValue();
		int tIndex = getTree().getRowForLocation(x, y);

		return (tIndex != -1) ? getTree().isRowSelected(tIndex) : false;
	} else
		return false;
}


protected Object getElementFrom(Object selectedElement){
	return selectedElement;
}


public Object[] getSelectedElements(){
	Object 		selectedElements[]	= null;
	TreePath 	selectionPaths[] 	= getTree().getSelectionPaths();
	if (selectionPaths != null)
	{
		selectedElements = new Object[selectionPaths.length];
		for (int i = selectionPaths.length-1; i>=0; i--)
		{
			Object iPath[]			= selectionPaths[i].getPath();
			selectedElements[i] 	= getElementFrom(iPath[iPath.length-1]);
		}	
	}
	return selectedElements;	
}


public Transferable getTransferable() {
	return new CoTreeSelection(getSelectedElements());
}


public JTree getTree() {
	return (JTree) getComponent();
}
}