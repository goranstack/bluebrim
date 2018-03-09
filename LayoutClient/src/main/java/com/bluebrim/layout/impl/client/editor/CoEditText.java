package com.bluebrim.layout.impl.client.editor;

import java.awt.*;
import java.awt.event.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.client.*;

/**
 * Layout editor operation: Edit text in separate (simple but fast) text editor.
 * 
 * @author: Dennis
 */
 
public class CoEditText extends CoExternalUIDialogAction
{
	protected CoSimpleTextEditorPane m_simpleTextEditor;
public CoEditText( String name, CoLayoutEditor e )
{
	super( name, e );
}
public CoEditText( CoLayoutEditor e )
{
	super( e );
}
public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	final CoPageItemAbstractTextContentView v = m_editor.getCurrentTextContentView();

	boolean firstCall = m_dialog == null;
	
	getSimpleTextEditorDialog();

	m_simpleTextEditor.setContext( v.getDocument() );
 
	boolean b = m_editor.startTextEditing( v, m_simpleTextEditor.getTextEditor(), false );

	if
		( b )
	{
	  m_dialog.addWindowListener(
			new WindowAdapter()
			{
				public void windowClosing( WindowEvent e )
				{
					m_editor.stopTextEditing( v, m_simpleTextEditor.getTextEditor() );
					m_dialog.removeWindowListener( this );
				}
			}
		);

	  if
	  	( firstCall )
	  {
			m_dialog.pack();
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			m_dialog.setSize( Math.min( m_dialog.getWidth(), (int) ( d.height * 0.8 ) ), 500 );
	  }

	  m_dialog.show();
	}
}
protected void buildSimpleTextEditorDialog() 
{
	CoUserInterfaceBuilder builder = m_editor.getUIBuilder();
	com.bluebrim.menus.client.CoMenuBuilder mb = m_editor.getMenuBuilder();
	
	m_simpleTextEditor = new CoSimpleTextEditorPane( builder, mb );
	
	m_simpleTextEditor.setBackground( Color.white );
	m_simpleTextEditor.setOpaque( false );

	
	CoPanel p = new CoPanel( new BorderLayout() );
	p.add( m_simpleTextEditor, BorderLayout.CENTER );

	CoZoomPanel zoomPanel = new CoZoomPanel( builder, CoLayouteditorUIStringResources.getName( SCALE ), 1, new double [] { 50, 100, 110, 120, 150, 200, 400 }, null );
	zoomPanel.setZoomable(
		new CoZoomPanel.Zoomable()
		{
			public void setScale( double scale )
			{
				m_simpleTextEditor.setScale( scale / 100.0 );
			}
			public double getScale()
			{
				return m_simpleTextEditor.getScale() * 100;
			}
		}
	);


	p.add( zoomPanel, BorderLayout.NORTH );
	
	m_dialog.getContentPane().add( p );

	m_dialog.pack();
	m_dialog.setSize( m_dialog.getWidth(), 500 );
}
protected CoDialog getSimpleTextEditorDialog()
{
	if
		( m_dialog == null )
	{
		Window w = m_editor.getWindow();
		if
			( w instanceof Frame )
		{
			Frame f = (Frame) w;
			m_dialog = new CoDialog( f, "", true );
		} else if
			( w instanceof Dialog )
		{
			Dialog d = (Dialog) w;
			m_dialog = new CoDialog( d, "", true );
		}
		
		buildSimpleTextEditorDialog();
	}

	return m_dialog;
}
void setContext( CoPageItemEditorContextIF c )
{
}

	public static final String SCALE = "CoEditText.SCALE";
}