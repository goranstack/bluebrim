package com.bluebrim.gui.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JRootPane;

import com.bluebrim.swing.client.CoButton;
import com.bluebrim.swing.client.CoLabel;
import com.bluebrim.swing.client.CoPanel;
import com.bluebrim.swing.client.CoTextField;

public class CoInputDialog extends CoDialog {
	protected String m_answer = "";

	// Text field that holds the answer.
	protected CoTextField m_textField;
	protected CoLabel m_questionLabel;
public CoInputDialog( Component c )
{
	this( getJFrame( c ) );
}
public CoInputDialog( JFrame frame )
{
	this( frame, "", "" );
}
public CoInputDialog( JFrame frame, String title, String question )
{
	super( frame, title, true );
	init();
	setQuestion( question );
}
public String getInput()
{
	return m_answer;
}
private static JFrame getJFrame( Component c )
{
	while
		( ( c != null ) &&
			( ! ( c instanceof JFrame ) ) )
	{
		c = c.getParent();
	}
	
	return (JFrame) c;
}
private void init()
{
	JRootPane pane = new JRootPane();
	pane.setBackground(Color.white);
	pane.setLayout( new BoxLayout( pane, BoxLayout.Y_AXIS ) );
		
	// Mellanrum
	pane.add( Box.createVerticalStrut( 5 ) );
	
	{ // Rubriken
	
		CoPanel p = new CoPanel( null, false, null );
		p.setLayout( new BoxLayout( p, BoxLayout.X_AXIS ) );
		p.setBackground(Color.white);
		p.add( Box.createHorizontalStrut( 10 ) );
		p.add( m_questionLabel = new CoLabel( "" ) );
		p.add( Box.createHorizontalStrut( 10 ) );
		p.add( Box.createHorizontalGlue() );
		pane.add(p);
	}
		
	// Mellanrum
	pane.add( Box.createVerticalStrut( 2 ) );
	
	{ // Textfältet
	
		CoPanel p = new CoPanel( null, false, null );
		p.setLayout( new BoxLayout( p, BoxLayout.X_AXIS ) );
		p.setBackground(Color.white);
		p.add( Box.createHorizontalStrut( 10 ) );
		p.add( 	m_textField = new CoTextField() );
		p.add( Box.createHorizontalStrut( 10 ) );
		pane.add(p);
	}
		
	// Mellanrum
	pane.add( Box.createVerticalStrut( 10 ) );
	
	CoButton okButton = new CoButton(CoUIStringResources.getName("OK"));
	okButton.setDefaultCapable( true );
	okButton.setFocusPainted( true );
	
	CoButton cancelButton = new CoButton(CoUIStringResources.getName("CANCEL"));
	cancelButton.setDefaultCapable( true );
	cancelButton.setFocusPainted( true );

	{ // Knapparna
		CoPanel p = new CoPanel( null, false, null );
		p.setLayout( new BoxLayout( p, BoxLayout.X_AXIS ) );
		p.setBackground(Color.white);
		p.add( Box.createHorizontalStrut( 10 ) );
		p.add( okButton );
		p.add( Box.createHorizontalStrut( 10 ) );
		p.add( cancelButton );
		p.add( Box.createHorizontalStrut( 10 ) );
		p.add( Box.createHorizontalGlue() );
		pane.add(p);
	}
		
	// Mellanrum
	pane.add( Box.createVerticalStrut( 10 ) );
	
	pane.setDefaultButton( okButton );
	
	getContentPane().add( pane );
	getContentPane().setBackground(Color.white);

	// Actionlisteners
	ActionListener l = new ActionListener()
	{
		public void actionPerformed( ActionEvent e )
		{
			m_answer = m_textField.getText();
			setClosingReason(CLOSED_BY_OK);
			setVisible( false );
		}
	};
	
	m_textField.addActionListener( l );
	okButton.addActionListener( l );

	cancelButton.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			m_answer = null;
			setClosingReason(CLOSED_BY_CANCEL);
			setVisible(false);
		}
	});


	pack();

}
/**
 * This method was created in VisualAge.
 * @param args java.lang.String[]
 */
public static void main(String args[])
{
	try
	{
		JFrame m_frame = new JFrame("Test");
		m_frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		m_frame.setLocation(400, 0);
		m_frame.setSize(700, 600);
		m_frame.pack();
		m_frame.show();
		CoInputDialog d = new CoInputDialog( m_frame, "Titel", "Ange önskat tonvärde?" );
		String value = d.openDialog();
		System.out.println("Svar: " + value);
		System.exit(0);
	}
	catch (Throwable exception)
	{
		System.err.println("Exception occurred in main() of CoOthreInputDialog");
		exception.printStackTrace();
	}
}
public String openDialog()
{
	setBounds(CoGUI.centerOnScreen(getBounds()));
	show();
	return getInput();
}
public String openDialog( String title, String question )
{
	setTitle( title );
	setQuestion( question );
	return openDialog();
}
private void setQuestion( String q )
{
	m_questionLabel.setText( q );
	pack();
}
}
