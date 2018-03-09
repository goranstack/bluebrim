package com.bluebrim.text.impl.client;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.transact.shared.*;

/**
 * A UI representation and modifier for CoChainRule.	 
 * Uses CoChooserPanel as widget 
 * 
 * 
 * Creation date: (2000-06-07 14:38:43)
 * @author: Michael Klimczak 
 */
public class CoTagChainUI extends CoDomainUserInterface implements CoDnDDataProvider {

	public static final String NAME = "CoTagChainUI.NAME";
	public static final String AVAILABLE_TAGS = CoTextMeasurementPrefsUI.AVAILABLE_TAGS;
	public static final String CHAIN = "CoTagChainUI.CHAIN";
	
	CoAbstractListAspectAdaptor.Default listAspectAdaptor;

	ListModel m_availableTags;
	CoChooserPanel chooserPanel = null;

/**
 * CoChainRuleUI constructor comment.
 */
public CoTagChainUI() {
	super();
}


/**
 * CoChainRuleUI constructor comment.
 * @param aDomainObject com.bluebrim.base.shared.CoObjectIF
 */
public CoTagChainUI(com.bluebrim.base.shared.CoObjectIF aDomainObject) {
	super(aDomainObject);
}


boolean addTags()
{
	com.bluebrim.text.shared.CoTagChainIF c = getTagChain();
	java.util.List l = c.getChain();
	l.addAll( Arrays.asList( chooserPanel.getSourceBox().getList().getSelectedValues() ) );
	c.setChain( l );
	return true;
}


protected void createListeners () {

	super.createListeners();

	chooserPanel.addChooserEventListener(new CoChooserEventListener() {
		public void handleChooserEvent(CoChooserEvent e)
		{
			Object [] tmp = e.getElements();
			if ( tmp == null ) return;
			if ( tmp.length == 0 ) return;
			
			if 
				( e.isAddEvent() )
			{
				CoTransactionUtilities.execute( new CoCommand( "ADD_TO_CHAIN_RULE" ) { public boolean doExecute() { return addTags(); } } , getDomain() );
			} else {
				CoTransactionUtilities.execute( new CoCommand( "REMOVE_FROM_CHAIN_RULE" ) { public boolean doExecute() { return removeTags(); } } , getDomain() );
			}
		}
	});
	
	chooserPanel.getDestinationBox().getList().registerKeyboardAction(new ActionListener() {
		public void actionPerformed(ActionEvent e)
		{
			if (hasContinuousSelection())
			{
				CoList coList = chooserPanel.getDestinationBox().getList();
				Object[] selection = coList.getSelectedValues();
				int firstSelectionIndex = coList.getSelectedIndex();
				int lastSelectionIndex = selection.length - 1 + firstSelectionIndex;			
				java.util.List list = getTagChain().getChain();
				
				if(firstSelectionIndex != 0){	
					
					moveSelectionUpwards(coList, selection, list, firstSelectionIndex, lastSelectionIndex);
				}
			}
		}
	},KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.ALT_MASK),JComponent.WHEN_FOCUSED );
	
	chooserPanel.getDestinationBox().getList().registerKeyboardAction(new ActionListener() {
		public void actionPerformed(ActionEvent e)
		{
			if (hasContinuousSelection())
			{
				CoList coList = chooserPanel.getDestinationBox().getList();
				Object[] selection = coList.getSelectedValues();
				int firstSelectionIndex = coList.getSelectedIndex();
				int lastSelectionIndex = selection.length - 1 + firstSelectionIndex;			
				java.util.List list = getTagChain().getChain();
				
				if(lastSelectionIndex + 1 < list.size()){	
				
					moveSelectionDownwards(coList, selection, list, firstSelectionIndex, lastSelectionIndex);
				}
			}
		}
	},KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.ALT_MASK),JComponent.WHEN_FOCUSED );


	
	ActionListener a =
		new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				CoTransactionUtilities.execute( new CoCommand( "REMOVE_FROM_CHAIN_RULE" ) { public boolean doExecute() { return removeTags(); } } , getDomain() );
			}
		};
		
	chooserPanel.getDestinationBox().getList().registerKeyboardAction( a, KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 ), JComponent.WHEN_FOCUSED );
	chooserPanel.getDestinationBox().getList().registerKeyboardAction( a, KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0 ), JComponent.WHEN_FOCUSED );


	
	a =
		new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				CoTransactionUtilities.execute( new CoCommand( "ADD_TO_CHAIN_RULE" ) { public boolean doExecute() { return addTags(); } } , getDomain() );
			}
		};
		
	chooserPanel.getSourceBox().getList().registerKeyboardAction( a, KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), JComponent.WHEN_FOCUSED );
}


