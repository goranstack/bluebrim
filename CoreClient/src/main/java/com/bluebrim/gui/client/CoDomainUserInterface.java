package com.bluebrim.gui.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.menus.client.CoPopupMenu;
import com.bluebrim.swing.client.CoPanel;

/**
 	Subclass to <code>CoUserInterface</code> that is used to display and edit data
 	from a business object which implements <code>CoObjectIF</code>.
 	<code>CoDomainUserInterface</code> implements <code>CoValueable</code> and has as its 
 	value the instance variable <code>domain</code> that holds the business object.
 	<p>
 	The userinterface gets its business object either via the constructor or via its
 	<code>setDomain</code> method. A change of business object triggers a posting of a
 	<code>CoValueChangeEvent</code> which is sent to all listeners, normally value models
 	representing values in the business object.
 	<p>
 	In this way the value models get to know that there's a new business object and that the
 	widgets must be updated.
	Look in the class comments for <code>CoUserInterface</code>..
	
 	@see CoUserInterface
 	
 	@author Lasse Svadängs 9710101
 */
public abstract class CoDomainUserInterface extends CoUserInterface implements CoValueable  {
	private CoObjectIF domain;

	/**
	 * DomainUserInterface constructor comment.
	 */
	public CoDomainUserInterface() {
		super();
	}

	/**
	 */
	public CoDomainUserInterface(CoObjectIF aDomainObject) {
		super();
		initDomain(aDomainObject);
	}

	public synchronized void addEnableDisableListener(CoEnableDisableListener l) {
		getUIBuilder().addEnableDisableListener(l);
	}

	public synchronized void addPostValueListener(CoPostValueListener l) {
		m_listenerList.add(CoPostValueListener.class, l);
	}

	public synchronized void addPreValueListener(CoPreValueListener l) {
		m_listenerList.add(CoPreValueListener.class, l);
	}

	/**
		Registrerar de objekt som vill lyssna efter ändringar av gränssnittets värde, 
		dvs efter ett nytt verksamhetsobjekt.
	*/
	final public void addValueListener(CoValueListener l) {
		m_listenerList.add(CoValueListener.class, l);
	}

	/**
		Registrerar de objekt som vill lyssna efter och eventuellt
		protestera mot ändringar av gränssnittets värde,
		dvs efter ett nytt verksamhetsobjekt.
	*/
	final public void addVetoableChangeListener(VetoableChangeListener l) {
		m_listenerList.add(VetoableChangeListener.class, l);
	}

	public boolean canBeEnabled() {
		return super.canBeEnabled() && (getDomain() != null);
	}


	protected void createListeners() {
		super.createListeners();

		if (listenToDomainChanges()) {
			addValueListener(new CoValueListener() {
				public void valueChange(CoValueChangeEvent e) {
					if (!e.isWindowClosingEvent())
						setEnabled(e.getNewValue() != null);
				}
			});
		}

	}

	protected void createObjectMenu() {
		CoPopupMenu tMenu = doCreateObjectMenu();
		prepareObjectMenu(tMenu);
		setObjectMenu(tMenu);
	}

	protected PropertyChangeEvent createVetoableChangeEvent(String propertyName, Object oldValue, Object newValue) {
		return new PropertyChangeEvent(this, propertyName, oldValue, newValue);
	}

	protected void createWidgets(CoPanel aPanel, CoUserInterfaceBuilder builder) {
		super.createWidgets(aPanel, builder);
	}

	/**
	 	Called from <code>afterCreateUserInterface</code> and <code>afterCreateComponentUserInterface</code> when the
	 	userinterface has been built. This is good place to add code that must be executed after <code>createUserInterface</code>.
	 	<br>
	 	Note that this method is called when the userinterface model is opened in a framed as well as in a subcanvas. 
	 	Code that is specific for either case must be implemented in <code>afterCreateUserInterface</code> 
	 	and <code>afterCreateComponentUserInterface</code>.
	 */
	protected void doAfterCreateUserInterface() {
		super.doAfterCreateUserInterface();
		setEnabled(true);
	}

	protected void doSetDomain(CoObjectIF oldValue, CoObjectIF aDomain) {
		preDomainChange(aDomain);
		doSetValue(aDomain);
		propagateDomainChange(oldValue, getDomain());
		postDomainChange(aDomain);
	}

	protected void doSetValue(Object aValue) {
		domain = (CoObjectIF) aValue;
	}

	protected void firePostValueChangeEvent(CoValueChangeEvent event) {
		Object tListeners[] = m_listenerList.getListenerList();
		Class tListenerClass = CoPostValueListener.class;
		for (int i = tListeners.length - 2; i >= 0; i -= 2) {
			if (tListeners[i] == tListenerClass) {
				((CoPostValueListener) tListeners[i + 1]).postValueChange(event);
			}
		}
	}

	protected void firePreValueChangeEvent(CoValueChangeEvent event) {
		Object tListeners[] = m_listenerList.getListenerList();
		Class tListenerClass = CoPreValueListener.class;
		for (int i = tListeners.length - 2; i >= 0; i -= 2) {
			if (tListeners[i] == tListenerClass) {
				((CoPreValueListener) tListeners[i + 1]).preValueChange(event);
			}
		}
	}

