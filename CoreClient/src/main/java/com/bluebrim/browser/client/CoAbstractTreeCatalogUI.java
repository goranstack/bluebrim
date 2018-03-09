package com.bluebrim.browser.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.bluebrim.base.shared.CoFactoryElementIF;
import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.base.shared.CoStringResources;
import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.gui.client.CoDomainUserInterface;
import com.bluebrim.gui.client.CoIdentityLayout;
import com.bluebrim.gui.client.CoPreValueListener;
import com.bluebrim.gui.client.CoSubcanvas;
import com.bluebrim.gui.client.CoTreeValueable;
import com.bluebrim.gui.client.CoUIFactoryManager;
import com.bluebrim.gui.client.CoUserInterfaceBuilder;
import com.bluebrim.gui.client.CoUserInterfaceFactory;
import com.bluebrim.gui.client.CoValueChangeEvent;
import com.bluebrim.gui.client.CoValueable;
import com.bluebrim.menus.client.CoMenu;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.swing.client.CoPanel;
import com.bluebrim.swing.client.CoSplitPane;
import com.bluebrim.swing.client.CoTreeBox;

/**
	En abstrakt klass som utgör superklassen för de gränssnitt som representerar
	en trädkatalog - dvs visar upp en trädstruktur. <br>
	Följande metoder är abstrakta och måste implementeras i subklassen:
	<ul>
	<li>	createRootHolder - svarar med ett värdeobjekt som håller objektet som är rot i trädstrukturen.
	<li>	creatTreeCatalogHolder - svarar med ett värdeobjekt som håller trädstrukturen.
	</ul>
 */
public abstract class CoAbstractTreeCatalogUI extends CoDomainUserInterface implements CoTreeCatalogUI {
	protected CoAbstractTreeCatalogEditor		catalogEditor 			= null;
	protected CoTreeCatalogElementIF			selectedElement			= null;
	protected Hashtable							userInterfaceCache;

