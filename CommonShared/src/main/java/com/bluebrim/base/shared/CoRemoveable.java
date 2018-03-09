package com.bluebrim.base.shared;


/**
 * Interface for classes whose instances can be removed 
 * when som conditions are satisfied. The #isRemoved method
 * is there in case the object is referenced from somewhere
 * despite the case that it should be removed.
 * 
 * @author: Ali Abida 2000-08-22.
 */
public interface CoRemoveable {
	public boolean isRemoveable();
	public boolean isRemoved();
	public void remove();
}