	protected void fireValueChangeEvent(Object oldValue, Object newValue) {
		fireValueChangeEvent(new CoValueChangeEvent(this, getValueName(), oldValue, newValue));
	}

	public void fireValueChangeEvent(CoValueChangeEvent event) {
		firePreValueChangeEvent(event);
		Object tListeners[] = m_listenerList.getListenerList();
		Class tListenerClass = CoValueListener.class;
		for (int i = tListeners.length - 2; i >= 0; i -= 2) {
			if (tListeners[i] == tListenerClass) {
				((CoValueListener) tListeners[i + 1]).valueChange(event);
			}
		}
		firePostValueChangeEvent(event);
	}

	/**
	 	Värdet skall ändras och alla som validera det nya värdet skall meddelas om detta.
	 */
	protected void fireVetoableChangeEvent(String propertyName, Object oldValue, Object newValue) throws PropertyVetoException {
		Object tListeners[] = m_listenerList.getListenerList();
		PropertyChangeEvent tEvent = null;
		Class tListenerClass = VetoableChangeListener.class;
		for (int i = tListeners.length - 2; i >= 0; i -= 2) {
			if (tListeners[i] == tListenerClass) {
				if (tEvent == null)
					tEvent = createVetoableChangeEvent(propertyName, oldValue, newValue);
				((VetoableChangeListener) tListeners[i + 1]).vetoableChange(tEvent);
			}
		}

	}

	final public CoObjectIF getDomain() {
		return (CoObjectIF) getValue();
	}

	public Object getValue() {
		return domain;
	}

	public String getValueName() {
		return DOMAIN;
	}

	public Object getKey() {
		return getValueName();
	}

	/**
		Anropas från konstruktorn för att 
		initialt sätta verksamhetsobjektet.
	 */
	protected final void initDomain(CoObjectIF aDomain) {
		doSetValue(aDomain);
	}

	public void initValue(Object aValue) {
		setValue(aValue);
	}

	protected boolean listenToDomainChanges() {
		return true;
	}


	protected void postDomainChange(CoObjectIF aDomain) {
	}

	/**
	 */
	protected void preDomainChange(CoObjectIF aDomain) {
	}

	protected void propagateDomainChange(CoObjectIF oldDomain, CoObjectIF newDomain) {
		fireValueChangeEvent(oldDomain, newDomain);
	}

	/**
		Fönstret är på väg att stängas. All validering av inmatat data har redan skett. 
		Skicka ett "speciellt" CoValueChangeEvent så att alla aspectAdaptors fattar att de skall sluta 
		lyssna efter propertyförändringar i sitt 'subject'.
		@see CoAspectAdaptor#valueChange
	*/
	protected void release() {
		if (m_listenerList != null)
			fireValueChangeEvent(new CoValueChangeEvent(this, CoValueChangeEvent.WINDOW_CLOSING, getDomain(), null));
		super.release();
	}

	public synchronized void removeEnableDisableListener(CoEnableDisableListener l) {
		getUIBuilder().removeEnableDisableListener(l);
	}

	public synchronized void removePostValueListener(CoPostValueListener l) {
		if (m_listenerList != null)
			m_listenerList.remove(CoPostValueListener.class, l);
	}

	public synchronized void removePreValueListener(CoPreValueListener l) {
		if (m_listenerList != null)
			m_listenerList.remove(CoPreValueListener.class, l);
	}

	final public void removeValueListener(CoValueListener l) {
		if (m_listenerList != null)
			m_listenerList.remove(CoValueListener.class, l);
	}

	/**
		Avegistrerar de objekt som vill lyssna efter och eventuellt
		protestera mot ändringar av gränssnittets värde,
		dvs efter ett nytt verksamhetsobjekt.
	*/
	final public void removeVetoableChangeListener(VetoableChangeListener l) {
		if (m_listenerList != null)
			m_listenerList.remove(VetoableChangeListener.class, l);
	}

	/**
		Sätter gränssnittets verksamhetsobjektet.
		Observera att #postDomainChange anropas först efter
		det att #fireValueChangeEvent har anropats.
		På så sätt kan vi vara säkra på att alla värdemodeller
		har korrekta kopplingar innan #postDomainChange. 
	 */
	public void setDomain(CoObjectIF aDomain) {

		CoObjectIF tOldValue = getDomain();
		if (tOldValue != aDomain) {
			doSetDomain(tOldValue, aDomain);
		}
	}

	public void setDomain(Object domain) {
		setDomain((CoObjectIF) domain);
	}

	/**
	 */
	public void setValue(Object aValue) {
		setDomain((CoObjectIF) aValue);
	}

	public Object validate(Object newValue) throws PropertyVetoException {
		return newValue;
	}

	public void valueHasChanged() {
		fireValueChangeEvent(new CoValueChangeEvent(this, CoValueChangeEvent.UPDATE, null, getDomain()));
	}
}