package com.bluebrim.gui.client;

import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

/**
 * Interface f�r ett v�rdeobjekt, dvs ett objekt som har ett v�rde
 * och har abstrakta metoder f�r att s�tta och h�mta detta v�rde.
 * @author Lasse Svad�ngs 970916
 */
public interface CoValueable {
	void addEnableDisableListener(CoEnableDisableListener l);

	/**
	 *	Registrera en lyssnare efter �ndringar av v�rdet.
	 */
	public void addValueListener(CoValueListener l);

	public void addVetoableChangeListener(VetoableChangeListener l);

	public Object getValue();

	/**
	 * Svarar med namnet p� v�rdeobjektet.
	 */
	public String getValueName();

	/**
	 * Returns a key that (depending on context) may be used to
	 * retrieve this valueable from some sort of map.
	 * This method serves the same purposes as getValueName()
	 * did and could replace it entirely if not for property
	 * change events that stupidly requires a String ...
	 * 
	 * @author Markus Persson 2002-08-27
	 */
	public Object getKey();

	public void initValue(Object value);

	boolean isEnabled();

	void removeEnableDisableListener(CoEnableDisableListener l);

	public void removeValueListener(CoValueListener l);

	public void removeVetoableChangeListener(VetoableChangeListener l);

	void setEnabled(boolean e);

	public void setValue(Object value);

	public Object validate(Object newValue) throws PropertyVetoException;

	public void valueHasChanged();
}