	protected static final String 				ROOT					= "root";
/**
 * CoAbstractTreeCatalogUI constructor comment.
 */
public CoAbstractTreeCatalogUI() {
	this(null);
}
/**
 * CoAbstractTreeCatalogUI constructor comment.
 * @param aDomainObject java.lang.Object
 */
public CoAbstractTreeCatalogUI(CoObjectIF aDomainObject) {
	super(aDomainObject);
	initialize();
}
/**
	Bygger upp "Lägg till" menyn när ett nytt element har selekterats.
	Ansvaret för detta ligger hos det selekterade elementets 
	gränssnittsfactoryklass.
 */
public void buildAddElementMenuFor(CoTreeCatalogElementIF element,CoMenu aMenu) {
	CoTreeCatalogUIFactoryIF tFactory = (CoTreeCatalogUIFactoryIF )CoUIFactoryManager.getFactory(element);
	tFactory.buildAddElementMenuFor(element,getTreeCatalogEditor(),aMenu);
}
protected boolean canCreateRootObject () {
	return getDomain() != null;
}
protected java.util.List collectExpandedNodes()
{
	java.util.List expanded = new ArrayList();
	collectExpandedNodes( getTreeComponent(), (CoTreeCatalogElementIF) createRootObject(), new TreePath( createRootObject() ), expanded );
	return expanded;
}
private void collectExpandedNodes( JTree t, CoTreeCatalogElementIF e, TreePath p, java.util.List expanded )
{
	Iterator i = e.getElements().iterator();
	while
		( i.hasNext() )
	{
		CoTreeCatalogElementIF n = (CoTreeCatalogElementIF) i.next();
		TreePath q = p.pathByAddingChild( n );
		if
			( t.isExpanded( q ) )
		{
			expanded.add( n );
			java.util.List exp = new ArrayList();
			expanded.add( exp );
			collectExpandedNodes( t, n, q, exp );
		}
	}
}
protected void createListeners()
{
	super.createListeners();
	getTreeCatalogHolder().addTreeSelectionListener(new TreeSelectionListener()
	{
		public void valueChanged(TreeSelectionEvent e)
		{
			treeSelectionChanged(e);
		}
	});
	addPreValueListener(new CoPreValueListener() {
		public void preValueChange(CoValueChangeEvent e)
		{
			getRootHolder().setValue(e.isWindowClosingEvent() ? null : newRootObject());
		}
	});
}
/**
*/
protected abstract Object createRootObject();
/**
	Svarar med en CoSubcanvas som skall visa upp det 
	selekterade elementet i sitt gränssnitt.
*/
protected CoSubcanvas createSelectedElementSubcanvas () {
	CoSubcanvas tSubcanvas			= getUIBuilder().createSubcanvas(new CoIdentityLayout(), "SELECTED_ELEMENT");
	tSubcanvas.setEmptySize(getInitialElementSubcanvasSize());
	return tSubcanvas;
}
/**
 */
protected CoPanel createSubcanvasPanel (CoUserInterfaceBuilder builder ) {
	CoPanel tPanel				= builder.createPanel(new BorderLayout(), true, new Insets(4,4,4,4));
	tPanel.add(new JScrollPane(createSelectedElementSubcanvas()), BorderLayout.CENTER);
	return tPanel;
}
protected CoTreeBox createTreeBox() {
	CoTreeBox tTreeBox =  getUIBuilder().createTreeBox("TREE");
	tTreeBox.setDoubleBuffered(true);
	setTreeViewParameters(tTreeBox.getTreeView());
	tTreeBox.setPreferredSize(new Dimension(200, 200));
	return tTreeBox;
}
protected abstract CoAbstractTreeCatalogEditor createTreeCatalogEditor();
protected abstract CoTreeValueable createTreeCatalogHolder();
/**
 	Svarar med en CoPanel som innehåller trädvyn.
 */
protected CoPanel createTreePanel (CoUserInterfaceBuilder builder ) {
	CoPanel tPanel				= builder.createPanel(new BorderLayout(), true, new Insets(4,4,4,4));
	tPanel.add(createTreeBox(),BorderLayout.CENTER);
	return tPanel;
}
/**
 * Answer the ui model that should be used for displaying the 
 * <code>aDomainObject</code> argument. First look through the cache 
 * to see if an ui model for this type of object has been created already. 
 * The key used is the factory key for the argument. 
 * <br>
 * If it isn't already in the cache it is created, initialized and put in the cache.
 * It's permitted to have elements in the tree structure that isn't diaplyed in an ui.
 *
 */
protected CoDomainUserInterface createUserInterfaceFor(CoFactoryElementIF aDomainObject) {
	CoDomainUserInterface tUserInterface = null;

	if (aDomainObject != null)
	{
		tUserInterface = (CoDomainUserInterface )userInterfaceCache.get(aDomainObject.getFactoryKey());
		if (tUserInterface == null)
		{
			tUserInterface =  doCreateUserInterfaceFor(aDomainObject);
			if (tUserInterface != null)
			{
				tUserInterface.buildForComponent();
//Dennis, 2001-09-05				tUserInterface.prepareDragAndDrop(getCurrentFrame());
				userInterfaceCache.put(aDomainObject.getFactoryKey(),tUserInterface);
			}
		}
		else
		{
			if (! tUserInterface.isBuilt())
			{
				tUserInterface.buildForComponent();
//Dennis, 2001-09-05				tUserInterface.prepareDragAndDrop(getCurrentFrame());
			}
			tUserInterface.setDomain(aDomainObject);
		}
	}


	return tUserInterface;		
		
}
protected void createValueModels (CoUserInterfaceBuilder builder ) {
	super.createValueModels(builder);	
	builder.addValueHolder(builder.createValueHolder(ROOT,newRootObject()));
	createTreeCatalogHolder();
	builder.createTreeBoxAdaptor(getTreeCatalogHolder(),getTreeBox());
	
	
}
/**
 	Som default har en trädkatalog en splitpane med trädvyn i den högra delen 
 	och subcanvasen för det selekterade elementet i den högra.
 */
protected void createWidgets (CoPanel aPanel, CoUserInterfaceBuilder builder ) {
	super.createWidgets(aPanel, builder);

	CoPanel left = createTreePanel(builder);
	CoPanel right = createSubcanvasPanel(builder);

	if
		( right != null )
	{
		CoSplitPane tSplitPane 		= builder.createSplitPane(true, "SPLIT_PANE");
	
		tSplitPane.setLeftComponent(left);
		tSplitPane.setRightComponent(right);
		aPanel.add(tSplitPane,BorderLayout.CENTER);
	} else {
		aPanel.add(left,BorderLayout.CENTER);
	}

}
/**
	Disablar alla menyelement i objektmenyn eftersom inget element är selekterat.
 */
protected void disableElementMenu(CoMenu addElementMenu) {
	addElementMenu.setEnabled(false);
}
protected void doAfterCreateUserInterface ( ) {
	super.doAfterCreateUserInterface();
	initEditor();
}
protected CoDomainUserInterface doCreateUserInterfaceFor(CoFactoryElementIF aDomainObject) {
	CoUserInterfaceFactory factory = CoUIFactoryManager.getFactory(aDomainObject);
	return factory != null ? factory.createUserInterfaceForAndInstall(aDomainObject,null) : null;
}
public TreePath elementToPath(Object element)
{
	return getTreeCatalogHolder().getPathToRootFrom(getTreeComponent(),element);
}
/**
	Enablar/disablar alla menyelement i objektmenyn för det selekterade elementet.
 */
public void enableElementMenu(CoMenu addElementMenu) {
	CoAbstractTreeCatalogEditor 	tCatalogEditor		= getTreeCatalogEditor();
	tCatalogEditor.getMenuAction(CoAbstractTreeCatalogEditor.REMOVE_ELEMENTS_ACTION).setEnabled(enableRemoveElementItem());
	CoTreeCatalogElementIF tSelectedElement 	= getSingleSelectedTreeElement();
	if (tSelectedElement != null)
	{
		CoTreeCatalogUIFactoryIF tFactory 		= (CoTreeCatalogUIFactoryIF )CoUIFactoryManager.getFactory(tSelectedElement);
		tFactory.enableElementMenuFor(tSelectedElement, tCatalogEditor, addElementMenu);
	}
	else
	{
		disableElementMenu(addElementMenu);
	}		
}
/**
	Som default skall "Ta bort" - meny enablas om det 
	finns selekterade element. Subklasser kan behöva mera 
	komplicerade tester för att avgöra om "Ta bort" kan 
	enablas eller inte.
 */
protected boolean enableRemoveElementItem() {
	return hasSelectedTreeElements();
}
/**
 */
public String getAddElementItemLabel() {
	return CoStringResources.getName("ADD_ITEM");
}
protected  CoDomainUserInterface getCurrentlySelectedUserInterface () {
	CoSubcanvas tmp = getSelectedElementSubcanvas();
	return ( tmp == null ) ? null : (CoDomainUserInterface )tmp.getUserInterface();
}
protected Dimension getInitialElementSubcanvasSize () {
	return new Dimension(20,20);
}
/**
 */
public String getRemoveElementItemLabel() {
	return CoStringResources.getName("REMOVE_ITEM");
}
public CoValueable getRootHolder() {
	return getNamedValueModel(ROOT);
}
protected CoTreeCatalogElementIF getSelectedElement () {
	return selectedElement;
}
/**
 */
protected CoSubcanvas getSelectedElementSubcanvas()
{
	return (CoSubcanvas)getNamedWidget("SELECTED_ELEMENT");
}
/**
 	Svarar med en array innehållande alla selekterade element.
 */
public CoTreeCatalogElementIF[] getSelectedTreeElements(){
	CoTreeCatalogElementIF tSelectedElements[]	= null;
	TreePath tSelectionPaths[] 					= getTreeComponent().getSelectionPaths();
	if (tSelectionPaths != null)
	{
		tSelectedElements = new CoTreeCatalogElementIF[tSelectionPaths.length];
		for (int i = tSelectionPaths.length-1; i>=0; i--)
		{
			Object tPath[]			= tSelectionPaths[i].getPath();
			tSelectedElements[i] 	= ((CoTreeCatalogElementIF )tPath[tPath.length-1]).getTreeCatalogElement();
		}	
	}
	return tSelectedElements;	
}
protected int getSelectionCount(){
	return getTreeComponent().getSelectionCount();
}
/**
 */
protected TreePath[] getSelectionPaths()
{
	return getTreeComponent().getSelectionPaths();
}
/**
 * getSelectedTreeElements method comment.
 */
public CoTreeCatalogElementIF getSingleSelectedTreeElement(){
	CoTreeCatalogElementIF tSelectedElements[] 		= getSelectedTreeElements();
	return ((tSelectedElements != null) && (tSelectedElements.length == 1))
				? tSelectedElements[0]
				: null;	
}
/**
 */
protected TreePath getSingleSelectionPath(){
	TreePath selectionPaths[] 		= getSelectionPaths();
	return (selectionPaths != null && selectionPaths.length == 1)
				? selectionPaths[0]
				: null;	
}
protected CoTreeBox getTreeBox() {
	return (CoTreeBox )getNamedWidget("TREE");
}
/**
*/
protected CoAbstractTreeCatalogEditor getTreeCatalogEditor ( ) {
	return catalogEditor;
}
public CoTreeValueable getTreeCatalogHolder() {
	return (CoTreeValueable )getNamedValueModel("TREE");
}
/**
 */
public JTree getTreeComponent() {
	return getTreeBox().getTreeView();
}
/**
 */
public CoMenuBuilder getUIMenuBuilder()
{
	return getMenuBuilder();
}
/**
 */
public boolean hasSelectedTreeElements() {
	CoTreeCatalogElementIF tSelectedTreeElements[] = getSelectedTreeElements();
	return ((tSelectedTreeElements != null) && (tSelectedTreeElements.length > 0));
}
protected void initEditor ( ) {
	catalogEditor = createTreeCatalogEditor();
	catalogEditor.initialize();
	catalogEditor.install();
}
private void initialize ( ) {
	userInterfaceCache		= new Hashtable();
}
protected void install(CoDomainUserInterface ui) {
	CoSubcanvas tmp = getSelectedElementSubcanvas();
	if
		( tmp != null )
	{
		tmp.setUserInterface(ui);
		if (ui != null)
		{
			ui.getUIBuilder().userInterfaceOpenedInSubcanvas();
		}
	}
}
protected Object newRootObject () {
	return canCreateRootObject() ? createRootObject() : null;
}
protected void restoreExpandedNodes( java.util.List expanded )
{
	restoreExpandedNodes( getTreeComponent(), (CoTreeCatalogElementIF) createRootObject(), new TreePath( createRootObject() ), expanded );
}
private void restoreExpandedNodes( JTree t, CoTreeCatalogElementIF e, TreePath p, java.util.List expanded )
{
	Iterator i = expanded.iterator();
	while
		( i.hasNext() )
	{
		CoTreeCatalogElementIF n = (CoTreeCatalogElementIF) i.next();
		java.util.List exp = (java.util.List) i.next();
		TreePath q = p.pathByAddingChild( n );
		t.expandPath( q );
		if
			( ! exp.isEmpty() )
		{
			restoreExpandedNodes( t, n, q, exp );
		}
	}
}
protected void setSelectedElement (CoTreeCatalogElementIF element) {
	selectedElement = element;
}
protected void setTreeViewParameters(JTree treeView) {
	treeView.setCellRenderer(new CoCatalogTreeCellRenderer());
	treeView.setRowHeight(25);
}
public void treeSelectionChanged ( TreeSelectionEvent e) {
	CoTreeCatalogElementIF tSelectedElement = getSingleSelectedTreeElement();

	uninstall(getCurrentlySelectedUserInterface());
	install((tSelectedElement != null) ? createUserInterfaceFor(tSelectedElement): null);
	setSelectedElement(tSelectedElement);
}
protected void uninstall (CoDomainUserInterface ui) {
	if (ui != null)
		ui.setDomain(null);
}
public void updateTree(Object changedElement) {
	TreePath 	tSelPath 		= getSingleSelectionPath();
	JTree 		tTree 			= getTreeComponent();
	TreePath 	tParentPath		= tSelPath.getParentPath();
	boolean 	tExpanded 		= tTree.isExpanded(tSelPath);
	
	if (tParentPath != null) 
		getTreeCatalogHolder().reload(tParentPath);
	else
		getTreeCatalogHolder().reload();
		
	if (tExpanded)
		tTree.expandPath(tSelPath);

	tTree.setSelectionRow(tTree.getRowForPath(tSelPath));
}
}