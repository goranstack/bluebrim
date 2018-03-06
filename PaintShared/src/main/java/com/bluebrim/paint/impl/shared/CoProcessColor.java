package com.bluebrim.paint.impl.shared;

/**
 * Abstrakt superklass till de fyra processfärgerna
 * 
 */
public abstract class CoProcessColor extends com.bluebrim.paint.impl.shared.CoTrappableColor implements com.bluebrim.paint.impl.shared.CoProcessColorIF {
public boolean canBeDeleted( ) 
{
	return false;
}
public boolean canBeEdited( ) 
{
	return false;
}
public String getType ( )
{
	return PROCESS_COLOR;
}
}
