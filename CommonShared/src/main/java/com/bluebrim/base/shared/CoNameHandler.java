package com.bluebrim.base.shared;

/**
 * <p>Interface for objects that can deliver a name and possibly a key.</p>
 * <p>The name is not required to be unique in any sense, nor constant over time,
 * and shall thus <b>never</b> be used as a means to uniquely identify an object.
 * The key should be fairly unique in some sense, but generally not used to
 * uniquely identify an object.</p>
 *
 * @see CoNamed
 * @see CoRenameable
 * @author Markus Persson 1999-05-10
 */
public interface CoNameHandler extends CoRenameable {
	/**
	 * <p>Key that could be used to find the holder object in a collection.</p>
	 *
	 * @see CoRenameable#setName(String)
	 * @author Markus Persson 1999-05-10
	 */
	public String getKey();
}