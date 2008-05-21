package com.bluebrim.paint.impl.shared;
import java.io.*;
import java.util.*;

/**
 * Instanser av CoTrappingSpec beskriver sv�llning f�r en viss f�rg.
 * I QXP beskrivs sv�llning genom att man f�r varje annan f�rdefinierad
 * sv�llningsbar f�rg anger tre uppgifter.
 * 
 * 
 */
public class CoTrappingSpec implements Serializable, Cloneable{

	protected Hashtable trappingTable;
public Object clone() throws CloneNotSupportedException
{
	CoTrappingSpec tSpec 	= (CoTrappingSpec)super.clone();
	tSpec.trappingTable		= (trappingTable != null) ? (Hashtable )trappingTable.clone() : null;
	return tSpec;
}
}
