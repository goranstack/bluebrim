package com.bluebrim.base.shared;

/**
 * Interface for classes responsible for dispatching changes in 
 * the state of a domain object, e g classes implementing 
 * <code>addChangedObjectStateListener(CoChangedObjectStateListener)</code> 
 * and <code>removeChangedObjectStateListener(CoChangedObjectStateListener)</code>.
 * <br>
 * Creation date: (2000-12-15 13:31:48)
 * @author: Lasse S
 */
public interface CoChangedObjectStateDispatcher {
public void addChangedObjectStateListener(CoChangedObjectStateListener l);
public void removeChangedObjectStateListener(CoChangedObjectStateListener l);
}
