package com.bluebrim.layout.impl.client.view;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

import com.bluebrim.layout.impl.shared.view.*;

/**
 * 
 * 
 * @author: Dennis
 */
 
public class CoPageItemViewTreeCellRenderer extends DefaultTreeCellRenderer
{
/**
 * CoPageItemViewTreeCellRenderer constructor comment.
 */
public CoPageItemViewTreeCellRenderer()
{
	super();
}
public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
{
	super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus );

	if
		( value instanceof CoShapePageItemView )
	{
		CoShapePageItemView v = (CoShapePageItemView) value;

		setText( v.getName() );
		setIcon( v );
	}

	return this;
}
}