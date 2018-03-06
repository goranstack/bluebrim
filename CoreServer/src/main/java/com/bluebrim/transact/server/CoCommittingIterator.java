package com.bluebrim.transact.server;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.bluebrim.transact.shared.*;

/**
 * Wrapping iterator doing partial commits and retries.
 *
 * Using one of the factory methods, it is possible to
 * obtain a committing iterator, a wrapping collection
 * returning a committing iterator or a committing
 * iterator wrapping a fake iterator returning a single
 * null element. The latter is a simple means to add
 * commit retries to a piece of code. Just remember to
 * call next() ...
 * 
 *
 * Currently, this iterator tries to commit each chunk
 * of objects a total of six times. If it still cannot
 * commit the chunk, this is logged internally and
 * iteration proceeds with the next chunk.
 *
 * PENDING: Make the number of retries configurable.
 * PENDING: Include ability to obtain non-committed objects.
 *
 * This iterator can be used both inside and outside
 * of transactions. It handles both cases automatically,
 * returning to the initial state after hasNext() has
 * returned false for the first time. The initial state
 * is determined the first time hasNext() is called (and
 * not when the iterator is created)!
 *
 * Whatever case, if used normally (described below),
 * all operations on elements returned will be committed.
 * If the iterator is empty, no begins or commits will be
 * made.
 *  
 * Normal usage as mentioned above, means to loop while
 * hasNext() returns true, invoking next() inbetween.
 * Especially important is that following a next(),
 * hasNext() shouldn't be called until all persistent
 * changes pertaining to the object returned by next()
 * has been done. Other than that, hasNext() can be
 * invoked as many times as you'd like inbetween next()
 * calls. Although not recommended, this includes zero
 * times, since hasNext() is implicitly called as part
 * of next().
 *
 *
 * Example usage:
 *
 * Collection fishes = ...;
 *
 * Iterator iter = CoCommittingIterator.create(fishes);
 * while (iter.hasNext()) {
 *     Fish fish = (Fish) iter.next();
 *     fish.boil();
 *     person.eat(fish);
 *     if (!iter.hasNext()) {
 *         System.out.prinln("Last fish was " + fish + ".");
 *     }
 * }
 *
 * The last hasNext() is valid because no persistant changes pertaining
 * to the fish is done afterwards. (Assuming Fish.toString() has no side
 * effects.)
 *
 *
 *
 * WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING
 * Non supported usage:
 *
 * Collection fishes = ...;
 *
 * Iterator iter = CoCommittingIterator.create(fishes);
 * while (iter.hasNext()) {
 *     Fish fish = (Fish) iter.next();
 *     fish.boil();
 *     if (iter.hasNext()) { // WARNING: Errorneous usage!
 *         freezer.put(fish);
 *     } else {
 *         person.eat(fish);
 *     }
 * }
 *
 * This won't work, because hasNext() is called before all persistent
 * pertaining to the fish has been done. (Assuming eating the fish is a
 * persistent change to either the fish or the person.)
 * 
 * An attempt to fix this code by simply doing a commit afterwards, will
 * fail unless this code is inside a transaction. Even then, no retries
 * will be performed, so it is possible for the fish to be boiled but not
 * eaten.
 * 
 * Also, although it may seem as though the freezing of the fish always
 * is committed, albeit in the next round, this is not true for the last
 * fish in each "commit chunk" if the next chunk doesn´t commit on the
 * first try. In this case, the fish in question won't be frozen.
 * WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING
 *
 *
 * @author Markus Persson 2000-10-10
 * @author Markus Persson 2002-09-04
 */
public class CoCommittingIterator implements Iterator {
	private final static int DEFAULT_COMMIT_FREQUENCY = 50;
	private final static int DEFAULT_RETRIES = 5;

	private boolean m_initiallyInTransaction;
	private int m_freq;
	private Iterator m_delegate;
	private List m_buffer;
	private State m_state;
	private State m_firstTry;
	private State m_reTry;


	// Statistics.
	private int m_retries;
	private int m_failedChunks;

	// Weird method names due to slow VAJ otherwise ...
	private abstract class State {
		public void init() {
		}
		public abstract boolean hasMore();
		public abstract Object another();
		protected final boolean partialCommit() {
			boolean hasNext;
			try {
				CoTransactionService.commit();
//				System.out.println("Commit succeded in CoCommittingIterator.");
				m_state = successState();
				hasNext = m_delegate.hasNext();
			} catch (Exception tutti) {
				System.out.println("Commit failed in CoCommittingIterator.");
				System.out.println(tutti);
				hasNext = true;
				m_state = failedState();
				CoTransactionService.abort();
			}
			System.gc(); // PENDING: Helpful?
			if (hasNext || m_initiallyInTransaction) {
				try {
					CoTransactionService.begin();
				} catch (CoBadTransactionStateException btse) {
					System.out.println("Inconsistent behaviour of transaction services in CoCommittingIterator.");
				}
			}
			return hasNext;
		}
		protected final State successState() {
			if (m_delegate.hasNext()) {
				m_firstTry.init();
				return m_firstTry;
			} else {
				System.out.println("Committing iterator result: " + getStat());
				return new Done();
			}
		}
		protected State failedState() {
			return this;
		}
	}

