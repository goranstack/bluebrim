package com.bluebrim.layoutmanager;

/**
 * The LayoutManager for exjobb layout
 * Creation date: (2000-06-13 11:15:43)
 * @author: Arvid Berg & Masod Jalalian 
 */
import java.awt.geom.*;
import java.util.*;

import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layout.shared.*;

public class CoExjobbLayoutManager extends CoLayoutManager implements CoExjobbLayoutManagerIF
{

    public final static String XML_TAG = "exjobb-layout-manager";

    /**
     * Snap the point p to the grid g Creation date: (2000-06-16 13:45:53)
     * 
     * @return java.awt.geom.Point2D
     * @param g
     *            com.bluebrim.layout.impl.shared.CoLayoutableContainerIF
     */
    public Point2D alingWidthGrid(CoImmutableColumnGridIF g, Point2D p, CoLayoutableIF rect)
    {
        double tmpX = g.snap(p.getX() + rect.getLayoutWidth(), p.getY(), Double.POSITIVE_INFINITY, CoGeometryConstants.RIGHT_EDGE_MASK,
                CoGeometryConstants.TO_RIGHT_MASK, false, null).getX();
        Point2D point = new Point2D.Double(tmpX - rect.getLayoutWidth(), p.getY());
        return point;
    }

    /**
     * doesSetSize method comment.
     */
    public boolean doesSetSize()
    {
        return false;
    }

    /**
     * getFactoryKey method comment.
     */
    public String getFactoryKey()
    {
        return COLUMNALG_LAYOUT_MANAGER;
    }

    /**
     * Layout the children of parent using a CoLayoutSpace Object
     */
    public void layout(CoLayoutableContainerIF parent)
    {

        CoImmutableColumnGridIF g = parent.getColumnGrid();
        /*- Collections of elements to place and allredy placed */
        List list = new ArrayList();
        List locked = new ArrayList();
        /*- Get children to layout, separeates locked and not locked*/
        Iterator listIter = parent.getLayoutChildren().iterator();
        while (listIter.hasNext())
        {
            CoLayoutableIF ly = (CoLayoutableIF) listIter.next();
            if (ly.getLayoutSpec().getLocationSpec().getType().equalsIgnoreCase("Var som helst"))
                locked.add(ly);
            else
                list.add(ly);
            ly.setLayoutSuccess(true);
        }
        CoLayoutSpace layoutArea = new CoLayoutColumnFirst(parent, locked);
        /*- For each object to layout*/
        layoutArea.arange(list);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            CoLayoutableIF rect = (CoLayoutableIF) iter.next();
            Point2D p = layoutArea.canPlace(rect);
            /*- Add rect to layouted and update intervalls */
            if (p != null)
            {
                /*- Add rect to layouted*/
                rect.setLayoutLocation(p.getX(), p.getY());
                /*- Update intervalls */
                layoutArea.place(p, rect);
                rect.setLayoutSuccess(true);
            } else
                rect.setLayoutSuccess(false);
        }

    }

    public String getLocalizedName()
    {
        return CoExJobbLayoutManagerResources.getName(COLUMNALG_LAYOUT_MANAGER);
    }

    public String xmlGetTag()
    {
        return XML_TAG;
    }
}