package com.bluebrim.layout.shared;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import com.bluebrim.base.shared.*;

/**
 * Interface for a serializable and immutable snap grid
 * 
 * @author: Dennis Malmström
 */

public interface CoImmutableSnapGridIF extends CoObjectIF, Cloneable, com.bluebrim.xml.shared.CoXmlEnabledIF, java.io.Serializable
{
CoImmutableSnapGridIF deepClone();


Collection getGridLines();


Shape getShape( int detailMask );


Point2D snap( double x, double y, double width, double height, double range, Point2D delta );


Point2D snap( double x, double y, double range, int edgeMask, int dirMask, boolean useEdges, Point2D delta );
}