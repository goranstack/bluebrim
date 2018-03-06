package com.bluebrim.layoutmanager.test;

import java.awt.*;
/**
 * Insert the type's description here.
 * Creation date: (2000-06-06 16:50:23)
 * @author: Arvid Berg & Masod Jalalian 
 */
public class CoTestPanel extends javax.swing.JPanel {
	private java.util.List list;
/**
 * CoTestPanel constructor comment.
 */
public CoTestPanel() {
	super();
}
/**
 * CoTestPanel constructor comment.
 * @param layout java.awt.LayoutManager
 */
public CoTestPanel(java.awt.LayoutManager layout) {
	super(layout);
}
/**
 * CoTestPanel constructor comment.
 * @param layout java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
public CoTestPanel(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
	super(layout, isDoubleBuffered);
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-07 09:06:59)
 * @param param java.util.List
 */
public CoTestPanel(List param) {}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-07 09:06:59)
 * @param param java.util.List
 */
public CoTestPanel(java.util.List param) 
{
	list=param;
}
/**
 * CoTestPanel constructor comment.
 * @param isDoubleBuffered boolean
 */
public CoTestPanel(boolean isDoubleBuffered) {
	super(isDoubleBuffered);
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-06 16:50:52)
 * @param g java.awt.Graphics
 */
public void paint(java.awt.Graphics g) 
{
	int total=0,placed=0;
	super.paint(g);
	double width = getWidth(); 
 	double height = getHeight(); 
	setForeground(Color.black);
	g.fillRect(0, 0, (int)500, (int)500);
	java.util.Iterator iter=list.iterator();
	setForeground(Color.green);
	g.setColor(Color.blue);
	while(iter.hasNext())
	{
		total++;
		g.setColor(Color.blue);
		com.bluebrim.layoutmanager.test.RectIMLayoutable rect=(com.bluebrim.layoutmanager.test.RectIMLayoutable)iter.next();
		if(!rect.hasValidLayout())
			continue;
		placed++;
		g.fillRect((int)rect.getX(),(int)rect.getY(),
			(int)rect.getLayoutWidth(),(int)rect.getLayoutHeight());
		g.setColor(Color.green);
		g.drawRect((int)rect.getX(),(int)rect.getY(),
			(int)rect.getLayoutWidth(),(int)rect.getLayoutHeight());
	}
	g.setColor(Color.white);
	g.drawString("P rate: "+((double)placed/total)*100,5,10);
	setForeground(Color.black);
}
}
