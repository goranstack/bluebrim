package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.transact.shared.*;

/**
 * Component for displaying and manipulating page item properties.
 *
 * @author: Dennis
 */

public class CoPageItemPane extends CoTabbedPane implements CoPageItemView.Listener
{

	private CoPageItemEditorContextIF	m_context; // used to extract colors, strokes, ...

	private CoShapePageItemView m_domain; // model
	
	private CoPageItemPrototypeIF m_prototype; // used when displaying a creation tool prototype

	// tabs
	private CoPageItemPropertyPanel m_contentTab;
	private CoPageItemPropertyPanel m_geometryTab;
	private CoPageItemPropertyPanel m_layoutTab;
	private CoPageItemPropertyPanel m_layoutManagerTab;
	private CoPageItemPropertyPanel m_fillTab;
	private CoPageItemPropertyPanel m_strokeTab;
	private CoPageItemPropertyPanel m_gridTab;
	private CoPageItemPropertyPanel m_runAroundSpecTab;
	private CoPageItemPropertyPanel m_imageTab;
	private CoPageItemPropertyPanel m_layoutContentTab;
	private CoPageItemPropertyPanel m_textTab;
	private CoPageItemPropertyPanel m_zOrderTab;
	private CoPageItemPropertyPanel m_layoutOrderTab;
	private CoPageItemPropertyPanel m_workPieceTextTab;


	private CoPageItemPropertyPanel [] m_tabs;

