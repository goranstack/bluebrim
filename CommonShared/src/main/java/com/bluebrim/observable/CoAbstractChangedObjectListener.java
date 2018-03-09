package com.bluebrim.observable;
import javax.swing.SwingUtilities;

/**
 * An abstract implementation of <code> CoChangedObjectListener</code> that
 * wraps the call to call that handles the event in a <code>SwingUtilities.
 * invokeLater</code>. Creation date: (2000-02-01 14:22:22)
 * @author: 
 */
public abstract class CoAbstractChangedObjectListener implements CoChangedObjectListener {

	public CoAbstractChangedObjectListener() {
		super();
	}

	public abstract void changedServerObject(final CoChangedObjectEvent e);

	public void serverObjectChanged(final CoChangedObjectEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				changedServerObject(e);
			}
		});
	}
}
