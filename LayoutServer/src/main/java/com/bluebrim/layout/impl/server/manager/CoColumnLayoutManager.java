package com.bluebrim.layout.impl.server.manager;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.layoutmanager.*;

import java.util.*;
import com.bluebrim.base.shared.geom.*;

/**
 * Creation date: (2000-06-05 10:12:05)
 * @author: Dennis
 */

public class CoColumnLayoutManager extends CoLayoutManager implements CoColumnLayoutManagerIF
{

    public final static String XML_TAG = "column-layout-manager";

    CoColumnLayoutManager()
    {
    }

    public boolean doesSetSize()
    {
        return false;
    }

    /**
     * getFactoryKey method comment.
     */
    public String getFactoryKey()
    {
        return COLUMN_LAYOUT_MANAGER;
    }

    public void layout(CoLayoutableContainerIF parent)
    {
        CoImmutableColumnGridIF g = parent.getColumnGrid();

        double x = g.getLeftMarginPosition();
        double y = g.getTopMarginPosition();
        double H = g.getBottomMarginPosition();
        double w = 0;

        Iterator i = parent.getLayoutChildren().iterator();
        while (i.hasNext())
        {
            CoLayoutableIF l = (CoLayoutableIF) i.next();
            double h = l.getLayoutHeight();
            if (y + h > H)
            {
                y = g.getTopMarginPosition();
                x += w;
                x = g.snap(x, y, Double.POSITIVE_INFINITY, CoGeometryConstants.LEFT_EDGE_MASK, CoGeometryConstants.TO_RIGHT_MASK, false,
                        null).getX();
                w = 0;
            }

            l.setLayoutLocation(x, y);
            y += h;
            w = Math.max(w, l.getLayoutWidth());
        }
    }

    public static CoColumnLayoutManager getInstance()
    {
        return (CoColumnLayoutManager) getFactory().getLayoutManager(COLUMN_LAYOUT_MANAGER);
    }

    public String xmlGetTag()
    {
        return XML_TAG;
    }

    public String getLocalizedName()
    {
        return CoExJobbLayoutManagerResources.getName(COLUMN_LAYOUT_MANAGER);
    }

}