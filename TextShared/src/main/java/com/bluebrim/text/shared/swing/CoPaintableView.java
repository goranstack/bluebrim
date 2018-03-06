package com.bluebrim.text.shared.swing;

import java.awt.*;

import com.bluebrim.base.shared.*;

/**
 * Interface for text views that can be painted
 * 
 * Changed (from package-private) to public. /Markus 2002-02-05
 * 
 * @author: Dennis Malmström
 */
public interface CoPaintableView
{

public void paint( CoPaintable g, Shape allocation );
}