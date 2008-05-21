package com.bluebrim.gui.client;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventListener;
import java.util.EventObject;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.WindowConstants;
import javax.swing.event.EventListenerList;
import javax.swing.event.PopupMenuEvent;

import com.bluebrim.base.shared.CoBaseUtilities;
import com.bluebrim.base.shared.CoConstants;
import com.bluebrim.base.shared.CoStringResources;
import com.bluebrim.base.shared.debug.CoAssertion;
import com.bluebrim.menus.client.CoMenuBar;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.menus.client.CoPopupMenu;
import com.bluebrim.menus.client.CoPopupMenuAdapter;
import com.bluebrim.swing.client.CoBorderLayout;
import com.bluebrim.swing.client.CoPanel;

/**
	Abstract superclass for classes that are used to display and edit data. 
	There's an abstract subclass, <code>CoDomainUserInterface</code>, that's
	used to connect userinterface components to data from a business object.
	<p>
  	The data in the userinterface are represented by value models , objects that has 
  	(or can get hold of) a value and has a basic protocol of <code>getValue</code> and 
  	<code>setValue</code>. A value model is connected to its widget via a component adaptor, 
  	an object that acts as a protocol converter, knowing that its value models can handle 
  	<code>getValue</code> and <code>setValue</code> and that its widget can handle for example,
  	if its a textfield, <code>getText</code> and <code>setText</code>.
  	<p>
	The main component in a userinterface is a <code>CoPanel</code> which contains all other
	components.
	<p>
	<pre>
 		CoPanel
 			CoLabel
 			CoButton
 			CoLabel
 				CoLabel
 				CoTextField
 		...
 		...
  	<pre>
  	This model makes it possible to, in an easy way, reuse the userinterface, i.e the panel,
  	as a component in another userinterface. A call to <code>getPanel</code> creates, if not 
  	already done, and returns the panel and a call to <code>buildForComponent</code> builds 
  	the userinterface and creates all connections between value models and widgets..
  	<p>
  	The responsibility for the widgets and value models are delegated to an instance of
  	<code>CoUserInterfaceBuilder</code>. This object holds all widgets and value models which
  	can be accessed via <code>getNamedWidget</code> and <code>getNamedValueModel</code>.
  	<p>
  	A userinterface can be displayed in
  	<ul>
  	<li>a CoFrame
  	<li>a CoDialog
  	<li>a CoInternalFrame
  	</ul>
  	It's prepared and displayed by sending it as an argument to a constructor in either class.
  	<pre><code>
  		CoFrame tFrame = new CoFrame(new CoAddressUI());
  		tFrame.show();
  	</code></pre>
 	<p> 
 	Instance variables:
 	<ul>
 	<li>	m_listenerList	<EventListenerList>  
 	<li>	m_objectMenu 	<CoPopupMenu> a popup menu with actions with which to manipulate the userinterface and its object
 	<li>	m_menuBuilder	pt<CoMenuBuilder> helper class for creation of the optional menubar, menus an popup menus.
 	<li>	m_uiBuilder		<CoUserInterfaceBuilder>
 	<li>	m_panel			<CoPanel>.
 	<li>	m_hasBeenBuilt	<boolean> true when the userinterface is ready for showing.
 	<li>	m_enabled		<boolean> true when the userinterface is enabled, i e all of its components should be enabled.
 	</ul>
 	@see CoDomainUserInterface.
 	@author Lasse Svadängs 971010.
 	  
 */
public abstract class CoUserInterface implements CoUIConstants, CoConstants {
	protected EventListenerList m_listenerList;
	private CoPopupMenu m_objectMenu;
	private CoMenuBuilder m_menuBuilder;
	private CoUserInterfaceBuilder m_uiBuilder;
	private CoPanel m_panel;
	private boolean m_hasBeenBuilt;
	private boolean m_enabled;
	private boolean m_prebuildingForComponent = false;

	public static String CLOSE_DIALOG = "close_dialog";
	public static String CLOSE_WINDOW = "close_window";

