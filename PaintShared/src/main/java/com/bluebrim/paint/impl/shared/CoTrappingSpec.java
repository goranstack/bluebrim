package com.bluebrim.paint.impl.shared;
import java.io.*;
import java.util.*;

/**
 * Instanser av CoTrappingSpec beskriver svällning för en viss färg.
 * I QXP beskrivs svällning genom att man för varje annan fördefinierad
 * svällningsbar färg anger tre uppgifter.
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