	public final static String CONTENT_TAB = "CoPageItemPanel.CONTENT_TAB";
	public final static String GEOMETRY_TAB = "CoPageItemPanel.GEOMETRY_TAB";
	public final static String LAYOUT_TAB = "CoPageItemPanel.LAYOUT_TAB";
	public final static String LAYOUT_MANAGER_TAB = "CoPageItemPanel.LAYOUT_MANAGER_TAB";
	public final static String FILL_TAB = "CoPageItemPanel.FILL_TAB";
	public final static String STROKE_TAB = "CoPageItemPanel.STROKE_TAB";
	public final static String GRID_TAB = "CoPageItemPanel.GRID_TAB";
	public final static String RUN_AROUND_SPEC_TAB = "CoPageItemPanel.RUN_AROUND_SPEC_TAB";
	public final static String IMAGE_TAB = "CoPageItemPanel.IMAGE_TAB";
	public final static String LAYOUT_CONTENT_TAB = "CoPageItemPanel.LAYOUT_CONTENT_TAB";
	public final static String TEXT_TAB = "CoPageItemPanel.TEXT_TAB";
	public final static String CHILD_Z_ORDER_TAB = "CoPageItemPanel.CHILD_Z_ORDER_TAB";
	public final static String CHILD_LAYOUT_ORDER_TAB = "CoPageItemPanel.CHILD_LAYOUT_ORDER_TAB";
	public final static String WORKPIECE_TEXT_TAB = "CoPageItemPanel.WORKPIECE_TEXT_TAB";


	
	private Map m_tabNames = new HashMap();

	



private void domainHasChanged()
{
	updateTabs();

	if ( m_domain == null ) return;
	
	for
		( int i = 0; i < m_tabs.length; i++ )
	{
		m_tabs[ i ].domainHasChanged();
	}
}
public CoShapePageItemView getDomain()
{
	return m_domain;
}
public void selectTab( String tabName )
{
	if ( m_domain == null ) return;
	
	try
	{
		Component c = (Component) m_tabNames.get( tabName );
		setSelectedComponent( c );
	}
	catch ( IllegalArgumentException ex )
	{
	}
}
public void setContext( CoPageItemEditorContextIF context )
{
	m_context = context;
	
	for
		( int i = 0; i < m_tabs.length; i++ )
	{
		m_tabs[ i ].setContext( m_context );
	}
}
public void setDomain( CoShapePageItemView domain )
{
	if ( m_domain == domain ) return;

	if
		( m_prototype != null )
	{
		fireToolChange();
	}
	
	m_prototype = null;

	if
		( m_domain != null )
	{
		m_domain.removeViewListener( this );
	}

	m_domain = domain;
	
	if
		( m_domain != null )
	{
		m_domain.addViewListener( this );
	}

	updateTabs();
	
	for
		( int i = 0; i < m_tabs.length; i++ )
	{
		m_tabs[ i ].setDomain( m_domain );
	}
}
public void setPrototype( CoPageItemPrototypeIF p, CoShapePageItemView domain )
{
	setDomain( domain );
	m_prototype = p;

	if
		( m_prototype != null )
	{
		if ( m_prototypeTab.getParent() == null ) add( m_prototypeTab, CoPageItemUIStringResources.getName( PROTOTYPE_TAB ) );
		m_prototypeTab.setPrototype( m_prototype );
	} else {
		remove( m_prototypeTab );
	}
}
private void updateTabs()
{
	Container tla = getTopLevelAncestor();
	if
		( tla instanceof Dialog )
	{
		( (Dialog) tla ).setTitle( m_domain == null ? "" : m_domain.getName() );
	} else if
		( tla instanceof Frame )
	{
		( (Frame) tla ).setTitle( m_domain == null ? "" : m_domain.getName() );
	}

	setEnabled( m_domain != null );
	
	if
		( m_domain == null )
	{
		getSelectedComponent().setVisible( false );
		return;
	}

	boolean isSlave = m_domain.isSlave();
	
	getSelectedComponent().setVisible( true );
	
	boolean hasParent = m_domain.getParent() != null;
	boolean isPage = ( m_domain instanceof CoPageLayoutAreaView );
	boolean notPageAndNotPrototype = ! isPage && ( m_prototype == null );
	boolean notPageAndNotPrototypeAndNotSlave = notPageAndNotPrototype && ! isSlave;
	boolean isContentWrapper = ( m_domain instanceof CoContentWrapperPageItemView );
	boolean isComposite = ( m_domain instanceof CoCompositePageItemView );

	CoPageItemContentView cv = null;
	if
		( isContentWrapper )
	{
		cv = ( (CoContentWrapperPageItemView) m_domain ).getContentView();
	}
	
	boolean isLayoutArea = m_domain instanceof CoLayoutAreaView;
	boolean isLayout = ( isContentWrapper && ( cv instanceof CoPageItemLayoutContentView ) );
	boolean isImage = ( isContentWrapper && ( cv instanceof CoPageItemImageContentView ) );
	boolean isText = ( isContentWrapper && ( cv instanceof CoPageItemAbstractTextContentView ) );
	boolean isWorkPieceText = ( isContentWrapper && ( cv instanceof CoPageItemWorkPieceTextContentView ) );
	boolean gridable = ( isText || isComposite );

	if
		( isContentWrapper && ! isSlave )
	{
		if ( m_contentTab.getParent() == null ) add( m_contentTab, CoPageItemUIStringResources.getName( CONTENT_TAB ) );
	} else {
		remove( m_contentTab );
	}

	if
		( isPage )
	{
		if ( m_pageLayerTab.getParent() == null ) add( m_pageLayerTab, CoPageItemUIStringResources.getName( PAGE_LAYER_TAB ) );
		remove( m_geometryTab );
//		remove( m_fillTab );
		remove( m_strokeTab );
		remove( m_runAroundSpecTab );
	} else {
		remove( m_pageLayerTab );
		if ( m_geometryTab.getParent() == null ) add( m_geometryTab, CoPageItemUIStringResources.getName( GEOMETRY_TAB ) );
		if ( m_fillTab.getParent() == null ) add( m_fillTab, CoPageItemUIStringResources.getName( FILL_TAB ) );
		if ( m_strokeTab.getParent() == null ) add( m_strokeTab, CoPageItemUIStringResources.getName( STROKE_TAB ) );
		if ( m_runAroundSpecTab.getParent() == null ) add( m_runAroundSpecTab, CoPageItemUIStringResources.getName( RUN_AROUND_SPEC_TAB ) );
	}

	if
		( ( m_prototype == null ) && isComposite )
	{
		if ( m_zOrderTab.getParent() == null ) add( m_zOrderTab, CoPageItemUIStringResources.getName( CHILD_Z_ORDER_TAB ) );
		if ( m_layoutOrderTab.getParent() == null ) add( m_layoutOrderTab, CoPageItemUIStringResources.getName( CHILD_LAYOUT_ORDER_TAB ) );
	} else {
		remove( m_layoutOrderTab );
		remove( m_zOrderTab );
	}

	if
		( notPageAndNotPrototypeAndNotSlave )
	{
		if ( m_layoutTab.getParent() == null ) add( m_layoutTab, CoPageItemUIStringResources.getName( LAYOUT_TAB ) );
	} else {
		remove( m_layoutTab );
	}

	if
		( isComposite )
	{
		if ( m_layoutManagerTab.getParent() == null ) add( m_layoutManagerTab, CoPageItemUIStringResources.getName( LAYOUT_MANAGER_TAB ) );
		//if ( m_trappingTab.getParent() == null ) add( m_trappingTab, CoPageItemUIStringResources.getName( TRAPPING_TAB ) );
	} else {
		remove( m_layoutManagerTab );
		//remove( m_trappingTab );
	}

	if
		( gridable )
	{
		if ( m_gridTab.getParent() == null ) add( m_gridTab, CoPageItemUIStringResources.getName( GRID_TAB ) );
	} else {
		remove( m_gridTab );
	}

	if
		( isImage )
	{
		if ( m_imageTab.getParent() == null ) add( m_imageTab, CoPageItemUIStringResources.getName( IMAGE_TAB ) );
	} else {
		remove( m_imageTab );
	}

	if
		( isLayout )
	{
		if ( m_layoutContentTab.getParent() == null ) add( m_layoutContentTab, CoPageItemUIStringResources.getName( LAYOUT_CONTENT_TAB ) );
	} else {
		remove( m_layoutContentTab );
	}

	if
		( isText )
	{
		if ( m_textTab.getParent() == null ) add( m_textTab, CoPageItemUIStringResources.getName( TEXT_TAB ) );
	} else {
		remove( m_textTab );
	}

	if
		( isWorkPieceText )
	{
		if ( m_workPieceTextTab.getParent() == null ) add( m_workPieceTextTab, CoPageItemUIStringResources.getName( WORKPIECE_TEXT_TAB ) );
	} else {
		remove( m_workPieceTextTab );
	}

	if
		( isLayoutArea )
	{
		if ( m_layoutAreaTab.getParent() == null ) add( m_layoutAreaTab, CoPageItemUIStringResources.getName( LAYOUT_AREA_TAB ) );
	} else {
		remove( m_layoutAreaTab );
	}

	remove( m_prototypeTab );

}
public void valueHasChanged()
{
	domainHasChanged();
}

