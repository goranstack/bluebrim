package com.bluebrim.layoutmanager.test;

import java.awt.*;
import java.awt.event.*;

public class Terminator extends WindowAdapter {

	Frame frame;

	public Terminator(Frame frame) {
		super();
		this.frame = frame;
	} // Terminator()
	public void windowClosing(WindowEvent e) {
		// termiate the program when the window is being closed
		e.getWindow().dispose();
		if(e.getWindow() == frame)
			System.exit(0);
	} // windowClosing()
} // class Terminator
