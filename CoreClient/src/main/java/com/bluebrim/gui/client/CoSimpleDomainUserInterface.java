package com.bluebrim.gui.client;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.swing.client.CoPanel;

public class CoSimpleDomainUserInterface extends CoDomainUserInterface
{
	public final static String TO_STRING = "TO_STRING";
public CoSimpleDomainUserInterface()
{
	super();
}
public CoSimpleDomainUserInterface( CoObjectIF domain )
{
	super( domain );
}
protected void createValueModels( CoUserInterfaceBuilder b )
{
	b.createTextAreaAdaptor(
		new CoValueModel()
		{
			public Object getValue()
			{
				if
					( getDomain() == null )
				{
					return "null";
				} else {
					return getDomain().toString();
				}
			}
			public void setValue( Object newValue )
			{
			}
		}, 
	(JTextArea) b.getNamedWidget( TO_STRING )
	);

}
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder b )
{
	super.createWidgets( p, b );

	p.setLayout( new BorderLayout() );
	
	JTextArea t = b.createTextArea( TO_STRING );
	t.setEditable( false );
	
	p.add( new JLabel( "Domain object: " ), BorderLayout.NORTH );
	p.add( new JScrollPane( t ), BorderLayout.CENTER  );
}
}
