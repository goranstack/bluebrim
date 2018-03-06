package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.swing.client.*;

/**
 * Page item workpiece text property panel.
 *
 * @author: Dennis
 */

public class CoPageItemWorkPieceTextPanel extends CoPageItemPropertyPanel
{
	{
		init();
	}

	public static final String ACCEPTED = "CoPageItemWorkPieceTextPanel.accepted_tags";	
	public static final String AVAILABLE = "CoPageItemWorkPieceTextPanel.available_tags";
	public static final String TAG = "CoPageItemWorkPieceTextPanel.TEXT_TAG";
	public static final String GROUPS = "CoPageItemWorkPieceTextPanel.GROUPS";
	public static final String SELECT = "CoPageItemWorkPieceTextPanel.SELECT";

	private CoPageItemEditorContextIF m_context;
	
	private List m_availableTags = new ArrayList();
	private List m_tagGroups = new ArrayList();
	private CoTextField m_tagTextField;
	private CoList m_availableTagsList;
	private CoList m_acceptedTagsList;
	private CoPopupMenu m_tagGroupMenu;
	private CoButton m_selectTextButton;
	
	private CoWorkPieceTextSelectionDialog m_textSelectionDialog;





	
	private abstract class ParagraphTagsListModel extends AbstractListModel
	{
		protected CoPageItemWorkPieceTextContentView m_workPieceText;

		public void setWorkPieceText( CoPageItemWorkPieceTextContentView at )
		{
			int I = ParagraphTagsListModel.this.getSize() - 1;
			m_workPieceText = at;
			fireContentsChanged( this, 0, I );
		}
	};

	private class AcceptedParagraphTagsListModel extends ParagraphTagsListModel
	{
		public int getSize()
		{
			return ( m_workPieceText == null ) ? 0 : m_workPieceText.getAcceptedTags().size();
		}

		public Object getElementAt( int i )
		{
			return m_workPieceText.getAcceptedTags().get( i );
		}
	};
	
	private class AvailableParagraphTagsListModel extends ParagraphTagsListModel
	{
		private List m_tags = new ArrayList();

		public void setWorkPieceText( CoPageItemWorkPieceTextContentView at )
		{
			m_tags.clear();
			m_tags.addAll( m_availableTags );
			if ( at != null ) m_tags.removeAll( at.getAcceptedTags() );
			super.setWorkPieceText( at );
		}
		
		public int getSize()
		{
			return m_tags.size();
		}

		public Object getElementAt( int i )
		{
			return m_tags.get( i );
		}

		public int indexOf( Object o )
		{
			return m_tags.indexOf( o );
		}
	};

	private AvailableParagraphTagsListModel m_availableTagsListModel;
	private ParagraphTagsListModel m_acceptedTagsListModel;

	private ActionListener m_tagGroupAction;




public void doUpdate()
{
	CoPageItemWorkPieceTextContentView t = (CoPageItemWorkPieceTextContentView) ( (CoContentWrapperPageItemView) m_domain ).getContentView();

	int o = t.getOrderTag();
	m_tagTextField.setText( ( o != -1 ) ? "" + o : "" );
	m_acceptedTagsListModel.setWorkPieceText( t );

	m_availableTagsListModel.setWorkPieceText( t );

	m_selectTextButton.setEnabled( t.getWorkPiece() != null );
}
private void init()
{
	m_tagGroupAction =
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				selectTagGroup( ev.getActionCommand() );
			}
		};
}
private void openTextSelectionDialog()
{
	CoPageItemWorkPieceTextContentView tv = (CoPageItemWorkPieceTextContentView) ( (CoContentWrapperPageItemView) m_domain ).getContentView();

	final int i = m_textSelectionDialog.open( tv.getWorkPiece().getTexts() );

	if ( i == -1 ) return;

	CoPageItemCommands.SET_TEXT_TAG.prepare( m_domain, i );
	m_commandExecutor.doit( CoPageItemCommands.SET_TEXT_TAG, null );

}
private void selectTagGroup( String name )
{
	Iterator i = m_context.getPreferences().getTagGroups().iterator();

	while
		( i.hasNext() )
	{
		com.bluebrim.text.shared.CoTagGroupIF g = (com.bluebrim.text.shared.CoTagGroupIF) i.next();
		if
			( g.getName().equals( name ) )
		{
			m_availableTagsList.clearSelection();
			List members = g.getTags();
			int N = members.size();
			int m [] = new int [ N ];
			for
				( int n = 0; n < N; n++ )
			{
				m[ n ] = m_availableTagsListModel.indexOf( members.get( n ) );
			}
			m_availableTagsList.setSelectedIndices( m );
			break;
		}
	}
}
public void setContext( CoPageItemEditorContextIF context )
{
	m_context = context;

	m_tagGroups.clear();
	m_tagGroupMenu.removeAll();
	if
		( context != null )
	{
		Iterator i = context.getPreferences().getTagGroups().iterator();
		while
			( i.hasNext() )
		{
			com.bluebrim.text.shared.CoTagGroupIF g = (com.bluebrim.text.shared.CoTagGroupIF) i.next();
			m_tagGroups.add( g.getName() );
			m_tagGroupMenu.add( g.getName() ).addActionListener( m_tagGroupAction );
		}
	}
	
	// paragraph tags
	{
		m_availableTags.clear();
	
		List tags = ( context == null ) ? null : context.getPreferences().getParagraphTagNames();

		if
			( tags != null )
		{
			Iterator iter = tags.iterator();
			while
				( iter.hasNext() )
			{
				m_availableTags.add( iter.next() );
			}
		}
	}
}

	public static final String LOCK = "CoPageItemWorkPieceTextPanel.LOCK";
	private CoUndoableCommandExecutor m_commandExecutor;
	private CoCheckBox m_textLockCheckBox;

public CoPageItemWorkPieceTextPanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, new CoAttachmentLayout(), commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, final CoUndoableCommandExecutor commandExecutor )
{
	m_commandExecutor = commandExecutor;

	
	CoLabel tagLabel = b.createLabel( CoPageItemUIStringResources.getName( TAG ) );
	m_tagTextField = b.createSlimTextField( CoTextField.RIGHT, 4 );
	CoSeparator separator = new CoSeparator();

	CoChooserPanel chooser = b.createChooserPanel( CoPageItemUIStringResources.getName( AVAILABLE ),
		                                             CoPageItemUIStringResources.getName( ACCEPTED ),
		                                             CoChooserPanel.SOURCE_TO_THE_LEFT,
		                                             CoChooserPanel.REMOVE_FROM_SOURCE );
	chooser.setExtraInsets( new Insets( 2, 2, 2 ,2 ) );
	{
		CoLabel l = b.createLabel( CoPageItemUIStringResources.getName( GROUPS ) );
		l.setBorder( BorderFactory.createEtchedBorder() );
		chooser.getSourcePanel().add( l, BorderLayout.SOUTH );

		l.addMouseListener(
			new MouseAdapter()
			{
				public void mousePressed( MouseEvent ev )
				{
					m_tagGroupMenu.show( (Component) ev.getSource(), ev.getX(), ev.getY() );
				}
			}
		);
		m_tagGroupMenu = new CoPopupMenu();
	}

	m_availableTagsList = chooser.getSourceBox().getList();
	m_availableTagsListModel = new AvailableParagraphTagsListModel();
	m_availableTagsList.setModel( m_availableTagsListModel );

	m_acceptedTagsList = chooser.getDestinationBox().getList();
	m_acceptedTagsListModel = new AcceptedParagraphTagsListModel();
	m_acceptedTagsList.setModel( m_acceptedTagsListModel );

	m_selectTextButton = b.createButton( CoPageItemUIStringResources.getName( SELECT ), null );
	m_textSelectionDialog = new CoWorkPieceTextSelectionDialog( b );
	
	m_textLockCheckBox = b.createCheckBox( CoPageItemUIStringResources.getName( LOCK ), null );

		
	add( tagLabel,
	     new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, m_tagTextField ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_BOTTOM, 0, m_tagTextField ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_tagTextField,
	     new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, tagLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_selectTextButton,
	     new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, m_tagTextField ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( separator,
	     new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 5, m_selectTextButton ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );
	add( chooser,
	   new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, separator ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );



	m_tagTextField.addActionListener(
		new IntegerTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_TEXT_TAG, -2 )
		{
			protected int parse( String str )
			{
				return ( str.length() == 0 ) ? -1 : CoLengthUnitSet.parse( str, -2 );
			}
		}
	);


	chooser.addChooserEventListener(
		new CoChooserEventListener()
		{
			public void handleChooserEvent( CoChooserEvent e )
			{
				if
					( e.isAddEvent() )
				{
					CoPageItemCommands.ADD_ACCEPTED_TAGS.prepare( m_domain, Arrays.asList( e.getElements() ) );
					commandExecutor.doit( CoPageItemCommands.ADD_ACCEPTED_TAGS, null );
				} else {
					CoPageItemCommands.REMOVE_ACCEPTED_TAGS.prepare( m_domain, Arrays.asList( e.getElements() ) );
					commandExecutor.doit( CoPageItemCommands.REMOVE_ACCEPTED_TAGS, null );
				}
			}
		}
	);

	m_selectTextButton.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				openTextSelectionDialog();
			}
		}
	);

	m_textLockCheckBox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_TEXT_LOCK ) );
}
}