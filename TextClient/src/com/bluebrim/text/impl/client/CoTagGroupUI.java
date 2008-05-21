package com.bluebrim.text.impl.client;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.transact.shared.*;

/**
 * @author: Dennis 
 */
 
public class CoTagGroupUI extends CoDomainUserInterface implements CoDnDDataProvider {
	public static final String NAME = "CoTagGroupUI.NAME";
	public static final String AVAILABLE_TAGS = CoTextMeasurementPrefsUI.AVAILABLE_TAGS;
	public static final String MEMBERS = "CoTagGroupUI.MEMBERS";
	
	CoAbstractListAspectAdaptor.Default listAspectAdaptor;

//	ListModel m_availableTags;
	List m_availableTags = new ArrayList();
	CoChooserPanel chooserPanel = null;

	com.bluebrim.text.shared.CoStyledTextPreferencesIF m_context;

public CoTagGroupUI()
{
	super();
}


public CoTagGroupUI( CoObjectIF aDomainObject )
{
	super(aDomainObject);
}


boolean addTags()
{
	return addTags( chooserPanel.getSourceBox().getList().getSelectedValues() );
}


boolean addTags( Object [] tags )
{
	com.bluebrim.text.shared.CoTagGroupIF c = getTagGroup();
	java.util.List l = c.getTags();
	l.addAll( Arrays.asList( tags ) );
	c.setTags( l );

	updateAvailableTags();
	return true;
}


protected void createListeners () {

	super.createListeners();

	chooserPanel.addChooserEventListener(new CoChooserEventListener() {
		public void handleChooserEvent(CoChooserEvent e)
		{
			final Object [] tmp = e.getElements();
			if ( tmp == null ) return;
			if ( tmp.length == 0 ) return;
			
			if 
				( e.isAddEvent() )
			{
				CoTransactionUtilities.execute( new CoCommand( "ADD_TO_TAG_GROUP" ) { public boolean doExecute() { return addTags( tmp ); } } , getDomain() );
			} else {
				CoTransactionUtilities.execute( new CoCommand( "REMOVE_FROM_TAG_GROUP" ) { public boolean doExecute() { return removeTags( tmp ); } } , getDomain() );
			}
		}
	});

	
	ActionListener a =
		new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				CoTransactionUtilities.execute( new CoCommand( "REMOVE_FROM_TAG_GROUP" ) { public boolean doExecute() { return removeTags(); } } , getDomain() );
			}
		};
		
	chooserPanel.getDestinationBox().getList().registerKeyboardAction( a, KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 ), JComponent.WHEN_FOCUSED );
	chooserPanel.getDestinationBox().getList().registerKeyboardAction( a, KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0 ), JComponent.WHEN_FOCUSED );


	
	a =
		new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				CoTransactionUtilities.execute( new CoCommand( "ADD_TO_TAG_GROUP" ) { public boolean doExecute() { return addTags(); } } , getDomain() );
			}
		};
		
	chooserPanel.getSourceBox().getList().registerKeyboardAction( a, KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), JComponent.WHEN_FOCUSED );
}


protected void createValueModels(CoUserInterfaceBuilder builder){

	super.createValueModels(builder);

	listAspectAdaptor = new CoAbstractListAspectAdaptor.Default(this, MEMBERS){
			
			protected Object get(CoObjectIF subject)
			{
				return ((com.bluebrim.text.shared.CoTagGroupIF) subject).getTags();
			}
		};
	
	builder.createListBoxAdaptor(builder.addListAspectAdaptor(listAspectAdaptor), chooserPanel.getDestinationBox());



	builder.createTextFieldAdaptor(
		builder.addAspectAdaptor(
			new CoGsAspectAdaptor( this, NAME )
			{
				public Object get( CoObjectIF subject )
				{
					return ( (com.bluebrim.text.shared.CoTagGroupIF) subject ).getName();
				}
				public void set( CoObjectIF subject, Object value )
				{
					 ( (com.bluebrim.text.shared.CoTagGroupIF) subject ).setName( value.toString() );
				}
			},
			NAME ),
		(CoTextField) getNamedWidget( NAME ) );
	
}


