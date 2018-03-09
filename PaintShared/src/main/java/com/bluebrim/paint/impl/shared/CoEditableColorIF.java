package com.bluebrim.paint.impl.shared;
import java.awt.*;

import com.bluebrim.paint.shared.*;

/**
 *
 */
public interface CoEditableColorIF extends CoColorIF
{
public void copyFrom( CoEditableColorIF source );
public CoEditableColorIF createObject();
public void setColor( Color color );
}