/*
 *
 *
 *
 */
protected void createValueModels(CoUserInterfaceBuilder builder){

	super.createValueModels(builder);

	listAspectAdaptor = new CoAbstractListAspectAdaptor.Default(this, CHAIN){
			
			protected Object get(CoObjectIF subject)
			{
				return ((com.bluebrim.text.shared.CoTagChainIF) subject).getChain();
			}
		};
	
	builder.createListBoxAdaptor(builder.addListAspectAdaptor(listAspectAdaptor), chooserPanel.getDestinationBox());



	builder.createTextFieldAdaptor(
		builder.addAspectAdaptor(
			new CoGsAspectAdaptor( this, NAME )
			{
				public Object get( CoObjectIF subject )
				{
					return ( (com.bluebrim.text.shared.CoTagChainIF) subject ).getName();
				}
				public void set( CoObjectIF subject, Object value )
				{
					 ( (com.bluebrim.text.shared.CoTagChainIF) subject ).setName( value.toString() );
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
		                                         CoTextStringResources.getName( CHAIN ), 
									                           CoChooserPanel.SOURCE_TO_THE_LEFT,
									                           CoChooserPanel.COPY_FROM_SOURCE );
	
	p.add( chooserPanel, BorderLayout.CENTER );

	{
		CoPanel P = builder.createPanel( new CoFormLayout() );
		p.add( P, BorderLayout.NORTH );
		P.add( builder.createLabel( CoTextStringResources.getName( NAME ) ) );
		P.add( builder.createTextField( CoTextField.LEFT, 20, NAME ) );
	}
}


protected void dispatchEnableDisable( boolean e )
{
	super.dispatchEnableDisable( e );

	chooserPanel.enableBoxes( e );
	chooserPanel.enableButtons( e );
}


protected void doAfterCreateUserInterface( ) 
{
	super.doAfterCreateUserInterface();
	updateAvailableTags();
}


/**
 * Insert the method's description here.
 * Creation date: (2000-06-07 16:25:16)
 */
private final com.bluebrim.text.shared.CoTagChainIF getTagChain()
{

	return (com.bluebrim.text.shared.CoTagChainIF) getDomain();
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

			com.bluebrim.text.shared.CoTagChainIF cr = getTagChain();
			java.util.List list = cr.getChain();
			LinkedList temp = new LinkedList(list);
			
			for(int i=selection.length-1; i >= 0; i--){

				temp.add(position, new String((String)selection[i]));
			}
					
			cr.setChain(temp);			
			
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
				
			getTagChain().setChain(temp);					
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

			getTagChain().setChain(temp);						
			return true;
		}

		public void finish(){

			listAspectAdaptor.listHasChanged(this);
			coList.setSelectionInterval(firstSelectionIndex-1, lastSelectionIndex-1);
		}
	}
	, getDomain());		
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
				CoTagChainUI.this.handleDrop(selection, position);
			}
			catch(UnsupportedFlavorException e) {}
			catch (IOException e1) {}
			
		}
	};
	
	new DropTarget(target, tDropTargetListener);
}


boolean removeTags()
{
	com.bluebrim.text.shared.CoTagChainIF c = getTagChain();
	java.util.List l = c.getChain();

	int [] is = chooserPanel.getDestinationBox().getList().getSelectedIndices();
	chooserPanel.getDestinationBox().getList().clearSelection();
	chooserPanel.getDestinationBox().getList().repaint();
	for
		( int i = is.length - 1; i >= 0; i-- )
	{
		l.remove( is[ i ] );
	}

	c.setChain( l );
	return true;
}


public void setAvailableTags( ListModel tags )
{
	m_availableTags = tags;
	updateAvailableTags();
}


private void updateAvailableTags()
{
	if ( chooserPanel == null ) return;
	if ( m_availableTags == null ) m_availableTags = chooserPanel.getSourceBox().getList().getModel();
	
	chooserPanel.getSourceBox().getList().setModel( m_availableTags );
}
}