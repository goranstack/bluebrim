package com.bluebrim.gui.client;


/**
 * A <code>CoSubcanvasAdaptor</code> connects a <code>CoValueable</code>, normally representing a
 * a subdomain in a domain object, with a <code>CoSubcanvas</code> holding the userinterface
 * displaying this subdomain.
 * <br>
 * @author Lasse Svadängs 1997-10-08
 */
public class CoSubcanvasAdaptor extends CoSimpleSubcanvasAdaptor{
public CoSubcanvasAdaptor (CoValueable aValueable, CoSubcanvas aSubcanvas ) {
	super( aValueable, aSubcanvas );
	initializeSubcanvas();
}
/**
 * Override to return UI based on value, if required.
 *
 * NOTE: Currently also needs to override initializeSubcanvas() to do nothing. Very ugly!
 *
 * @see #initializeSubcanvas()
 * @see #updateSubcanvas()
 * @author Markus Persson 1999-10-07
 */
protected CoDomainUserInterface getPreparedUserInterfaceFor(CoDomainUserInterface oldUI, Object value) {
	return oldUI;
}
/**
 */
protected CoDomainUserInterface getUserInterface() {
	return (CoDomainUserInterface) getUI();
}
/**
 * NOTE: Very ugly workaround due to initialization order problems.
 *
 * Override this to do nothing and call updateSubcanvas() from your
 * subclass constructor.
 *
 * PENDING: Handle this by almost duplicate CoSubcanvasAdaptor for subclassing or perhaps
 * by a general post-construction initialization convention.
 *
 * @see #getPreparedUserInterfaceFor(CoDomainUserInterface, Object) 
 * @author Markus Persson 1999-10-07
 */
protected void initializeSubcanvas() {
	updateSubcanvas();
}
/**
 * Modified to support UI based on value, if required.
 *
 * @see #getPreparedUserInterfaceFor(CoDomainUserInterface, Object) 
 * @author Markus Persson 1999-10-07
 */
public void updateSubcanvas() {
	Object value = getValueModel().getValue();
	CoDomainUserInterface oldUI = getUserInterface();
	CoDomainUserInterface newUI = getPreparedUserInterfaceFor(oldUI, value);
	if (newUI != null)
	{
		newUI.buildForComponent();
		newUI.setValue(value);
	}

	if (newUI != oldUI) {
		if (oldUI != null)
			oldUI.setValue(null);
		getSubcanvas().setUserInterface(newUI);

	} else {
//		getSubcanvas().repaint();
	}
	getSubcanvas().setEnabled(getValueModel().isEnabled() && value != null );
}
/**
 */
public void valueChange(CoValueChangeEvent anEvent) {
	super.valueChange( anEvent );
	updateSubcanvas();
}
}