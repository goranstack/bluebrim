package com.bluebrim.browser.client;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.base.shared.CoPropertyChangeEvent;
import com.bluebrim.base.shared.CoStringResources;
import com.bluebrim.browser.shared.CoCatalogElementIF;
import com.bluebrim.gui.client.CoAutomaticUIKit;
import com.bluebrim.gui.client.CoDomainUserInterface;
import com.bluebrim.gui.client.CoEnableDisableEvent;
import com.bluebrim.gui.client.CoEnableDisableListener;
import com.bluebrim.gui.client.CoListValueable;
import com.bluebrim.gui.client.CoSelectionEvent;
import com.bluebrim.gui.client.CoSelectionListener;
import com.bluebrim.gui.client.CoSubcanvas;
import com.bluebrim.gui.client.CoUserInterface;
import com.bluebrim.gui.client.CoUserInterfaceBuilder;
import com.bluebrim.gui.client.CoUserInterfaceEvent;
import com.bluebrim.gui.client.CoValueHolder;
import com.bluebrim.gui.client.CoValueModelListener;
import com.bluebrim.gui.client.CoValueable;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.menus.client.CoPopupMenu;
import com.bluebrim.swing.client.CoList;
import com.bluebrim.swing.client.CoListBox;
import com.bluebrim.swing.client.CoPanel;
import com.bluebrim.swing.client.CoSplitPane;

/**
 En abstrakt klass som representerar ett kataloggränssnitt, 
 dvs ett gränssnitt med en CoListBox med element i vänstra delen
 samt en CoSubcanvas i den högra som visar upp det seleketerade
 elementet i listan.
 <br>
 Följande metoder är abstrakta och måste implementeras i subklassen:
 <ul>
 <li>	#createCatalogElementUI - svarar med en instans av det gränssnitt som används i subcanvasen.
 <li>	#newCatalogElement - svarar med ett nytt katalogelement 
 <li>	#getWindow - ärvd från CoUserInterface
 <li>	#getPanel - ärvd från CoUserInterface
 </ul>
 
 CoAbstractListAspectAdaptor#createCatalogHolder som svarar med det värdeobjeket som håller listan svarar 
 som default med en instans av CoElementContainerIF_Elements som förutsätter att katalogobjektet har en 
 metod #getElements som svarar med katalogens element. Subklasser med en avvikande uppfattining måste 
 implementer om #createCatalogHolder.
 
 Instansvariabler
 <ul>
 <li>	catalogHolder				CoListAspectAdaptor som håller verksamhetsobjektets lista	
 <li>	catalogElementUIHolder		CoValueHolder som håller gränssnittet i subcanvasen.
 <li>	catalogEditor				CoListCatalogEditor som sköter editeringen av listan
 <li>	selectedElementHolder		CoValueable	som håller  selekterat element ur listan
 </ul>
  @see CoSubcanvas
*/
public abstract class CoAbstractCatalogUI extends CoDomainUserInterface implements CoCatalogUI, com.bluebrim.base.shared.CoPropertyChangeListener {
	protected CoListValueable.Mutable 					m_catalogHolder;
	protected CoListCatalogEditor 						m_catalogEditor;
	protected CoValueable								m_selectedElementHolder;
	protected CoValueModelListener						m_catalogElementListener;
	protected int										m_orientation;

