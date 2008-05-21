package com.bluebrim.gemstone.client;

import java.text.MessageFormat;

import javax.swing.SwingUtilities;

import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.base.shared.CoSetValueException;
import com.bluebrim.base.shared.debug.CoAssertion;
import com.bluebrim.gui.client.CoAspectAdaptor;
import com.bluebrim.gui.client.CoValueable;
import com.bluebrim.transact.shared.*;
import com.bluebrim.transact.shared.CoCommand;
/**
	Aspect adaptor class that assumes its <code>subject</code>, i e the object whose aspect
	the adaptor is handling is a server based businessobject accessed via a remote adaptor.
	Changes to any of its aspects must be in a transaction to permanent the chang.
	<br>
	By subclassing <code>CoGsAspectAdaptor</code> this is handled automatically, the only thing
	the subclass must do is to implement the <code>get</code> and <code>set</code> methods.
 	<p>
 	A problem with editing of persistent object that are in a composition is to make Gemstone/J aware of which 
 	objects the design look upon as changed when an aspect is changed.
 	<br>
 	An example:
 	<br>
 	Object A contains object B and C, C contains object D and E. A change of an aspect in object D adds D to the 
 	notify set in Genstone but B isn't considered as modified. This may not be what you want. If for example D is a collection
 	it's natural to also consider B as changed. This can be handled by toggling a "dirty " flag in B in the same transaction.
 	<br>
 	When running in a simulated mode outside of Gemstone/J the situation is further complicated by the fact that the transaction
 	must be made aware of all objects that are to be considered as changed. 
 */
public abstract class CoGsAspectAdaptor extends CoAspectAdaptor implements CoGemStoneUIConstants, com.bluebrim.gemstone.shared.CoGemstoneConstants {
	protected boolean m_inTransaction;

	protected CoGsAspectAdaptor(String name) {
		super(name);
	}
	/**
	 * CoGsAspectAdaptor constructor comment.
	 * @param context com.bluebrim.base.client.CoValueable
	 * @param name java.lang.String
	 */
	public CoGsAspectAdaptor(CoValueable context, String name) {
		this(context, name, false);
	}
	/**
	 * @deprecated as of 2000-12-01
	 */
	protected CoGsAspectAdaptor(CoValueable context, String name, boolean subjectFiresChange) {
		super(context, name, subjectFiresChange);
	}
	protected void handleTransactionException(final com.bluebrim.gemstone.shared.CoTransactionException e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (e.m_detail != null) {
					MessageFormat msgFormat = new MessageFormat(CoGsUIStringResources.getName(UNABLE_TO_COMMIT));
					CoTransactionUtilities.handleTransactionException(e, msgFormat.format(new Object[] { getValueName()}));
				} else
					CoTransactionUtilities.handleTransactionException(e, e.getMessage());

				m_inTransaction = false;
				valueHasChanged();
			}
		});
	}
	/**
		Update its aspect of the subject with <code>aValue</code>.
		This update is done inside a transaction. If the transaction fails
		an unchecked exception, <code>CoSetValueException</code> is thrown.
		This exception can be caught and handled in the caller, i e a refocus 
		the textfield which was the source of the change. 
	*/
	public void setValue(final Object aValue) {
		if (m_inTransaction)
			return;
		Object tOldValue = getValue();
		if (needToChangeValue(tOldValue, aValue)) {
			final CoObjectIF _subject = getSubject();
			try {
				m_inTransaction = true;
				CoCmdKit.doInTransaction(new CoCommand(getValueName()) {
					public boolean doExecute() {
						if (CoAssertion.SIMULATION_SUPPORT) {
							CoAssertion.addChangedObject(_subject);
						}
						set(_subject, aValue);
						return true;
					}
				});
			} catch (com.bluebrim.gemstone.shared.CoTransactionException e) {
				handleTransactionException(e);
				throw new CoSetValueException(true);
			} finally {
				m_inTransaction = false;
			}
			m_cachedValue = aValue;
			// PENDING: getValue() adds another roundtrip!!
			handleNewValue(tOldValue, getValue());
		}
	}
}
