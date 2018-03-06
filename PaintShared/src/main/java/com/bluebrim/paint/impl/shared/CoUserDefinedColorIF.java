package com.bluebrim.paint.impl.shared;
import java.awt.*;
public interface CoUserDefinedColorIF extends com.bluebrim.paint.impl.shared.CoTrappableColorIF, com.bluebrim.paint.impl.shared.CoEditableColorIF 
{
	public static final String USER_DEFINED_COLOR = "user_defined";	
/**
 * This method was created in VisualAge.
 * @param c Color
 */
public abstract void setColor(Color c);
/**
 * This method was created in VisualAge.
 * @param name java.lang.String
 */
public void setName(String newName);
}
