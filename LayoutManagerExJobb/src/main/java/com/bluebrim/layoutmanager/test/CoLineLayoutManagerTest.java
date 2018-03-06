package com.bluebrim.layoutmanager.test;

/**
 * Insert the type's description here.
 * Creation date: (2000-07-13 10:48:52)
 * @author: Arvid Berg & Masod Jalalian 
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layoutmanager.*;

public class CoLineLayoutManagerTest extends JPanel
	implements ActionListener, MouseListener, MouseMotionListener
{
	private List toPlace;
	private List locked;
	private int align=CoCornerUtilities.BOTTOM_RIGHT;
	private java.awt.geom.Rectangle2D tempRect = null;
	public java.awt.geom.Point2D start;
	private List lines;
/**
 * CoAdPlacementTest constructor comment.
 */
public CoLineLayoutManagerTest() 
{
	super();
	locked=new LinkedList();
	toPlace=new LinkedList();
	lines=new LinkedList();
	tempRect=new Rectangle2D.Double(-1,-1,0,0);
}
/**
 * Depending on ActionCommand chose the placement form 
 * 		and place them in the screen.
 */
public void actionPerformed(java.awt.event.ActionEvent e) 
{
	String command=e.getActionCommand();
	if(command.equals("Clear"))
	{
		locked.clear();
		toPlace.clear();
		lines.clear();
	}	
	if(command.equals("Line"))
	{
		lines.clear();
		placeRects(locked,toPlace,lines, new Rectangle2D.Double(0,0,this.getWidth(),this.getHeight()),align,new CoDistVertical());
	}
	if(command.equals("Fun"))
	{
		for(int i=0;i<50;i++)
			toPlace.add(new Rectangle2D.Double(0,0,10,10));
		System.out.println("Numer of rects: "+toPlace.size());
	}
	repaint();
}		
/**
 * .
 * Creation date: (2000-07-14 14:06:50)
 * @param g java.awt.Graphics
 * @param value double
 */
public void drawFunc(Graphics g, double value) 
{
	g.setColor(Color.red);
	double x,y,x1,y1;
	x1=this.getWidth();
	y1=0;
	for(int i=0;i<this.getWidth();i+=(this.getWidth()/10))
	{
		x=i;
		y=value/x;
		g.drawLine((int)(this.getWidth()-x),(int)(this.getHeight()-y),
			(int)(this.getWidth()-x1),(int)(this.getHeight()-y1));
		x1=x;
		y1=y;
	}
}
/**
 * Drow a rectangle with rects size.
 * Creation date: (2000-07-05 09:34:37)
 * @param g java.awt.Graphics
 * @param rect java.awt.geom.Rectangle2D
 * @param c java.awt.Color
 */
public void drawRect(Graphics g, Rectangle2D rect, Color c) 
{
	g.setColor(c);	
	g.drawRect((int)rect.getX(),(int)rect.getY(),
			(int)rect.getWidth(),(int)rect.getHeight());
}
/**
 * Draw a rectagle with rects size, c:s color and bk:s foreground color.
 * Creation date: (2000-07-05 09:34:37)
 * @param g java.awt.Graphics
 * @param rect java.awt.geom.Rectangle2D
 * @param c java.awt.Color
 */
public void drawRectFill(Graphics g, Rectangle2D rect, Color c,Color bk) 
{
	if(rect==null)
		return;
	g.setColor(c);
	setForeground(c);
	g.setColor(bk);	
	g.fillRect((int)rect.getX(),(int)rect.getY(),
		(int)rect.getWidth(),(int)rect.getHeight());
	drawRect(g,rect,c);
}
/**
 * Insert the method's description here.
 * Creation date: (2000-08-22 09:40:59)
 * @param g java.awt.Graphics2D
 * @param l java.awt.geom.Line2D
 * @param c java.awt.Color
 */
