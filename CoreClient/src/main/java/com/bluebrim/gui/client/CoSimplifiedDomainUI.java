package com.bluebrim.gui.client;

import com.bluebrim.base.shared.CoObjectIF;

/**
 * This class contains only general extensions to the CoUserInterface class
 * which could and perhaps should be moved up the class hierarchy. This class
 * exists only because I didn't want to add stuff to CoDomainUserInterface at
 * this point.
 *
 * @author Markus Persson 1999-10-07
 */
public abstract class CoSimplifiedDomainUI extends CoDomainUserInterface {
public CoSimplifiedDomainUI() {
	this(null);
}
public CoSimplifiedDomainUI(CoObjectIF domainObject) {
	super(domainObject);
}
/**
 * PENDING: This implementation of this method is a general extension
 * of the CoUserInterface class and could and perhaps should be moved
 * up the hierarchy.
 *
 * @author Markus Persson 1999-10-02
 */
public void closing() {
	CoObjectIF domain = getDomain();
	if ((domain != null) && isBuilt())
		preRemoveDomain(domain);

	super.closing();
}
/**
 * PENDING: This implementation of this method is a general extension
 * of the CoUserInterface class and could and perhaps should be moved
 * up the hierarchy.
 *
 * @author Markus Persson 1999-10-02
 */
protected void doAfterCreateUserInterface() {
	super.doAfterCreateUserInterface();

	CoObjectIF domain = getDomain();
	// NOTE: Don't need to check isBuilt() here ...
	if (domain != null)
		postInstallDomain(domain);
}
/**
 * It us now much better to use postInstallDomain().
 *
 * PENDING: This implementation of this method is a general extension
 * of the CoUserInterface class and could and perhaps should be moved
 * up the hierarchy. (In which case the "final" probably should be
 * skipped. It is handy here, however).
 *
 * @see #postInstallDomain()
 * @author Markus Persson 1999-10-02
 */
protected final void postDomainChange(CoObjectIF domain) {
	super.postDomainChange(domain);

	if ((domain != null) && isBuilt())
		postInstallDomain(domain);
}
/**
 * IMHO, a method handling the common case better than postDomainChange().
 * Also better named, since the term "change" doesn't make clear whether
 * we have a new domain object or the current domain object merely changed
 * state.
 *
 * Guaranteed to be called only with a built user interface and
 * then exactly once for every domain object (other than null)
 * used as soon as these criteria are met. In other words,
 * called conditionally from both doAfterCreateUserInterface()
 * and postDomainChange().
 *
 * @see #doAfterCreateUserInterface()
 * @see #postDomainChange()
 * @see #preRemoveDomain()
 * @author Markus Persson 1999-10-02
 */
protected void postInstallDomain(CoObjectIF domain) {
}
/**
 * It us now much better to use preRemoveDomain().
 *
 * PENDING: This implementation of this method is a general extension
 * of the CoUserInterface class and could and perhaps should be moved
 * up the hierarchy. (In which case the "final" probably should be
 * skipped. It is handy here, however).
 *
 * @see #preRemoveDomain()
 * @author Markus Persson 1999-10-02
 */
protected final void preDomainChange(CoObjectIF domain) {
	CoObjectIF oldDomain = getDomain();
	
	if ((oldDomain != null) && isBuilt())
		preRemoveDomain(oldDomain);

	super.preDomainChange(domain);
}
/**
 * NOTE: The domain object given is the domain object about to be removed.
 *
 * IMHO, a method handling the common case better than preDomainChange().
 * Also better named, since the term "change" doesn't make clear whether
 * we have a new domain object or the current domain object merely changed
 * state. (Of course it would be quite hard with the current design to do
 * anything before a state change. That would be more like a vetoable change.)
 *
 * Guaranteed to be called only with a built user interface and then
 * exactly once for every domain object (other than null) used. At
 * least that's the intention. The current implementation is to be
 * conditionally called from both preDomainChange() and closing().
 *
 * @param CoObjectIF domain The domain object about to be removed.
 * @see #preDomainChange()
 * @see #release()
 * @see #postInstallDomain()
 * @author Markus Persson 1999-10-02
 */
protected void preRemoveDomain(CoObjectIF domain) {
}
}
