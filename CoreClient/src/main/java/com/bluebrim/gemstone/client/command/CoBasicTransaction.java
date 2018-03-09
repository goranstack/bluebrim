package com.bluebrim.gemstone.client.command;

import com.bluebrim.transact.shared.CoCommand;
/**

	An "almost" concrete subclass to <code>CoAbstractTransaction</code> which is meant to be a 
	pluggable replacement for the now obsolete <code>CoTransactionCommand</code>.
	This class was (normally ) used by the user interface model through a straight forward subclassing 
	and implementation of <code>doTransaction</code> and (more rarely )<code>doLocalTransacion</code>.
	The reason for this division is that when running locally without a GemStone/J server, the session manager
	needs to keep track of which objects are changed by the transaction so it can simulate the notifying mechanism
	normally handled by GemStone/J. In some cases this can lead to unnecessarily complicated transaction code. 
	By providing a separate method for the local case it may be possible to keep the production code in
	<code>doLocalTransaction</code>simple. As default this method just calls <code>doTransaction</code>.
	<br>
	The command based pattern described in <code>CoAbstractionCommand</code> is implemented in <code>CoBasicTransaction</code>
	through an instance variable <code>command</code> which holds an instance of the inner class
	<code>BasicTransactionCommand</code>. The <code>doExecute</code> of this class calls <code>doTransaction</code> or 
	<code>doLocalTransaction</code> of the outer class. Eg by subclassing <code>CoBasicTransaction</code> and implementing
	<code>doTransaction</code> you get exactly the same behavior as with the old class.
 	NOT deprecated As of 2000-01-05,
 	replaced by <code>com.bluebrim.gemstone.shared.CoSession.getSessionManager().doInTransaction(CoCommand, Object)</code>.
 	@see com.bluebrim.gemstone.impl.shared.CoDeploymentSession
 */
public abstract class CoBasicTransaction extends CoAbstractTransaction {

	protected CoCommand m_command;
	public class BasicTransactionCommand extends CoCommand {
		public BasicTransactionCommand(String cmdName) {
			super(cmdName);
		}
		public boolean doExecute() {
			doTransaction();
			return true;
		}
		public void finish() {
			CoBasicTransaction.this.finish();
		}
	};
	/**
	 */
	protected CoBasicTransaction(String name, Object target, Object source) {
		super(name, target, source);
	}
	protected CoCommand createTransactionCommand() {
		return new BasicTransactionCommand("");
	}
	/**
		Implementeras i konkreta subklasserna för att utföra det som 
		utgör själva transaktionen.
	 */
	protected abstract void doTransaction();
	protected final CoCommand getCommand() {
		if (m_command == null)
			m_command = createTransactionCommand();
		return m_command;
	}
}
