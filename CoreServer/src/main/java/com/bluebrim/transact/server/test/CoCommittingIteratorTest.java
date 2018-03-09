package com.bluebrim.transact.server.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bluebrim.transact.server.CoCommittingIterator;
import com.bluebrim.transact.shared.*;

/**
 * Test class for CoCommittingIterator.
 *
 * @see #main(String[])
 * @see CoCommittingIterator
 * @see CoFakeGsTransactionService
 * @author Markus Persson 2000-10-10
 */
public class CoCommittingIteratorTest {
	// Test parameters. See also main().
	private final static int INVERSE_FAILURE_PROBABILTY = 3;
	private final static int NUM_ELEMENTS = 1000;
	private final static int COMMIT_FREQUENCY = 50;
	// Works both in and out of transactions.
	private final static boolean START_INSIDE_TRANSACTION = true;

	public CoCommittingIteratorTest() {
	}

	/**
	 * Test!
	 * 
	 * @param args java.lang.String[]
	 * @author Markus Persson 2000-10-10
	 */
	public static void main(String[] args) {
		List elems = new ArrayList(NUM_ELEMENTS);
		for (int i = 0; i < NUM_ELEMENTS; i++) {
			elems.add(new Integer(i));
		}

		Iterator iter = CoCommittingIterator.create(elems, COMMIT_FREQUENCY);

		// Works both in and out of transactions.
		if (START_INSIDE_TRANSACTION) {
			try {
				CoTransactionService.begin();
			} catch (CoBadTransactionStateException btse) {
				System.out.println("Initial begin failed!");
			}
		}

		System.out.println("Transaction state: " + CoTransactionService.inTransaction());
		while (iter.hasNext()) {
			iter.next();
			System.out.print("#");
		}
		System.out.println("Transaction state: " + CoTransactionService.inTransaction());
		System.out.println("Iter stat: " + ((CoCommittingIterator) iter).getStat());

		// Works both in and out of transactions.
		if (START_INSIDE_TRANSACTION) {
			try {
				CoTransactionService.commit();
			} catch (Exception e) {
				System.out.println("Last commit failed!");
			}
		}
	}
}