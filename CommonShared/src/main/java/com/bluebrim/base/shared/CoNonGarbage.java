package com.bluebrim.base.shared;

import java.util.*;

/**
 * Small utility class that prevents garbage collection
 * of classes that register with it. This is useful because
 * it allows class garbage collection to be activated in the
 * VM, but still protects classes that hold singletons and
 * similar objects that rely on object identity.
 * 
 * This is accomplished by starting a minimum priority daemon
 * thread that sleeps all the time, but by holding a reference
 * to this class prevents it from being garbage collected.
 * Finally, this class has a static collection to which classes
 * may add themselves.
 * 
 * NOTE: Adding a class to the non-garbage set does not prevent
 * garbage collection of instances of the class.
 * 
 * @author Markus Persson 2002-08-13
 */
public final class CoNonGarbage implements Runnable {
	private static final Set CLASSES = new HashSet();
	private static final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;

	static {
		Thread thread = new Thread(new CoNonGarbage(), "Undesired Class Garbage Collection preventing thread");
		thread.setPriority(Thread.MIN_PRIORITY);
 		// So that it won't prevent the VM from exiting.
 		thread.setDaemon(true);
 		thread.start();
	}

	/**
	 * Do not allow anyone else to instantiate us.
	 */	
	private CoNonGarbage() {
	}

	/**
	 * Add the given class to the set of classes protected from
	 * garbage collection.
	 */
	public static final synchronized void add(Class classObj) {
		CLASSES.add(classObj);
	}

	public void run() {
		// Just sleep until the VM exits ...
		while (true) {
			try {
				Thread.sleep(MILLIS_PER_DAY);
			} catch (InterruptedException ie) {
				Thread.interrupted();
			}
		}
	}

}
