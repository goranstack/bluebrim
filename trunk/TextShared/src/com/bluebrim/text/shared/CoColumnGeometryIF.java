package com.bluebrim.text.shared;
import com.bluebrim.base.shared.*;

/**
 * Interface defining the protocol of column grid geometry.
 * A column grid geometry is a collection of columns.
 *
 * @author: Dennis Malmström
 */
 
public interface CoColumnGeometryIF extends java.io.Serializable
{
	// Interface defining the protocol of a column.
	public interface CoColumnIF
	{
		void setMargins( float leftMargin, float rightMargin );

		public CoRectangle2DFloat getBounds();
		
		boolean isNarrowing( float y ); // sematics: false = column is not narrowing, true = column might be narrowing
		public boolean isRectangular();
		public boolean isEquivalentTo( CoColumnGeometryIF.CoColumnIF c );

		// contents of argument range in methods below
		static final int X = 0;
		static final int WIDTH = 1;

		// get column span at y.
		// parameter index is used to handle multiple spans. This feature is not used by the current text editor
		boolean getRange( float y, int index, float [] range );

		// get the minimal column span between y0 and y1
		// Note: upon entry, the argument range is expected to contain the results from a call to getRange at y = y0
		void getMinimalRange( float y0, float y1, float [] range );
	};
CoColumnIF getColumn( int index );
int getColumnCount();
public boolean isEquivalentTo( CoColumnGeometryIF g );
// are all columns rectangular ?

public boolean isRectangular();
}
