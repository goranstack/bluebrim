package com.bluebrim.paint.impl.shared;
import java.awt.*;

/**
 * Representerar processfärgen magenta
 * 
 */
public class CoProcessMagenta extends com.bluebrim.paint.impl.shared.CoProcessColor implements com.bluebrim.paint.impl.shared.CoProcessMagentaIF
{
	public final static Color PREVIEW_COLOR = new Color(0.937f,0.16f,0.496f); // Värden från QuarkXPress
/**
 * This method was created by a SmartGuide.
 */
public CoProcessMagenta ( ) {
}
public boolean equals( Object o )
{
	return ( o instanceof CoProcessMagenta );
}
public float getBlackPercentage(){
	
	return 0;
}
public float getCyanPercentage(){
	
	return 0;
}
public String getFactoryKey (){
	return PROCESS_MAGENTA;
}
public float getMagentaPercentage() {
	
	return 100;
}
public String getName ( )
{
	return com.bluebrim.paint.impl.shared.CoColorResources.getName(PROCESS_MAGENTA);
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