	public static class ObjectMenuEvent extends EventObject {
		private CoPopupMenu m_menu;

		public ObjectMenuEvent(CoUserInterface ui, CoPopupMenu menu) {
			super(ui);
			m_menu = menu;
		}
		public CoPopupMenu getMenu() {
			return m_menu;
		}
		public CoUserInterface getMenuOwner() {
			return (CoUserInterface) getSource();
		}

	}

	public static interface ObjectMenuListener extends EventListener {
		public void objectMenuWillBecomeVisible(ObjectMenuEvent e);
		public void objectMenuWillBecomeInvisible(ObjectMenuEvent e);

	}

	/**
	 *	Override in subclasses to modify menus
	 *  after they are created. Can be used to add
	 *  menu items last in the menus
	 *
	 */
	protected void postCreatePopupMenus() {
	}

	public CoUserInterface() {
		super();
		m_enabled = true;
		initialize();
	}

	public void activate() {
		Window w = getWindow();
		if (w == null)
			return;

		Component focusOwner = w.getFocusOwner();
		if (focusOwner == null)
			requestFirstFocus();
	}

	/**
	 * Adds an <code>ObjectMenuListener</code>.  Object menu listeners are notified
	 * each time the the object menu is about to be visible/invisible.
	 * <p>
	 * One usage is for containing userinterfaces to be notified when an
	 * object menu is about to be shown in a sub ui so they for example can add 
	 * items to the menu. 
	 */
	public synchronized void addObjectMenuListener(ObjectMenuListener l) {
		m_listenerList.add(ObjectMenuListener.class, l);
	}

