package com.bluebrim.base.shared;

/**
 * This interface implements by objects that represents an unit that can be
 * associated with a quantity. For exemple currency and percent, . 
 * 
 * @author Göran Stäck
 */
public interface CoUnit {
	public String getName();
	public String getKey();	
}