public void drawRedLine(Graphics g, Line2D l, Color c) 
{
	g.setColor(c);
	g.drawLine((int)l.getX1(),(int)l.getY1(),(int)l.getX2(),(int)l.getY2());	
}
/**
 * The main method to show the window.
 * Creation date: (2000-07-13 10:52:21)
 * @param args java.lang.String[]
 */
public static void main(String[] args) 
{
	JFrame frame=new JFrame("CoAdPlacementTest");
	frame.setSize(500,500);
	frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	CoLineLayoutManagerTest drawPanel=new CoLineLayoutManagerTest();
	drawPanel.addMouseListener(drawPanel);
	drawPanel.addMouseMotionListener(drawPanel);
	
	JPanel buttonPanel=new JPanel();
	JButton b;
	b=new JButton("Clear");
	b.addActionListener(drawPanel);
	buttonPanel.add(b);
	
	b=new JButton("Line");
	b.addActionListener(drawPanel);
	buttonPanel.add(b);

	b=new JButton("Fun");
	b.addActionListener(drawPanel);
	buttonPanel.add(b);
	
	frame.getContentPane().add(BorderLayout.NORTH,buttonPanel);
	frame.getContentPane().add(BorderLayout.CENTER,drawPanel);
	frame.show();
}
/**
 * Insert the method's description here.
 * Creation date: (2000-08-23 09:03:24)
 * @param param java.awt.event.MouseEvent
 */
public void mouseClicked(java.awt.event.MouseEvent e) {}
/**
 * mouseDragged method comment.
 */
public void mouseDragged(java.awt.event.MouseEvent e) 
{
	tempRect.setFrameFromDiagonal(start.getX(),start.getY(),e.getX(),e.getY());
	repaint();	
}
/**
 * Insert the method's description here.
 * Creation date: (2000-08-23 09:06:27)
 * @param e java.awt.event.MouseEvent
 */
public void mouseEntered(java.awt.event.MouseEvent e) {}

/**
 * mouseExited method comment.
 */
public void mouseExited(java.awt.event.MouseEvent e) {}
/**
 * Insert the method's description here.
 * Creation date: (2000-08-23 09:00:51)
 * @param e java.awt.event.MouseEvent
 */
public void mouseMoved(java.awt.event.MouseEvent e) {}
/**
 * mousePressed method comment.
 */
public void mousePressed(java.awt.event.MouseEvent e) 
{
	start=new Point2D.Double();
	start.setLocation(e.getPoint());
}
/**
 * mouseReleased method comment.
 */
public void mouseReleased(java.awt.event.MouseEvent e) 
{
	if((e.getModifiers()&java.awt.event.MouseEvent.BUTTON3_MASK)!=0)
		locked.add(tempRect);
	else 
		toPlace.add(tempRect);
	//tempRect=new Rectangle2D.Double();
	tempRect=new Rectangle2D.Double(-1,-1,0,0);
	repaint();	
}
/**
 * Draw the locked and unlocked rectangles.
 * Creation date: (2000-07-13 11:39:00)
 * @param g java.awt.Graphics
 */
public void paint(Graphics g) {
	setBackground(Color.black);
	super.paint(g);
	Iterator iter = locked.iterator();
	while (iter.hasNext()) {
		Rectangle2D rect = (Rectangle2D) iter.next();
		drawRectFill(g, rect, Color.lightGray, Color.gray);
	}
	iter = toPlace.iterator();
	while (iter.hasNext()) {
		Rectangle2D rect = (Rectangle2D) iter.next();
		drawRectFill(g, rect, Color.green, Color.blue);
	}
	iter = lines.iterator();
	while (iter.hasNext()) {
		Line2D line = (Line2D) iter.next();
		g.setColor(Color.red);
		drawRedLine(g, line, Color.red);
	}
	if (tempRect != null)
		drawRect(g, tempRect, Color.white);
}
/**
 * Place the toPlace rectangles in container with consideration
 * of locked rectangles alignment and distfunction.
 * Creation date: (2000-07-13 10:59:27)
 * @param locked Collection
 * @param toPlace Collection
 * @param align int
 * @param dist CoCalculateDistanceIF
 */
