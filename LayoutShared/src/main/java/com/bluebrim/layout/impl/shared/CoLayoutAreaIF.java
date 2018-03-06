package com.bluebrim.layout.impl.shared;

import com.bluebrim.content.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * RMI-enabling interface for class CoLayoutArea.
 * See class for method details.
 * 
 * @author: Dennis Malmström
 */

public interface CoLayoutAreaIF extends CoCompositePageItemIF, CoWorkPieceProjector
{
			
	public static String LAYOUT_AREA = "LAYOUT_AREA";
	
	// workpiece lock values
	public static int UNLOCKED = 0; // workpiece can be set and replaced
	public static int LOCKED = 1; // workpiece can be set but not replaced
	public static int FIXED = 2; // workpiece can't be set or replaced

	// see CoPageItemIF.State
	public static class State extends CoCompositePageItemIF.State
	{
		// page item state needed by view, see CoLayoutArea for details
		public CoWorkPieceIF m_workPiece;
		public boolean m_acceptsWorkPiece;
		public int m_workPieceLock;
	};

	void distribute();

	CoWorkPieceIF getWorkPiece();

	void setWorkPiece( CoWorkPieceIF wp );
		
	boolean getAcceptsWorkPiece();
	
	void setAcceptsWorkPiece( boolean b );

	int getWorkPieceLock();
	
	void setWorkPieceLock( int l );
}