package com.bluebrim.page.impl.server;
import java.util.EventObject;

import com.bluebrim.base.shared.CoEventListenerList;
import com.bluebrim.base.shared.CoObject;
import com.bluebrim.page.shared.CoPage;
import com.bluebrim.page.shared.CoPageChangeListener;
import com.bluebrim.page.shared.CoPageContext;

/**
 * The abstract super class for objects that implements the interface CoPage
 * Creation date: (2001-11-22 11:53:12)
 * @author Arvid Berg
 */
public abstract class CoAbstractPage extends CoObject implements CoPage  {

	private CoEventListenerList m_listeners = new CoEventListenerList();

	public void addPageChangeListener(CoPageChangeListener listener) {
		m_listeners.add(CoPageChangeListener.class, listener);
	}

	public void removePageChangeListener(CoPageChangeListener listener) {
		m_listeners.remove(CoPageChangeListener.class, listener);
	}
		
	/**
	 * Fires EventObject to the listeners.
	 */
	protected void firePageChange(Object source) {
		Object[] targets;

		synchronized (this) {
			if (m_listeners.getListenerCount() < 1)
				return;
			targets = (Object[]) m_listeners.getListenerList().clone();
		}
		EventObject evt = new EventObject(source);

		for (int i = 0; i < targets.length; i += 2) {
			if ((Class) targets[i] == CoPageChangeListener.class) {
				CoPageChangeListener target = (CoPageChangeListener) targets[i + 1];
				target.pageChange(evt);
			}
		}
	}

	public abstract String getName();
	
	public String getDescription() {
		return getName();
	}
	
	public void layoutsChanged() {
		firePageChange(this);
	}

	public abstract CoPageContext getPageContext();

	public boolean satisfyPagePlaceRequest(CoPage page) {
		return (getPageContext().equals(page.getPageContext()) & (getName().equals(page.getName())));
	}


}