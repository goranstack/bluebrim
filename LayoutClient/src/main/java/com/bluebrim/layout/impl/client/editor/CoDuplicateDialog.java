package com.bluebrim.layout.impl.client.editor;

import java.awt.*;
import java.awt.event.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.client.*;

/**
 * Dialog for entering parameters for the "duplicate and repeat page item" operation.
 *
 * @author: Dennis
 */
 
class CoDuplicateDialog extends CoDialog
{
	public static final String COUNT = "DUPLICATE_COUNT";
	public static final String DX = "DUPLICATE_DX";
	public static final String DY = "DUPLICATE_DY";
	public static final String OK = "DUPLICATE_OK";
	public static final String CANCEL = "DUPLICATE_CANCEL";

	private CoDuplicateAndRepeat m_action;

	private CoNumericTextField m_countTextField;
	private CoNumericTextField m_dxTextField;
	private CoNumericTextField m_dyTextField;
public CoDuplicateDialog( CoDuplicateAndRepeat action, Dialog owner )
{
	super( owner, "", true );

	m_action = action;
}
public CoDuplicateDialog( CoDuplicateAndRepeat action, Frame owner )
{
	super( owner, "", true );

	m_action = action;
}
protected void create()
{
	CoUserInterfaceBuilder b = new CoUserInterfaceBuilder( null );

	Container c = getContentPane();

	c.setLayout( new CoColumnLayout() );

	{
		CoPanel p = b.createPanel( new CoFormLayout() );
		c.add( p );

		p.add( b.createLabel( CoLayouteditorUIStringResources.getName( COUNT ) ) );
		m_countTextField = new CoNumericTextField( null, 5 );
		b.prepareTextField( m_countTextField );
		p.add( m_countTextField );
		
		p.add( b.createLabel( CoLayouteditorUIStringResources.getName( DX ) ) );
		m_dxTextField = new CoNumericTextField( CoLengthUnit.LENGTH_UNITS, 10 );
		b.prepareTextField( m_dxTextField );
		p.add( m_dxTextField );
		
		p.add( b.createLabel( CoLayouteditorUIStringResources.getName( DY ) ) );
		m_dyTextField = new CoNumericTextField( CoLengthUnit.LENGTH_UNITS, 10 );
		b.prepareTextField( m_dyTextField );
		p.add( m_dyTextField );
	}
	
	{
		CoPanel p = b.createPanel( new FlowLayout() );
		c.add( p );

		CoButton button = b.createButton( CoLayouteditorUIStringResources.getName( OK ), null );
		p.add( button );
		button.addActionListener(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent ev )
				{
					doit();
				}
			}
		);
		
		button = b.createButton( CoLayouteditorUIStringResources.getName( CANCEL ), null );
		p.add( button );
		button.addActionListener(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent ev )
				{
					setVisible( false );
				}
			}
		);
	}
}
private void doit()
{
	setVisible( false );

	int count = Integer.parseInt( m_countTextField.getText() );
	
	double dx = 0;
	double dy = 0;

	try
	{
		dx = Double.parseDouble( m_dxTextField.getText() );
	}
	catch ( NumberFormatException ex )
	{
		m_dxTextField.setText( "0" );
	}
	
	try
	{
		dy = Double.parseDouble( m_dyTextField.getText() );
	}
	catch ( NumberFormatException ex )
	{
		m_dyTextField.setText( "0" );
	}
	
	m_action.doDuplicate( count, dx, dy );
}
public void open()
{
	if
		( m_countTextField == null )
	{
		create();
		pack();
	}
	
	show();
}
}
