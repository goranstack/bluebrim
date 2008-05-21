package com.bluebrim.layoutmanager.test;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.layoutmanager.*;

/**
 * This class handles events that are generated by buttons and responds to
 * them accordingly
 */

class ButtonActionListener implements ActionListener {

	AppController frame; // frame to be controlled

	ButtonActionListener(AppController frame) {
		this.frame = frame;
	} // AddActionListener()
	public void actionPerformed(ActionEvent e) {

		// if the user has clicked the add button
		if(e.getActionCommand().equals("Add")) {
			// the user has clicked add button, so add the values
			// for width and height of the collection rectangle
			// to the list
			try {
				// get the height and width that the user has entered
				double width = Double.parseDouble(frame.collWidth.getText());
				double height = Double.parseDouble(frame.collHeight.getText());

				// check if the values are valid
				if((width > 0)&&(height > 0)) {
					// if so, add it to the list
					String s = "W=" + width + ", H=" + height;
					// add the values to the list on the right hand side
					frame.list.add(s);
					frame.collWidth.setText("");
					frame.collHeight.setText("");
				} else {
					MessageBox msgbox = new MessageBox(new Frame(), "Invalid Dimensions for Collection rectangle.", false);
				} // if-else
			} catch(NumberFormatException exp) {
				//System.out.println("Invalid dimensions for Collection rectangle.");
				MessageBox msgbox = new MessageBox(new Frame(), "Invalid dimensions for Collection rectangle.", false);
			} // try-catch
		} else if(e.getActionCommand().equals("Remove")) {
			// the user has clicked on the remove button
			// so remove the value from the list that he has selected
			int index = frame.list.getSelectedIndex();
			if(index > -1) {
				// if valid index, then remove the element
				frame.list.remove(index);
			}
		} else if(e.getActionCommand().equals("Place")) {
			// the user has clicked on place button

			try {
				// check if all the other values have been populated properly
				double trgwidth = Double.parseDouble(frame.targetWidth.getText());
				double trgheight = Double.parseDouble(frame.targetHeight.getText());
				double maxpercent = Double.valueOf(frame.maxpercent.getText()).doubleValue()/100;
				double leftGridWidth = Double.parseDouble(frame.leftGridWidth.getText());
				double rightGridWidth = Double.parseDouble(frame.rightGridWidth.getText());
				double horzGridWidth = Double.parseDouble(frame.horizGridWidth.getText());


				if((trgwidth > 0) && (trgheight > 0) && (maxpercent > 0) &&
				   (maxpercent <= 1.0) && (frame.list.getItemCount() > 0) &&
					(leftGridWidth > 0) && (rightGridWidth > 0) && (horzGridWidth > 0) &&
					(leftGridWidth <= trgwidth) && (rightGridWidth <= trgwidth) &&
					(horzGridWidth <= trgheight)) {

					// here we create the collection list and the container

					frame.target = new RectIMLayoutable(trgwidth, trgheight);
					frame.collection = new RectangularCollection();
					for(int i =0; i < frame.list.getItemCount(); i++) {
						String s = frame.list.getItem(i);
						// get the portion of the string between the first = and ,
						// which gives the width of the collection rectangle
						String temp = s.substring(s.indexOf("=")+1, s.indexOf(","));
						double width = Double.parseDouble(temp);
						// get the portion of the string between the last = and the end
						// which gives the height of the collection rectangle
						temp = s.substring(s.lastIndexOf("=")+1, s.length());
						double height = Double.parseDouble(temp);
						CoLayoutableIF rect = new RectIMLayoutable(width, height);
						frame.collection.addElement(rect);
					 } // for

					 // now we have created the target rectangle and the collection
					 // rectangles
					 frame.constraint = new MrConstraint(maxpercent, horzGridWidth,
													   horzGridWidth, leftGridWidth,
													   rightGridWidth);
					 GridArrangement arr = new GridArrangement();
					 SolutionConfig config = (SolutionConfig)arr.place(frame.target, frame.collection, frame.constraint, GridArrangement.BOTTOM_RIGHT);

					 System.out.println("Arranging the rectangles along BOTTOM RIGHT corner");
					 System.out.println("Target Rectangle dimensions: ");
					 frame.target.print();

					 System.out.println();
					 System.out.println("Constraint:");
					 System.out.println("Horz:" +  frame.constraint.getHorizontalBottomWidth() + " " +
										"Vert Left:"+ frame.constraint.getVerticalLeftWidth() + " " +
										"Vert Right:" + frame.constraint.getVerticalRightWidth());

					 System.out.println();
					 System.out.println("Dimension of selected Collection Rectangles: ");
					 MrCollection coll = config.getContainer().getCollection();
					 for(Enumeration e1 = coll.enumerate(); e1.hasMoreElements(); ) {
						 ((RectIMLayoutable)e1.nextElement()).print();
					 } // for

					 System.out.println();
					 System.out.println("The % area covered is: " +
							   ((double)config.getOccupiedArea()*100)/frame.target.getArea());

					 System.out.println();
					 System.out.println("The total distance is: " + config.getDistance());

					 System.out.println();
					 System.out.println("The locations for the selection rectangles:");
					 Vector locations = config.getLocations();
					 for(int i = 0; i < locations.size(); i++)
						System.out.println("Point : x = " + ((Point2D)locations.elementAt(i)).getX() + " y = " + ((Point2D)locations.elementAt(i)).getY());

					 // now start another thread of computation
					 frame.mutex = true;
					 frame.arranger = new Arranger(frame.target, frame.collection, frame.constraint, GridArrangement.BOTTOM_LEFT);
					 frame.arrangerThread = new Thread(frame.arranger);
					 frame.arrangerThread.start();

					 // now start the display for the first one
					 ViewManager viewManager = new ViewManager(frame, config);
					 //frame.dispose();
					 frame.setVisible(false);
					 try {
						 frame.arrangerThread.join();
					 } catch(InterruptedException exp) { }

					 frame.mutex = false;
					 frame.arrangerThread = null; // computation over

					 System.out.println();
					 System.out.println("Arranging the rectangles along BOTTOM LEFT corner");
					 config = (SolutionConfig)arr.place(frame.target, frame.collection, frame.constraint, GridArrangement.BOTTOM_LEFT);
					 System.out.println();
					 System.out.println("Target Rectangle dimensions: ");
					 frame.target.print();
					 System.out.println();
					 System.out.println("Constraint:");
					 System.out.println("Horz:" +  frame.constraint.getHorizontalBottomWidth() + " " +
										"Vert Left:"+ frame.constraint.getVerticalLeftWidth() + " " +
										"Vert Right:" + frame.constraint.getVerticalRightWidth());
					 System.out.println();
					 System.out.println("Dimension of selected Collection Rectangles: ");
					 coll = config.getContainer().getCollection();
					 for(Enumeration e1 = coll.enumerate(); e1.hasMoreElements(); ) {
						 ((RectIMLayoutable)e1.nextElement()).print();
					 } // for

					 System.out.println();
					 System.out.println("The % area covered is: " +
							   ((double)config.getOccupiedArea()*100)/frame.target.getArea());

					 System.out.println();
					 System.out.println("The total distance is: " + config.getDistance());

					 System.out.println();
					 System.out.println("The locations for the selection rectangles:");
					 locations = config.getLocations();
					 for(int i = 0; i < locations.size(); i++)
						System.out.println("Point : x = " + ((Point2D)locations.elementAt(i)).getX() + " y = " + ((Point2D)locations.elementAt(i)).getY());

				} else {
				   if(maxpercent > 1.0 || maxpercent < 0) {
						MessageBox msgbox = new MessageBox(new Frame(), "Max percent should" +
								  "be less than or equal to 100", false);
				   } else if(frame.list.getItemCount() == 0) {
						MessageBox msgbox = new MessageBox(new Frame(), "No elements in the collection", false);
				   } else if ((trgwidth <= 0) || (trgheight <= 0)) {
						MessageBox msgbox = new MessageBox(new Frame(), "Invalid Dimensions for Target Rectangle.", false);
				   } else if((leftGridWidth <= 0) || (rightGridWidth <= 0) ||
							 (horzGridWidth <= 0)) {
						MessageBox msgbox = new MessageBox(new Frame(), "Dimensions for grids should be positive", false);
				   } else if((leftGridWidth > trgwidth) || (rightGridWidth > trgwidth) || (horzGridWidth > trgheight)){
					   MessageBox msgbox = new MessageBox(new Frame(), "Dimensions for grids cannot exceed target rectangle dimensions", false);
				   } else {
					   MessageBox msgbox = new MessageBox(new Frame(), "Invalid Input Specification.", false);
				   } // if-else
				} // if-else
			} catch(NumberFormatException exp) {
				MessageBox msgbox = new MessageBox(new Frame(), "Invalid dimensions for Target or Constraints.", false);
			} // try-catch
		} // if-else
	} //actionPerformed()
} // class ButtonActionListener