	private class FirstTry extends State {
		public void init() {
			m_buffer.clear();
		}
		public boolean hasMore() {
			if (m_delegate.hasNext()) {
				if (m_buffer.size() >= m_freq) {
					partialCommit();
				}
				return true;
			} else {
				if (m_buffer.size() > 0) {
					return partialCommit();
				} else {
					System.out.println("Unexpected state in CoCommittingIterator. (May be benign.)");
					return false;
				}
			}
		}
		public Object another() {
			// NOTE: Outer class assures hasMore() has
			// been called, meaning state is consistent.
			Object obj = m_delegate.next();
			m_buffer.add(obj);
			return obj;
		}
		protected State failedState() {
			if (m_reTry == null) {
				m_reTry = new ReTry();
			}
			m_reTry.init();
			return m_reTry;
		}
	}

	private class ReTry extends State {
		private int m_pos;
		private int m_retriesLeft;
		public void init() {
			++m_retries;
			m_pos = 0;
			m_retriesLeft = DEFAULT_RETRIES;
		}
		public boolean hasMore() {
			if (m_pos < m_buffer.size()) {
				return true;
			} else {
				return partialCommit();
			}
		}
		public Object another() {
			// NOTE: Outer class assures hasMore() has
			// been called, meaning state is consistent.
			return m_buffer.get(m_pos++);
		}
		protected State failedState() {
			if (--m_retriesLeft > 0) {
				++m_retries;
				m_pos = 0;
				return this;
			} else {
				// Log failure.
				++m_failedChunks;
				// Pretend success.
				return successState();
			}
		}
	}

	private class Done extends State {
		public boolean hasMore() {
			return false;
		}
		public Object another() {
			throw new NoSuchElementException();
		}
	}
	private class Init extends State {
		public boolean hasMore() {
			if (m_delegate.hasNext()) {
				if (!(m_initiallyInTransaction = CoTransactionService.inTransaction())) {
					try {
						CoTransactionService.begin();
					} catch (CoBadTransactionStateException btse) {
						System.out.println("Inconsistent behaviour of transaction services in CoCommittingIterator.");
					}
				}
				m_state = m_firstTry = new FirstTry();
				return true;
			} else {
				System.out.println("Unexpected initial state in CoCommittingIterator. (May be benign.)");
				m_state = new Done();
				return false;
			}
		}
		public Object another() {
			throw new NoSuchElementException("Implementation error!");
		}
	}

	// Factory methods
	
	public static Iterator create(Collection coll) {
		return create(coll, DEFAULT_COMMIT_FREQUENCY);
	}

	public static Iterator create(Collection coll, int commitFrequency) {
		return create(coll.iterator(), commitFrequency);
	}

	public static Iterator create(Iterator iter) {
		return create(iter, DEFAULT_COMMIT_FREQUENCY);
	}

	public static Collection createCollection(Collection coll) {
		return createCollection(coll, DEFAULT_COMMIT_FREQUENCY);
	}

	public static Collection createCollection(final Collection coll, final int commitFrequency) {
		return new AbstractCollection() {
			public Iterator iterator() {
				return create(coll, commitFrequency);
			}
			public int size() {
				return coll.size();
			}
		};
	}

	public static Iterator create(Iterator iter, int commitFrequency) {
		if (iter instanceof CoCommittingIterator) {
			System.out.println("Attempt to wrap a CoCommittingIterator. Returning input!");
			return iter;
		} else {
			if (commitFrequency <= 0) {
				System.out.println("Commit frequency less than 1! Defaulting to " + DEFAULT_COMMIT_FREQUENCY + ".");
				commitFrequency = DEFAULT_COMMIT_FREQUENCY;
			}
			return new CoCommittingIterator(iter, commitFrequency);
		}
	}

	/**
	 * Although the name may be confusing, this method returns
	 * a committing iterator wrapping a fake iterator returning
	 * a single null element.
	 *
	 * Slightly hacky, this is a simple means to add commit
	 * retries to a piece of code. Just remember to call next().
	 *
	 * @author Markus Persson 2000-10-11
	 */
	public static Iterator createSingleShot() {
		return create(new Iterator() {
			private boolean m_done = false;
			public boolean hasNext() {
				return !m_done;
			}
			public Object next() {
				if (m_done)
					throw new NoSuchElementException();
				m_done = true;
				return null;
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}, 1);
	}

	protected CoCommittingIterator(Iterator delegate, int freq) {
		m_delegate = delegate;
		m_freq = freq;
		m_buffer = new ArrayList(freq);
		m_state = delegate.hasNext() ? (State) new Init() : new Done();
	}

	public int getFailedObjectCount() {
		// NOTE: Approximation.
		return m_failedChunks * m_freq;
	}
	public String getStat() {
		return m_retries + " retries and " + m_failedChunks + " failed chunks.";
	}
	public final boolean hasAnyFailed() {
		return m_failedChunks != 0;
	}
	public final boolean hasNext() {
		return m_state.hasMore();
	}
	public final Object next() {
		// Just to make sure state is properly updated.
		hasNext();
		return m_state.another();
	}
	public final void remove() {
		throw new UnsupportedOperationException("Modifying operations currently not supported since they may interfere with transactions.");
	}

}