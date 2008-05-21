package com.bluebrim.swing.client.jdj;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

/**
 */
public class CoListLayout extends ListLayout {
	protected int orientation;
	public static final int TOP 		= 4;
	public static final int BOTTOM 		= 5;
	public static final int HORIZONTAL 	= 0;
	public static final int VERTICAL	= 1;
/**
 * CoListLayout constructor comment.
 */
public CoListLayout() {
	super();
	orientation = VERTICAL;
}
/**
 * CoListLayout constructor comment.
 */
public CoListLayout(int orientation) {
	super();
	this.orientation = orientation;
}
/**
 * CoListLayout constructor comment.
 */
public CoListLayout(int alignment,int orientation) {
	super(alignment);
	this.orientation = orientation;
}
/**
 * CoListLayout constructor comment.
 * @param hgap int
 * @param vgap int
 * @param alignment int
 */
public CoListLayout(int hgap, int vgap, int orientation) {
	super(hgap, vgap);
	this.orientation = orientation;
}
/**
 * CoListLayout constructor comment.
 * @param hgap int
 * @param vgap int
 * @param alignment int
 */
public CoListLayout(int hgap, int vgap, int alignment,int orientation) {
	super(hgap, vgap, alignment);
	this.orientation = orientation;
}
/**
**/
public void layoutContainer(Container parent)
{
	if (orientation == VERTICAL)
		super.layoutContainer(parent);
	else
	{
		Insets 	insets 	= parent.getInsets();
		int 	h 		= parent.getSize().height;
		Component comp;
		Dimension size;
		int position 	= insets.left;
		int ncomponents = parent.getComponentCount();
		int tMaxWidth	= 0;
		for (int i = 0; i < ncomponents; i++)
		{
			comp 	= parent.getComponent(i);
			size 	= comp.getPreferredSize();
			if (size.width > tMaxWidth)
				tMaxWidth	= size.width;
		}

		for (int i = 0; i < ncomponents; i++)
		{
			comp 	= parent.getComponent(i);
			size 	= comp.getPreferredSize();
			switch (alignment)
			{
				case CENTER :
				
						{
						int l = (h - size.height) / 2;
						comp.setBounds(position,insets.top + vgap + l, tMaxWidth, size.height - insets.top - insets.bottom - (vgap * 2));
						break;
					}
				case TOP :
				
						{
						comp.setBounds(position,insets.top + vgap, tMaxWidth, size.height - insets.top - insets.bottom - (vgap * 2));
						break;
					}
				case BOTTOM :
				
						{
						int l = h - size.height;
						comp.setBounds(position,insets.left + hgap + l, tMaxWidth, size.height - insets.top - insets.bottom - (vgap * 2));
						break;
					}
				default :
				
						{
						comp.setBounds(position,insets.top + vgap, tMaxWidth, h - insets.top - insets.bottom - (vgap * 2));
						break;
					}
			}
			position += tMaxWidth + hgap;
		}
	}
}
/**
 * Returns the minimum dimensions needed to layout the
 * components contained in the specified target container. 
 * @param target The Container on which to do the layout
**/
public Dimension minimumLayoutSize(Container target)
{
	if (orientation == VERTICAL)
		return super.minimumLayoutSize(target);
	else
	{
		Insets insets = target.getInsets();
		int w = 0;
		int h = 0;
		Dimension size;
		int ncomponents = target.getComponentCount();
		for (int i = 0; i < ncomponents; i++)
		{
			size = target.getComponent(i).getMinimumSize();
			if (size.width > w)
				w = size.width;
			if (size.height > h)
				h = size.height;
		}
		w = ((w + hgap) * ncomponents) - hgap;
		return new Dimension(w, h + (vgap * 2));
	}
}
/**
 * Returns the preferred dimensions for this layout given the
 * components in the specified target container.
 * @param target The component which needs to be laid out
**/
public Dimension preferredLayoutSize(Container target)
{
	if (orientation == VERTICAL)
		return super.preferredLayoutSize(target);
	else
	{
		Insets 	insets 	= target.getInsets();
		int 	w 		= 0;
		int 	h 		= 0;
		Dimension size;
		int ncomponents = target.getComponentCount();
		for (int i = 0; i < ncomponents; i++)
		{
			size = target.getComponent(i).getPreferredSize();
			if (size.width > w)
				w = size.width;
			if (size.height > h)
				h = size.height;
		}
		w = ((w + hgap) * ncomponents) - hgap;
		return new Dimension(w,h + (vgap * 2));
	}
}
}
