package com.bluebrim.observable;
import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.base.shared.debug.CoAssertion;
import com.bluebrim.gui.client.CoDomainUserInterface;
import com.bluebrim.gui.client.CoPreValueListener;
import com.bluebrim.gui.client.CoValueChangeEvent;
import com.bluebrim.gui.client.CoValueListener;

public abstract class CoServerObjectListener
	extends CoAbstractChangedObjectListener
	implements CoPreValueListener, CoValueListener {

	private CoDomainUserInterface m_userInterface;

	public CoServerObjectListener() {
	}

	public CoServerObjectListener(CoDomainUserInterface ui) {
		m_userInterface = ui;
	}

	protected void addAsListenerTo(Object serverObject) {
		if (serverObject != null) {
			CoObservable.addChangedObjectListener(this, serverObject);
		}
	}

	protected Object getServerObjectFrom(CoObjectIF domain) {
		return domain;
	}

	protected final CoDomainUserInterface getUserInterface() {
		return m_userInterface;
	}

	public void initialize() {
		m_userInterface.addValueListener(this);
		m_userInterface.addPreValueListener(this);
		addAsListenerTo(getServerObjectFrom(m_userInterface.getDomain()));
	}

	public void initialize(CoDomainUserInterface ui) {
		m_userInterface = ui;
		initialize();
	}

	public void preValueChange(CoValueChangeEvent e) {
	}

	public void release() {
		if (m_userInterface != null) {
			m_userInterface.removeValueListener(this);
			m_userInterface.removePreValueListener(this);
			removeAsListenerTo(getServerObjectFrom(m_userInterface.getDomain()));
			m_userInterface = null;
		}
	}

	protected void removeAsListenerTo(Object serverObject) {
		if (serverObject != null) {
			CoObservable.removeChangedObjectListener(this, serverObject);
		} else if (CoAssertion.TRACE) {
			CoAssertion.trace("Trying to remove null as changed object listener");
		}
	}

	public void valueChange(CoValueChangeEvent e) {
		if (!e.isUpdateEvent()) {
			removeAsListenerTo(getServerObjectFrom((CoObjectIF) e.getOldValue()));
			if (!e.isWindowClosingEvent()) {
				addAsListenerTo(getServerObjectFrom((CoObjectIF) e.getNewValue()));
			}
		}
	}
}
