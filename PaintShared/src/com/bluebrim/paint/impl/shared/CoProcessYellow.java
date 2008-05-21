package com.bluebrim.paint.impl.shared;
import java.awt.*;

/**
 * Representerar processfärgen gul
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
 * Returnerar en färg som kan användas för att visa upp mottagaren på
 * bildskärmen. 
 */
public  Color getPreviewColor ( )
{
	return PREVIEW_COLOR;
}
public float getYellowPercentage(){
	
	return 100;
}
}