	public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
		m_listenerList.add(PropertyChangeListener.class, listener);
	}

	/**
		Registrera de som vill lyssna efter CoValueModelEvent, dvs event som representerar 
		förändringar i gränssnittets tillstånd. Används bl a för att kommunicera mellan 
		gränssnitt som ligger i en subcanvas och det gränssnitt som äger subcanvasen.
	 */
	public synchronized void addValueModelListener(CoValueModelListener l) {
		m_listenerList.add(CoValueModelListener.class, l);
	}

	/**
		Anropas i createComponentUserInterface efter det att
		gränssnittet har byggts upp.
		Används för saker som inte kan göras förrän efter
		det att ett gränssnitt som öppnas i t ex en subcanvas 
		eller applet har byggst upp.
	*/
	protected void afterCreateComponentUserInterface() {
		doAfterCreateUserInterface();
	}

	/**
		Anropas i creatUserInterface efter det att
		gränssnittet har byggts upp för att visas upp i ett fönster.
		Används för saker som inte kan göras förrän efter
		det att ett gränssnitt är uppbyggt.
	*/
	protected void afterCreateUserInterface(Window aWindow) {
		doAfterCreateUserInterface();
	}

	/**
		Anropas innan createComponentUserInterface. Ett bra ställe att lägga in kod som måste
		exekveras innan createComponentUserInterface för ett gränssnitt som skall visas upp 
	 	i t ex en subcanvas eller applet.
	 */
	protected void beforeCreateComponentUserInterface() {
		doBeforeCreateUserInterface();
	}

	/**
	  	Anropas innan createUserInterface. Ett bra ställe att lägga in kod som måste
	  	exekveras innan createUserInterface
		@see CoUserInterface#doBeforeCreateUserInterface
	 */
	protected void beforeCreateUserInterface(Window aWindow) {
		doBeforeCreateUserInterface();
	}

	/**
		Utilitymetod som anropas från t ex CoSubcanvas eller 
		från CoUserInterfaceApplet för att göra ett gränssnitt 
		färdigt att visas upp.
	 */
	final public void buildForComponent() {
		if (!m_hasBeenBuilt) {
			if (m_prebuildingForComponent)
				waitForPreBuildForComponent();
			else
				createComponentUserInterface();
		}
	}

	/**
		Utilitymetod som kan anropas för att göra 
		ett gränssnitt färdigt för att visas upp.
	 */
	final protected void buildForWindow(Window aWindow) {

		if (!m_hasBeenBuilt) {
			createUserInterface(aWindow);
			/* NOTE: I think it is more accurate to set this inside 
			 * createUserInterface(), before the after-hooks,
			 * now that you can query its status.
			 * /Markus 1999-10-02
			 */
//			m_hasBeenBuilt = true;
		}
	}

	public boolean canBeEnabled() {
		return true;
	}

	/**
		Called from the <code>CoUserInterfaceBuilder</code> when
		the window showing the userinterface is closed.
	 */
	public void closed() {
		release();
	}

	/**
	 * Called from the <code>CoUserInterfaceBuilder</code> when
	 * the window showing the userinterface is closed iff the close 
	 * operation of the window is DISPOSE_ON_CLOSE.
	 * <p>
	 * There are 3 possible ways to use a ui model.
	 * <ol>
	 * <li>create a new ui model each time. Then you don't need to do anything specific.
	 * When the window is closed this method is called with <code>close</code>=true 
	 * and the ui model is released properly.
	 * <li>reuse the ui model but create a new window each time it's shown. 
	 * Then you need to make sure the window is disconnected from the ui model w/o 
	 * releasing the ui. The easiest way to do this is to reimplement this method to 
	 * call super with <code>close</code>=false. If you want to properly release the ui 
	 * when it's no longer in use, make an explicit call to <code>closed</code>.
	 * <li>both the ui and its window are reused. Set the default close operation of
	 * the window to HIDE_ON_CLOSE. When you no longer have any use for the window 
	 * with its ui, set the the close operation to DISPOSE_ON_CLOSE and call
	 * close for the window.
	 * </ol>
	 */
	public void closedBy(Window w, boolean close) {
		if (CoAssertion.TRACE)
			CoAssertion.trace(CoBaseUtilities.debugInfo(this) + " closedBy " + CoBaseUtilities.debugInfo(w));
		releaseDragAndDrop();
		w.removeWindowListener(getUIBuilder().getWindowListener());
		if (close)
			closed();
	}

	/**
		Called from the <code>CoUserInterfaceBuilder</code> when
		the window showing the userinterface is about to be closed
		and no one has vetoed the operation. Just a hook, a NOP as
		default.
	 */
	public void closing() {
	}

	/**
		Anropar <code>doFirePostBuildEvent</code> mellan <code>beforeSubcanvasPostBuild</code>
		och <code>afterSubcanvasPostBuild</code>.
	 */
	private void createComponentUserInterface() {
		beforeCreateComponentUserInterface();
		doCreateUserInterface();
		/* NOTE: I think it is more accurate to set this here than in
		 * buildForComponent(), now that you can query its status.
		 * /Markus 1999-10-02
		 */
		m_hasBeenBuilt = true;
		afterCreateComponentUserInterface();
	}

	/**
		Creates and returns the instance of <code>CoPanel</code> that will be the 
		instance variable <code>panel</code>.
		Reimplemented by the subclasses that for example need another type
		of layoutmanager.
		Default values for the panel are:
		<ul>
		<li>a <code>CoBorderLayout</code> as layout manager
		<li>no double buffering
		<li>a default insets of null which can be changed by implementing the <code>getDefaultPanelInsets</code>
		<li>a name equal to "PANEL"
		<ul>
	 */
	protected CoPanel createDefaultPanel() {
		return getUIBuilder().createPanel(new CoBorderLayout(), false, getDefaultPanelInsets(), "PANEL");
	}

	/**
	  	Anropas i <code>doAfterPostBuild</code>. Används för att manuellt bygga upp bereonden
	  	mellan olika komponenter i gränssnittet.
	  	<p>
		Observera att koden som implementeras i denna metod anropas både
	 	när gränssnittet öppnas i ett fönster och läggs in i en
	 	subcanvas. Kod som är specifik för endera fallet måste implementeras
	 	i <code>afterPostBuild</code> resp <code>afterSubcanvasPostBuild</code>.
	*/
	protected void createListeners() {
	}

	/**
		Stubbmetod som anropas från <code>afterPostBuild</code>.
		Implementeras i de subklasser som 
		vill ha en menybar i sitt fönster.
	 */
	public CoMenuBar createMenuBar() {
		return null;
	}

	/**
		Anopas från <code>afterPostBuild</code> för att sätta 'objectMenu' som skall hålla den ev 
		objektmeny som gränssnittet skall ha. 
		<code>doCreateObjectMenu</code> svara som default med null och måste implementeras om i de subklasser 
		som verkligen vill ha en objektmeny.
	 */
	protected void createObjectMenu() {
		m_objectMenu = doCreateObjectMenu();
		prepareObjectMenu(m_objectMenu);
	}

	/**
		Anropas från <code>doAfterPostBuild</code> för att instansiera ev popupmenyer. SOm default anropas 
		<code>createObjectMenu</code>. Subklasser som har behov av ytterligare popupmenyer implementerar om 
		denna metod.
	 */
	protected void createPopupMenus() {
		createObjectMenu();
	}

	/**
		Anropar <code>doFirePostBuildEvent</code> mellan <code>beforePostBuild</code>
		och #afterPostBuild.
	 */
	private void createUserInterface(Window aWindow) {
		beforeCreateUserInterface(aWindow);
		doCreateUserInterface();
		/* NOTE: I think it is more accurate to set this here than in
		 * buildForWindow(), now that you can query its status.
		 * /Markus 1999-10-02
		 */
		m_hasBeenBuilt = true;
		afterCreateUserInterface(aWindow);
	}

	protected CoUserInterfaceBuilder createUserInterfaceBuilder() {
		return new CoUserInterfaceBuilder(this);
	}

	/**
		Stubbmetod som kan användas av subklasser för att manuellt
		instansiera värdeobjekt. Anropas från <code>doAfterPostBuild</code>
		Observera att koden som implementeras i denna metod anropas både
	 	när gränssnittet öppnas i ett fönster och läggs in i en
	 	subcanvas. Kod som är specifik för endera fallet måste implementeras
	 	i <code>afterPostBuild</code> resp <code>afterSubcanvasPostBuild</code>.
	*/
	protected void createValueModels(CoUserInterfaceBuilder builder) {
	}

	/**
	  	Anropas i <code>doAfterPostBuild</code>. Används för att manuellt lägga till
	  	gränssnittskomponenter.
	  	<p>
		Observera att koden som implementeras i denna metod anropas både
	 	när gränssnittet öppnas i ett fönster och läggs in i en
	 	subcanvas. Kod som är specifik för endera fallet måste implementeras
	 	i <code>afterPostBuild</code> resp <code>afterSubcanvasPostBuild</code>.
	*/
	protected void createWidgets(CoPanel aPanel, CoUserInterfaceBuilder builder) {
	}

	public void deactivate() {
	}

	protected void dispatchEnableDisable(boolean enable) {
		getUIBuilder().enableDisable(enable);
	}

	/**
	 	Called from <code>afterCreateUserInterface</code> and <code>afterCreateComponentUserInterface</code> when the
	 	userinterface has been built. This is good place to add code that must be executed after <code>createUserInterface</code>.
	 	<p>
	 	Note that this method is called when the userinterface model is opened in a framed as well as in a subcanvas. 
	 	Code that is specific for either case must be implemented in <code>afterCreateUserInterface</code> 
	 	and <code>afterCreateComponentUserInterface</code>.
	 */
	protected void doAfterCreateUserInterface() {
		prepareDragAndDrop();
	}

	/**
	 	Anropas från #createUserInterface och #createComponentUserInterface innan gränssnittet börjar byggas upp.
	 	Ett bra ställe att lägga in kod som måste exekveras innan #doCreateUserInterface.
	 	<p>
	 	Observera att koden som implementeras i denna metod anropas både
	 	när gränssnittet öppnas i ett fönster och läggs in i en
	 	subcanvas. Kod som är specifik för endera fallet skall implementeras
	 	i #beforeCreateUserInterface resp #beforeCreateComponentUserInterface
	 */
	protected void doBeforeCreateUserInterface() {
	}

	/**
		Anropas från #createObjectMenu.
		Implementeras i de subklasser som behöver en objektmeny.
	 */
	protected CoPopupMenu doCreateObjectMenu() {
		return null;
	}

	/**
	  	Anropas från createUserInterface och createComponentUserInterface.
	  	Ansvarar för att anropa ett antal "stubbmetoder" såsom
		<ul>
		<li>	#createWidgets
		<li>	#createValueModels
		<li>	#createPopupMenus
		<li>	#createListeners
		</ul>
		I de allra flest fall finns ingen anledning för att subklasser att implementera om 
		denna metod utan tanken är att det skall räcka med att implementera om ovan 
		uppräknade "stubbmetoderna".
	*/
	protected void doCreateUserInterface() {
		CoUserInterfaceBuilder tBuilder = getUIBuilder();
		createWidgets(getPanel(), tBuilder);
		createValueModels(tBuilder);
		createPopupMenus();
		postCreatePopupMenus();
		createListeners();
	}

	protected void fireObjectMenuWillBecomeInvisibleEvent() {
		fireObjectMenuWillBecomeInvisibleEvent(new ObjectMenuEvent(this, getObjectMenu()));
	}

	public void fireObjectMenuWillBecomeInvisibleEvent(ObjectMenuEvent e) {
		Object tListeners[] = m_listenerList.getListenerList();
		Class tListenerClass = ObjectMenuListener.class;
		for (int i = tListeners.length - 2; i >= 0; i -= 2) {
			if (tListeners[i] == tListenerClass) {
				((ObjectMenuListener) tListeners[i + 1]).objectMenuWillBecomeInvisible(e);
			}
		}
	}

	protected void fireObjectMenuWillBecomeVisibleEvent() {
		fireObjectMenuWillBecomeVisibleEvent(new ObjectMenuEvent(this, getObjectMenu()));
	}

	public void fireObjectMenuWillBecomeVisibleEvent(ObjectMenuEvent e) {
		Object tListeners[] = m_listenerList.getListenerList();
		Class tListenerClass = ObjectMenuListener.class;
		for (int i = tListeners.length - 2; i >= 0; i -= 2) {
			if (tListeners[i] == tListenerClass) {
				((ObjectMenuListener) tListeners[i + 1]).objectMenuWillBecomeVisible(e);
			}
		}
	}

	protected void firePropertyChangeEvent(String propertyName, Object oldValue, Object newValue) {
		Object tListeners[] = m_listenerList.getListenerList();
		PropertyChangeEvent tEvent = null;
		Class tListenerClass = PropertyChangeListener.class;
		for (int i = tListeners.length - 2; i >= 0; i -= 2) {
			if (tListeners[i] == tListenerClass) {
				if (tEvent == null)
					tEvent = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
				((PropertyChangeListener) tListeners[i + 1]).propertyChange(tEvent);
			}
		}
	}

	public void fireValueModelChangeEvent(String valueName, Object oldValue, Object newValue) {
		CoValueable tValueable = getNamedValueModel(valueName);
		fireValueModelChangeEvent(new CoValueChangeEvent(tValueable != null ? (Object) tValueable : (Object) this, valueName, oldValue, newValue));
	}

	public void fireValueModelChangeEvent(String propertyName, CoValueChangeEvent e) {
		fireValueModelChangeEvent(new CoValueModelChangeEvent(this, propertyName, e));
	}

	public void fireValueModelChangeEvent(CoValueChangeEvent e) {
		fireValueModelChangeEvent(new CoValueModelChangeEvent(this, e));
	}

	protected void fireValueModelChangeEvent(CoValueModelChangeEvent event) {
		Object tListeners[] = m_listenerList.getListenerList();
		Class tListenerClass = CoValueModelListener.class;
		for (int i = tListeners.length - 2; i >= 0; i -= 2) {
			if (tListeners[i] == tListenerClass) {
				((CoValueModelListener) tListeners[i + 1]).valueModelChange(event);
			}
		}
	}

	/**
	 * If the userinterface model is shown in a <code>CoFrame</code>
	 * return this frame, otherwise return null.
	 */
	final public CoFrame getCurrentFrame() {
		Window w = getWindow();
		return (w instanceof CoFrame) ? (CoFrame) w : null;
	}

	/**
	 * Return the default size this ui should have 
	 * when opened in a window.
	 * Can return null.
	 */
	public Rectangle getDefaultBounds() {
		return null;
	}

	/**
		Returns the defualt insets for the main panel.
	 */
	protected Insets getDefaultPanelInsets() {
		return null;
	}

	public String getDefaultWindowTitle() {
		return CoStringResources.getName("UNTITLED");
	}

	/**
	 * Return the component that should request focus when
	 * this ui is opened.
	 */
	public Component getFirstFocusOwner() {
		return null;
	}

	protected Component getGlassPane() {

		Window window = getWindow();
		if (window != null) {
			if (window instanceof JFrame)
				return ((JFrame) window).getGlassPane();
			if (window instanceof JWindow)
				return ((JWindow) window).getGlassPane();
			if (window instanceof JDialog)
				return ((JDialog) window).getGlassPane();
		}
		return null;

	}

	final public EventListenerList getListenerList() {
		return m_listenerList;
	}

	public CoMenuBar getMenuBar() {
		CoMenuBar tMenuBar = getMenuBuilder().getMenuBar();
		if (tMenuBar == null)
			tMenuBar = createMenuBar();
		return tMenuBar;
	}

	final public CoMenuBuilder getMenuBuilder() {
		return m_menuBuilder;
	}

	/**
	 	Svarar med det namngivna värdeobjekt som har nyckeln 'key'. 
	 	Som nyckel används oftast värdeobjektets namn.
	 */
	public final CoValueable getNamedValueModel(Object key) {
		CoValueable valueModel = getUIBuilder().getNamedValueModel(key);
		if (CoAssertion.TRACE && valueModel == null)
			System.out.println("ERROR: getNamedValueModel(" + key + "). Valuemodel not found");
		return valueModel;
	}

	/**
	 	Svarar med den namngivna gränssnittskomponenten 
	 	som har nyckeln 'key'. Som nyckel används oftast
	 	komponentens namn.
	 */
	public final Component getNamedWidget(Object key) {
		Component tComponent = getUIBuilder().getNamedWidget(key);
		if (tComponent == null && CoAssertion.TRACE)
			CoAssertion.trace("CoUserInterface.getNamedWidget: Widget not found - " + key.toString());
		return tComponent;
	}

	/**
	 * Svara med min objektmeny. OBS kan vara null.
	 */
	final protected CoPopupMenu getObjectMenu() {
		return m_objectMenu;
	}

	/**
		Svarar med den CoPanel som används för att visa upp gränssnittet.
	 */
	final public CoPanel getPanel() {
		if (m_panel == null)
			m_panel = createDefaultPanel();
		return m_panel;
	}

	final public CoUserInterfaceBuilder getUIBuilder() {
		return m_uiBuilder;
	}

	/**
		If the userinterface model is shown in a <code>Window</code> 
		answer with this window, else answer with null.
	 */
	final public Window getWindow() {
		if (m_panel == null)
			return null;

		for (Container c = getPanel().getParent(); c != null; c = c.getParent()) {
			if (c instanceof Window)
				return (Window) c;
		}

		return null;
	}

	final public WindowListener getWindowListener() {
		return getUIBuilder().getWindowListener();
	}

	private void initialize() {
		m_listenerList = new EventListenerList();
		m_uiBuilder = createUserInterfaceBuilder();
		m_menuBuilder = new CoMenuBuilder(this);
	}

	/**
	 * My name isBuilt, Carl Built. (Dry Swedish humor)
	 *
	 * Returns true if and only if this UI is in such a state
	 * that it has been built and not yet destroyed.
	 *
	 * @author Markus Persson 1999-10-02
	 */
	public final boolean isBuilt() {
		return m_hasBeenBuilt;
	}

	final public boolean isEnabled() {
		return m_enabled;
	}

	/**
	 * Called from the ui builder when creating
	 * widgets that display popup menus.
	 * Must be reimplemented to return false if
	 * lightweight popups can't be used.
	 */
	public boolean isLightWeightPopupEnabled() {
		return true;
	}

	/**
		This method is called from CoFrame, CoDialog, CoWindow or CoInternalFrame
		after pack() and just before the window or frame is opened.
	*/
	public void opened() {
	}

	public CoDialog openInDialog(Frame frame, boolean modal) {
		CoDialog dialog = new CoDialog(frame, modal, this);
		dialog.show();
		return dialog;
	}

	public CoFrame openInWindow() {
		CoFrame frame = new CoFrame(this);
		frame.show();
		return frame;
	}

	final public void prebuildForComponent() {
		if (!m_hasBeenBuilt) {
			Thread prebuildingThread = new Thread(new Runnable() {
				public void run() {
					preCreateComponentUserInterface();
				}
			});
			prebuildingThread.start();
		}
	}

	private synchronized void preCreateComponentUserInterface() {
		try {
			if (CoAssertion.TRACE)
				System.out.println("Starting preCreateComponentUserInterface ...");
			m_prebuildingForComponent = true;
			createComponentUserInterface();
			if (CoAssertion.TRACE)
				System.out.println("Finished preCreateComponentUserInterface");
		} finally {
			m_prebuildingForComponent = false;
		}
	}

	public void prepareDialog(CoDialog dialog) {
		// Replacing the content pane instead of adding into it. /Markus 2002-06-14
		dialog.setContentPane(getPanel());
		prepareWindow(dialog);
		dialog.setJMenuBar(createMenuBar());
		dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		if (!dialog.isModal() && dialog.getTitle() == null)
			dialog.setTitle(getDefaultWindowTitle());

	}

	public void prepareDragAndDrop() {
	}

	public void prepareFrame(CoFrame frame) {
		// Replacing the content pane instead of adding into it. /Markus 2002-06-14
		frame.setContentPane(getPanel());
		prepareWindow(frame);
		frame.setJMenuBar(createMenuBar());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		if (frame.getTitle() == null)
			frame.setTitle(getDefaultWindowTitle());
	}

	public void prepareInternalFrame(CoInternalFrame frame) {
		// Replacing the content pane instead of adding into it. /Markus 2002-06-14
		frame.setContentPane(getPanel());
		buildForComponent();
		frame.setJMenuBar(createMenuBar());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.addInternalFrameListener(getUIBuilder().getInternalFrameListener());
		if (frame.getTitle() == null)
			frame.setTitle(getDefaultWindowTitle());
	}

	protected void prepareObjectMenu(final CoPopupMenu objectMenu) {
		if (objectMenu != null) {
			getPanel().addMouseListener(new CoUIpopupGestureListener(this));
			objectMenu.addPopupMenuListener(new CoPopupMenuAdapter() {
				private ObjectMenuEvent m_event = new ObjectMenuEvent(CoUserInterface.this, objectMenu);
				public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
					fireObjectMenuWillBecomeInvisibleEvent(m_event);
				}
				public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
					fireObjectMenuWillBecomeVisibleEvent(m_event);
				}
			});
		}

	}

	public void prepareWindow(Window w) {
		buildForWindow(w);
		w.addWindowListener(getUIBuilder().getWindowListener());
	}

	/**
	 * Dispatches the event to the 
	 * userinterface builder.
	 * <p>
	 * PENDING: We have a problem with code 
	 * sometimes calling this method after
	 * the ui has been released (or never initialized!).
	 * The exception seems to be harmless though.
	 * <p>
	 * 2001-04-11. Lasse S
	 */
	public void processUserInterfaceEvent(CoUserInterfaceEvent e) {
		if (m_uiBuilder != null)
			m_uiBuilder.processUserInterfaceEvent(e);
		else
			System.err.println("processUserInterfaceEvent called for " + this +" with m_uiBuilder == null");
	}

	/**
		Called from <code>close</code> and is rsponsible for
		undoing everything done in <code>initialize</code>.
		After a call to this you should (at least in theory ) be
		able to call initialize and open it up again, in a new window.
	*/
	protected void release() {
		m_listenerList = null;
		m_uiBuilder = null;
		m_menuBuilder = null;
		m_panel = null;
		m_hasBeenBuilt = false;
	}

	public void releaseDragAndDrop() {
		//CoDragSource.removeAllDropTargetsFor(w);
	}

	/**
	 * Removes an <code>ObjectMenuListener</code>.  Object menu listeners are notified
	 * each time the the object menu is about to be visible/invisible.
	 * <p>
	 * One usage is for containing userinterfaces to be notified when an
	 * object menu is about to be shown in a sub ui so they for example can add 
	 * items to the menu. 
	 */
	public synchronized void removeObjectMenuListener(ObjectMenuListener l) {
		m_listenerList.remove(ObjectMenuListener.class, l);
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
		m_listenerList.remove(PropertyChangeListener.class, listener);
	}

	/**
		@see CoUserInterface#addValueModelListener
	 */
	public synchronized void removeValueModelListener(CoValueModelListener l) {
		m_listenerList.remove(CoValueModelListener.class, l);
	}

	public void requestFirstFocus() {
		Component firstFocus = getFirstFocusOwner();
		if (firstFocus != null)
			firstFocus.requestFocus();
	}

	final public void sendCloseDialog(CoDialog.ClosingReason reason) {
		firePropertyChangeEvent(CLOSE_DIALOG, null, reason);
	}

	final public void sendCloseWindow() {
		firePropertyChangeEvent(CLOSE_WINDOW, null, CLOSE_WINDOW);
	}

	public void setBusy(boolean busy) {
		Component glass = getGlassPane();
		if (glass != null) {
			Cursor cursor = Cursor.getPredefinedCursor(busy ? Cursor.WAIT_CURSOR : Cursor.DEFAULT_CURSOR);
			glass.setCursor(cursor);
			glass.setVisible(busy);
			getWindow().setCursor(cursor);
		}
	}

	public void setEnabled(boolean enable) {
		if (enable != m_enabled) {
			m_enabled = enable && canBeEnabled();
			dispatchEnableDisable(m_enabled);
		}
	}

	final protected void setObjectMenu(CoPopupMenu objectMenu) {
		m_objectMenu = objectMenu;
	}

	/**
		Sätter titeln på det fönster som gränssnittet visas upp i.
	 */
	protected void setWindowBounds(Rectangle bounds) {
		firePropertyChangeEvent("WINDOW_BOUNDS", null, bounds);
	}

	/**
		Sätter titeln på det fönster som gränssnittet visas upp i.
	 */
	public void setWindowTitle(String aTitle) {
		firePropertyChangeEvent("WINDOW_TITLE", null, aTitle);
	}

	/**
	 	Utilitymetod som visar upp en dialog med meddelandet 'message'.
	 */
	public void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(getPanel(), message, "", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 	Utilitymetod som visar upp en dialog med meddelandet 'message'.
	 */
	public void showInformationMessage(String message) {
		showInformationMessage(CoUIStringResources.getName(MESSAGE), message);
	}

	/**
	 	Utilitymetod som visar upp en dialog med meddelandet 'message'.
	 */
	public void showInformationMessage(String title, String message) {
		JOptionPane.showMessageDialog(getPanel(), message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 	Utilitymetod som visar upp en dialog med varningen 'message'.
	 */
	public void showWarningMessage(String message) {
		JOptionPane.showMessageDialog(getPanel(), message, "", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 	Utilitymetod som visar upp en dialog med meddelandet 'message'.
	 */
	public int showYesNoMessage(String message) {
		return JOptionPane.showConfirmDialog(getPanel(), message, "", JOptionPane.YES_NO_OPTION);
	}

	private synchronized void waitForPreBuildForComponent() {
	}
}