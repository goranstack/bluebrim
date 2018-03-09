package com.bluebrim.gui.client;

import com.bluebrim.base.shared.CoObjectIF;

/**
 * Abstrakt superklass f�r klasser som anv�nds f�r att h�mta
 * och s�tta v�rden f�r en instansvariabel hos ett verksamhetsobject.
 */
public abstract class CoAccessor {
	public abstract Object get(CoObjectIF subject);
	public void set(CoObjectIF subject, Object value) {
	}
}