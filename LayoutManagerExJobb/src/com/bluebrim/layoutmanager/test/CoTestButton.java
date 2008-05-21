package com.bluebrim.layoutmanager.test;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.bluebrim.layoutmanager.*;
/**
 * Insert the type's description here.
 * Creation date: (2000-06-09 14:07:41)
 * @author: Arvid Berg & Masod Jalalian 
 */
public class CoTestButton extends JButton implements ActionListener {
	private List list;
	public JPanel jp;
	/**
	 * CoTestButton constructor comment.
	 */
	public CoTestButton() {
		super();
	}
	/**
	 * CoTestButton constructor comment.
	 * 
	 * @param text
	 *            java.lang.String
	 */
	public CoTestButton(String text) {
		super(text);
	}
	/**
	 * CoTestButton constructor comment.
	 * 
	 * @param text
	 *            java.lang.String
	 * @param icon
	 *            javax.swing.Icon
	 */
	public CoTestButton(String text, javax.swing.Icon icon) {
		super(text, icon);
	}
	/**
	 * Insert the method's description here. Creation date: (2000-06-09
	 * 14:08:22)
	 * 
	 * @param param
	 *            java.util.Collection
	 */
	public CoTestButton(java.util.List param, javax.swing.JPanel f) {
		super("Next");
		list = param;
		jp = f;
	}
	/**
	 * CoTestButton constructor comment.
	 * 
	 * @param icon
	 *            javax.swing.Icon
	 */
	public CoTestButton(javax.swing.Icon icon) {
		super(icon);
	}
	/**
	 * actionPerformed method comment.
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {
		list.clear();
		Random rand = new Random();
		double area = 0;
		double totarea = 500 * 500;
		for (int i = 0; i < 30; i++) {
			RectIMLayoutable rect = new RectIMLayoutable(100 * Math.ceil(rand
					.nextDouble() * 2.0 + 0.1), rand.nextDouble() * 200 + 50);
			if ((area + rect.getArea()) <= totarea) {
				list.add(rect);
				area += rect.getArea();
			}
		}
		CoInterval.sort(list);
		CoTestClass.layout(list);
		jp.repaint();
	}
}