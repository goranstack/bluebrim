package com.bluebrim.swing.client;

import java.awt.*;

import javax.swing.*;
/**
		
 * 
 */
public class CoSeparator extends JSeparator {
		private boolean direction;
		public static boolean VERTICAL          = true;
		public static boolean HORIZONTAL        = false;
/**
 * CoSeparator constructor comment.
 */
public CoSeparator() {
		this(HORIZONTAL);
}
/**
  */
public CoSeparator(boolean direction) {
		super(direction ? SwingConstants.VERTICAL : SwingConstants.HORIZONTAL);
		this.direction = direction;
}
/**
  */
public Dimension getMaximumSize() {
		Dimension tSize =super.getMaximumSize();
		if (direction == VERTICAL)
				tSize.width     = 2;
		else
				tSize.height = 2;
		return tSize;
}
/**
  */
public Dimension getPreferredSize() {
		Dimension tSize =super.getPreferredSize();
		if (direction == VERTICAL)
				tSize.width     = 2;
		else
				tSize.height = 2;
		return tSize;
}
/**
  */
public Dimension getSize() {
		Dimension tSize =super.getSize();
		if (direction == VERTICAL)
				tSize.width     = 2;
		else
				tSize.height = 2;
		return tSize;
}
}
