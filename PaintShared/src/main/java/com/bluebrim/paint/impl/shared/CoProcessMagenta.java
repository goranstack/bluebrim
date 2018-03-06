package com.bluebrim.paint.impl.shared;
import java.awt.*;

/**
 * Representerar processf�rgen magenta
 * 
 */
public class CoProcessMagenta extends com.bluebrim.paint.impl.shared.CoProcessColor implements com.bluebrim.paint.impl.shared.CoProcessMagentaIF
{
	public final static Color PREVIEW_COLOR = new Color(0.937f,0.16f,0.496f); // V�rden fr�n QuarkXPress
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
 * Returnerar en f�rg som kan anv�ndas f�r att visa upp mottagaren p�
 * bildsk�rmen. RGB-v�rdena f�r Cyan �r h�mtade fr�n QuarkQPress.
 */
public  Color getPreviewColor ( )
{
	return PREVIEW_COLOR;
}
public float getYellowPercentage(){
	
	return 0;
}
}