	public static int VERTICAL				= 0;
	public static int HORIZONTAL			= 1;

/**
 * CatalogUI constructor comment.
 */
public CoAbstractCatalogUI() {
	this(HORIZONTAL);
}


/**
 * CatalogUI constructor comment.
 */
public CoAbstractCatalogUI(int orientation) {
	this(null, orientation);
}


/**
 * CatalogUI constructor comment.
 * @param aDomainObject java.lang.Object
 */
public CoAbstractCatalogUI(CoObjectIF aDomainObject) {
	this(aDomainObject, HORIZONTAL);
}


/**
 * CatalogUI constructor comment.
 * @param aDomainObject java.lang.Object
 */
public CoAbstractCatalogUI(CoObjectIF aDomainObject, int orientation) {
	super(aDomainObject);
	m_orientation	= orientation;
}


/**
	Ett nytt element skall läggas till i listan.
 */
public CoCatalogElementIF addElement()
{
	return addElement(newCatalogElement());
}


/**
	Ett nytt element skall läggas till i listan.
 */
public CoCatalogElementIF addElement(CoCatalogElementIF element)
{
	getCatalogHolder().addElement(element);
	return element;
}


protected void checkSelectionChange(PropertyChangeEvent e) throws PropertyVetoException {
	if (e.getPropertyName().equals(CoList.SELECTION_CHANGED)) {
		CoUserInterface selectedElementUI = getCurrentElementUI();
		if (selectedElementUI != null && e.getNewValue() != getSelectedElement()) {
			CoUserInterfaceEvent tEvent = new CoUserInterfaceEvent(selectedElementUI, CoUserInterfaceEvent.UI_VALIDATED);
			selectedElementUI.processUserInterfaceEvent(tEvent);
			if (tEvent.isConsumed()) {
				throw new PropertyVetoException("Validation failed in catalog element ui", e);
			}
		}
	}

}


public void clearSelection()
{
	getCatalogList().clearSelection();
}


protected Object [] collectSelectedElements()
{
	return getSelectedCatalogElements();
}


/**
	Anropas från #doAfterPostBuild()
	för att instansiera en CoCatalogEditor. 
 */
protected CoListCatalogEditor createCatalogEditor () {
	return  new CoListCatalogEditor(this);
}


/**
 * Legacy support.
 *
 * NOTE: Only overridden in legacy subclasses. Newer subclasses should
 * rely on CoAutomaticUIKit. If special treatment is needed, override
 * createElementUIMap() instead.
 *
 * @see #createElementUIMap()
 * @author Lasse (?)
 * @author Markus Persson 2001-10-15
 */
protected CoUserInterface createCatalogElementUI() {
	return null;
}


/**
*/
protected abstract CoListValueable.Mutable createCatalogHolder();


/**
	Defaultbeteende är att skapa en instans av CoListBox med en 
	"vanlig" CoCatalogListCellRenderer som renderer. Subklasser med 
	andra krav får implementera om denna metod.
*/
protected CoListBox createCatalogListBox(CoUserInterfaceBuilder builder) {
	CoListBox 	tListBox		= builder.createListBox(createCatalogListCellRenderer(builder),"ELEMENTS");
	/*Dimension	tPreferredSize	= new Dimension(150,150);	
	tListBox.setPreferredSize(tPreferredSize);
	tListBox.setMinimumSize(tPreferredSize);*/
	JList		tList		= tListBox.getList();		
	tList.setFixedCellHeight(25);
	tList.setFixedCellWidth(150);

	return tListBox;
}


protected ListCellRenderer createCatalogListCellRenderer(CoUserInterfaceBuilder builder) {
	// NOTE: Changed return type from unnecessarily specific. /Markus 2001-10-15
	// gohu: Removed builder.getDefaults() according to Lars. Is this called from subclasses?
	return new CoCatalogListCellRenderer();
}


/**
	Defaultbeteende är att skapa en CoPanel med en BorderLayout där 
	subcanvasen läggs i NORTH. Subklasser med andra krav får 
	implementera om denna metod.
*/
protected CoPanel createCatalogSubcanvasPanel(CoUserInterfaceBuilder builder) {
	CoPanel tPanel = builder.createPanel(getCatalogSubcanvasLayout());
	tPanel.add(builder.createSubcanvas("SELECTED_ELEMENT"), getCatalogSubcanvasLayoutConstraint());
	return tPanel;
}


/**
 * Override for special treatment, such as showing
 * an element UI even if no element is selected.
 * (Then, super should NOT be called.)
 *
 * @author Markus Persson 2001-10-15
 */
protected Map createElementUIMap() {
	Map uiMap = new HashMap();
	CoUserInterface legacyUI = createCatalogElementUI();
	if (legacyUI != null) {
		// Show UI when no element is selected ...
		uiMap.put(CoAutomaticUIKit.NO_ITEM_KEY, legacyUI);
		// Show UI when non-handled element is selected ...
		uiMap.put(CoAutomaticUIKit.UNKNOWN_ITEM_KEY, legacyUI);
	}
	return uiMap;
}


/**
 * Override in subclasses to customize the component that
 * appears in the left hand area of the split pane. Default
 * is a plain listbox
 */
protected Component createLeftComponent(CoListBox listBox, CoUserInterfaceBuilder builder) {
	return listBox;
}


/**
*/
protected void createListeners () {
	super.createListeners();


	getCatalogHolder().addSelectionListener(new CoSelectionListener() {
		public void selectionChange(CoSelectionEvent anEvent) 
		{
			handleCatalogSelection(anEvent);
		}
	});
	getCatalogList().addVetoableChangeListener(new VetoableChangeListener() {
		public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException
		{
			checkSelectionChange(e);
		}
	});
	getUIBuilder().addEnableDisableListener(new CoEnableDisableListener() {
		public void enableDisable(CoEnableDisableEvent e)
		{
			enableCatalogElementUI(e.enable());
		}
	});
}


protected CoValueable createSelectedElementHolder()
{
	CoValueHolder valueHolder = new CoValueHolder(null, "SELECTED_ELEMENT") {
		protected boolean hasValueChanged(Object oldValue, Object newValue)
		{
			return oldValue != newValue || super.hasValueChanged(oldValue, newValue);
		}
	};
	getUIBuilder().addNamedValueModel("SELECTED_ELEMENT", valueHolder);
	return valueHolder;
}


protected void createValueModels(CoUserInterfaceBuilder builder) {
	super.createValueModels(builder);
	builder.createListBoxAdaptor(getCatalogHolder(), getCatalogListBox());
	builder.createAutoSubcanvasAdaptor(getSelectedElementHolder(), getSelectedElementSubcanvas(), createElementUIMap());
}


protected void createWidgets(CoPanel aPanel, CoUserInterfaceBuilder builder) {
	super.createWidgets(aPanel,builder);

	CoSplitPane tSplitPane 		= builder.createSplitPane(true, "SPLIT_PANE");
	tSplitPane.setOrientation(m_orientation);
	tSplitPane.setContinuousLayout(splitPaneHasContinuousLayout());
	tSplitPane.setOneTouchExpandable(splitPaneIsOneTouchExpandable());
	
	tSplitPane.setLeftComponent(createLeftComponent(createCatalogListBox(builder), builder));
	tSplitPane.setRightComponent(createCatalogSubcanvasPanel(builder));

	aPanel.add(tSplitPane,BorderLayout.CENTER);
}


/**
 	Anropar #enableDisable(false) för det gränssnitt som skall
	visa upp selekterat katalogelement och ser på så sätt till
	att dess gränssnittskomponenter från början är disablade.
*/
protected void doAfterCreateUserInterface () {
	super.doAfterCreateUserInterface();
	m_catalogEditor = createCatalogEditor();
	if (m_catalogEditor != null)
		m_catalogEditor.initialize();
}


protected void enableCatalogElementUI(boolean enable) {
	if (getCurrentElementUI() != null) {
		getCurrentElementUI().setEnabled(enable);
	}
}


/**
 */
public String getAddItemLabel() {
	return CoStringResources.getName("ADD_ITEM");
}


/**
 */
final public CoListCatalogEditor getCatalogEditor () {
	return m_catalogEditor;
}


/**
 */
final public CoListValueable.Mutable getCatalogHolder()
{
	if (m_catalogHolder == null)
		m_catalogHolder = createCatalogHolder();
	return m_catalogHolder;	
}


/**
 */
final public CoList getCatalogList()
{
	return (CoList )getCatalogListBox().getList();
}


/**
 */
public CoListBox getCatalogListBox()
{
	return (CoListBox )getNamedWidget("ELEMENTS");
}


final protected CoPopupMenu getCatalogMenu () {
	return (CoPopupMenu)getNamedWidget("CatalogMenu");
}


/**
 */
public CoObjectIF getCatalogOwner () {
	return getDomain();
}


/**
	Defaultbeteende är att skapa en CoPanel med en BorderLayout.
	Subklasser med andra krav får implementera om denna metod.
*/
protected LayoutManager getCatalogSubcanvasLayout() {
	return new BorderLayout();
}


/**
	Defaultbeteende är att skapa en CoPanel med en BorderLayout där 
	subcanvasen läggs i NORTH. Subklasser med andra krav får 
	implementera om denna metod.
	
	CHANGED 2000-11-01 Kargy
	Since all collection UIn seem to want CENTER instead I changed it to CENTER
	If subclasses want something else, they must override this method
	
*/
protected Object getCatalogSubcanvasLayoutConstraint() {
	return BorderLayout.CENTER;
}


/**
 * Returns the current catalog element UI or null if in such a state
 * that there are no current UI. Depending on subclasses, this could
 * be at initialization, when no element is selected or when the
 * selected element does not have a corresponding UI.
 *
 * Compare with getGuaranteedElementUI() that always returns a UI. This
 * method is preferred over that if the code using it can handle it.
 *
 * @see #getGuaranteedElementUI()
 * @author Markus Persson 2001-10-15
 */
final protected CoDomainUserInterface getCurrentElementUI() {
	CoSubcanvas subcanvas = (CoSubcanvas) getNamedWidget("SELECTED_ELEMENT");
	return (subcanvas != null) ? (CoDomainUserInterface) subcanvas.getUserInterface() : null;
}


/**
 * Attempts to guarantee to return an element UI by attempting the same
 * paths as used by the old code. If it still fails, it will be notified.
 *
 * @see #getCurrentElementUI()
 * @author Markus Persson 2001-10-15
 */
final protected CoDomainUserInterface getGuaranteedElementUI() {
	CoDomainUserInterface ui = getCurrentElementUI();
	if (ui != null) return ui;

/*
	// PENDING:  Hmm, obtain the current ui map.
	ui = getCurrentElementUI();
	if (ui != null) return ui;
*/
	System.out.println("Failed to deliver a non-null element UI for " + getClass().getName());
	new Exception("Dummy").printStackTrace();
	return null;
}


/**
 */
public String getRemoveItemLabel() {
	return CoStringResources.getName("REMOVE_ITEM");
}


public Object[] getSelectedCatalogElements () {
	return getCatalogList().getSelectedValues();
}


protected CoValueable getSelectedElementHolder () {
	if (m_selectedElementHolder == null)
		m_selectedElementHolder = createSelectedElementHolder();
	return m_selectedElementHolder;
}


protected CoSubcanvas getSelectedElementSubcanvas() {
	return (CoSubcanvas) getNamedWidget("SELECTED_ELEMENT");
}


public CoSplitPane getSplitPane() {
	return (CoSplitPane )getNamedWidget("SPLIT_PANE");
}


/**
 */
public CoMenuBuilder getUIMenuBuilder()
{
	return getMenuBuilder();
}


/**
 	En selektering/avseleketering har skett i listan med katalogelement.
 	Om det är en selektering så skall motsvarande katalogelement visas upp 
 	i mitt 'catalogElementUI'. Via #setSelectedElement får subcanvasen med
 	katalogelementets gränssnitt sitt nya verksamhetsobjekt.
 *
 * @author Lasse (?)
 * @author Markus Persson 2001-10-15
 */
public void handleCatalogSelection(CoSelectionEvent event) {
	if (!event.getValueIsAdjusting()) {
		CoObjectIF selectedElement = null;
		int firstIndex = event.getFirstIndex();

		if ((firstIndex > -1) && (firstIndex == event.getLastIndex())) {
			selectedElement = (CoObjectIF) getCatalogHolder().getElementAt(firstIndex);
		}
		
		if (selectedElement != getSelectedElement()) {
			handleNewSelection(selectedElement);
		}
	}
}


public boolean hasSelectedCatalogElements () {
	Object tSelectedElements[] = getSelectedCatalogElements(); 
	return (tSelectedElements != null) && (tSelectedElements.length > 0);
}


/**
 */
public String listeningFor()
{
	return "Identity";
}


/**
 */
protected abstract CoCatalogElementIF newCatalogElement();


public void openSelectedElementInNewWindow() {
	CoObjectIF selected = getSelectedElement();

	if (selected != null) {
		// NOTE: Crude way that could be very expensive for some subclasses.
		// Fortunately, this method is not used much ... (Also note that
		// the old code could be about equally expensive.) /Markus 2001-10-15
		Map uiMap = createElementUIMap();
		CoDomainUserInterface ui = CoAutomaticUIKit.getUIForUsing(selected, uiMap);
		ui.setDomain(selected);
		ui.openInWindow(); //.setTitle( selected.getIdentity() );
	}
}


public void postRemoveElements() {
	setSelectedElement(null);
	getCatalogList().clearSelection();
	getCatalogList().repaint();
}


protected void preDomainChange(CoObjectIF domain) {
	if ( isBuilt() ) getCatalogList().clearSelection();

	super.preDomainChange(domain);
}


public void propertyChange( CoPropertyChangeEvent anEvent)
{
	if (anEvent.getSource() == this)
		return;
	if (((Object )anEvent.getPropertyOwner() == (Object )getSelectedElement())
			&& ("Identity".equals(anEvent.getPropertyName())))
	{
		getCatalogHolder().elementHasChanged(this,getSelectedElement());
	}	
}


/**
 */
public void removeElements(Object[] elements)
{
	getCatalogHolder().removeElements(elements);
}


protected void restoreSelectedElements( Object [] elements )
{
	java.util.List oldSelected = java.util.Arrays.asList( elements );

	ListSelectionModel sm = getCatalogList().getSelectionModel();
	sm.clearSelection();
	
	ListModel m = getCatalogList().getModel();
	int I = m.getSize();
	for
		( int i = 0; i < I; i++ )
	{
		if
			( oldSelected.contains( m.getElementAt( i ) ) )
		{
			sm.addSelectionInterval( i, i );
		}
			
	}
}


public void selectIndex (int index ) {
	getCatalogList().setSelectedIndex(index);
	if (index != -1)
		getCatalogList().ensureIndexIsVisible(index);
}


/**
 */
protected boolean splitPaneHasContinuousLayout()
{
	return true;
}


/**
 */
protected boolean splitPaneIsOneTouchExpandable()
{
	return true;
}

protected void basicSetSelectedElement(CoObjectIF element) {
	getSelectedElementHolder().setValue(element);
}


public CoObjectIF getSelectedElement() {
	return (CoObjectIF) getSelectedElementHolder().getValue();
}


public CoObjectIF getSingleSelectedCatalogElement() {
	return getSelectedElement();
}


protected void handleNewSelection(CoObjectIF element) {
	setSelectedElement(element);
}


public void selectElement(CoCatalogElementIF element) {
	int index = getCatalogHolder().indexOf(element);
	if (getCatalogList().isSelectedIndex(index))
		getCatalogList().clearSelection();
	selectIndex(index);
}


protected void setSelectedElement(CoObjectIF element) {
	basicSetSelectedElement(element);
}
}