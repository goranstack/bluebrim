package com.bluebrim.swing.client;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JRootPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.plaf.TreeUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Subclass to <code>JTree</code> that implements <code>CoDragComponentIF</code>, 
 * i e the protocol required for the tree to act as source for a d&d operation.
 */
public class CoTree extends JTree {

	private boolean m_isMousePressedAtSelected = false;

/**
 * CoTree constructor comment.
 */
public CoTree() {
	super();
}


/**
 * CoTree constructor comment.
 * @param arg1 java.lang.Object[]
 */
public CoTree(java.lang.Object[] arg1) {
	super(arg1);
}


/**
 * CoTree constructor comment.
 * @param arg1 java.util.Hashtable
 */
public CoTree(java.util.Hashtable arg1) {
	super(arg1);
}


/**
 * CoTree constructor comment.
 * @param arg1 java.util.Vector
 */
public CoTree(java.util.Vector arg1) {
	super(arg1);
}


/**
 */
public CoTree(TreeModel model) {
	super(model);
}


/**
 * @param arg1 javax.swing.tree.TreeNode
 */
public CoTree(TreeNode node) {
	super(node);
}


/**
 */
public CoTree(TreeNode node, boolean aBoolean) {
	super(node, aBoolean);
}


public void expandAll()
{
	 TreeModel 	model 		= getModel();
	 Object		root		= model.getRoot();
	 TreePath	rootPath	= new TreePath(root);
	 expandDescendants(rootPath);
}


public void expandDescendants(TreePath path)
{
	 TreeModel model = getModel();
	 Object node = path.getLastPathComponent();
 
	 if (model.isLeaf(node))
		 throw new IllegalArgumentException("Can't work on a leaf: "+node); 
 
	 for (int count = model.getChildCount(node), i = 0; i < count; i++)     {
		 Object child = model.getChild(node, i);
 
		 if (!model.isLeaf(child))
		 {
			 TreePath childPath = path.pathByAddingChild(child);             
			 expandPath(childPath);
			 expandDescendants(childPath);
		 }
	 }
} 


private Component getGlassPane() {
	JRootPane rootPane = SwingUtilities.getRootPane(this);
	return rootPane != null ? rootPane.getGlassPane() : null;
}


private Component getRootComponent() {
	return SwingUtilities.getRoot(this);
}


/** 
 */
protected void processMouseEvent(MouseEvent e) {
	if (e.getID() == MouseEvent.MOUSE_PRESSED) {
		Point p = e.getPoint();
		int x = (new Double(p.getX())).intValue();
		int y = (new Double(p.getY())).intValue();
		int tIndex = getRowForLocation(x, y);

		m_isMousePressedAtSelected = isRowSelected(tIndex);
	}
	super.processMouseEvent(e);
}


private void setBusy(boolean busy) {}


protected void setExpandedState(TreePath path, boolean state)
{
	if (state)
	{
		try
		{
			setBusy(true);
			super.setExpandedState(path, state);
		}
		finally
		{
			setBusy(false);
		}
	}
	else
		super.setExpandedState(path, state);
}


public void setUI(TreeUI newUI) {
	int _rowHeight = getRowHeight();
	super.setUI(newUI);
	if (_rowHeight > 0)
		setRowHeight(_rowHeight);
}


/* 
*  The purpose of overriding this method is to create a MouseHandler
*  (and override some of its methods)so that selecting 
*  does not interfere with an attempt to start a Drag&Drop operation.
*/
public void updateUI() {

	setUI(new BasicTreeUI() {
		protected MouseListener createMouseListener() {
			return new MouseHandler() {

				//mousePressed fires deselection, hence derailing
				public void mousePressed(MouseEvent e) {
					if (m_isMousePressedAtSelected)
						return; 				//ready for dragging
					super.mousePressed(e);
				}

				public void mouseClicked(MouseEvent e) {
					Point p = e.getPoint();
					int x = (new Double(p.getX())).intValue();
					int y = (new Double(p.getY())).intValue();
					int pointIndex = getRowForLocation(x, y);

					if (m_isMousePressedAtSelected) {
						if (e.isShiftDown()) 	//redefining selection with a Shift down
							{
							setSelectionInterval(getMinSelectionRow(), pointIndex);
							super.mouseClicked(e);
							return;
						}
						if (e.isControlDown()) 	//redefining selection with a Ctrl down
							{
							removeSelectionInterval(pointIndex, pointIndex);
							super.mouseClicked(e);
							return;
						} else 					//deselecting
							{
							m_isMousePressedAtSelected = false;
							setSelectionRow(pointIndex);
						}
					}
					super.mouseClicked(e);
				}

			};
		}
	});
}
}