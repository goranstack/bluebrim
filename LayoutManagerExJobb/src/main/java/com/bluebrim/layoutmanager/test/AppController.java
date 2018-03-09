package com.bluebrim.layoutmanager.test;

import java.awt.*;
import java.io.*;

import com.bluebrim.layoutmanager.*;
		
/**
 * This class is the controller of the whole application and is responsible
 * for displaying the initial screen for accepting and validating inputs
 * @author Murali Ravirala
 * @version 1.0
 */

public class AppController extends Frame {
	// text boxes for accepting the target height and width
	TextField targetWidth, targetHeight;
	// text box for accepting the maximum percentage of the area that can
	// be occupied
	TextField maxpercent, leftGridWidth, rightGridWidth, horizGridWidth;
	// text boxes for accepting the dimensions of an element
	// of the  collection rectangle
	TextField collWidth, collHeight;
	// this list box contains the collection rectangles added by the user
	List list;

	MrConstraint constraint;

	RectangularCollection collection;
	RectIMLayoutable target;

	boolean mutex = true;
	Arranger arranger;
	Thread arrangerThread;

	/**
	 * Generates the screen to accept input parameters
	 */

   public AppController() {

		setBackground(Color.lightGray);

		// the main screen has a border layout with the background as gray
		setLayout(new BorderLayout());
		Panel p1 = new Panel();
		Panel p2 = new Panel();
		Panel p5 = new Panel();

		// this panel comes on the right hand side and has a
		// list box that displays the values of the rectangles
		// and the Remove button
		p1.setLayout(new GridLayout(4,1));
		add("East", p1);
		p1.add(new Panel());
		p1.add(new Panel());
		p1.add(p2);
		p1.add(p5);

		Button btnRemove = new Button("Remove");
		ButtonActionListener removeListener = new ButtonActionListener(this);
		btnRemove.addActionListener(removeListener);
		btnRemove.setActionCommand("Remove");
		// display the remove button on the extreme right hand side
		p5.add(btnRemove);
		// display nine elements at a time with multiple select disabled
		list = new List(9,false);
		p2.add(list);

		// the central panel has a grid layout
		GridLayout gridLayout = new GridLayout(23, 5);

		Panel p = new Panel();
		add(p);
		p.setLayout(gridLayout);

		addBlankLabels(p, 5); // add a blank line

		addBlankLabels(p, 1);
		setFont(new Font("Times Roman", Font.BOLD, 15));
		p.add(new Label(" Target Rectangle", Label.RIGHT));
		p.add(new Label(" Parameters", Label.LEFT));
		addBlankLabels(p, 2);

		addBlankLabels(p, 5); // add a blank line to the grid

		//addBlankLabels(p, 5); // add a blank line to the grid
		setFont(new Font("Courier", Font.PLAIN, 12));
		// add text boxes to input the height and the width of the target
		// rectangle
		addBlankLabels(p, 1);
		p.add(new Label("  Width: ", Label.CENTER));
		targetWidth = new TextField(5);
		p.add(targetWidth);
		p.add(new Label("  Height: ", Label.CENTER));
		targetHeight = new TextField(5);
		p.add(targetHeight);

		addBlankLabels(p, 5);

		addBlankLabels(p, 2);
		p.add(new Label(" Constraints ", Label.CENTER));
		addBlankLabels(p, 2);

		addBlankLabels(p, 5); // add a blank line to the grid

		p.add(new Label("LHS Grid Width:", Label.CENTER));
		leftGridWidth = new TextField(5);
		p.add(leftGridWidth);

		p.add(new Label("RHS Grid Width:", Label.CENTER));
		rightGridWidth = new TextField(5);
		p.add(rightGridWidth);
		addBlankLabels(p, 1);

		addBlankLabels(p, 5);

		p.add(new Label("Horiz.Grid Width:", Label.CENTER));
		horizGridWidth = new TextField(5);
		p.add(horizGridWidth);
		
		// add a text box to accept the maximum percentage from the input
		p.add(new Label("Max. Percent:", Label.CENTER));
		maxpercent = new TextField(7);
		p.add(maxpercent);
		p.add(new Label(" (1-100) ", Label.CENTER));

		addBlankLabels(p, 5); // add a blank line

		addBlankLabels(p, 5); // add a blank line

		addBlankLabels(p, 1);
		setFont(new Font("Times Roman", Font.BOLD, 15));
		p.add(new Label("Collection Rect", Label.RIGHT));
		p.add(new Label("angle Parameters", Label.LEFT));
		addBlankLabels(p, 2);

		addBlankLabels(p, 5); // add a blank line
		addBlankLabels(p, 5); // add a blank line

		setFont(new Font("Courier", Font.PLAIN, 12));
		// add the width label and the text field to accept collection
		// rectangle width
		p.add(new Label("  Width: ", Label.CENTER));
		collWidth = new TextField(5);
		p.add(collWidth);
		addBlankLabels(p, 3);    // add a blank line

		addBlankLabels(p, 3);    // add a blank line
		// add the Add button
		Button btnAdd = new Button("Add =>");
		ButtonActionListener addActionListener = new ButtonActionListener(this);
		btnAdd.addActionListener(addActionListener);
		btnAdd.setActionCommand("Add");
		p.add(btnAdd);
		addBlankLabels(p, 1);

		// add the height label and the text field to accept collection
		// rectangle height
		p.add(new Label("  Height: ", Label.CENTER));
		collHeight = new TextField(5);
		p.add(collHeight);
		addBlankLabels(p, 3);

		addBlankLabels(p, 5); // add a blank line

		addBlankLabels(p, 5); // add a blank line

		addBlankLabels(p, 2);
		Button btnPlace = new Button("Place");
		ButtonActionListener placeActionListener = new ButtonActionListener(this);
		btnPlace.setActionCommand("Place");
		btnPlace.addActionListener(placeActionListener);
		// add the place button to the bottom
		p.add(btnPlace);
		addBlankLabels(p, 2);

		addBlankLabels(p, 5); // add a blank line
		addBlankLabels(p, 5); // add a blank line

		// set the size of the frame
		setSize(750, 575);
		setVisible(true);
		addWindowListener(new Terminator(this));
	} // AppController()
	/**
	 * A utility method that just puts blank labels to
	 * the grid
	 * @param  p  the panel into which the labels are to be put
	 * @param  n  the number of blank to be put
	 */

	private void addBlankLabels(Panel p, int n) {
		for(int i = 0; i < n; i++)
			p.add(new Label(""));
	} // addBlanks()
	/**
	 * This is the main method which initiates the whole application
	 * @param args  an array of string arguments
	 */
	public static void main(String[] args) {
		AppController appController = new AppController();
		try {
			System.in.read();
		} catch(IOException e) {}
		System.exit(0);
	} // main()
} // class AppController
