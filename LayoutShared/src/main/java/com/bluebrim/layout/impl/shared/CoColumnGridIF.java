package com.bluebrim.layout.impl.shared;

import com.bluebrim.layout.shared.*;



/**
 * Interface for a serializable and mutable column grid
 * 
 * @author: Dennis Malmström
 */

public interface CoColumnGridIF extends CoSnapGridIF, CoImmutableColumnGridIF
{
void setBottomMargin( double bottom );
void setColumnCount( int c );
void setColumnSpacing( double s );
void setLeftMargin( double left );
void setMargins( double left, double top, double right, double bottom );
void setRightMargin( double right );
void setTopMargin( double top );

void set( int columnCount, double spacing, double left, double top, double right, double bottom );

public void setLeftOutsideSensitive( boolean s );

public void setSpread( boolean s );
}