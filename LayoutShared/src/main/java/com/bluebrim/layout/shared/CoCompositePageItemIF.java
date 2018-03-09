package com.bluebrim.layout.shared;

import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;

/**
 * RMI-enabling interface for class CoLayoutArea.
 * See class for method details.
 * 
 * @author: Dennis Malmström
 */

public interface CoCompositePageItemIF extends CoShapePageItemIF
{
	// see CoPageItemIF.State
	public static class State extends CoShapePageItemIF.State
	{
		// page item state needed by view, see CoLayoutArea for details
		public List m_childrenIds; // [ com.bluebrim.gemstone.shared.CoRef ]
		public int[] m_layoutOrder;
		public CoImmutableLayoutManagerIF m_layoutManager;

		public CoImmutableColumnGridIF m_columnGrid;
		public CoImmutableBaseLineGridIF m_baseLineGrid;
		public CoImmutableShapeIF m_clipping;

		public CoReparentOperationRecord[] m_reparentOperationRecords;

		public boolean m_areChildrenLocked;
	};

	public static class ViewState extends CoPageItemIF.ViewState
	{
		public int m_reparentOperationRecordID;
	};
	// utilities

	public static class AreaComparator implements Comparator, java.io.Serializable
	{
		public boolean equals(Object o)
		{
			return false;
		}
		public int compare(Object o1, Object o2)
		{
			double a1 = ((CoShapePageItemIF) o1).getCoShape().calculateArea();
			double a2 = ((CoShapePageItemIF) o2).getCoShape().calculateArea();
			if
				(a1 == a2)
			{
				return 0;
			} else if 
				(a1 < a2)
			{
				return 1;
			} else {
				return -1;
			}
		}
	}



	public class CurrentOrder implements Comparator, java.io.Serializable
	{
		private List m_order;

		public CurrentOrder(List order)
		{
			m_order = order;
		}

		public boolean equals(Object o)
		{
			return false;
		}

		public int compare(Object o1, Object o2)
		{
			return (o1 == o2) ? 0 : m_order.indexOf(o1) - m_order.indexOf(o2);
		}

	}

;
public void add( CoShapePageItemIF pageItem [] );
public void add( CoShapePageItemIF pageItem );
CoPoint2DDouble calculatePosition( CoPageItemPositionIF p, CoShapePageItemIF child );
public boolean contains (CoShapePageItemIF aPageItem );
CoShapePageItemIF getChildAt( int i );
int getChildCount();
List getChildren();
int getIndexOfChild( CoShapePageItemIF child );
CoShapePageItemIF getLayoutChildAt( int i );
int getLayoutIndexOfChild( CoShapePageItemIF child );
CoImmutableLayoutManagerIF getLayoutManager();

CoLayoutManagerIF getMutableLayoutManager();
public void remove(CoShapePageItemIF shapeItem [] );
public void remove(CoShapePageItemIF shapeItem);

void setLayoutManager( CoLayoutManagerIF m );





public boolean areChildrenLocked();

Comparator setChildrenLayoutOrder( Comparator c );

public void setChildrenLocked( boolean l );

public void add( CoShapePageItemIF pageItem [], CoPoint2DDouble positions [] );

public void add( CoShapePageItemIF pageItem,CoPoint2DDouble pos );

public boolean hasChildren ( );
}