/**
 * Insert the method's description here.
 * Creation date: (2000-06-07 16:25:16)
 */
protected void createWidgets(CoPanel p, CoUserInterfaceBuilder builder){
	
	super.createWidgets(p, builder);

	p.setLayout( new BorderLayout() );
	
	chooserPanel = builder.createChooserPanel( CoTextStringResources.getName( AVAILABLE_TAGS ),
		                                         CoTextStringResources.getName( MEMBERS ), 
									                           CoChooserPanel.SOURCE_TO_THE_LEFT,
									                           CoChooserPanel.REMOVE_FROM_SOURCE );
	
	p.add( chooserPanel, BorderLayout.CENTER );

	{
		CoPanel P = builder.createPanel( new CoFormLayout() );
		p.add( P, BorderLayout.NORTH );
		P.add( builder.createLabel( CoTextStringResources.getName( NAME ) ) );
		P.add( builder.createTextField( CoTextField.LEFT, 20, NAME ) );
	}

	chooserPanel.getSourceBox().getList().setModel(
		new CoCollectionListModel()
		{
			protected List getList()
			{
				return m_availableTags;
			}
		}
	);
}


protected void dispatchEnableDisable( boolean e )
{
	super.dispatchEnableDisable( e );

	chooserPanel.enableBoxes( e );
	chooserPanel.enableButtons( e );
}


private final com.bluebrim.text.shared.CoTagGroupIF getTagGroup()
{
	return (com.bluebrim.text.shared.CoTagGroupIF) getDomain();
}


public Transferable getTransferableFor(Object[] rawSelection) {
	// PENDING: Check if flavor needs to be specified. If not,
	// remove this entire method, CoDnDDataProvider implementation
	// and pass null in prepareDragAndDrop(). /Markus 2001-09-20
	
	// NOTE: This method is used for dragging from both the source and
	// from the target. It works since they both have the same type.
	return CoDataTransferKit.getTransferableForUsing(rawSelection, CoDataTransferKit.STRING_ARRAY_FLAVOR);
}

/**
 * Insert the method's description here.
 * Creation date: (2000-06-26 17:19:51)
 */
public void handleDrop(final Object[] selection,final int position){

	CoTransactionUtilities.execute(new CoCommand("ChainRuleUI drop") {
		
		public boolean doExecute() {

			com.bluebrim.text.shared.CoTagGroupIF cr = getTagGroup();
			java.util.List list = cr.getTags();
			LinkedList temp = new LinkedList(list);
			
			for(int i=selection.length-1; i >= 0; i--){

				temp.add(position, new String((String)selection[i]));
			}
					
			cr.setTags(temp);			
			
			return true;
		}

		public void finish(){

			listAspectAdaptor.listHasChanged(this);
			chooserPanel.getDestinationBox().getList().clearSelection();
		}
	}
	, getDomain());
	
}


/**
 * Insert the method's description here.
 * Creation date: (2000-06-26 17:19:51)
 */
private boolean hasContinuousSelection () {
	JList 	tList = chooserPanel.getDestinationBox().getList();
	int 	tFirstIndex 	= tList.getMinSelectionIndex();
	int 	tLastIndex		= tList.getMaxSelectionIndex();
	if (tFirstIndex != -1 && tLastIndex != -1)
	{
		for (int i=tFirstIndex; i<=tLastIndex; i++)
		{
			if (!tList.isSelectedIndex(i))
				return false;
		}
		return true;
	}
	else
		return false;
	
}


