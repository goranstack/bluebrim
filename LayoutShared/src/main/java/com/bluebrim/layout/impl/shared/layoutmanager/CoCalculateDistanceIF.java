package com.bluebrim.layout.impl.shared.layoutmanager;
import java.awt.geom.*;
 
public interface CoCalculateDistanceIF
{
/**
 * Calculates the distance between p1,p2.
 * Creation date: (2000-07-13 09:41:57)
 * @return double
 * @param p1 java.awt.geom.Point2D
 * @param p2 java.awt.geom.Point2D
 */
double calculateDistance(Point2D p1, Point2D p2);
/**
 * Returns the name of calculateDistance method.
 * Creation date: (2000-07-13 15:06:00)
 * @return java.lang.String
 */
String getName();
}
