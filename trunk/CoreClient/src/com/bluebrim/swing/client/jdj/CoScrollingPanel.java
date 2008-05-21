package com.bluebrim.swing.client.jdj;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.basic.*;
/**

	CoScrollingPanel.java
	En vidareutveckling av ScrollingPanel som kan scrolla 
	såväl vertikalt som horisontellt.

	Created by PS MediaTech AB
	Copyright (c) 1998
	
	ScrollingPanel created by Claude Duguay
	Copyright (c) 1998

  */
public class CoScrollingPanel extends JPanel{
	protected int orientation;
	protected JButton southOrEast, northOrWest;
	protected JViewport viewport;
	protected int incr = 64;
	private int m_buttonSize		= 15;
	
	public static int HORIZONTAL 	= 0;
	public static int VERTICAL		= 1;
/**
 * CoScrollingPanel constructor comment.
 * @param component java.awt.Component
 */
public CoScrollingPanel(Component component) {
	this(component, VERTICAL);
}
/**
 * CoScrollingPanel constructor comment.
 * @param component java.awt.Component
 */
public CoScrollingPanel(Component component, int orientation) {
	super();
	this.orientation 	= orientation;
	setLayout(new BorderLayout());
	viewport 			= new JViewport();
	add(viewport, BorderLayout.CENTER);
	viewport.setView(component);
	if (orientation == VERTICAL)
	{
		northOrWest = new BasicArrowButton(BasicArrowButton.NORTH);
		southOrEast = new BasicArrowButton(BasicArrowButton.SOUTH);
	
		ActionListener tActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				scrollVertically((JButton )e.getSource());
			}
		};
		northOrWest.addActionListener(tActionListener);
		southOrEast.addActionListener(tActionListener);
	}
	else
	{
		northOrWest = new BasicArrowButton(BasicArrowButton.WEST);
		southOrEast = new BasicArrowButton(BasicArrowButton.EAST);
		ActionListener tActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				scrollHorizontally((JButton )e.getSource());
			}
		};
		northOrWest.addActionListener(tActionListener);
		southOrEast.addActionListener(tActionListener);
	}
}
public static void main(String[] args)
{
	JPanel scroll 	= new JPanel();
	scroll.setBackground(Color.white);
	scroll.setLayout(new CoListLayout(CoListLayout.VERTICAL));
	scroll.add(new CoRolloverButton("Alpha"));
	scroll.add(new CoRolloverButton("Beta"));
	scroll.add(new CoRolloverButton("Gamma"));
	scroll.add(new CoRolloverButton("Delta"));
	scroll.add(new CoRolloverButton("Epsilon"));
	scroll.add(new CoRolloverButton("Omega"));
	JFrame frame 	= new JFrame("ScrollingPanel Test");
	frame.getContentPane().setLayout(new BorderLayout());
	frame.getContentPane().add("Center", new CoScrollingPanel(scroll, CoScrollingPanel.VERTICAL));
	frame.setBounds(0, 0, 200, 100);
	frame.setVisible(true);
	frame.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e)
		{
			System.exit(0);
		}
	});
}
protected void scrollHorizontally(JButton scrollSource)
{
		Dimension 	view 	= getSize();
		Dimension 	pane 	= viewport.getView().getPreferredSize();
		Point 		top 	= viewport.getViewPosition();
		if (scrollSource == northOrWest)
		{
			if (pane.width > view.width)
				add(southOrEast,BorderLayout.EAST);
			if (top.x < incr)
			{
				viewport.setViewPosition(new Point(0, 0));
				remove(northOrWest);
			}
			else
			{
				viewport.setViewPosition(new Point(top.x-incr,0));
			}
			doLayout();
		}
		if (scrollSource == southOrEast)
		{
			if (pane.width > view.width)
				add(northOrWest, BorderLayout.WEST);
			int max = pane.width - view.width;
			if (top.x > (max - incr))
			{
				remove(southOrEast);
				doLayout();
				view 	= viewport.getExtentSize();
				max 	= pane.width - view.width;
				viewport.setViewPosition(new Point(max, 0));
			}
			else
			{
				viewport.setViewPosition(new Point(top.x + incr,0 ));
			}
			doLayout();
		}
}
protected void scrollVertically(JButton scrollSource)
{
	Dimension 	view 	= getSize();
	Dimension 	pane 	= viewport.getView().getPreferredSize();
	Point 		top 	= viewport.getViewPosition();
	if (scrollSource == northOrWest)
	{
		if (pane.height > view.height)
			add(southOrEast, BorderLayout.SOUTH);
		if (top.y < incr)
		{
			viewport.setViewPosition(new Point(0, 0));
			remove(northOrWest);
		}
		else
		{
			viewport.setViewPosition(new Point(0, top.y - incr));
		}
		doLayout();
	}
	if (scrollSource == southOrEast)
	{
		if (pane.height > view.height)
			add(northOrWest, BorderLayout.NORTH);
		int max = pane.height - view.height;
		if (top.y > (max - incr))
		{
			remove(southOrEast);
			doLayout();
			view = viewport.getExtentSize();
			max = pane.height - view.height;
			viewport.setViewPosition(new Point(0, max));
		}
		else
		{
			viewport.setViewPosition(new Point(0, top.y + incr));
		}
		doLayout();
	}
}
public void setBounds(int x, int y, int w, int h)
{
	super.setBounds(x, y, w, h);
	Dimension 	view 			= new Dimension(w, h);
	Dimension 	pane 			= viewport.getView().getPreferredSize();
	Point 		tViewportPos	= viewport.getViewPosition();
	if (orientation == VERTICAL)
	{
		if (tViewportPos.y <= m_buttonSize)
			remove(northOrWest);
		if (pane.height >= tViewportPos.y+view.height)
		{
			add(southOrEast,BorderLayout.SOUTH);
		}
		else
		{
			remove(southOrEast);
		}
	}
	else
	{
		if (tViewportPos.x <= m_buttonSize)
			remove(northOrWest);
		if (pane.width > tViewportPos.x+view.width)
		{
			add(southOrEast, BorderLayout.EAST);
		}
		else
		{
			remove(southOrEast);
		}
	}
		doLayout();
}
}
