package com.bluebrim.base.shared;


//

public interface CoTransformIF extends CoImmutableTransformIF, com.bluebrim.xml.shared.CoXmlExportEnabledIF
{
	public static String TRANSFORM = "transform";
public void setRotation(double rotation);
void setRotationPoint( double x, double y);
}