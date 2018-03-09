package com.bluebrim.text.shared.swing;

import java.awt.*;

/**
 * Interface connecting com.bluebrim.text.client.swing.CoSectionView with whichever object supplies it with text layout configuration data
 * 
 * @author: Dennis Malmström
 */

public interface CoViewGeometryProviderIF
{
boolean doesHaveFocus();
com.bluebrim.text.shared.CoBaseLineGeometryIF getBaseLineGeometry();
Color getColumnBorderColor();
com.bluebrim.text.shared.CoColumnGeometryIF getColumnGeometry();
String getDummyText();
com.bluebrim.text.shared.CoTextGeometryIF getTextGeometry();
boolean isColumnGeometryDirty();
}
