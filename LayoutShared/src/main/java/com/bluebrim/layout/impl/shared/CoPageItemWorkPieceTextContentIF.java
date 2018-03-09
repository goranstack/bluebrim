package com.bluebrim.layout.impl.shared;

import java.util.*;

/**
 * RMI-enabling interface for class CoPageItemWorkPieceTextContent.
 * See class for method details.
 * 
 * @author: Dennis Malmström
 */

public interface CoPageItemWorkPieceTextContentIF extends CoPageItemAbstractTextContentIF, CoOrderTaggedIF
{
	public static final String WORKPIECE_TEXT_CONTENT = "WORKPIECE_TEXT_CONTENT";

	// see CoPageItemIF.State
	public static class State extends CoPageItemAbstractTextContentIF.State
	{
		// page item state needed by view, see CoPageItemWorkPieceTextContent for details
		public int m_textTag;
		public List m_acceptedTags;
		public com.bluebrim.content.shared.CoWorkPieceIF m_workPiece;
	};
public void addAcceptedTags( List tags );
public List getAcceptedTags();
com.bluebrim.content.shared.CoWorkPieceIF getWorkPiece();
public void removeAcceptedTags( List tags );
public void setAcceptedTags( List tags );
}