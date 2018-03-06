package com.bluebrim.base.shared;
import java.io.*;
import java.util.*;


/**
 * A object implementing <code>CoUIServantIF</code> acts a server based part of a ui model.
 * For now it's only used to deliver all values for the value models in one round-trip
 * but we can see other uses in the future.
 * <br>
 * Note:
 * <br>
 * All concrete servant classes must implement the subinterface <code>com.bluebrim.gemstone.shared.CoRemoteUIServantIF</code>.
 * The sole reason for <code>CoUIServantIF</code> to exist is to define all methods not using any
 * GS/J specific types, thereby making it possible to use <code>CoUIServantIF</code> in classes
 * not knowing anything about GS/J.
 * <br>
 * Creation date: (2000-02-03 08:34:16)
 * @author: Lasse Svadängs
 */
public interface CoUIServantIF {

	public static interface Context extends Serializable {
		public CoObjectIF getServerObject();
	}
public boolean isRegistered();

public void addChangedObjectStateListener(CoChangedObjectStateListener l);


public void addSubServant(Object key, CoUIServantIF servant);


public Map getValuesFor(CoObjectIF object);


public void register(CoUIServantIF.Context context);


public void register(CoUIServantIF.Context context, boolean broadcastStateChanged);


public void removeChangedObjectStateListener(CoChangedObjectStateListener l);


public void removeSubServant(Object key);


public void unregister(CoUIServantIF.Context context);


public void unregister(CoUIServantIF.Context context, boolean fireChanged);

public void register(CoObjectIF object, boolean broadcastStateChanged);


public void unregister(CoObjectIF object, boolean fireChanged);
}