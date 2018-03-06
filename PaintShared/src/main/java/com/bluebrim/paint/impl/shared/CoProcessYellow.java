package com.bluebrim.paint.impl.shared;
import java.awt.*;

/**
 * Representerar processf�rgen gul
 * 
 */
public class CoProcessYellow extends com.bluebrim.paint.impl.shared.CoProcessColor implements com.bluebrim.paint.impl.shared.CoProcessYellowIF   
{
	public final static Color PREVIEW_COLOR = Color.yellow;
/**
 * This method was created by a SmartGuide.
 */
public CoProcessYellow ( ) {
}
public boolean equals( Object o )
{
	return ( o instanceof CoProcessYellow );
}
public float getBlackPercentage(){
	
	return 0;
}
public float getCyanPercentage(){
	
	return 0;
}
public String getFactoryKey (){
	return PROCESS_YELLOW;
}
public float getMagentaPercentage() {
	
	return 0;
}
public String getName ( )
{
	return com.bluebrim.paint.impl.shared.CoColorResources.getName(PROCESS_YELLOW);
}
/*
 * Returnerar en f�rg som kan anv�ndas f�r att visa upp mottagaren p�
 * bildsk�rmen. 
 */
public  Color getPreviewColor ( )
{
	return PREVIEW_COLOR;
}
public float getYellowPercentage(){
	
	return 100;
}
}
