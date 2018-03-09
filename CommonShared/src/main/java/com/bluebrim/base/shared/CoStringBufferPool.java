package com.bluebrim.base.shared;

/**
 * Pool handling StringBuffers.
 * Note that this has synchronized access methods
 * to make it able to run in a multi 
 * threaded environment.
 * Creation date: (2001-06-06 09:01:24)
 * @author: Lasse S
 */
public class CoStringBufferPool extends CoObjectPool {
	private static CoStringBufferPool POOL = new CoStringBufferPool(10);

	public static void main(String args[]) {
		int size = 100000;
		if (args != null) {
			try {
				size = Integer.valueOf(args[0]).intValue();
			} catch (NumberFormatException e) {
			}
		}

		System.out.println("\n======= Iterations " + size + " =======");
		long start = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Hejsan");
			buffer.append(" hoppsan");
			buffer.toString();
		}
		System.out.println("\nAllocating StringBuffers      " + (System.currentTimeMillis() - start));

		long start2 = System.currentTimeMillis();
		StringBuffer buffer2 = new StringBuffer();
		for (int i = 0; i < size; i++) {
			buffer2.setLength(0);
			buffer2.append("Hejsan ");
			buffer2.append(" hoppsan");
			buffer2.toString();
		}
		System.out.println("Reusing StringBuffer          " + (System.currentTimeMillis() - start2));

		long start3 = System.currentTimeMillis();
		CoStringBufferPool pool = new CoStringBufferPool(5);
		for (int i = 0; i < size; i++) {
			StringBuffer buffer3 = (StringBuffer) pool.getObject();
			buffer3.append("Hejsan ");
			buffer3.append(" hoppsan");
			buffer3.toString();
			pool.releaseObject(buffer3);
		}
		System.out.println("StringBufferPool              " + (System.currentTimeMillis() - start3));

		long start4 = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			StringBuffer buffer4 = CoStringBufferPool.getStringBuffer();
			buffer4.append("Hejsan ");
			buffer4.append(" hoppsan");
			buffer4.toString();
			CoStringBufferPool.releaseStringBuffer(buffer4);
		}
		System.out.println("Synchronized StringBufferPool " + (System.currentTimeMillis() - start4));
		System.out.println("\n================================");
	}

	/**
	 * CoStringBufferPool constructor comment.
	 * @param initialSize int
	 */
	public CoStringBufferPool(int initialSize) {
		super(initialSize);
	}

	protected Object createObject() {
		return new StringBuffer();
	}

	public static StringBuffer getStringBuffer() {
		synchronized (POOL) {
			return (StringBuffer) POOL.getObject();
		}
	}

	protected Object prepareObject(Object obj) {
		((StringBuffer) obj).setLength(0);
		return obj;
	}

	public static void releaseStringBuffer(StringBuffer buffer) {
		synchronized (POOL) {
			POOL.releaseObject(buffer);
		}
	}
}