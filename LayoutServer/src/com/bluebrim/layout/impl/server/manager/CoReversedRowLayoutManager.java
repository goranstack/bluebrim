package com.bluebrim.layout.impl.server.manager;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layout.shared.*;

import java.util.*;
import com.bluebrim.base.shared.geom.*;

/**
 * Creation date: (2000-06-05 10:12:05)
 * @author: Dennis
 */

public class CoReversedRowLayoutManager extends CoLayoutManager implements CoReversedRowLayoutManagerIF
{

    public final static String XML_TAG = "reversed-row-layout-manager";

    CoReversedRowLayoutManager()
    {
    }

    public boolean doesSetSize()
    {
        return false;
    }

    public String getFactoryKey()
    {
        return REVERSED_ROW_LAYOUT_MANAGER;
    }

    public void layout(CoLayoutableContainerIF parent)
    {
        CoImmutableColumnGridIF g = parent.getColumnGrid();

        double x = g.getRightMarginPosition();
        double y = g.getBottomMarginPosition();
        double W = g.getLeftMarginPosition();
        double h = 0;

        Iterator i = parent.getLayoutChildren().iterator();
        while (i.hasNext())
        {
            CoLayoutableIF l = (CoLayoutableIF) i.next();
            double w = l.getLayoutWidth();
            if (x - w < W)
            {
                x = g.getRightMarginPosition();
                y -= h;
                h = 0;
            }

            l.setLayoutLocation(x - w, y - l.getLayoutHeight());
            x -= w;
            x = g.snap(x, y, Double.POSITIVE_INFINITY, CoGeometryConstants.RIGHT_EDGE_MASK, CoGeometryConstants.TO_LEFT_MASK, false, null)
                    .getX();
            h = Math.max(h, l.getLayoutHeight());
        }
    }

    public static CoReversedRowLayoutManager getInstance()
    {
        return (CoReversedRowLayoutManager) getFactory().getLayoutManager(REVERSED_ROW_LAYOUT_MANAGER);
    }

    public String getLocalizedName()
    {
        return CoPageItemLayoutResources.getName(REVERSED_ROW_LAYOUT_MANAGER);
    }

    public String xmlGetTag()
    {
        return XML_TAG;
    }
}