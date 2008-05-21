package com.bluebrim.base.shared;

import java.awt.*;
import java.io.*;

import com.bluebrim.base.shared.debug.*;

/**
 * Abstract root class for views, objects that are serializable
 * snapshot representations of pageitems or viewables.
 */
 
public abstract class CoView implements Serializable
{
	public abstract double getHeight();

	public abstract double getWidth();

	public final void paint( Graphics2D g )
	{
		CoAssertion.assertTrue( false, "Illegal call" );
	}

	public String toString()
	{
		String tmp = super.toString();
		tmp = tmp.substring( tmp.lastIndexOf( '.' ) + 1 );
		return tmp;
	}
	
	public abstract void paint( CoPaintable g );
}