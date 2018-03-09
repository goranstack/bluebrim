package com.bluebrim.paint.shared;
import java.rmi.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.paint.impl.shared.*;
import com.bluebrim.system.shared.*;


public interface CoColorCollectionIF extends CoObjectIF, Remote
{
	public static final String BLUE = "CoColorCollectionIF.BLUE";
	public static final String GREEN = "CoColorCollectionIF.GREEN";
	public static final String RED = "CoColorCollectionIF.RED";

	public CoMultiInkColorIF createMultiInkColor();

	public CoSpotColorIF createSpotColor();

	void addColor( CoColorIF color );
	
	public void addRGBColors();
	
	public void addCMYKColors();
	
	public CoColorIF getColor( String name );
	
	public CoColorIF getColor( CoGOI name );
	
	public List getColors();
	
	public int getImmutableColorCount();
	
	public void removeColor( Object[] color );
}