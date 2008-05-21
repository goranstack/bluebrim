package com.bluebrim.layout.impl.shared;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;

import com.bluebrim.base.shared.*;

/**
 * Interface for a serializable and immutable fill styles
 * 
 * @author: Dennis Malmström
 */

public interface CoImmutableFillStyleIF extends CoFactoryElementIF, Serializable
{
public CoFillStyleIF deepClone();


public Paint getPaint( Rectangle2D bounds );
}