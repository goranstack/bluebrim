package com.bluebrim.base.shared;

/**
 * <p>Interface for objects whoose user displayable name possibly can be set.</p>
 * <p>The name is not required to be unique in any sense, nor constant over time,
 * and shall thus <b>never</b> be used as a means to uniquely identify an object.</p>
 *
 * @see CoNamed
 * @author Markus Persson 1999-05-10
 */
public interface CoRenameable extends CoNamed {
/**
 * <p>Whether the user displayable name of this object can be set or not.</p>
 *
 * <p>Intended for classes for which this may change, for instance through
 * use of the strategy pattern.</p>
 *
 * @return boolean true iff the name can be set.
 * @author Markus Persson 1999-05-10
 */
public boolean isRenameable();
/**
 * <p>Set the user displayable name of this object. The name
 * is associated with the current locale, if such awareness
 * is supported.</p>
 *
 * <p>The method is allowed to do nothing, if isRenameable()
 * returns false. Under no circumstances, however, should an
 * exception be thrown.</p>
 *
 * @see CoNamed#getName
 * @author Markus Persson 1999-05-10
 */
public void setName(String name);
}
