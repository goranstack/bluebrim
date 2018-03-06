package com.bluebrim.text.shared;

/**
 * Interface defining the protocol of base line grid geometry
 * 
 * @author: Dennis Malmström
 */

public interface CoBaseLineGeometryIF extends java.io.Serializable
{
// distance between base lines

float getDeltaY();
// position of first base line

float getY0();
boolean isEquivalentTo( CoBaseLineGeometryIF g );
}
