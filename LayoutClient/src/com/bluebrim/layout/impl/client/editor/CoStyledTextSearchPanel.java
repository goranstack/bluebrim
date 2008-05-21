package com.bluebrim.layout.impl.client.editor;
import java.awt.event.*;

import javax.swing.event.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.swing.client.*;

/**
 * UI for searching text and formats.
 * Creation date: (2001-02-12 13:59:38)
 * @author: Dennis
 */
 
public class CoStyledTextSearchPanel extends CoPanel 
{
	private CoLabeledTextField m_searchTextField;
	private CoLabeledTextField m_replaceTextField;
	
	private CoCheckBox m_caseSensitiveCheckbox;
	private CoCheckBox m_wordCheckbox;
	private CoCheckBox m_smartReplaceCheckbox;

	private CoCheckBox m_forwardCheckbox;
	private CoCheckBox m_backwardCheckbox;
	
	private CoButton m_findButton;
	private CoButton m_replaceButton;
	private CoButton m_replaceThenFindButton;

	private CoLayoutEditor m_layoutEditor;

	private String m_capitalizedReplaceString;
	private String m_allCapsReplaceString;


public CoStyledTextSearchPanel( CoUserInterfaceBuilder b )
{
	super( new CoAttachmentLayout() );

	m_searchTextField = b.createLabeledTextField( "Find" );
	m_replaceTextField = b.createLabeledTextField( "Replace" );

	m_searchTextField.setLabelBackground( CoLabeledTextField.PARENT_BACKGROUND );
	m_replaceTextField.setLabelBackground( CoLabeledTextField.PARENT_BACKGROUND );
	
	m_caseSensitiveCheckbox = b.createCheckBox( "Case", null );
	m_wordCheckbox = b.createCheckBox( "Word", null );
	m_smartReplaceCheckbox = b.createCheckBox( "Smart replace", null );
	
	CoLabel directionLabel = b.createLabel( "Direction" );
	CoButtonGroup bg = b.createButtonGroup();
	m_forwardCheckbox = b.createCheckBox( "Forward", null );
	bg.add( m_forwardCheckbox );
	m_backwardCheckbox = b.createCheckBox( "Backward", null );
	bg.add( m_backwardCheckbox );
	m_forwardCheckbox.setSelected( true );
	
	m_findButton = b.createButton( "Find", null );
	m_replaceButton = b.createButton( "Replace", null );
	m_replaceThenFindButton = b.createButton( "Replace then find", null );

	add( m_searchTextField,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );
	add( m_replaceTextField,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, m_searchTextField ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );
	add( m_caseSensitiveCheckbox,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 5, m_replaceTextField ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_wordCheckbox,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, m_caseSensitiveCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 0, m_caseSensitiveCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_smartReplaceCheckbox,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, m_wordCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 0, m_wordCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( directionLabel,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, m_forwardCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_BOTTOM, 0, m_forwardCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_forwardCheckbox,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 5, m_caseSensitiveCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 0, directionLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_backwardCheckbox,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, m_forwardCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 0, m_forwardCheckbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_findButton,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 5, directionLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_replaceButton,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, m_findButton ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, m_findButton ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_replaceThenFindButton,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, m_replaceButton ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, m_replaceButton ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );


	DocumentListener dl =
		new DocumentListener()
		{
			public void changedUpdate( DocumentEvent e ) {}
			public void insertUpdate( DocumentEvent e ) { updateButtons(); }
			public void removeUpdate( DocumentEvent e ) { updateButtons(); }
		};
	
	m_searchTextField.getDocument().addDocumentListener( dl );
	m_replaceTextField.getDocument().addDocumentListener( dl );

	
	m_findButton.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				find();
			}
		}
	);
	
	m_replaceButton.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				replace();
			}
		}
	);
	
	m_replaceThenFindButton.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				replace();
				find();
			}
		}
	);

	updateButtons();
}


private void down()
{
	String key = m_searchTextField.getText();
	
	if ( key.length() == 0 ) return;
	
	if
		( m_layoutEditor.getActiveTextContentView() == null )
	{
		down( null, key );
		return;
	}

	
	int pos = m_layoutEditor.getTextEditor().getCaretPosition();

	pos = m_layoutEditor.getTextEditor().getCoStyledDocument().search( key, pos, true, m_caseSensitiveCheckbox.isSelected(), m_wordCheckbox.isSelected() );

	if
		( pos != -1 )
	{
		m_layoutEditor.getTextEditor().select( pos, pos + key.length() );
	} else {

		CoPageItemAbstractTextContentView v = m_layoutEditor.getActiveTextContentView();
		m_layoutEditor.activateContentTool();
//		m_layoutEditor.stopTextEditing( v );
		down( v, key );
	}
}


private void down( CoPageItemAbstractTextContentView v, String key )
{
	v = getNextTextContentView( v );
	while
		( v != null )
	{
		int pos = v.getDocument().search( key, 0, true, m_caseSensitiveCheckbox.isSelected(), m_wordCheckbox.isSelected() );
		if
			( pos != -1 )
		{
			if
				( startTextEditor( v ) ) // skip locked texts (PENDING:is this correct???)
			{
				m_layoutEditor.getTextEditor().select( pos, pos + key.length() );
				break;
			}
		}
		v = getNextTextContentView( v );
	}

	if
		( v == null )
	{
		java.awt.Toolkit.getDefaultToolkit().beep();
	}
}


