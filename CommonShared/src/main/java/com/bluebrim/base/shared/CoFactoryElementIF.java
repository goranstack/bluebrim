package com.bluebrim.base.shared;

/**
 	Utvidgning av CoObjectIF för de verksamhetsobjekt som instansieras via en factoryklass. 
 	Definierar metoden #getFactoryKey som skall svara med en sträng som är den 
 	nyckel som används av factoryklassen.
 */
public interface CoFactoryElementIF {
	public String getFactoryKey();
}
