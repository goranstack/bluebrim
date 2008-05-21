package com.bluebrim.system.shared;

import java.rmi.*;

import com.bluebrim.base.shared.*;

public interface CoDistinguishable extends CoObjectIF, Remote {
	public long getCOI();
	public CoGOI getGOI();
}
