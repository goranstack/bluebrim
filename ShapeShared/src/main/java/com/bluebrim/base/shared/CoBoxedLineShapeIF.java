package com.bluebrim.base.shared;

public interface CoBoxedLineShapeIF extends CoBoundingShapeIF, CoImmutableBoxedLineIF
{
	public final static String BOXED_LINE		= "boxed_line";
void setHorizontal();
void setHorizontal( boolean b );
void setMargin( double m );
void setVertical();
void setVertical( boolean b );
}
