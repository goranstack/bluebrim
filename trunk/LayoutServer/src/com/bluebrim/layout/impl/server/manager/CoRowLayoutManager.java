package com.bluebrim.layout.impl.server.manager;

import java.util.*;

import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Creation date: (2000-06-05 10:12:05)
 * @author: Dennis
 */

public class CoRowLayoutManager extends CoLayoutManager implements CoRowLayoutManagerIF
{

    public final static String XML_GAP = "gap";

    public final static String XML_TAG = "row-layout-manager";

    private double m_gap = 0;

    protected class MutableProxy extends CoLayoutManager.MutableProxy implements CoRowLayoutManagerIF
    {

        public double getGap()
        {
            return CoRowLayoutManager.this.getGap();
        }

        public void setGap(double g)
        {
            CoRowLayoutManager.this.setGap(g);
            notifyOwner();
        }
    };

    protected CoLayoutManager.MutableProxy createMutableProxy()
    {
        return new MutableProxy();
    }

    public CoLayoutManagerIF deepClone()
    {
        CoRowLayoutManager m = new CoRowLayoutManager();
        m.m_gap = m_gap;

        return m;
    }

    public boolean doesSetSize()
    {
        return false;
    }

    public String getFactoryKey()
    {
        return ROW_LAYOUT_MANAGER;
    }

    public double getGap()
    {
        return m_gap;
    }

    public String getPanelClassName()
    {
        return "com.bluebrim.layout.impl.client.CoRowLayoutManagerPanel";
    }

    public void layout(CoLayoutableContainerIF parent)
    {
        CoImmutableColumnGridIF g = parent.getColumnGrid();

        double x = g.getLeftMarginPosition();
        double y = g.getTopMarginPosition();
        double W = g.getRightMarginPosition();
        double h = 0;

        Iterator i = parent.getLayoutChildren().iterator();
        while (i.hasNext())
        {
            CoLayoutableIF l = (CoLayoutableIF) i.next();
            double w = l.getLayoutWidth();
            if (x + w > W)
            {
                x = g.getLeftMarginPosition();
                y += h + m_gap;
                h = 0;
            }

            l.setLayoutLocation(x, y);
            x += w;
            x = g.snap(x, y, Double.POSITIVE_INFINITY, CoGeometryConstants.LEFT_EDGE_MASK, CoGeometryConstants.TO_RIGHT_MASK, false, null)
                    .getX();
            h = Math.max(h, l.getLayoutHeight());
        }
    }

    public void setGap(double g)
    {
        m_gap = g;
    }

    public String getLocalizedName()
    {
        return CoPageItemLayoutResources.getName(ROW_LAYOUT_MANAGER);
    }

    public String xmlGetTag()
    {
        return XML_TAG;
    }

    public void xmlInit(java.util.Map attributes)
    {
        super.xmlInit(attributes);

        m_gap = CoXmlUtilities.parseDouble((String) attributes.get(XML_GAP), m_gap);
    }

    public void xmlVisit(CoXmlVisitorIF visitor)
    {
        visitor.exportAttribute(XML_GAP, Double.toString(m_gap));
    }
}