package com.bluebrim.layout.impl.client.transfer;


public abstract class CoLayoutOnSomethingOperation extends CoAbstractDropOperation
{
	protected static final int DEFAULT_X_POSITION			= 10;
	protected static final int DEFAULT_Y_POSITION			= 10;



public String getDescription( String nameOfTarget )
{
	return "Släpp layout på " + nameOfTarget;
}
}