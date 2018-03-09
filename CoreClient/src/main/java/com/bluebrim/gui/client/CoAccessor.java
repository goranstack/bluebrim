package com.bluebrim.gui.client;

import com.bluebrim.base.shared.CoObjectIF;

/**
 * Abstrakt superklass för klasser som används för att hämta
 * och sätta värden för en instansvariabel hos ett verksamhetsobject.
 */
public abstract class CoAccessor {
	public abstract Object get(CoObjectIF subject);
	public void set(CoObjectIF subject, Object value) {
	}
}