	public final static String LAYOUT_AREA_TAB = "CoPageItemPanel.LAYOUT_AREA_TAB";
	private CoPageItemPropertyPanel m_layoutAreaTab;
	private CoPageItemPrototypeDataPanel m_prototypeTab;
	public final static String PROTOTYPE_TAB = "CoPageItemPanel.PROTOTYPE_TAB";


	private CoPageItemPageLayerPanel m_pageLayerTab;
//	public final static String SUBSECTION_TAB = "CoPageItemPanel.SUBSECTION_TAB";
	public final static String PAGE_LAYER_TAB = "CoPageItemPanel.PAGE_LAYER_TAB";

public CoPageItemPane( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	b.prepareTabbedPane( this );

	buildTabs( b, commandExecutor );
}

public void addNotify()
{
	super.addNotify();

	Container w = getTopLevelAncestor();

	if
		( w instanceof Window )
	{
		( (Window) w ).addWindowListener(
			new WindowAdapter()
			{
				public void windowClosing( WindowEvent ev )
				{
					if
						( m_prototype != null )
					{
						fireToolChange();
					}
				}
			}
		);
	}
}

private void buildTabs(CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor)
{
	add(m_geometryTab = new CoPageItemGeometryPanel( b, commandExecutor), CoPageItemUIStringResources.getName(GEOMETRY_TAB));
	add(m_contentTab = new CoPageItemContentPanel( b, commandExecutor ), CoPageItemUIStringResources.getName(CONTENT_TAB));
	add(m_layoutTab = new CoPageItemLayoutSpecPanel( b, commandExecutor ), CoPageItemUIStringResources.getName(LAYOUT_TAB));
	add(m_layoutManagerTab = new CoPageItemLayoutManagerPanel( b, commandExecutor ), CoPageItemUIStringResources.getName(LAYOUT_MANAGER_TAB));
	add(m_fillTab = new CoPageItemFillPanel( b, commandExecutor ), CoPageItemUIStringResources.getName(FILL_TAB));
	add(m_strokeTab = new CoPageItemStrokePanel( b, commandExecutor ), CoPageItemUIStringResources.getName(STROKE_TAB));
	add(m_gridTab = new CoPageItemGridPanel( b, commandExecutor ), CoPageItemUIStringResources.getName(GRID_TAB));
	add(m_runAroundSpecTab = new CoPageItemRunAroundSpecPanel( b, commandExecutor ), CoPageItemUIStringResources.getName(RUN_AROUND_SPEC_TAB));
	add(m_layoutOrderTab = new CoPageItemLayoutOrderPanel( b, commandExecutor ), CoPageItemUIStringResources.getName(CHILD_LAYOUT_ORDER_TAB));
	add(m_zOrderTab = new CoPageItemZOrderPanel( b, commandExecutor ), CoPageItemUIStringResources.getName(CHILD_Z_ORDER_TAB));
	add(m_imageTab = new CoPageItemImagePanel( b, commandExecutor ), CoPageItemUIStringResources.getName(IMAGE_TAB));
	add(m_layoutContentTab = new CoPageItemLayoutContentPanel( b, commandExecutor ), CoPageItemUIStringResources.getName(LAYOUT_CONTENT_TAB));
	add(m_textTab = new CoPageItemTextPanel( b, commandExecutor ), CoPageItemUIStringResources.getName(TEXT_TAB));
	add(m_workPieceTextTab = new CoPageItemWorkPieceTextPanel( b, commandExecutor ), CoPageItemUIStringResources.getName(WORKPIECE_TEXT_TAB));
	add(m_layoutAreaTab = new CoPageItemLayoutAreaPanel( b, commandExecutor ), CoPageItemUIStringResources.getName(LAYOUT_AREA_TAB));
	add(m_prototypeTab = new CoPageItemPrototypeDataPanel( b, commandExecutor ), CoPageItemUIStringResources.getName(PROTOTYPE_TAB));
	add(m_pageLayerTab = new CoPageItemPageLayerPanel( b, commandExecutor ), CoPageItemUIStringResources.getName(PAGE_LAYER_TAB));
	
	m_tabs = new CoPageItemPropertyPanel[] 
	{
		m_geometryTab, 
		m_contentTab, 
		m_layoutTab, 
		m_layoutManagerTab, 
		m_fillTab, m_strokeTab, 
		m_gridTab, 
		m_runAroundSpecTab, 
		m_layoutOrderTab, 
		m_zOrderTab, 
		m_imageTab, 
		m_layoutContentTab, 
		m_textTab, 
		m_workPieceTextTab, 
		m_layoutAreaTab, 
		m_prototypeTab,
		m_pageLayerTab,
	};
	
	m_tabNames.put(GEOMETRY_TAB, m_geometryTab);
	m_tabNames.put(CONTENT_TAB, m_contentTab);
	m_tabNames.put(LAYOUT_TAB, m_layoutTab);
	m_tabNames.put(FILL_TAB, m_fillTab);
	m_tabNames.put(STROKE_TAB, m_strokeTab);
	m_tabNames.put(GRID_TAB, m_gridTab);
	m_tabNames.put(RUN_AROUND_SPEC_TAB, m_runAroundSpecTab);
	m_tabNames.put(IMAGE_TAB, m_imageTab);
	m_tabNames.put(LAYOUT_CONTENT_TAB, m_layoutContentTab);
	m_tabNames.put(TEXT_TAB, m_textTab);
	m_tabNames.put(CHILD_Z_ORDER_TAB, m_zOrderTab);
	m_tabNames.put(CHILD_LAYOUT_ORDER_TAB, m_layoutOrderTab);
	m_tabNames.put(WORKPIECE_TEXT_TAB, m_workPieceTextTab);
	m_tabNames.put(LAYOUT_AREA_TAB, m_layoutAreaTab);
	m_tabNames.put(PROTOTYPE_TAB, m_prototypeTab);
	m_tabNames.put(PAGE_LAYER_TAB, m_pageLayerTab);
}

private void fireToolChange()
{
	CoTransactionUtilities.execute(
		new CoCommand( "PAGE ITEM TOOL CHANGED" )
		{
			public boolean doExecute()
			{
				m_prototype.pageItemsChanged();
				return true;
			}
		},
		null
	);
}

public void viewChanged( CoPageItemView.Event ev )// view, boolean boundsChanged, int structuralChange, CoShapePageItemView child, int index )
{
	domainHasChanged();
}
}