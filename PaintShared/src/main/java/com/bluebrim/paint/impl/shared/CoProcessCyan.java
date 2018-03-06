package com.bluebrim.paint.impl.shared;
import java.awt.*;

/**
 * Representerar processfärgen cyan
 * 
 */
public class CoProcessCyan extends com.bluebrim.paint.impl.shared.CoProcessColor implements com.bluebrim.paint.impl.shared.CoProcessCyanIF
{
	public final static Color PREVIEW_COLOR = new Color(0.07f,0.626f,0.776f); // Värden från QuarkXPress
/**
 * This method was created by a SmartGuide.
 */
public CoProcessCyan ( ) {
}
public boolean equals( Object o )
{
	return ( o instanceof CoProcessCyan );
}
public float getBlackPercentage(){
	
	return 0;
}
public float getCyanPercentage(){
	
	return 100;
}
public String getFactoryKey (){
	return PROCESS_CYAN;
}
public float getMagentaPercentage() {
	
	return 0;
}
public String getName ( )
{
	return com.bluebrim.paint.impl.shared.CoColorResources.getName(PROCESS_CYAN);
}
/*
 * Returnerar en färg som kan användas för att visa upp mottagaren på
 * bildskärmen. RGB-värdena för Cyan är hämtade från QuarkQPress.
 */
public  Color getPreviewColor ( )
{
	return PREVIEW_COLOR;
}
public float getYellowPercentage(){
	
	return 0;
}
}
