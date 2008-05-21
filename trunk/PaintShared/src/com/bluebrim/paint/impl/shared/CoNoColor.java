package com.bluebrim.paint.impl.shared;
import java.awt.*;

/**
 * Representerar ingen färg.
 * 
 */
public class CoNoColor extends com.bluebrim.paint.impl.shared.CoColor implements com.bluebrim.paint.impl.shared.CoNoColorIF
{
/**
 * This method was created by a SmartGuide.
 */
public CoNoColor ( ) {
}
public boolean canBeDeleted( ) 
{
	return false;
}
public boolean canBeEdited( ) 
{
	return false;
}
public boolean equals( Object o )
{
	return o instanceof CoNoColor;
}
public String getFactoryKey (){
	return NO_COLOR;
}
public String getName ( )
{
	return com.bluebrim.paint.impl.shared.CoColorResources.getName(NO_COLOR);
}
/*
 * Returnerar en färg som kan användas för att visa upp mottagaren på
 * bildskärmen. 
 */
public Color getPreviewColor ( )
{
	return null;
}
/*
 * Det går inte att applicera ett tonvärde på mottagaren men om
 * någon ändå gör det så händer ingenting
 */
public Color getShadedPreviewColor (float shade )
{
	return getPreviewColor();
}
public String getType ( )
{
	return getName();
}

public float getBlackPercentage()
{
	return 0;
}

public float getCyanPercentage()
{
	return 0;
}

public float getMagentaPercentage()
{
	return 0;
}

public float getYellowPercentage()
{
	return 0;
}
}