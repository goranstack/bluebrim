package com.bluebrim.swing.client.test;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

import com.bluebrim.swing.client.*;

/**
 * @author Göran Stäck
 *
 */
public class CoOptionMenuTest implements ComboBoxModel{

	List items = new ArrayList();
	Object selectedItem;
	
	CoOptionMenu menu1 = new CoOptionMenu(this);
	CoOptionMenu menu2 = new CoOptionMenu(this);

//	JComboBox menu1 = new JComboBox(this);
//	JComboBox menu2 = new JComboBox(this);

	public static void main(String[] args) {
		new CoOptionMenuTest().run();
	}

	private void run() {
		items.add("Första");
		items.add("Andra");
		items.add("Tredje");
		items.add("Fjärde");
		
		JFrame frame = new JFrame("Test of CoOptionMenu");
		frame.setBounds(300,300,300,300);
		Container cont = frame.getContentPane();
		cont.setLayout(new FlowLayout());
		cont.add(new JLabel("Hello"));
		
		cont.add(menu1);
		cont.add(menu2);
		
		frame.show();
	}

	public Object getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(Object anItem) {
		selectedItem = anItem;
		
	}

	public int getSize() {
		return items.size();
	}

	public Object getElementAt(int index) {
		return items.get(index);
	}

	public void addListDataListener(ListDataListener l) {
		
	}

	public void removeListDataListener(ListDataListener l) {
		
	}
}
