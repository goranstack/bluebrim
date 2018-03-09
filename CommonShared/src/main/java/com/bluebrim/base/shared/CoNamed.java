package com.bluebrim.base.shared;

/**
 * <p>Interface for objects that can deliver a name suitable to be displayed to the user.</p>
 * <p>The name is not required to be unique in any sense, nor constant over time,
 * and shall thus <b>never</b> be used as a means to uniquely identify an object.</p>
 *
 * <p>The name delivered is required to be localized to the current locale,
 * unless the object is user created and the name is modifiable.</p>
 *
 * <p><em>Note:</em> There used to be a similar interface named <code>CoNameIF</code>
 * but without specifications of what the name really was intented for, causing
 * inconsistency. It has now (more then a year later) finally been removed.</p>
 *
 * @see CoRenameable
 * @author Markus Persson 1999-05-10
 */
public interface CoNamed {
/**
 * <p>User displayable name of this object.</p>
 *
 * <p>The name is required to be localized to the current locale,
 * unless the object is user created. If the name isn't localized,
 * it should be modifiable.</p>
 *
 * <p>Currently, null is not disallowed as a return value, but it
 * should perhaps be. See CoPlainName.</p>
 *
 * @see CoPlainName#getName()
 * @see CoRenameable#setName(String)
 * @author Markus Persson 1999-05-10
 */
public String getName();
}
