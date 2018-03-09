package com.bluebrim.swing.client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;

/**
 * Layout manager that stacks component in vertical or horizontal direction.
 * Each component gets as wide/high as the biggest in the corresponding direction
 * and it's possible to stack components from the left and right (top and bottom)
 * simultanuosly.
 * Creation date: (2000-02-16 14:42:28)
 * @author: Lasse Svadängs
 */
public class CoButtonPanelLayout extends CoAbstractLayoutManager2 {
	private List 		m_rightComponents;
	private List 		m_leftComponents;
	private int			m_gap;
	private boolean 	m_orientation;
	private int			m_maxWidth;
	private int			m_prefWidth;
	private int			m_minWidth;
	private int			m_maxHeight;
	private int			m_prefHeight;
	private int			m_minHeight;
	private Component 	m_filler;

	public static final boolean VERTICAL 	= true;
	public static final boolean HORIZONTAL 	= false;
	
	public static class Direction {};
	public static Direction FROM_RIGHT_BOTTOM 	= new Direction();
	public static Direction FROM_LEFT_TOP 		= new Direction();
	
public 	CoButtonPanelLayout()
{
	this(4);
}
public 	CoButtonPanelLayout(int gap)
{
	this(HORIZONTAL, gap);
}
public 	CoButtonPanelLayout(boolean orientation, int gap)
{
	m_orientation 		= orientation;
	m_gap				= gap;
	m_rightComponents 	= new ArrayList();
	m_leftComponents 	= new ArrayList();
	m_filler			= orientation ? Box.createVerticalGlue() : Box.createHorizontalGlue();
}
public void addLayoutComponent(Component comp, Object constraints)
{
	if (constraints == FROM_LEFT_TOP)
		m_leftComponents.add(comp);
	else
		m_rightComponents.add(comp);
		
}
public void addLayoutComponent(String name, Component comp)
{
	addLayoutComponent(comp, FROM_RIGHT_BOTTOM);
}
private Dimension calculateLayoutSize(Container target, Dimension fillerDim, int cWidth, int cHeight) {
	int 		count 	= target.getComponentCount();
	Insets 		insets 	= target.getInsets();
	int 		width	= 0;
	int			height	= 0;
	if (m_orientation == HORIZONTAL)
	{
		width 	= count*cWidth+insets.left+insets.right+Math.max(0,(count-1)*m_gap)+fillerDim.width;
		height	= cHeight+insets.top+insets.bottom;
	}
	else
	{
		width 	= cWidth+insets.left+insets.right;
		height	= count*cHeight+insets.top+insets.bottom+Math.max(0,(count-1)*m_gap)+fillerDim.height;
	}
	return new Dimension(width, height);

}
public void invalidateLayout(Container target) {}
public void layoutContainer(Container target)
{
	int targetHeight	= target.getHeight();
	int targetWidth		= target.getWidth();
	Insets insets		= target.getInsets();
	int lChildren 		= m_leftComponents.size();
	int rChildren 		= m_rightComponents.size();
	
	if (m_orientation == HORIZONTAL)
	{
		int xOffset = insets.left;
		int yOffset = (targetHeight-insets.top-insets.bottom-m_prefHeight)/2;
		for (int i = 0; i < lChildren; i++)
		{
			Component c = (Component )m_leftComponents.get(i);
			c.setBounds(xOffset,insets.top+yOffset,m_prefWidth, m_prefHeight);
			xOffset += m_prefWidth+m_gap;
		}
		m_filler.setLocation(xOffset, yOffset);
		xOffset = targetWidth-insets.right;
		for (int i = rChildren-1; i >=0; i--)
		{
			Component c =  (Component )m_rightComponents.get(i);
			c.setBounds(xOffset-m_prefWidth, insets.top+yOffset, m_prefWidth, m_prefHeight);
			xOffset -= (m_prefWidth+m_gap);
		}
	}
	else
	{
		int xOffset = (targetWidth-insets.right-insets.left -m_prefWidth)/2;
		int yOffset = insets.top;
		for (int i = 0; i < lChildren; i++)
		{
			Component c =  (Component )m_leftComponents.get(i);
			c.setBounds(insets.left+xOffset,yOffset,m_prefWidth, m_prefHeight);
			yOffset += m_prefHeight+m_gap;
		}
		m_filler.setLocation(xOffset, yOffset);
		yOffset = targetHeight-insets.bottom;
		for (int i = rChildren-1; i >=0; i--)
		{
			Component c =  (Component )m_rightComponents.get(i);
			c.setBounds(insets.left+xOffset, yOffset-m_prefHeight, m_prefWidth, m_prefHeight);
			yOffset -= (m_prefHeight+m_gap);
		}
	}
}
public Dimension maximumLayoutSize(Container target) {
	recalculate(target);
	return calculateLayoutSize(target,m_filler.getMaximumSize(), m_maxWidth, m_maxHeight);
}
public Dimension minimumLayoutSize(Container target) {
	recalculate(target);
	return calculateLayoutSize(target,m_filler.getMinimumSize(), m_minWidth, m_minHeight);
}
public Dimension preferredLayoutSize(Container target) {
	recalculate(target);
	return calculateLayoutSize(target,m_filler.getPreferredSize(), m_prefWidth, m_prefHeight);
}
private void recalculate(Container target)
{
	int n = m_leftComponents.size();
	for (int i = 0; i < n; i++)
	{
		Component c 	= (Component )m_leftComponents.get(i);
		Dimension min 	= c.getMinimumSize();
		Dimension pref 	= c.getPreferredSize();
		Dimension max 	= c.getMaximumSize();
		if (min.width > m_minWidth)
			m_minWidth = min.width;
		if (max.width > m_maxWidth)
			m_maxWidth = max.width;
		if (pref.width > m_prefWidth)
			m_prefWidth = pref.width;
		if (min.height > m_minHeight)
			m_minHeight = min.height;
		if (max.height > m_maxHeight)
			m_maxHeight = max.height;
		if (pref.height > m_prefHeight)
			m_prefHeight = pref.height;
	}
	n = m_rightComponents.size();
	for (int i = 0; i < n; i++)
	{
		Component c 	= (Component )m_rightComponents.get(i);
		Dimension min 	= c.getMinimumSize();
		Dimension pref 	= c.getPreferredSize();
		Dimension max 	= c.getMaximumSize();
		if (min.width > m_minWidth)
			m_minWidth = min.width;
		if (max.width > m_maxWidth)
			m_maxWidth = max.width;
		if (pref.width > m_prefWidth)
			m_prefWidth = pref.width;
		if (min.height > m_minHeight)
			m_minHeight = min.height;
		if (max.height > m_maxHeight)
			m_maxHeight = max.height;
		if (pref.height > m_prefHeight)
			m_prefHeight = pref.height;
	}
}
}
