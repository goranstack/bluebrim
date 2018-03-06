package com.bluebrim.layout.impl.client.editor;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.swing.client.*;

/**
 * Layout editor operation: Adjust the bounds of an content box to the natural size of the content.
 * 
 * @author: Dennis
 */
 
public class CoAdjustToEmbeddedPath extends CoLayoutEditorAction
{
public CoAdjustToEmbeddedPath( String name, CoLayoutEditor e )
{
	super( name, e );
}
public CoAdjustToEmbeddedPath( CoLayoutEditor e )
{
	super( e );
}
public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	CoPageItemImageContentView v = m_editor.getCurrentImageContentView();

	if
		( v != null )
	{
		if
			( m_listBox == null )
		{
			Container c = m_editor.getPanel().getTopLevelAncestor();
			if
				( c instanceof Frame )
			{
				m_dialog = new JDialog( (Frame) c, "", true );
			} else if
				( c instanceof Dialog )
			{
				m_dialog = new JDialog( (Dialog) c, "", true );
			} else {
				m_dialog = new JDialog( new Frame(), "", true );
			}
			
			m_listBox = m_editor.getUIBuilder().createListBox();
			m_listBox.getList().setVisibleRowCount( 10 );

			m_dialog.getContentPane().add( m_listBox );

			m_listBox.getList().getSelectionModel().addListSelectionListener(
				new ListSelectionListener()
				{
					public void valueChanged( ListSelectionEvent ev )
					{
						m_dialog.setVisible( false );
					}
				}
			);
		}

		DefaultListModel m = new DefaultListModel();
		java.util.Iterator i = v.getImageContent().getEmbeddedPathMap().entrySet().iterator();
		while
			( i.hasNext() )
		{
			java.util.Map.Entry e = (java.util.Map.Entry) i.next();
			m.addElement( e.getKey() );
		}

		m_listBox.getList().clearSelection();
		m_listBox.getList().setModel( m );

		m_dialog.pack();
		m_dialog.setLocationRelativeTo( m_editor.getPanel() );
		m_dialog.show();

		CoPageItemCommands.SET_EMBEDDED_PATH_SHAPE.prepare( v.getOwner(), (String) m_listBox.getList().getSelectedValue() );
		m_editor.getCommandExecutor().doit( CoPageItemCommands.SET_EMBEDDED_PATH_SHAPE, v );
	}
}

	private JDialog m_dialog;
	private CoListBox m_listBox;

public void prepare( CoShapePageItemView v )
{
	super.prepare( v );

	if
	 	( check( v, CoContentWrapperPageItemView.class ) )
	{
		CoPageItemContentView cv = ( (CoContentWrapperPageItemView) v ).getContentView();
		if
			( check( cv, CoPageItemImageContentView.class ) )
		{
			CoPageItemImageContentView icv = (CoPageItemImageContentView) cv;
			setEnabled( icv.hasContent() && icv.getImageContent().hasEmbeddedPath() );
		}
	}

}
}