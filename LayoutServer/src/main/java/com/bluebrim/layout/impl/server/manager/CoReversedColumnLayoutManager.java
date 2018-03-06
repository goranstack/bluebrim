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

public class CoReversedColumnLayoutManager extends CoLayoutManager implements CoReversedColumnLayoutManagerIF
{

    public final static String XML_TAG = "reversed-column-layout-manager";

    CoReversedColumnLayoutManager()
    {
    }

    public boolean doesSetSize()
    {
        return false;
    }

    public String getFactoryKey()
    {
        return REVERSED_COLUMN_LAYOUT_MANAGER;
    }

    public void layout(CoLayoutableContainerIF parent)
    {
        CoImmutableColumnGridIF g = parent.getColumnGrid();

        double x = g.getRightMarginPosition();
        double y = g.getBottomMarginPosition();
        double H = g.getTopMarginPosition();
        double w = 0;

        Iterator i = parent.getLayoutChildren().iterator();
        while (i.hasNext())
        {
            CoLayoutableIF l = (CoLayoutableIF) i.next();

            // test {
            //if ( g.getColumnCount() > 1 ) l.setLayoutWidth(
            // g.getColumnWidth() );
            //l.setLayoutHeight( l.getContentHeight() );
            // }

            double h = l.getLayoutHeight();
            if (y - h < H)
            {
                y = g.getBottomMarginPosition();
                x -= w;
                x = g.snap(x, y, Double.POSITIVE_INFINITY, CoGeometryConstants.RIGHT_EDGE_MASK, CoGeometryConstants.TO_LEFT_MASK, false,
                        null).getX();
                w = 0;
                if (x <= g.getLeftMarginPosition())
                {
                    l.setLayoutSuccess(false);
                    while (i.hasNext())
                    {
                        l = (CoLayoutableIF) i.next();
                        l.setLayoutSuccess(false);
                    }
                    break;
                }
            }

            l.setLayoutLocation(x - l.getLayoutWidth(), y - h);
            l.setLayoutSuccess(true);
            y -= h;
            w = Math.max(w, l.getLayoutWidth());
        }
    }

    public static CoReversedColumnLayoutManager getInstance()
    {
        return (CoReversedColumnLayoutManager) getFactory().getLayoutManager(REVERSED_COLUMN_LAYOUT_MANAGER);
    }

    public String xmlGetTag()
    {
        return XML_TAG;
    }

    public String getLocalizedName()
    {
        return CoPageItemLayoutResources.getName(REVERSED_COLUMN_LAYOUT_MANAGER);
    }

}