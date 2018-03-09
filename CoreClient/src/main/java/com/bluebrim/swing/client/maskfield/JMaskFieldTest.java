package com.bluebrim.swing.client.maskfield;

/*
=====================================================================

	JMaskFieldTest.java
	
	Created by Claude Duguay
	Copyright (c) 1998

=====================================================================
*/

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class JMaskFieldTest extends JPanel
{
	JMaskField field;
	
	public JMaskFieldTest()
	{
		int count = 9;
		setLayout(new BorderLayout());
		setBorder(new CompoundBorder(
			new TitledBorder("Masked Fields"),
			new EmptyBorder(5, 5, 5, 5)));

		JPanel tNorthPanel	= new JPanel();
		tNorthPanel.setLayout(new BorderLayout());
		JPanel prompt = new JPanel();
		prompt.setLayout(new GridLayout(count, 1, 5, 5));
		JPanel fields = new JPanel();		
		fields.setLayout(new GridLayout(count, 1, 5, 5));
		tNorthPanel.add("West", prompt);
		tNorthPanel.add("Center", fields);

		add(tNorthPanel, BorderLayout.NORTH);
		
		MaskMacros macros = new MaskMacros();
		macros.addMacro('#', "[0-9]");
		macros.addMacro('@', "[a-zA-Z]");
		macros.addMacro('*', "[0-9a-zA-Z-]");
		
		int labelAlignment = JLabel.RIGHT;
		prompt.add(new JLabel("Phone: ", labelAlignment));
		fields.add(field = new JMaskField("(###) ###-####", macros));
		
		prompt.add(new JLabel("Zip Code: ", labelAlignment));
		fields.add(new JMaskField("#####-####", macros));

		prompt.add(new JLabel("Postal Code: ", labelAlignment));
		fields.add(new JMaskField("@#@ #@#", macros));

		prompt.add(new JLabel("Credit Card: ", labelAlignment));
		fields.add(new JMaskField("#### #### #### ####", macros));

		prompt.add(new JLabel("Personnummer: ", labelAlignment));
		fields.add(new JMaskField("######-####", macros));

		prompt.add(new JLabel("Dag: ", labelAlignment));
//		fields.add(new JMaskField("{[01]}#/{[0-3]}#/##", macros));
		fields.add(new JMaskField("************", macros));

		prompt.add(new JLabel("Period: ", labelAlignment));
//		fields.add(new JMaskField("{[01]}#/{[0-3]}#/##", macros));
		fields.add(new JMaskField("********** - **********", macros));

		prompt.add(new JLabel("Tid: ", labelAlignment));
		fields.add(new JMaskField("kl {[0-2]}#.{[0-5]}#", macros));

		prompt.add(new JLabel("Belopp: ", labelAlignment));
		fields.add(new JMaskField("{[0-20]}# SEK", macros));
	}
	public static void main(String[] args)
	{
		PLAF.setNativeLookAndFeel(true);
		JFrame frame = new JFrame("JMaskFieldTest");
		JMaskFieldTest field = new JMaskFieldTest();
		frame.getContentPane().add(field);
		frame.setLocation(100, 100);
		frame.pack();
		frame.show();
	}
}