private void find()
{
	if
		( m_forwardCheckbox.isSelected() )
	{
		down();
	} else {
		up();
	}
}

private CoPageItemAbstractTextContentView getNextTextContentView( CoPageItemAbstractTextContentView v )
{
	
	class X extends CoPageItemViewVisitor
	{
		public CoPageItemAbstractTextContentView m_startLookingAt;
		public CoPageItemAbstractTextContentView m_found;
		
		public boolean visitContentView( CoPageItemContentView content )
		{
			if
				( m_startLookingAt != null )
			{
				if ( content == m_startLookingAt ) m_startLookingAt = null;
				return true;
			}

			if
				( content instanceof CoPageItemAbstractTextContentView )
			{
				m_found = (CoPageItemAbstractTextContentView) content;
				return false;
			}
			return true;
		}
	};

	X visitor = new X();
	visitor.m_startLookingAt = v;
	
	m_layoutEditor.getWorkspace().getRootView().visit( visitor );

	return visitor.m_found;
}


private CoPageItemAbstractTextContentView getPreviousTextContentView( CoPageItemAbstractTextContentView v )
{
	
	class X extends CoPageItemViewVisitor
	{
		public CoPageItemAbstractTextContentView m_stopLookingAt;
		public CoPageItemAbstractTextContentView m_found;
		
		public boolean visitContentView( CoPageItemContentView content )
		{
			if
				( m_stopLookingAt == content )
			{
				return false;
			}

			m_found = (CoPageItemAbstractTextContentView) content;
			return true;
		}
	};

	X visitor = new X();
	visitor.m_stopLookingAt = v;
	
	m_layoutEditor.getWorkspace().getRootView().visit( visitor );

	return visitor.m_found;
}


private void replace()
{
	if ( m_layoutEditor.getActiveTextContentView() == null ) return;
	
	String s = m_layoutEditor.getTextEditor().getSelectedText();
	if ( s == null ) return;
	int I = s.length();
	if ( I == 0 ) return;

	String newText = m_replaceTextField.getText();

	if
		( m_smartReplaceCheckbox.isSelected() )
	{
		
		if
			( Character.isUpperCase( s.charAt( 0 ) ) )
		{
			int c = 1;
			for
				( int i = 1; i < I; i++ )
			{
				if ( Character.isUpperCase( s.charAt( i ) ) ) c++;
			}

			if
				( c == 1 )
			{
				if
					( m_capitalizedReplaceString == null )
				{
					m_capitalizedReplaceString = Character.toUpperCase( newText.charAt( 0 ) ) + newText.substring( 1 );;
				}

				newText = m_capitalizedReplaceString;
			} else if
				( c == I )
			{
				if
					( m_allCapsReplaceString == null )
				{
					m_allCapsReplaceString = newText.toUpperCase();
				}

				newText = m_allCapsReplaceString;
			}
		}
	}
	
	m_layoutEditor.getTextEditor().replaceSelection( newText );
}

public void setEditor( CoLayoutEditor editor )
{
	m_layoutEditor = editor;
}


private boolean startTextEditor( CoPageItemAbstractTextContentView v )
{
	if
		( ! v.isTextLocked() )
	{
		m_layoutEditor.activateTextEditTool( v );
		return true;
	}

	return false;
}


private void up()
{
	String key = m_searchTextField.getText();
	
	if ( key.length() == 0 ) return;
	
	if
		( m_layoutEditor.getActiveTextContentView() == null )
	{
		up( null, key );
		return;
	}

	
	int pos = Math.min( m_layoutEditor.getTextEditor().getCaret().getDot(), m_layoutEditor.getTextEditor().getCaret().getMark() );
	pos -= key.length();

	pos = m_layoutEditor.getTextEditor().getCoStyledDocument().search( key, pos, false, m_caseSensitiveCheckbox.isSelected(), m_wordCheckbox.isSelected() );

	if
		( pos != -1 )
	{
		m_layoutEditor.getTextEditor().select( pos, pos + key.length() );
	} else {

		CoPageItemAbstractTextContentView v = m_layoutEditor.getActiveTextContentView();
		m_layoutEditor.activateContentTool();
//		m_layoutEditor.stopTextEditing( v );
		up( v, key );
	}
}


private void up( CoPageItemAbstractTextContentView v, String key )
{
	v = getPreviousTextContentView( v );
	while
		( v != null )
	{
		int pos = v.getDocument().search( key, v.getDocument().getLength(), false, m_caseSensitiveCheckbox.isSelected(), m_wordCheckbox.isSelected() );
		if
			( pos != -1 )
		{
			if
				( startTextEditor( v ) ) // skip locked texts (PENDING:is this correct???)
			{
				m_layoutEditor.getTextEditor().select( pos, pos + key.length() );
				break;
			}
		}
		v = getPreviousTextContentView( v );
	}

	if
		( v == null )
	{
		java.awt.Toolkit.getDefaultToolkit().beep();
	}
}


private void updateButtons()
{
	boolean b = m_searchTextField.getText().length() != 0;

	m_findButton.setEnabled( b );
	
	b = m_replaceTextField.getText().length() != 0;
	m_replaceButton.setEnabled( b );
	m_replaceThenFindButton.setEnabled( b );

	m_capitalizedReplaceString = null;
	m_allCapsReplaceString = null;
}
}