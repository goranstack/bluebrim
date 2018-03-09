package com.bluebrim.stroke.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.paint.shared.*;

/**
 * RMI-enabling interface for class com.bluebrim.stroke.impl.shared.CoDashColor.
 * 
 * @author: Dennis Malmström
 */

public interface CoDashColorIF extends CoFactoryElementIF, java.rmi.Remote, Cloneable, com.bluebrim.xml.shared.CoXmlExportEnabledIF
{
public CoDashColorIF deepClone();

float getDashShade( CoImmutableStrokePropertiesIF props );

CoColorIF getDashColor( CoImmutableStrokePropertiesIF props );
}