public void placeRects(Collection locked, Collection toPlace, Collection lines,
		Rectangle2D container, int align, CoCalculateDistanceIF dist) 
{
	CoRectanglePlacement rp=new CoRectanglePlacement();
	List freeRects=new LinkedList();
	freeRects.add(container);
// Utplacering av låsta objekt	
	Iterator iter=locked.iterator();
	while(iter.hasNext())
	{
		Rectangle2D rect=(Rectangle2D) iter.next();
		
		Rectangle2D rectTemp= new Rectangle2D.Double(rect.getX()-5.5,rect.getY()-5.5,
			rect.getWidth()+11,rect.getHeight()+11); 
		
		freeRects=rp.reduceRectangles(rp.removeRect(rectTemp,freeRects));
		
		// Upp
		Line2D l = new Line2D.Double(rect.getX()-5.5,rect.getY()-5.5,
			rect.getX()+rect.getWidth()+11,rect.getY()-5.5);
		lines.add(l);
		// Ner
		l = new Line2D.Double(rect.getX()-5.5,rect.getY()+rect.getHeight()+5.5,
			rect.getX()+rect.getWidth()+11,rect.getY()+rect.getHeight()+5.5);
		lines.add(l);
		// Vänster
		l = new Line2D.Double(rect.getX()-5.5,rect.getY()-5.5,
			rect.getX()-5.5,rect.getY()+rect.getHeight()+5.5);
		lines.add(l);
		// Höger
		l = new Line2D.Double(rect.getX()+rect.getWidth()+11,rect.getY()-5.5,
			rect.getX()+rect.getWidth()+11,rect.getY()+rect.getHeight()+5.5);
		lines.add(l);

	}
	Point2D containerPoint=new Point2D.Double(
		-CoCornerUtilities.calculateDisplacement(align).getX()*container.getWidth(),
			-CoCornerUtilities.calculateDisplacement(align).getY()*container.getHeight());
// Utplacering av olåsta objekt 	
	iter=toPlace.iterator();
	while(iter.hasNext())
	{
		Rectangle2D rect=(Rectangle2D) iter.next();
		
		Rectangle2D rectTemp= new Rectangle2D.Double(rect.getX()-5.5,rect.getY()-5.5,
			rect.getWidth()+11,rect.getHeight()+11); 
		
		Point2D point=rp.choseBestPoint(rectTemp,rp.getPoints(rp.getFreeRects(rectTemp,freeRects),align),
			containerPoint,align,dist);
		rect.setRect(point.getX(),point.getY()+5.5,rect.getWidth(),rect.getHeight());		
		
		// Upp
		Line2D l = new Line2D.Double(rect.getX()-5.5,rect.getY()-5.5,
			rect.getX()+rect.getWidth()+11,rect.getY()-5.5);
		lines.add(l);

		// Ner
		l = new Line2D.Double(rect.getX()-5.5,rect.getY()+rect.getHeight()+5.5,
			rect.getX()+rect.getWidth()+11,rect.getY()+rect.getHeight()+5.5);
		lines.add(l);
		
		// Vänster
		l = new Line2D.Double(rect.getX()-5.5,rect.getY()-5.5,
			rect.getX()-5.5,rect.getY()+rect.getHeight()+5.5);
		lines.add(l);

		// Höger
		l = new Line2D.Double(rect.getX()+rect.getWidth()+11,rect.getY()-5.5,
			rect.getX()+rect.getWidth()+11,rect.getY()+rect.getHeight()+5.5);
		lines.add(l);
	
		
		freeRects=rp.reduceRectangles(rp.removeRect(rectTemp,freeRects));
	}
}
}
