package com.bluebrim.text.impl.client;

import java.awt.event.*;
import java.rmi.*;

import javax.swing.*;
import javax.swing.event.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.observable.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.client.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.transact.shared.*;

//

public class CoHyphenationPatternUI extends CoDomainUserInterface
{
	public static final String TITLE = "CoHyphenationPatternUI.TITLE";
	
	private static CoHyphenationPatternUI m_instance;

	
	private CoTextField m_patternTextField;
	private CoListBox m_patternListbox;
	private CoList m_patternList;


	
	private CoCommand m_setCommand = new CoCommand( "SET HYPENATION PATTERN" )
	{
		public boolean doExecute()
		{
			Object [] o = m_patternList.getSelectedValues();
			if
				( o.length == 1 )
			{
				( (CoHyphenationPatternIF) o[ 0 ] ).setPattern( m_patternTextField.getText() );
			} else if
				( o.length == 0 )
			{
				CoHyphenationPatternCollectionIF d = getHyphenationPatternCollection();
				String p = m_patternTextField.getText();
				int i = d.indexOfPattern( p );
				if
					( i == -1 )
				{
					d.addPattern( m_patternTextField.getText() );
				} else {
					d.getPattern( i ).setPattern( p );
				}
				m_patternTextField.setText( "" );
			} else {
			}

			return true;
		}
	};


	private	CoCommand m_deleteCommand = new CoCommand( "DELETE HYPENATION PATTERN" )
	{
		public boolean doExecute()
		{
			CoHyphenationPatternCollectionIF d = getHyphenationPatternCollection();
			Object [] o = m_patternList.getSelectedValues();
			m_patternList.clearSelection();
			for
				( int i = 0; i < o.length; i++ )
			{
				d.removePattern( (CoHyphenationPatternIF) o[ i ] );
			}

			return true;
		}
	};

public CoHyphenationPatternUI()
{
	super();
}
protected void createListeners()
{
	super.createListeners();

	m_patternList.addListSelectionListener(
		new ListSelectionListener()
		{
			 public void valueChanged( ListSelectionEvent e )
			 {
				 if ( e.getValueIsAdjusting() ) return;
				 
				Object [] o = m_patternList.getSelectedValues();
				if
					( o.length == 1 )
				{
					m_patternTextField.setText( ( (CoHyphenationPatternIF) o[ 0 ] ).getPattern() );
					m_patternTextField.setEnabled( true );
				} else if
					( o.length == 0 )
				{
					m_patternTextField.setText( "" );
					m_patternTextField.setEnabled( true );
				} else {
					m_patternTextField.setText( "" );
					m_patternTextField.setEnabled( false );
				}
				 
			}
		}
	);


	
	m_patternTextField.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev  )
			{
				CoTransactionUtilities.execute( m_setCommand, getHyphenationPatternCollection() );
			}
		}
	);


	
	ActionListener l =
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev  )
			{
				if
					( m_patternList.getSelectedValues().length > 0 )
				{
					CoTransactionUtilities.execute( m_deleteCommand, getHyphenationPatternCollection() );
				}
			}
		};
	
	m_patternList.registerKeyboardAction( l, KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 ), JComponent.WHEN_IN_FOCUSED_WINDOW );
	m_patternList.registerKeyboardAction( l, KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0 ), JComponent.WHEN_IN_FOCUSED_WINDOW );


}
protected void createValueModels( CoUserInterfaceBuilder b )
{
	super.createValueModels( b );

	CoAbstractListAspectAdaptor vm = new CoAbstractListAspectAdaptor( this, "PATTERNS" )
	{
		public CoAbstractListModel getDefaultListModel()
		{
			return new CoAbstractListModel()
			{
				public Object getElementAt( int i )
				{
					CoHyphenationPatternCollectionIF d = getHyphenationPatternCollection();
					if ( d == null ) return null;
					return d.getPattern( i );
				}

				public int getSize()
				{
					CoHyphenationPatternCollectionIF d = getHyphenationPatternCollection();
					if ( d == null ) return 0;
					return d.getPatternCount();
				}

				public int indexOf( Object element )
				{
					CoHyphenationPatternCollectionIF d = getHyphenationPatternCollection();
					if ( d == null ) return -1;
					return d.indexOfPattern( (CoHyphenationPatternIF) element );
				}

				public void sort( java.util.Comparator c ) {}
			};
		}
		
		protected Object get( CoObjectIF subject )
		{
			return subject;
		}
	};

	b.createListBoxAdaptor( vm, m_patternListbox );
	b.addAspectAdaptor( vm );

}
protected void createWidgets( CoPanel panel, CoUserInterfaceBuilder builder )
{
	panel.setLayout( new CoAttachmentLayout() );

	m_patternListbox = builder.createListBox();
	m_patternList = m_patternListbox.getList();

	m_patternTextField = new CoTextField( "",  20 )
	{
		public void replaceSelection( String content )
		{
			for
				( int i = 0; i < content.length(); i++ )
			{
				if ( ! Character.isLetter( content.charAt( i ) ) && ! Character.isDigit( content.charAt( i ) ) ) return;
			}
			super.replaceSelection( content );
		}
	};
	builder.prepareTextField( m_patternTextField );

	panel.add( m_patternListbox,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_TOP, m_patternTextField ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );
	panel.add( m_patternTextField,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_CONTAINER ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_LEFT, 0, m_patternListbox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );
}
private CoHyphenationPatternCollectionIF getHyphenationPatternCollection()
{
	return (CoHyphenationPatternCollectionIF) getDomain();
}

    public static void open()
    {
        if (m_instance == null)
        {
            m_instance = new CoHyphenationPatternUI();
            try
            {
                m_instance.setDomain(CoTextClient.getTextServer().getHyphenationPatterns());
            } catch (RemoteException e)
            {
                throw new RuntimeException(e);
            }
            (new CoDefaultServerObjectListener(m_instance)).initialize();

            CoFrame f = m_instance.openInWindow();
            f.setTitle(CoTextStringResources.getName(TITLE));
            f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        } else
        {
            m_instance.getWindow().setVisible(true);
        }

    }
}