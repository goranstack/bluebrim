package com.bluebrim.stroke.shared;
import java.awt.*;

import com.bluebrim.base.shared.*;

/**
 * RMI-enabling interface for class com.bluebrim.stroke.impl.shared.CoDash.
 * 
 * @author: Dennis Malmström
 */

public interface CoDashIF extends CoObjectIF, java.rmi.Remote, Cloneable, com.bluebrim.xml.shared.CoXmlEnabledIF
{
	final static int JOIN_MITER = BasicStroke.JOIN_MITER;
	final static int JOIN_ROUND = BasicStroke.JOIN_ROUND;
	final static int JOIN_BEVEL = BasicStroke.JOIN_BEVEL;
	
	final static int CAP_BUTT   = BasicStroke.CAP_BUTT;
	final static int CAP_ROUND  = BasicStroke.CAP_ROUND;
	final static int CAP_SQUARE = BasicStroke.CAP_SQUARE;
float calculateCycleLength( float width );
public Stroke createStroke( float width, float phase );
public Stroke createUndashedStroke( float width );
public CoDashIF deepClone();
public int getCap();
public float getCycleLength();
public float[] getDash();
public int getJoin();
public float getMiterLimit();
public boolean isCycleLengthInWidth();
public void setCap(int cap);
public void setCycleLength(float cycleLength);
public void setCycleLengthInWidth( boolean cycleLengthInWidth );
public void setDash(float[] dash);
public void setJoin(int join);
public void setMiterLimit( float miterLimit );
}