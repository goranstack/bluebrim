package com.bluebrim.font.impl.client;

import java.awt.*;
import java.awt.event.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;


/**
 * 
 * 
 * @author: Dennis
 */
 
public class CoFontSpecDialog extends CoDialog
{
	private CoFontSpecPanel m_fontSpecPanel;
	private boolean m_wasCanceled;

public CoFontSpecDialog( CoUserInterfaceBuilder builder, String label, Dialog owner )
{
	super( owner, "", true );

	init( builder, label );
}
public CoFontSpecDialog( CoUserInterfaceBuilder builder, String label, Frame owner )
{
	super( owner, "", true );

	init( builder, label );
}
private void init( CoUserInterfaceBuilder builder, String label )
{
	getContentPane().setLayout( new CoColumnLayout() );

	getContentPane().add( builder.createLabel( label ) );

	m_fontSpecPanel = new CoFontSpecPanel( builder );
	getContentPane().add( m_fontSpecPanel );

	CoPanel buttons = builder.createPanel( new CoRowLayout() );
	getContentPane().add( buttons );
	
	CoButton b = builder.createButton( "Ok", null );
	buttons.add( b );
	b.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				m_wasCanceled = false;
				setVisible( false );
			}
		}
	);
	
	b = builder.createButton( "Cancel", null );
	buttons.add( b );
	b.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				setVisible( false );				
			}
		}
	);
	
}
public com.bluebrim.font.shared.CoFontFaceSpec open( Component invoker, com.bluebrim.font.shared.CoFontFaceSpec spec )
{
	m_fontSpecPanel.set( spec );
	m_wasCanceled = true;

	setLocationRelativeTo( invoker );
	setVisible( true );

	return m_wasCanceled ? null : m_fontSpecPanel.get();
}
}
