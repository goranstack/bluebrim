package com.bluebrim.base.shared.geom;

import java.awt.geom.*;
/**
 * Calculates the length of a BezierPath use method arclen.
 * Creation date: (2000-09-29 14:08:03)
 * @author Arvid Berg 
 */
public class CoBezierLength 
{
	
	public final static double TOLERANCE = 0.0000001;
/**
 * BezierLength constructor comment.
 */
public CoBezierLength() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (2000-10-02 11:00:10)
 * @param V java.awt.geom.Point2D[]
 * @param length double
 * @param error double
 */
public static double addifclose(Point2D[] V, double length, double error) {
	Point2D left[]=new Point2D[4], right[]=new Point2D[4]; // bez poly splits   	
	double len = 0.0; // arc length 
	double chord; // chord length   
	int index; // misc counter   
	for (index = 0; index <= 2; index++)
		len = len + V[index].distance(V[index + 1]);
	chord = V[0].distance(V[3]);
	if ((len - chord) > error) {
		bezsplit(V, left, right); // split in two 
		length=addifclose(left, length, error); // try left side 
		length=addifclose(right, length, error); // try right side     
		return length;
	}
	length += len;
	return length;
}
/**
 * Insert the method's description here.
 * Creation date: (2000-10-02 11:09:12)
 * @return double
 * @param V java.awt.geom.Point2D[]
 * @param error double
 */
public static double arclen(Point2D[] V, double error) {
	double length=0; 	// length of curve 
	length=addifclose(V, length, error);	// kick off recursion 
	return (length);	// that's it! 
	
}
/**
 * Insert the method's description here.
 * Creation date: (2000-10-02 10:51:00)
 * @param V java.awt.geom.Point2D[]
 * @param Left java.awt.geom.Point2D[]
 * @param Right java.awt.geom.Point2D[]
 */
public static void bezsplit(Point2D[] V, Point2D[] Left, Point2D[] Right) 
{
	
	
	int   i, j;				// Index variables  
	Point2D[][]   Vtemp=new Point2D[4][4];    // Triangle Matrix
	for(i=0;i<4;i++)
		for(j=0;j<4;j++)
			Vtemp[i][j]=new Point2D.Double();
	// Copy control points    
	for (j =0; j <= 3; j++) 
	  Vtemp[0][j] = new Point2D.Double(V[j].getX(),V[j].getY());   // Triangle computation 
	for (i = 1; i <= 3; i++) 
	{
		for (j =0 ; j <= 3 - i; j++) 
		{
			Vtemp[i][j].setLocation(
				0.5 * Vtemp[i-1][j].getX() + 0.5 * Vtemp[i-1][j+1].getX(),
				0.5 * Vtemp[i-1][j].getY() + 0.5 * Vtemp[i-1][j+1].getY());          
   			//Vtemp[i][j].y = 0.5 * Vtemp[i-1][j].y + 0.5 * Vtemp[i-1][j+1].y;          
		}                                       // end for i 
	}                                       // end for j     
	for (j = 0; j <= 3; j++)         
		Left[j]  = Vtemp[j][0];      
	for (j = 0; j <= 3; j++)         
		Right[j] = Vtemp[3-j][j];
	

	
}                                           // end splitbez 
}
