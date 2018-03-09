package com.bluebrim.text.impl.shared;

import java.rmi.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Interface defining the protocol of a collection of paragraph tag chains.
 * 
 * @author Dennis Malmström
 */
public interface CoTagChainCollectionIF extends CoObjectIF, Remote {
	public void addChain(CoTagChainIF chain);

	public CoTagChainIF createChain(String name);

	public List getChains(); // [ CoTagChainIF ]

	public void removeChain(CoTagChainIF chain);

	public int getImmutableChainCount();
}