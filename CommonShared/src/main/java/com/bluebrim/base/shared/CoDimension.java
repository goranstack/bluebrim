package com.bluebrim.base.shared;

import java.awt.geom.*;
import java.io.*;

/**
  Representerar en storleksangivelse där bredd och höjd är angivna som float.
 */
public class CoDimension extends CoSimpleObject implements Cloneable, Serializable{
	float width = 0.0f;
	float height = 0.0f;
/**
 * This method was created by a SmartGuide.
 */
public CoDimension ( ) {
}
/**
 */
public CoDimension (float width, float height) {
	setWidth(width);
	setHeight(height);
}
public Object clone() throws CloneNotSupportedException
{
	return super.clone();
}
public boolean equals(Object o)
{
	if (o == this)
		return true;
	if (!(o instanceof CoDimension))
		return false;
	CoDimension dim = (CoDimension )o;
	return (dim.getWidth()-width < 0.001 && dim.getHeight()-height <0.001);
}
/**
 * getType method comment.
 */
public float getHeight() {
	 return height;
}
/**
 * getType method comment.
 */
public float getWidth() {
	 return width;
}
/**
 */
public void setHeight(float height) {
	this.height = height;
}
/**
 */
public void setWidth(float width) {
	this.width = width;
}

public Dimension2D getDimension2D()
{
	return new CoDimension2D( width, height );
}
}