private void moveSelectionDownwards(final CoList coList,
									final Object[] selection,
									final java.util.List list, 
									final int firstSelectionIndex, 
									final int lastSelectionIndex){
	
	CoTransactionUtilities.execute(new CoCommand("ChainRuleUI down"){ 
				
		public boolean doExecute(){

			LinkedList temp = new LinkedList(list);
				
			for(int i=0; i<selection.length; i++){

				temp.remove(firstSelectionIndex);
			}

			for(int i=selection.length-1; i >= 0; i--){

				temp.add(firstSelectionIndex+1, selection[i]);
			}
				
			getTagGroup().setTags(temp);					
			return true;
		}

		public void finish(){

			listAspectAdaptor.listHasChanged(this);
			coList.setSelectionInterval(firstSelectionIndex+1, lastSelectionIndex+1);
		}
	}
	, getDomain());
}


private void moveSelectionUpwards(final CoList coList,
									final Object[] selection,
									final java.util.List list, 
									final int firstSelectionIndex, 
									final int lastSelectionIndex){

	CoTransactionUtilities.execute(new CoCommand("ChainRuleUI up"){

		public boolean doExecute(){

			LinkedList temp = new LinkedList(list);
			
			for(int i=0; i<selection.length; i++){

				temp.remove(firstSelectionIndex);
			}

			for(int i=selection.length-1; i >= 0; i--){

				temp.add(firstSelectionIndex-1, selection[i]);
			}

			getTagGroup().setTags(temp);						
			return true;
		}

		public void finish(){

			listAspectAdaptor.listHasChanged(this);
			coList.setSelectionInterval(firstSelectionIndex-1, lastSelectionIndex-1);
		}
	}
	, getDomain());		
}


protected void postDomainChange( CoObjectIF d ) 
{
	super.postDomainChange( d );
	updateAvailableTags();
}


public void prepareDragAndDrop() {
	super.prepareDragAndDrop();
	CoList source = chooserPanel.getSourceBox().getList();
	CoList target = chooserPanel.getDestinationBox().getList();

	// Drag, new style
	source.prepareDrag(DnDConstants.ACTION_COPY_OR_MOVE, this);
	target.prepareDrag(DnDConstants.ACTION_COPY_OR_MOVE, this);

	// Drop, old style
	// drag from source to destination 
	CoDropTargetListener	tDropTargetListener	= new CoListDropTargetListener(target, (new DataFlavor[] {CoDataTransferKit.STRING_ARRAY_FLAVOR})) {
		protected void handleDrop(DropTargetDropEvent dtde)
		{
			try
			{
				Object[] selection = (Object[])dtde.getTransferable().getTransferData(CoDataTransferKit.STRING_ARRAY_FLAVOR);
				if (selection == null)
					return;
				int position = getDropPosition();
				CoTagGroupUI.this.handleDrop(selection, position);
			}
			catch(UnsupportedFlavorException e) {}
			catch (IOException e1) {}
			
		}
	};
	
	new DropTarget(target, tDropTargetListener);
}

boolean removeTags()
{
	return removeTags( chooserPanel.getDestinationBox().getList().getSelectedValues() );
}


boolean removeTags( Object [] tags )
{
	com.bluebrim.text.shared.CoTagGroupIF c = getTagGroup();
	java.util.List l = c.getTags();

	for
		( int i = 0; i < tags.length; i++ )
	{
		l.remove( tags[ i ] );
	}

	c.setTags( l );

	chooserPanel.getDestinationBox().getList().clearSelection();
	chooserPanel.getDestinationBox().getList().repaint();

	updateAvailableTags();
	
	return true;
}


public void setContext( com.bluebrim.text.shared.CoStyledTextPreferencesIF c )
{
	m_context = c;
	updateAvailableTags();
}


private void updateAvailableTags()
{
//	if ( chooserPanel == null ) return;

	m_availableTags.clear();
	if ( m_context == null ) return;
	if ( getTagGroup() == null ) return;

	m_availableTags.addAll( m_context.getParagraphTagNames() );
	m_availableTags.removeAll( getTagGroup().getTags() );
	
	( (CoCollectionListModel) chooserPanel.getSourceBox().getList().getModel() ).listHasChanged( this );
}
}