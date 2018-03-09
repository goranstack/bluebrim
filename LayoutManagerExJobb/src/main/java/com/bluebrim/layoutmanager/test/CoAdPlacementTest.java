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

public class CoAdPlacementTest extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
	private List toPlace;
	private List locked;
	private int align = CoCornerUtilities.BOTTOM_RIGHT;
	private Rectangle2D tempRect = null;
	public Point2D start;

	public CoAdPlacementTest() {
		super();
		locked = new LinkedList();
		toPlace = new LinkedList();
		tempRect = new Rectangle2D.Double(-1, -1, 0, 0);
	}
	/**
	 * Depending on ActionCommand chose the placement form 
	 * 		and place them in the screen.
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Clear")) {
			locked.clear();
			toPlace.clear();
		}
		if (command.equals("Convex"))
			placeRects(locked, toPlace, new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight()), align, new CoDistConvex());
		if (command.equals("Rect"))
			placeRects(locked, toPlace, new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight()), align, new CoDistRectangle());
		if (command.equals("Triangle"))
			placeRects(locked, toPlace, new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight()), align, new CoDistTriangle());
		if (command.equals("Horizontal"))
			placeRects(locked, toPlace, new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight()), align, new CoDistHorizontal());
		if (command.equals("Vertical"))
			placeRects(locked, toPlace, new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight()), align, new CoDistVertical());
		if (command.equals("Concave"))
			placeRects(locked, toPlace, new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight()), align, new CoDistConcave());
		if (command.equals("Fun")) {
			for (int i = 0; i < 50; i++)
				toPlace.add(new Rectangle2D.Double(0, 0, 10, 10));
			System.out.println("Numer of rects: " + toPlace.size());
		}
		if (command.equals("Test")) {
			for (int i = 0; i < 5; i++)
				toPlace.add(new CoRectClass(new Rectangle2D.Double(0, 0, 125, 125), CoCornerUtilities.BOTTOM_LEFT));
			for (int i = 0; i < 5; i++)
				toPlace.add(new CoRectClass(new Rectangle2D.Double(0, 0, 125, 125), CoCornerUtilities.BOTTOM_RIGHT));
			/*for(int i=0;i<5;i++)
				toPlace.add(new CoRectClass(new Rectangle2D.Double(0,0,125,125),Utilities.TOP_LEFT));
			for(int i=0;i<5;i++)
				toPlace.add(new CoRectClass(new Rectangle2D.Double(0,0,125,125),Utilities.TOP_RIGHT));
			*/
			placeRects(locked, toPlace, new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight()), align, new CoDistRectangle());

		}
		repaint();
	}
	/**
	 * .
	 * Creation date: (2000-07-14 14:06:50)
	 * @param g java.awt.Graphics
	 * @param value double
	 */
	public void drawFunc(Graphics g, double value) {
		g.setColor(Color.red);
		double x, y, x1, y1;
		x1 = this.getWidth();
		y1 = 0;
		for (int i = 0; i < this.getWidth(); i += (this.getWidth() / 10)) {
			x = i;
			y = value / x;
			g.drawLine((int) (this.getWidth() - x), (int) (this.getHeight() - y), (int) (this.getWidth() - x1), (int) (this.getHeight() - y1));
			x1 = x;
			y1 = y;
		}
	}
	/**
	 * Drow a rectangle with rects size.
	 * Creation date: (2000-07-05 09:34:37)
	 * @param g java.awt.Graphics
	 * @param rect java.awt.geom.Rectangle2D
	 * @param c java.awt.Color
	 */
	public void drawRect(Graphics g, Rectangle2D rect, Color c) {
		g.setColor(c);
		g.drawRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
	}
	/**
	 * Draw a rectagle with rects size, c:s color and bk:s foreground color.
	 * Creation date: (2000-07-05 09:34:37)
	 * @param g java.awt.Graphics
	 * @param rect java.awt.geom.Rectangle2D
	 * @param c java.awt.Color
	 */
	public void drawRectFill(Graphics g, Rectangle2D rect, Color c, Color bk) {
		if (rect == null)
			return;
		g.setColor(c);
		setForeground(c);
		g.setColor(bk);
		g.fillRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
		drawRect(g, rect, c);
	}
	/**
	 * Draw three deferent lines with rects size and c: color.
	 * Creation date: (2000-07-05 09:34:37)
	 * @param g java.awt.Graphics
	 * @param rect java.awt.geom.Rectangle2D
	 * @param c java.awt.Color
	 */
	public void drawRectX(Graphics g, Rectangle2D rect, Color c) {
		g.setColor(c);
		g.drawLine((int) rect.getX(), (int) rect.getY(), (int) rect.getX() + (int) rect.getWidth(), (int) rect.getY() + (int) rect.getHeight());
		g.drawLine((int) rect.getX() + (int) rect.getWidth(), (int) rect.getY(), (int) rect.getX(), (int) rect.getY() + (int) rect.getHeight());
		g.drawRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
	}
	/**
	 * The main method to show the window.
	 * Creation date: (2000-07-13 10:52:21)
	 * @param args java.lang.String[]
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("CoAdPlacementTest");
		frame.setSize(500, 500);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		CoAdPlacementTest drawPanel = new CoAdPlacementTest();
		drawPanel.addMouseListener(drawPanel);
		drawPanel.addMouseMotionListener(drawPanel);

		JPanel buttonPanel = new JPanel();
		JButton b;
		b = new JButton("Test");
		b.addActionListener(drawPanel);
		buttonPanel.add(b);

		b = new JButton("Clear");
		b.addActionListener(drawPanel);
		buttonPanel.add(b);

		b = new JButton("Convex");
		b.addActionListener(drawPanel);
		buttonPanel.add(b);

		b = new JButton("Rect");
		b.addActionListener(drawPanel);
		buttonPanel.add(b);

		b = new JButton("Triangle");
		b.addActionListener(drawPanel);
		buttonPanel.add(b);

		b = new JButton("Horizontal");
		b.addActionListener(drawPanel);
		buttonPanel.add(b);

		b = new JButton("Vertical");
		b.addActionListener(drawPanel);
		buttonPanel.add(b);

		b = new JButton("Concave");
		b.addActionListener(drawPanel);
		buttonPanel.add(b);

		b = new JButton("Line");
		b.addActionListener(drawPanel);
		buttonPanel.add(b);

		b = new JButton("Fun");
		b.addActionListener(drawPanel);
		buttonPanel.add(b);

		frame.getContentPane().add(BorderLayout.NORTH, buttonPanel);
		frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
		frame.show();
	}
	/**
	 * mouseClicked method comment.
	 */
	public void mouseClicked(MouseEvent e) {
	}
	/**
	 * mouseDragged method comment.
	 */
	public void mouseDragged(MouseEvent e) {
		tempRect.setFrameFromDiagonal(start.getX(), start.getY(), e.getX(), e.getY());
		repaint();
	}
	/**
	 * mouseEntered method comment.
	 */
	public void mouseEntered(MouseEvent e) {
	}
	/**
	 * mouseExited method comment.
	 */
	public void mouseExited(MouseEvent e) {
	}
	/**
	 * mouseMoved method comment.
	 */
	public void mouseMoved(MouseEvent e) {
	}
	/**
	 * mousePressed method comment.
	 */
	public void mousePressed(MouseEvent e) {
		start = new Point2D.Double();
		start.setLocation(e.getPoint());
	}
	/**
	 * mouseReleased method comment.
	 */
	public void mouseReleased(MouseEvent e) {
		if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0)
			locked.add(tempRect);
		else
			toPlace.add(tempRect);
		//tempRect=new Rectangle2D.Double();
		tempRect = new Rectangle2D.Double(-1, -1, 0, 0);
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
		Rectangle2D rect = null;
		Object o = null;
		Iterator iter = locked.iterator();
		while (iter.hasNext()) {
			if ((o = iter.next()) instanceof CoRectClass)
				rect = ((CoRectClass) o).getRect();
			else
				rect = (Rectangle2D) o;
			drawRectFill(g, rect, Color.lightGray, Color.gray);
		}
		iter = toPlace.iterator();
		while (iter.hasNext()) {
			if ((o = iter.next()) instanceof CoRectClass)
				rect = ((CoRectClass) o).getRect();
			else
				rect = (Rectangle2D) o;
			drawRectFill(g, rect, Color.green, Color.blue);
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
	public void placeRects(Collection locked, Collection toPlace, Rectangle2D container, int align, CoCalculateDistanceIF dist) {
		int aligntemp = align;
		CoRectanglePlacement rp = new CoRectanglePlacement();
		List freeRects = new LinkedList();
		freeRects.add(container);
		Iterator iter = locked.iterator();
		while (iter.hasNext()) {
			CoRectClass rc = (CoRectClass) iter.next();
			Rectangle2D rect = rc.getRect();
			freeRects = rp.reduceRectangles(rp.removeRect(rect, freeRects));
		}
		Point2D containerPoint =
			new Point2D.Double(-CoCornerUtilities.calculateDisplacement(align).getX() * container.getWidth(), -CoCornerUtilities.calculateDisplacement(align).getY() * container.getHeight());
		iter = toPlace.iterator();
		Object o = null;
		Rectangle2D rect = null;
		while (iter.hasNext()) {
			if ((o = iter.next()) instanceof CoRectClass) {
				CoRectClass rc = (CoRectClass) o;
				rect = rc.getRect();
				align = rc.getAlign();
			} else {
				rect = (Rectangle2D) o;
				align = aligntemp;
			}
			containerPoint = new Point2D.Double(-CoCornerUtilities.calculateDisplacement(align).getX() * container.getWidth(), -CoCornerUtilities.calculateDisplacement(align).getY() * container.getHeight());

			Point2D point = rp.choseBestPoint(rect, rp.getPoints(rp.getFreeRects(rect, freeRects), align), containerPoint, align, dist);
			rect.setRect(point.getX(), point.getY(), rect.getWidth(), rect.getHeight());
			freeRects = rp.reduceRectangles(rp.removeRect(rect, freeRects));
		}
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
	public void placeRects3(Collection locked, Collection toPlace, Rectangle2D container, int align, CoCalculateDistanceIF dist) {
		CoRectanglePlacement rp = new CoRectanglePlacement();
		List freeRects = new LinkedList();
		freeRects.add(container);
		Iterator iter = locked.iterator();
		while (iter.hasNext()) {
			Rectangle2D rect = (Rectangle2D) iter.next();
			freeRects = rp.reduceRectangles(rp.removeRect(rect, freeRects));
		}
		Point2D containerPoint =
			new Point2D.Double(-CoCornerUtilities.calculateDisplacement(align).getX() * container.getWidth(), -CoCornerUtilities.calculateDisplacement(align).getY() * container.getHeight());
		iter = toPlace.iterator();
		while (iter.hasNext()) {
			Rectangle2D rect = (Rectangle2D) iter.next();
			Point2D point = rp.choseBestPoint(rect, rp.getPoints(rp.getFreeRects(rect, freeRects), align), containerPoint, align, dist);
			rect.setRect(point.getX(), point.getY(), rect.getWidth(), rect.getHeight());
			freeRects = rp.reduceRectangles(rp.removeRect(rect, freeRects));
		}
	}
}